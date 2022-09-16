package com.addyai.services.campaign.impl;

import com.addyai.error_handling.ValidationErrorResponse;
import com.addyai.error_handling.exceptions.InvalidRequestException;
import com.addyai.models.BudgetDetails;
import com.addyai.models.CampaignDetails;
import com.addyai.repos.campaigns.CampaignRepository;
import com.addyai.repos.campaigns.budget.CampaignBudgetRepository;
import com.addyai.services.campaign.CampaignService;
import com.addyai.utils.EntityValidator;
import com.google.ads.googleads.lib.utils.FieldMasks;
import com.google.ads.googleads.v11.common.ManualCpc;
import com.google.ads.googleads.v11.enums.*;
import com.google.ads.googleads.v11.resources.Campaign;
import com.google.ads.googleads.v11.resources.CampaignBudget;
import com.google.ads.googleads.v11.services.CampaignBudgetOperation;
import com.google.ads.googleads.v11.services.CampaignOperation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.addyai.utils.Constants.MICRO_FACTOR;
import static java.lang.Math.round;

@Service
public class CampaignServiceImpl implements CampaignService {
    private final CampaignRepository campaignRepository;

    private final CampaignBudgetRepository campaignBudgetRepository;

    public CampaignServiceImpl(CampaignRepository campaignRepository, CampaignBudgetRepository campaignBudgetRepository) {
        this.campaignRepository = campaignRepository;
        this.campaignBudgetRepository = campaignBudgetRepository;
    }

    /**
     * Add campaigns to a client's account
     *
     * @param customerId          the customer id of the client account
     * @param campaignDetailsList [CampaignDetails] to be used in campaign creation
     */
    @Override
    public void addCampaignsToAccount(long customerId, List<CampaignDetails> campaignDetailsList) throws Exception {
        List<CampaignOperation> campaignOperations = new ArrayList<>();

        // fetch all campaign budget details from the client account
        List<BudgetDetails> existingBudgets = campaignBudgetRepository.fetchAllCampaignBudgetDetails(customerId);

        for (CampaignDetails campaignDetails : campaignDetailsList) {
            // validate campaign details before proceeding
            validateCampaignDetails(campaignDetails);

            // validate the campaign budget details and assign budget resource to campaign
            validateCampaignBudgetDetails(campaignDetails.getBudgetDetails());

            // get the resource name of an existing budget in the client's account
            String resourceName = getResourceNameForExistingBudget(campaignDetails.getBudgetDetails(), existingBudgets);

            // if the resource name is empty, create a campaign budget resource on the client account
            if (resourceName.isEmpty()) {
                // create a campaign budget operation only for this single budget
                List<CampaignDetails> singleCampaignDetailsList = Collections.singletonList(campaignDetails);
                List<CampaignBudgetOperation> campaignBudgetOperations =
                        buildCampaignBudgetOperationList(singleCampaignDetailsList, true);

                // create this budget on the client's account
                List<BudgetDetails> createdBudgets =
                        campaignBudgetRepository.createOrUpdateBudgets(customerId, campaignBudgetOperations);

                // associated the newly created budget with campaign
                BudgetDetails createdBudget = createdBudgets.get(0);
                campaignDetails.setBudgetResourceName(createdBudget.getResourceName());
            }

            // create a Google Ads campaign object from campaign details
            Campaign campaign = buildCampaignFromDetails(campaignDetails, true);

            // create a CREATE campaign operation
            CampaignOperation op = CampaignOperation.newBuilder()
                    .setCreate(campaign)
                    .build();

            // add to campaign operations list
            campaignOperations.add(op);
        }

        // add campaigns to the client account
        campaignRepository.addCampaigns(customerId, campaignOperations);
    }

    /**
     * Fetch campaigns from a client account
     *
     * @param customerId the customer id of the client account
     * @return a list of complete campaign details containing all campaigns in a client's account
     */
    @Override
    public List<CampaignDetails> findAllCampaignDetails(long customerId) throws Exception {
        // fetch all campaigns from the client account
        List<CampaignDetails> campaignDetailsList = campaignRepository.fetchAllCampaignDetails(customerId);

        // fetch all campaign budget details from the client account
        List<BudgetDetails> existingBudgets = campaignBudgetRepository.fetchAllCampaignBudgetDetails(customerId);

        // associate the budget details to its campaign details
        for (CampaignDetails campaignDetails : campaignDetailsList) {
            BudgetDetails budgetDetails = findBudgetDetailsByResourceName(campaignDetails.getBudgetResourceName(),
                    existingBudgets);

            campaignDetails.setBudgetDetails(budgetDetails);
        }
        return campaignDetailsList;
    }

