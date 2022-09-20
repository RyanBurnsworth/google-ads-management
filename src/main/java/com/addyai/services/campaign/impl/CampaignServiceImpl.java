/*
 * Copyright (c) 2022.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. This program
 * is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 *
 */

package com.addyai.services.campaign.impl;

import com.addyai.enums.OperationType;
import com.addyai.error_handling.ValidationErrorResponse;
import com.addyai.error_handling.exceptions.InvalidRequestException;
import com.addyai.models.BudgetDetails;
import com.addyai.models.CampaignDetails;
import com.addyai.models.campaign_criterion.CriterionDetails;
import com.addyai.repos.campaigns.CampaignRepository;
import com.addyai.repos.campaigns.budget.CampaignBudgetRepository;
import com.addyai.repos.campaigns.criterion.CriterionRepository;
import com.addyai.services.campaign.CampaignService;
import com.addyai.builder.OperationBuilder;
import com.addyai.builder.impl.OperationBuilderImpl;
import com.addyai.utils.validators.EntityValidator;
import com.google.ads.googleads.v11.services.CampaignBudgetOperation;
import com.google.ads.googleads.v11.services.CampaignCriterionOperation;
import com.google.ads.googleads.v11.services.CampaignOperation;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.addyai.utils.misc.Constants.INVALID_REQUEST_ERROR;
import static com.addyai.utils.misc.Constants.MISSING_PARAMS;

@Service
public class CampaignServiceImpl implements CampaignService {
    private final CampaignRepository campaignRepository;

    private final CampaignBudgetRepository campaignBudgetRepository;

    private final CriterionRepository criterionRepository;

    private final OperationBuilder operationBuilder;

    public CampaignServiceImpl(CampaignRepository campaignRepository,
                               CampaignBudgetRepository campaignBudgetRepository,
                               CriterionRepository criterionRepository) {
        this.campaignRepository = campaignRepository;
        this.campaignBudgetRepository = campaignBudgetRepository;
        this.criterionRepository = criterionRepository;
        this.operationBuilder = new OperationBuilderImpl();
    }

    @Override
    public void upsertCampaigns(long customerId, List<CampaignDetails> campaignDetailsList, boolean shouldCreate) throws Exception {
        validateCampaignDetails(campaignDetailsList);

        OperationType operationType = shouldCreate ? OperationType.CREATE : OperationType.UPDATE;

        campaignDetailsList = associateBudgetsToCampaigns(customerId, campaignDetailsList, operationType);

        List<CampaignOperation> campaignOperationList = operationBuilder
                .buildCampaignOperationList(campaignDetailsList, operationType);

        List<String> campaignResourceNameList =
                campaignRepository.performCampaignOperations(customerId, campaignOperationList);

        // TODO: update location geo target

        Map<String, List<CriterionDetails>> mapping =
                buildCampaignResNameToCriterionMapping(campaignResourceNameList, campaignDetailsList);

        List<CampaignCriterionOperation> campaignCriterionOperationList = operationBuilder
                .buildCampaignCriterionOperationList(mapping, operationType);

        criterionRepository.performCriterionOperations(customerId, campaignCriterionOperationList);
    }

    /**
     * Fetch all campaigns from a client account
     *
     * @param customerId the customer id of the client account
     * @return a list of all [CampaignDetails] in a client's account
     */
    @Override
    public List<CampaignDetails> findAllCampaignDetails(long customerId) throws Exception {
        // fetch all campaigns from the client account
        List<CampaignDetails> campaignDetailsList = campaignRepository.fetchAllCampaignDetails(customerId);

        // fetch all campaign budget details from the client account
        List<BudgetDetails> existingBudgets = campaignBudgetRepository.fetchAllBudgetDetails(customerId);

        // associate the budget details to its campaign details
        for (CampaignDetails campaignDetails : campaignDetailsList) {
            BudgetDetails budgetDetails = findBudgetDetailsByResourceName(campaignDetails.getBudgetResourceName(),
                    existingBudgets);

            // assign the budget details object to the campaign details object
            campaignDetails.setBudgetDetails(budgetDetails);

            List<CriterionDetails> criterionDetailsList = criterionRepository.fetchCampaignCriterionDetails(customerId,
                    campaignDetails.getCampaignResourceName());

            campaignDetails.setCampaignCriteriaList(criterionDetailsList);
        }
        return campaignDetailsList;
    }

