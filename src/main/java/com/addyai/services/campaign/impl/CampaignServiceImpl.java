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
import com.addyai.repos.campaign.CampaignRepository;
import com.addyai.repos.campaign.budget.BudgetRepository;
import com.addyai.repos.campaign.criterion.CriterionRepository;
import com.addyai.services.campaign.CampaignService;
import com.addyai.utils.validators.EntityValidator;
import com.google.ads.googleads.v14.services.CampaignBudgetOperation;
import com.google.ads.googleads.v14.services.CampaignCriterionOperation;
import com.google.ads.googleads.v14.services.CampaignOperation;
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
     * @throws ServiceFailureException if an error occurs while creating campaign operations
     */
    @Override
    public void upsertCampaigns(long customerId, List<CampaignDetails> campaignDetailsList,
                                boolean shouldCreate) throws Exception {
        OperationType operationType = shouldCreate ? OperationType.CREATE : OperationType.UPDATE;

        // validate CampaignDetails, BudgetDetails and CriterionDetails objects before proceeding
        validateDetails(campaignDetailsList, operationType);

        // create the budget from budget details on the account, then associate the budget to the campaign.
        campaignDetailsList = associateBudgetsToCampaigns(customerId, campaignDetailsList, operationType);

        // build a list of campaign operations based on the operation type
        List<CampaignOperation> campaignOperationList = operationBuilder
                .buildCampaignOperationList(campaignDetailsList, operationType);

        // if the campaignOperationsList is empty there was a problem creating operations
        if (campaignOperationList.isEmpty())
            throw new ServiceFailureException(INTERNAL_SERVICE_ERROR, CAMPAIGN_OPERATIONS_FAILED_ERROR_MSG);

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

            // retrieve the campaign criterion from the account
            List<CriterionDetails> criterionDetailsList = criterionRepository.fetchCampaignCriterionDetails(customerId,
                    campaignDetails.getCampaignResourceName());

            // update the CriterionDetails list in the CampaignDetails object
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
     * @throws NotFoundException       if the requested campaign is not found
     */
    @Override
    public CampaignDetails findCampaignDetailsByName(long customerId, String campaignName) throws Exception {
        // throw an InvalidRequestException if the campaignName is missing
        if (campaignName.isEmpty())
            throw new InvalidRequestException(INVALID_REQUEST_ERROR, MISSING_PARAMS);

        // fetch the campaign details from the client account using the campaign name
        CampaignDetails campaignDetails = campaignRepository.fetchCampaignDetailsByName(customerId, campaignName);
        if (campaignDetails == null)
            throw new NotFoundException(RESOURCE_NOT_FOUND_ERR_CODE, RESOURCE_NOT_FOUND_ERROR_MSG);

        // fetch all the campaign budgets from the client's account
        List<BudgetDetails> existingBudgets = budgetRepository.fetchAllBudgetDetails(customerId);

        // find the specific budget details object for this campaign
        BudgetDetails budgetDetails = findBudgetDetailsByName(campaignName, existingBudgets);

        // set the budget details object
        campaignDetails.setBudgetDetails(budgetDetails);

        // retrieve the campaign criterion from the account
        List<CriterionDetails> criterionDetailsList = criterionRepository.fetchCampaignCriterionDetails(customerId,
                campaignDetails.getCampaignResourceName());

        // update the CriterionDetails list in the CampaignDetails object
        campaignDetails.setCampaignCriteriaList(criterionDetailsList);
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
        // ensure each campaign details object has at least a campaign resource name set
        ValidationErrorResponse validationErrorResponse = EntityValidator
                .validateNonEmptyCampaignResourceNames(campaignDetailsList);

        // throw an exception if there are missing campaign resource ids
        if (validationErrorResponse != null)
            throw new InvalidRequestException(validationErrorResponse.getErrorCode(),
                    validationErrorResponse.getErrorMessage());

        // build delete campaigns operation list
        List<CampaignOperation> campaignOperationList =
                operationBuilder.buildCampaignOperationList(campaignDetailsList, OperationType.REMOVE);

        // perform delete campaigns operations on the client's account
        campaignRepository.performCampaignOperations(customerId, campaignOperationList);
    }

    /**
     * Create or update budgets on the client account and associate the budget with its appropriate campaign.
     *
     * @param customerId          the customer id of the client account
     * @param campaignDetailsList the campaignDetails that include budget details to be created or updated
     * @param operationType       the type of operation to be performed on the budgets (CREATE or UPDATE)
     * @return the list of campaign details with newly created or updated budgets associated
     * @throws InvalidRequestException if an error occurs during the associating of budgets to campaigns
     * @throws ServiceFailureException if an error occurs while creating the budget operations
     */
    private List<CampaignDetails> associateBudgetsToCampaigns(long customerId,
                                                              List<CampaignDetails> campaignDetailsList,
                                                              OperationType operationType) throws Exception {
        List<CampaignDetails> updateCampaignDetailsList = new ArrayList<>();
        for (CampaignDetails campaignDetails : campaignDetailsList) {
            // create a campaign budget operation for this individual budget
            List<BudgetDetails> singleBudgetDetailsList = Collections.singletonList(campaignDetails.getBudgetDetails());
            List<CampaignBudgetOperation> campaignBudgetOperations =
                    operationBuilder.buildCampaignBudgetOperationList(singleBudgetDetailsList, operationType);

            // if the campaignBudgetOperations is empty there was a problem creating operations
            if (campaignBudgetOperations.isEmpty())
                throw new ServiceFailureException(INTERNAL_SERVICE_ERROR, BUDGET_OPERATIONS_FAILED_ERROR_MSG);

            // create the budget and extract the budget resource name
            String budgetResourceName =
                    budgetRepository.performCampaignBudgetOperations(customerId, campaignBudgetOperations).get(0);

            // associated the newly created budget with campaign
            campaignDetails.setBudgetResourceName(budgetResourceName);
            updateCampaignDetailsList.add(campaignDetails);
        }
        return updateCampaignDetailsList;
    }

    /**
     * Create or update criterion and associate to the appropriate campaign associated by the campaign's resource name.
     * When updating campaign criterion, the existing criterion for the campaign will be deleted and the list of
     * criterion contained in campaignDetails will be added to the campaign.
     *
     * @param customerId               the customer id of the client account
     * @param campaignDetailsList      a list of campaign details containing a list of criterion details to be created or updated
     * @param campaignResourceNameList a list of each campaign's resource name have its criterion created or updated
     * @param operationType            the type of operation to be performed on the budgets (CREATE or UPDATE)
     * @throws InvalidRequestException if an error occurs during the associating of criterion to campaigns
     * @throws ServiceFailureException if an error occurs while creating the criterion operations
     */
    private void associateCriterionToCampaigns(long customerId,
                                               List<CampaignDetails> campaignDetailsList,
                                               List<String> campaignResourceNameList,
                                               OperationType operationType) throws Exception {

        // if campaign resource names or details list are empty then continue
        if (campaignDetailsList.isEmpty() || campaignResourceNameList.isEmpty()) return;

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

        // if the campaignCriterionOperationList there was a problem creating a mapping or operations
        if (campaignCriterionOperationList.isEmpty())
            throw new ServiceFailureException(INTERNAL_SERVICE_ERROR, CRITERION_OPERATIONS_FAILED_ERROR_MSG);

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

    /**
     * Validate each CampaignDetails object in the list including its BudgetDetails object and list of CriterionDetails.
     * Validate necessary fields are complete based on the operation type.
     *
     * @param campaignDetailsList a list of campaignDetails to be validated
     * @param operationType       the type of operation being validated against
     */
    private void validateDetails(List<CampaignDetails> campaignDetailsList, OperationType operationType) {
        // validate the CampaignDetails
        ValidationErrorResponse validationErrorResponse;
        for (CampaignDetails campaignDetails : campaignDetailsList) {
            validationErrorResponse = EntityValidator.isCampaignDetailsValid(campaignDetails, operationType);
            if (validationErrorResponse != null)
                throw new InvalidRequestException(
                        validationErrorResponse.getErrorCode(),
                        validationErrorResponse.getErrorMessage());

            // validate the BudgetDetails
            validationErrorResponse = EntityValidator.isBudgetDetailsValid(campaignDetails.getBudgetDetails(), operationType);
            if (validationErrorResponse != null)
                throw new InvalidRequestException(
                        validationErrorResponse.getErrorCode(),
                        validationErrorResponse.getErrorMessage());

            // validate the CriterionDetails
            validationErrorResponse = EntityValidator.isCriterionDetailsValid(campaignDetails.getCampaignCriteriaList());
            if (validationErrorResponse != null)
                throw new InvalidRequestException(
                        validationErrorResponse.getErrorCode(),
                        validationErrorResponse.getErrorMessage());
        }
    }

    /**
     * Create a mapping from a campaign resource to each of its associated criterion.
     *
     * @param campaignResourceNameList a list of campaign resource names to map to criterion
     * @param campaignDetailsList      a list of campaign details containing a list of criterion details
     * @return a one-to-one mapping of campaign resource name to a list of the campaign's associated criterionDetails
     */
    private Map<String, List<CriterionDetails>> buildCampaignResNameToCriterionMapping(List<String> campaignResourceNameList,
                                                                                       List<CampaignDetails> campaignDetailsList) {
        Map<String, List<CriterionDetails>> mapping = new HashMap<>();
        // a one-to-one mapping must existing
        if (campaignDetailsList.size() != campaignResourceNameList.size()) return mapping;

        for (int i = 0; i < campaignResourceNameList.size(); i++) {
            mapping.put(campaignResourceNameList.get(i), campaignDetailsList.get(i).getCampaignCriteriaList());
        }
        return mapping;
    }

    /**
     * Delete all criterion associated with a campaign
     *
     * @param customerId               the customer id of the client account
     * @param campaignResourceNameList a list of campaign resource names each of which will have their criterion removed.
     * @throws InvalidRequestException if an error occurs during the associating of criterion to campaigns
     * @throws ServiceFailureException if an error occurs while creating the criterion operations
     */
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

        // if the campaignCriterionOperationList there was a problem creating a mapping or operations
        if (campaignCriterionOperationList.isEmpty())
            throw new ServiceFailureException(INTERNAL_SERVICE_ERROR, CRITERION_OPERATIONS_FAILED_ERROR_MSG);

        // perform the criterion removal operations
        criterionRepository.performCriterionOperations(customerId, campaignCriterionOperationList);
    }

    /**
     * Update the geo target constant with each of the campaignDetails.criterionDetailsList
     * LocationDetails objects using the location string it contains.
     *
     * @param campaignDetailsList a list of campaignDetails contains
     *                            criterionDetailsLists with LocationDetails objects
     * @throws ServiceFailureException if the geo target constant is not found
     */
    private void associateGeoTargetConstantToLocation(List<CampaignDetails> campaignDetailsList) {
        // parse through each list of CriterionDetails in each of the CampaignDetails campaignDetailsList has
        campaignDetailsList.forEach((campaignDetails -> {
            campaignDetails.getCampaignCriteriaList().forEach((campaignCriterion -> {
                // update only the LocationDetails objects
                if (campaignCriterion instanceof LocationDetails) {
                    LocationDetails locationDetails = ((LocationDetails) campaignCriterion);

                    try {
                        // get the geo target constant using the location from Google Ads
                        String geotargetConstant = criterionRepository.getGeoTargetConstant(
                                DEFAULT_LOCALE, DEFAULT_COUNTRY_CODE, locationDetails.getLocation());

                        // update the LocationDetails object
                        locationDetails.setGeoTargetingConstant(geotargetConstant);
                    } catch (Exception e) {
                        throw new ServiceFailureException(INTERNAL_SERVICE_ERROR, LOCATION_NOT_FOUND_ERROR_MSG);
                    }
                }
            }));
        }));
    }
}
