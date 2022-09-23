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

import com.addyai.builder.OperationBuilder;
import com.addyai.builder.impl.OperationBuilderImpl;
import com.addyai.enums.OperationType;
import com.addyai.error_handling.ValidationErrorResponse;
import com.addyai.error_handling.exceptions.InvalidRequestException;
import com.addyai.error_handling.exceptions.NotFoundException;
import com.addyai.error_handling.exceptions.ServiceFailureException;
import com.addyai.models.BudgetDetails;
import com.addyai.models.CampaignDetails;
import com.addyai.models.campaign_criterion.CriterionDetails;
import com.addyai.models.campaign_criterion.DeviceDetails;
import com.addyai.models.campaign_criterion.LocationDetails;
import com.addyai.repos.campaigns.CampaignRepository;
import com.addyai.repos.campaigns.budget.BudgetRepository;
import com.addyai.repos.campaigns.criterion.CriterionRepository;
import com.addyai.services.campaign.CampaignService;
import com.addyai.utils.validators.EntityValidator;
import com.google.ads.googleads.v11.services.CampaignBudgetOperation;
import com.google.ads.googleads.v11.services.CampaignCriterionOperation;
import com.google.ads.googleads.v11.services.CampaignOperation;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.addyai.utils.misc.Constants.*;

@Service
public class CampaignServiceImpl implements CampaignService {
    private final CampaignRepository campaignRepository;

    private final BudgetRepository budgetRepository;

    private final CriterionRepository criterionRepository;

    private final OperationBuilder operationBuilder;

    public CampaignServiceImpl(CampaignRepository campaignRepository,
                               BudgetRepository budgetRepository,
                               CriterionRepository criterionRepository) {
        this.campaignRepository = campaignRepository;
        this.budgetRepository = budgetRepository;
        this.criterionRepository = criterionRepository;
        this.operationBuilder = new OperationBuilderImpl();
    }

    /**
     * Update or insert campaigns with in a client's account.
     * <p>Creating a campaign will create the associated budget and criterion.
     * Updating a campaign will update the campaign, <b>remove existing criterion and create
     * new criterion with values contained in CampaignDetails.criterionDetailsList</b>.</p>
     *
     * @param customerId          the customerId of the client account
     * @param campaignDetailsList [CampaignDetails] containing values to create/update campaigns with
     * @param shouldCreate        true if create and false if updating
     * @throws InvalidRequestException if an error occurs during the creating/updating of campaigns
     */
    @Override
    public void upsertCampaigns(long customerId, List<CampaignDetails> campaignDetailsList, boolean shouldCreate) throws Exception {
        OperationType operationType = shouldCreate ? OperationType.CREATE : OperationType.UPDATE;

        validateCampaignDetails(campaignDetailsList);

        // create the budget from budget details on the account, then associate the budget to the campaign.
        campaignDetailsList = associateBudgetsToCampaigns(customerId, campaignDetailsList, operationType);

        List<CampaignOperation> campaignOperationList = operationBuilder
                .buildCampaignOperationList(campaignDetailsList, operationType);

        // create or update the campaigns on the client account.
        List<String> campaignResourceNameList =
                campaignRepository.performCampaignOperations(customerId, campaignOperationList);

        // create campaign criterion on the account and associate to the appropriate campaign.
        associateCriterionToCampaigns(customerId, campaignDetailsList, campaignResourceNameList, operationType);
    }