    /**
     * Update a campaign in a Google Ads account
     *
     * @param campaignDetailsList a list of updated [CampaignDetails]
     */
    @Override
    public void updateCampaign(long customerId, List<CampaignDetails> campaignDetailsList) throws Exception {
        List<CampaignOperation> campaignOperations = new ArrayList<>();

        // create an UPDATE campaign operation for each campaign
        for (CampaignDetails campaignDetails : campaignDetailsList) {
            Campaign campaign = buildCampaignFromDetails(campaignDetails, false);
            System.out.println("CampaignResName: " + campaign.getResourceName());
            System.out.println("CampaignDetailsResName: " + campaignDetails.getCampaignResourceName());
            CampaignOperation operation = CampaignOperation.newBuilder()
                    .setUpdate(campaign)
                    .setUpdateMask(FieldMasks.allSetFieldsOf(campaign))
                    .build();

            // add newly created operation to list
            campaignOperations.add(operation);
        }

        // perform update on all campaigns
        campaignRepository.updateCampaigns(customerId, campaignOperations);
    }

    /**
     * Delete campaigns from a client's account
     *
     * @param customerId  the customer id of the client account
     * @param campaignIds the ids of the campaigns to be deleted
     */
    @Override
    public void deleteCampaigns(long customerId, List<Long> campaignIds) throws Exception {
        campaignRepository.deleteCampaigns(customerId, campaignIds);
    }

    /**
     * Update campaign budgets within a client's account
     *
     * @param customerId          the customer id of the client account
     * @param campaignDetailsList a list of [BudgetDetails] to be updated
     */
    @Override
    public void updateCampaignBudgets(long customerId, List<CampaignDetails> campaignDetailsList) throws Exception {
        // create a list of CREATE campaign budget operations using the budgetDetails within each campaignDetails
        // in the campaign details list
        List<CampaignBudgetOperation> campaignBudgetOperations
                = buildCampaignBudgetOperationList(campaignDetailsList, false);

        // perform update on all campaigns
        campaignBudgetRepository.createOrUpdateBudgets(customerId, campaignBudgetOperations);
    }

    /**
     * Generate a campaign object using a CampaignDetails object (for use in creating new campaigns)
     *
     * @param campaignDetails [CampaignDetails] to be parsed into a [Campaign]
     * @param shouldCreate    true/false if this campaign should be created or updated
     * @return [Campaign] parsed from [CampaignDetails] provided
     */
    private Campaign buildCampaignFromDetails(CampaignDetails campaignDetails,
                                              boolean shouldCreate) {
        // create a Manual cpc object with or without enhanced CPC
        ManualCpc manualCpc = buildManualCpc(campaignDetails.isEnhancedCpcEnabled());

        // create a GeoTargetTypeSetting using the negative and positive targets
        Campaign.GeoTargetTypeSetting geoTargetTypeSetting = buildGeoTargetTypeSettings(
                campaignDetails.getPositiveGeoTargetType(),
                campaignDetails.getNegativeGeoTargetType());

        // extract network settings into its own object
        Campaign.NetworkSettings networkSettings = buildNetworkSettings(
                campaignDetails.isTargetingContentNetwork(),
                campaignDetails.isTargetingPartnerSearchNetwork(),
                campaignDetails.isTargetingSearchNetwork()
        );

        // create the campaign status object
        CampaignStatusEnum.CampaignStatus status =
                CampaignStatusEnum.CampaignStatus.valueOf(campaignDetails.getStatus());

        //create the advertising channel type object
        AdvertisingChannelTypeEnum.AdvertisingChannelType advertisingChannelType =
                AdvertisingChannelTypeEnum.AdvertisingChannelType.valueOf(campaignDetails.getAdvertisingChannelType());

        // create and return a campaign object with the above settings
        if (shouldCreate) {
            // if shouldCreate is true return a campaign object for creation
            return Campaign.newBuilder()
                    .setStatus(status)
                    .setId(campaignDetails.getCampaignId())
                    .setStartDate(campaignDetails.getStartDate())
                    .setEndDate(campaignDetails.getEndDate())
                    .setName(campaignDetails.getCampaignName())
                    .setGeoTargetTypeSetting(geoTargetTypeSetting)
                    .setCampaignBudget(campaignDetails.getBudgetResourceName())
                    .setManualCpc(manualCpc)
                    .setNetworkSettings(networkSettings)
                    .setAdvertisingChannelType(advertisingChannelType) // only set for campaign creation
                    .build();
        } else {
            // if shouldCreate is false return a campaign object for updating
            return Campaign.newBuilder()
                    .setStatus(status)
                    .setId(campaignDetails.getCampaignId())
                    .setStartDate(campaignDetails.getStartDate())
                    .setEndDate(campaignDetails.getEndDate())
                    .setName(campaignDetails.getCampaignName())
                    .setResourceName(campaignDetails.getCampaignResourceName()) // needed for updating
                    .setGeoTargetTypeSetting(geoTargetTypeSetting)
                    .setCampaignBudget(campaignDetails.getBudgetResourceName())
                    .setManualCpc(manualCpc)
                    .setNetworkSettings(networkSettings)
                    .build();
        }
    }