    /**
     * Fetch a single CampaignDetails by name
     *
     * @param customerId   the customer id of the client account
     * @param campaignName the name of the campaign to fetch
     * @return CampaignDetails
     * @throws Exception
     */
    @Override
    public CampaignDetails findCampaignDetailsByName(long customerId, String campaignName) throws Exception {
        // throw an InvalidRequestException if the campaignName is missing
        if (campaignName.isEmpty())
            throw new InvalidRequestException(INVALID_REQUEST_ERROR, MISSING_PARAMS);

        // fetch the campaign details from the client account using the campaign name
        CampaignDetails campaignDetails = campaignRepository.fetchCampaignDetailsByName(customerId, campaignName);

        // TODO create an endpoint to grab a single budget details object by id or name
        // fetch all the campaign budgets from the client's account
        List<BudgetDetails> existingBudgets = campaignBudgetRepository.fetchAllBudgetDetails(customerId);

        // find the specific budget details object for this campaign
        BudgetDetails budgetDetails = findBudgetDetailsByName(campaignName, existingBudgets);

        // set the budget details object
        campaignDetails.setBudgetDetails(budgetDetails);

        return campaignDetails;
    }

    /**
     * Delete campaigns from a client's account
     *
     * @param customerId  the customer id of the client account
     * @param campaignIds the ids of the campaigns to be deleted
     */
    @Override
    public void deleteCampaigns(long customerId, List<Long> campaignIds) throws Exception {
        //campaignRepository.deleteCampaigns(customerId, campaignIds);
    }

    private String getGeoTargetConstant(String locale, String countryCode, String location) throws Exception {
        return criterionRepository.getGeoTargetConstant(locale, countryCode, location);
    }

    private List<CampaignDetails> associateBudgetsToCampaigns(long customerId,
                                                              List<CampaignDetails> campaignDetailsList,
                                                              OperationType operationType) throws Exception {
        List<CampaignDetails> updateCampaignDetailsList = new ArrayList<>();
        for (CampaignDetails campaignDetails : campaignDetailsList) {
            // create a campaign budget operation for this individual budget
            List<BudgetDetails> singleBudgetDetailsList = Collections.singletonList(campaignDetails.getBudgetDetails());
            List<CampaignBudgetOperation> campaignBudgetOperations =
                    operationBuilder.buildCampaignBudgetOperationList(singleBudgetDetailsList, operationType);

            // create the budget and extract the budget resource name
            String budgetResourceName =
                    campaignBudgetRepository.performCampaignBudgetOperations(customerId, campaignBudgetOperations).get(0);

            // associated the newly created budget with campaign
            campaignDetails.setBudgetResourceName(budgetResourceName);
            updateCampaignDetailsList.add(campaignDetails);
        }
        return updateCampaignDetailsList;
    }

    private BudgetDetails findBudgetDetailsByResourceName(String resourceName, List<BudgetDetails> budgetDetailsList) {
        for (BudgetDetails budgetDetails : budgetDetailsList) {
            if (budgetDetails.getResourceName().equals(resourceName))
                return budgetDetails;
        }
        return null;
    }

    private BudgetDetails findBudgetDetailsByName(String budgetName, List<BudgetDetails> budgetDetailsList) {
        for (BudgetDetails budgetDetails : budgetDetailsList) {
            if (budgetDetails.getName().equals(budgetName))
                return budgetDetails;
        }
        return null;
    }

    private void validateCampaignDetails(List<CampaignDetails> campaignDetailsList) {
        ValidationErrorResponse validationErrorResponse;
        for (CampaignDetails campaignDetails : campaignDetailsList) {
            validationErrorResponse = EntityValidator.isCampaignDetailsValid(campaignDetails);
            if (validationErrorResponse != null)
                throw new InvalidRequestException(
                        validationErrorResponse.getErrorCode(),
                        validationErrorResponse.getErrorMessage());

            validationErrorResponse = EntityValidator.isBudgetDetailsValid(campaignDetails.getBudgetDetails());
            if (validationErrorResponse != null)
                throw new InvalidRequestException(
                        validationErrorResponse.getErrorCode(),
                        validationErrorResponse.getErrorMessage());

            validationErrorResponse = EntityValidator.isCriterionDetailsValid(campaignDetails.getCampaignCriteriaList());
            if (validationErrorResponse != null)
                throw new InvalidRequestException(
                        validationErrorResponse.getErrorCode(),
                        validationErrorResponse.getErrorMessage());
        }
    }

    private Map<String, List<CriterionDetails>> buildCampaignResNameToCriterionMapping(List<String> campaignResourceNameList,
                                                                                       List<CampaignDetails> campaignDetailsList) {
        Map<String, List<CriterionDetails>> mapping = new HashMap<>();
        if (campaignDetailsList.size() != campaignResourceNameList.size()) return mapping;

        for (int i = 0; i < campaignResourceNameList.size(); i++) {

            mapping.put(campaignResourceNameList.get(i), campaignDetailsList.get(i).getCampaignCriteriaList());
        }

        return mapping;
    }
}