    /**
     * Fetch all campaigns from a client account.
     * Each campaign includes associated campaign budget and list of campaign criterion.
     *
     * @param customerId the customer id of the client account
     * @return a list of all [CampaignDetails] in a client's account
     * @throws InvalidRequestException if an error occurs during the retrieval of campaigns
     * @throws
     */
    @Override
    public List<CampaignDetails> findAllCampaignDetails(long customerId) throws Exception {
        // fetch all campaigns from the client account
        List<CampaignDetails> campaignDetailsList = campaignRepository.fetchAllCampaignDetails(customerId);

        // fetch all campaign budget details from the client account
        List<BudgetDetails> existingBudgets = budgetRepository.fetchAllBudgetDetails(customerId);

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
     * Fetch a single CampaignDetails by name.
     * The campaign includes associated campaign budget and list of campaign criterion.
     *
     * @param customerId   the customer id of the client account
     * @param campaignName the name of the campaign to fetch
     * @return CampaignDetails containing the values from the
     * @throws InvalidRequestException if an error occurs during the retrieval of campaign by name
     * @throws NotFoundException       if campaign is not found
     */
    @Override
    public CampaignDetails findCampaignDetailsByName(long customerId, String campaignName) throws Exception {
        // throw an InvalidRequestException if the campaignName is missing
        if (campaignName.isEmpty())
            throw new InvalidRequestException(INVALID_REQUEST_ERROR, MISSING_PARAMS);

        // fetch the campaign details from the client account using the campaign name
        CampaignDetails campaignDetails = campaignRepository.fetchCampaignDetailsByName(customerId, campaignName);
        if (campaignDetails == null)
            throw new NotFoundException("", ""); // TODO

        // TODO create an endpoint to grab a single budget details object by id or name
        // fetch all the campaign budgets from the client's account
        List<BudgetDetails> existingBudgets = budgetRepository.fetchAllBudgetDetails(customerId);

        // find the specific budget details object for this campaign
        BudgetDetails budgetDetails = findBudgetDetailsByName(campaignName, existingBudgets);

        // set the budget details object
        campaignDetails.setBudgetDetails(budgetDetails);

        return campaignDetails;
    }

    /**
     * Remove campaigns from a client's account.
     *
     * @param customerId          the customer id of the client account
     * @param campaignDetailsList the campaigns to be deleted.
     * @throws InvalidRequestException if an error occurs during the removal of campaigns
     */
    @Override
    public void deleteCampaigns(long customerId, List<CampaignDetails> campaignDetailsList) throws Exception {
        List<CampaignOperation> campaignOperationList =
                operationBuilder.buildCampaignOperationList(campaignDetailsList, OperationType.REMOVE);
        campaignRepository.performCampaignOperations(customerId, campaignOperationList);
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
                    budgetRepository.performCampaignBudgetOperations(customerId, campaignBudgetOperations).get(0);

            // associated the newly created budget with campaign
            campaignDetails.setBudgetResourceName(budgetResourceName);
            updateCampaignDetailsList.add(campaignDetails);
        }
        return updateCampaignDetailsList;
    }

    private void associateCriterionToCampaigns(long customerId,
                                               List<CampaignDetails> campaignDetailsList,
                                               List<String> campaignResourceNameList,
                                               OperationType operationType) throws Exception {

        // associate geotarget codes to location criterion
        associateGeoTargetConstantToLocation(campaignDetailsList);

        // to update criterion, first delete all user-created criterion and created new from list
        if (operationType.equals(OperationType.UPDATE)) {
            deleteAllCriterionForCampaigns(customerId, campaignResourceNameList);

            // update the operation type to CREATE the new campaign criterion
            operationType = OperationType.CREATE;
        }

        // create a mapping from campaignResourceName -> campaignCriterionList
        Map<String, List<CriterionDetails>> mapping =
                buildCampaignResNameToCriterionMapping(campaignResourceNameList, campaignDetailsList);

        // create a list of campaign criterion CREATE operations
        List<CampaignCriterionOperation> campaignCriterionOperationList = operationBuilder
                .buildCampaignCriterionOperationList(mapping, operationType);

        // perform the criterion CREATE operations
        criterionRepository.performCriterionOperations(customerId, campaignCriterionOperationList);
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

    private void deleteAllCriterionForCampaigns(long customerId, List<String> campaignResourceNameList) throws Exception {
        List<String> criterionResourceNameList = new ArrayList<>();

        // gather the resource names of user-created criterion to be removed from campaigns
        campaignResourceNameList.forEach((resourceName) -> {
            // fetch the existing criterion for the given campaign
            List<CriterionDetails> criterionDetailsList = criterionRepository.fetchCampaignCriterionDetails(
                    customerId, resourceName);

            // for each object that is not DeviceDetails, add its resource name to the list for removal
            criterionDetailsList.forEach((criterionDetails) -> {
                if (!(criterionDetails instanceof DeviceDetails))
                    criterionResourceNameList.add(criterionDetails.getCriterionResourceName());
            });
        });

        // create a list of campaign criterion operations to remove the non-device criterion from campaigns
        List<CampaignCriterionOperation> campaignCriterionOperationList = new ArrayList<>();
        criterionResourceNameList.forEach((resName) -> {
            CampaignCriterionOperation campaignCriterionOperation = CampaignCriterionOperation.newBuilder()
                    .setRemove(resName)
                    .build();
            campaignCriterionOperationList.add(campaignCriterionOperation);
        });

        // perform the criterion removal operations
        if (!campaignCriterionOperationList.isEmpty())
            criterionRepository.performCriterionOperations(customerId, campaignCriterionOperationList);
    }

    private void associateGeoTargetConstantToLocation(List<CampaignDetails> campaignDetailsList) {
        campaignDetailsList.forEach((campaignDetails -> {
            campaignDetails.getCampaignCriteriaList().forEach((campaignCriterion -> {
                if (campaignCriterion instanceof LocationDetails) {
                    LocationDetails locationDetails = ((LocationDetails) campaignCriterion);

                    try {
                        String geotargetConstant = criterionRepository.getGeoTargetConstant(
                                DEFAULT_LOCALE, DEFAULT_COUNTRY_CODE, locationDetails.getLocation());
                        locationDetails.setGeoTargetingConstant(geotargetConstant);
                    } catch (Exception e) {
                        throw new InvalidRequestException("", ""); //TODO
                    }
                }
            }));
        }));
    }
}