    /**
     * Build a list of CampaignBudgetOperations to be used for creating or updating campaign budgets
     *
     * @param campaignDetailsList a list of campaigns containing budget details
     * @return list of CampaignBudgetOperations to be performed on the client account
     */
    private List<CampaignBudgetOperation> buildCampaignBudgetOperationList(List<CampaignDetails> campaignDetailsList,
                                                                           boolean shouldCreate) {
        //TODO: convert to using budgetDetailsList and not CampaignDetails
        List<CampaignBudgetOperation> campaignBudgetOperations = new ArrayList<>();

        for (CampaignDetails campaignDetails : campaignDetailsList) {
            // validate budget details before proceeding
            validateCampaignBudgetDetails(campaignDetails.getBudgetDetails());

            // if the budget resource name is empty then create a budget from budget details
            // otherwise if budget resource name is not empty, use continue
            if (campaignDetails.getBudgetResourceName().isEmpty()) {

                // if shouldCreate is true, create the budget within the client account
                CampaignBudgetOperation operation;
                if (shouldCreate) {
                    // build a CampaignBudget object using the given budget details
                    CampaignBudget budget = CampaignBudget.newBuilder()
                            .setName(campaignDetails.getBudgetDetails().getName())
                            .setAmountMicros(campaignDetails.getBudgetDetails().getDailyBudgetAmount() * MICRO_FACTOR)
                            .setStatus(BudgetStatusEnum.BudgetStatus
                                    .forNumber(campaignDetails.getBudgetDetails().getStatus()))
                            .setDeliveryMethod(BudgetDeliveryMethodEnum.BudgetDeliveryMethod
                                    .forNumber(campaignDetails.getBudgetDetails().getDeliveryMethod()))
                            .setExplicitlyShared(campaignDetails.getBudgetDetails().isShared()) // only allowed in budget creation
                            .build();

                    operation = CampaignBudgetOperation.newBuilder()
                            .setCreate(budget)
                            .build();
                } else {
                    // if shouldCreate is false, update the existing budget within the client account
                    CampaignBudget budget = CampaignBudget.newBuilder()
                            .setName(campaignDetails.getBudgetDetails().getName())
                            .setResourceName(campaignDetails.getBudgetDetails().getResourceName()) // must include in order to update
                            .setAmountMicros(campaignDetails.getBudgetDetails().getDailyBudgetAmount() * MICRO_FACTOR)
                            .setStatus(BudgetStatusEnum.BudgetStatus
                                    .forNumber(campaignDetails.getBudgetDetails().getStatus()))
                            .setDeliveryMethod(BudgetDeliveryMethodEnum.BudgetDeliveryMethod
                                    .forNumber(campaignDetails.getBudgetDetails().getDeliveryMethod()))
                            .build();

                    operation = CampaignBudgetOperation.newBuilder()
                            .setUpdate(budget)
                            .build();
                }

                // add the budget operation to the list of operations
                campaignBudgetOperations.add(operation);
            }
        }
        return campaignBudgetOperations;
    }

    /**
     * Build a ManualCpc object for use in creating/updating campaigns
     *
     * @param isEnhancedCpcEnabled is the campaign supporting enhanced CPC
     * @return a completed [ManualCpc]
     */
    private ManualCpc buildManualCpc(boolean isEnhancedCpcEnabled) {
        return ManualCpc.newBuilder()
                .setEnhancedCpcEnabled(isEnhancedCpcEnabled)
                .build();
    }

    /**
     * Build a GeoTargetTypeSettings object for use in creating/updating campaigns
     *
     * @param positiveGeoTargetType the positive GeoTargetType code
     * @param negativeGeoTargetType the negative GeoTargetType code
     * @return completed [Campaign.GeoTargetTypeSetting]
     */
    private Campaign.GeoTargetTypeSetting buildGeoTargetTypeSettings(int positiveGeoTargetType,
                                                                     int negativeGeoTargetType) {
        return Campaign.GeoTargetTypeSetting.newBuilder()
                .setNegativeGeoTargetType(NegativeGeoTargetTypeEnum.NegativeGeoTargetType
                        .forNumber(negativeGeoTargetType))
                .setPositiveGeoTargetType(PositiveGeoTargetTypeEnum.PositiveGeoTargetType
                        .forNumber(positiveGeoTargetType))
                .build();
    }

    /**
     * Build a Networking Settings object for use in creating/updating campaigns
     *
     * @param isTargetingContentNetwork       is this campaign targeting the content network
     * @param isTargetingPartnerSearchNetwork is this campaign targeting the partner search network
     * @param isTargetingSearchNetwork        is this campaign targeting search networking
     * @return completed [Campaign.NetworkSettings]
     */
    private Campaign.NetworkSettings buildNetworkSettings(
            boolean isTargetingContentNetwork,
            boolean isTargetingPartnerSearchNetwork,
            boolean isTargetingSearchNetwork
    ) {
        return Campaign.NetworkSettings.newBuilder()
                .setTargetContentNetwork(isTargetingContentNetwork)
                .setTargetSearchNetwork(isTargetingSearchNetwork)
                .setTargetPartnerSearchNetwork(isTargetingPartnerSearchNetwork)
                .build();
    }

    /**
     * Validate campaign details before pushing to Google Ads API
     * If validation fails throw InvalidRequestException
     *
     * @param campaignDetails [CampaignDetails] to be validated
     */
    private void validateCampaignDetails(CampaignDetails campaignDetails) {
        // validate campaign details
        ValidationErrorResponse validationErrorResponse = EntityValidator.isCampaignDetailsValid(campaignDetails);
        if (validationErrorResponse != null)
            throw new InvalidRequestException(validationErrorResponse.getErrorType(),
                    validationErrorResponse.getErrorCode(),
                    validationErrorResponse.getErrorMessage());
    }

    /**
     * Validate budget details before pushing to Google Ads API
     * If validation fails throw InvalidRequestException
     *
     * @param budgetDetails [BudgetDetails] to be validated
     */
    private void validateCampaignBudgetDetails(BudgetDetails budgetDetails) {
        // validate budget details
        ValidationErrorResponse validationErrorResponse = EntityValidator.isBudgetDetailsValid(budgetDetails);
        if (validationErrorResponse != null)
            throw new InvalidRequestException(validationErrorResponse.getErrorType(),
                    validationErrorResponse.getErrorCode(),
                    validationErrorResponse.getErrorMessage());
    }

    /**
     * Get the resource name for an existing budget, if none exists return an empty string
     *
     * @param budgetDetails      the budget details to be matched
     * @param existingBudgetList a list of existing budget details from client account
     * @return the campaign budget resource name of an existing budget or an empty string if no budget is found
     */
    private String getResourceNameForExistingBudget(BudgetDetails budgetDetails, List<BudgetDetails> existingBudgetList) {
        // check for an existing budget that has the same budget value, delivery method and shared values
        for (BudgetDetails existingBudget : existingBudgetList) {
            int budgetValue = round(budgetDetails.getDailyBudgetAmount() * MICRO_FACTOR);

            if (existingBudget.getDailyBudgetAmount() == budgetValue &&
                    existingBudget.isShared() == budgetDetails.isShared() &&
                    existingBudget.getDeliveryMethod() == budgetDetails.getDeliveryMethod()) {
                return existingBudget.getResourceName();
            }
        }
        // return an empty string if no budget currently exists
        return "";
    }

    /**
     * Retrieve a single [BudgetDetails] based on a resoure name
     *
     * @param resourceName      the resource name of the campaign budget
     * @param budgetDetailsList a list of existing budgetDetails from the client account
     * @return [BudgetDetails] that matched the resource name given
     */
    private BudgetDetails findBudgetDetailsByResourceName(String resourceName, List<BudgetDetails> budgetDetailsList) {
        for (BudgetDetails budgetDetails : budgetDetailsList) {
            if (budgetDetails.getResourceName().equals(resourceName))
                return budgetDetails;
        }

        return null;
    }
}
