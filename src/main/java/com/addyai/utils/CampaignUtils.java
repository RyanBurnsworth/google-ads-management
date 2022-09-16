package com.addyai.utils;

import com.addyai.error_handling.ValidationErrorResponse;
import com.addyai.error_handling.exceptions.InvalidRequestException;
import com.addyai.models.BudgetDetails;
import com.addyai.models.CampaignDetails;
import com.google.ads.googleads.v11.common.ManualCpc;
import com.google.ads.googleads.v11.enums.*;
import com.google.ads.googleads.v11.resources.Campaign;
import com.google.ads.googleads.v11.resources.CampaignBudget;
import com.google.ads.googleads.v11.services.CampaignBudgetOperation;

import java.util.ArrayList;
import java.util.List;

import static com.addyai.utils.Constants.MICRO_FACTOR;

public class CampaignUtils {

    /**
     * Generate a campaign object using a CampaignDetails object (for use in creating new campaigns)
     *
     * @param campaignDetails [CampaignDetails] to be parsed into a [Campaign]
     * @param shouldCreate    true/false if this campaign should be created or updated
     * @return [Campaign] parsed from [CampaignDetails] provided
     */
    public Campaign buildCampaignFromDetails(CampaignDetails campaignDetails,
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
     * @param budgetDetailsList a list of budget details
     * @param shouldCreate      true if it should create budget, false if it should update
     * @return list of CampaignBudgetOperations to be performed on the client account
     */
    public List<CampaignBudgetOperation> buildCampaignBudgetOperationList(List<BudgetDetails> budgetDetailsList,
                                                                          boolean shouldCreate) {
        List<CampaignBudgetOperation> campaignBudgetOperations = new ArrayList<>();

        for (BudgetDetails budgetDetails : budgetDetailsList) {
            // if the budget resource name is empty then create a budget from budget details
            // otherwise if budget resource name is not empty, use continue
            if (budgetDetails.getResourceName().isEmpty()) {

                // if shouldCreate is true, create the budget within the client account
                CampaignBudgetOperation operation;
                if (shouldCreate) {
                    // build a CampaignBudget object using the given budget details
                    CampaignBudget budget = CampaignBudget.newBuilder()
                            .setName(budgetDetails.getName())
                            .setAmountMicros(budgetDetails.getDailyBudgetAmount() * MICRO_FACTOR)
                            .setStatus(BudgetStatusEnum.BudgetStatus
                                    .forNumber(budgetDetails.getStatus()))
                            .setDeliveryMethod(BudgetDeliveryMethodEnum.BudgetDeliveryMethod
                                    .forNumber(budgetDetails.getDeliveryMethod()))
                            .setExplicitlyShared(budgetDetails.isShared()) // only allowed in budget creation
                            .build();

                    operation = CampaignBudgetOperation.newBuilder()
                            .setCreate(budget)
                            .build();
                } else {
                    // if shouldCreate is false, update the existing budget within the client account
                    CampaignBudget budget = CampaignBudget.newBuilder()
                            .setName(budgetDetails.getName())
                            .setResourceName(budgetDetails.getResourceName()) // must include in order to update
                            .setAmountMicros(budgetDetails.getDailyBudgetAmount() * MICRO_FACTOR)
                            .setStatus(BudgetStatusEnum.BudgetStatus
                                    .forNumber(budgetDetails.getStatus()))
                            .setDeliveryMethod(BudgetDeliveryMethodEnum.BudgetDeliveryMethod
                                    .forNumber(budgetDetails.getDeliveryMethod()))
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
    public void validateCampaignDetails(CampaignDetails campaignDetails) {
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
    public void validateCampaignBudgetDetails(BudgetDetails budgetDetails) {
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
    public String getResourceNameForExistingBudget(BudgetDetails budgetDetails, List<BudgetDetails> existingBudgetList) {
        // check for an existing budget that has the same budget value, delivery method and shared values
        for (BudgetDetails existingBudget : existingBudgetList) {

            if (existingBudget.getDailyBudgetAmount() == budgetDetails.getDailyBudgetAmount() &&
                    existingBudget.isShared() == budgetDetails.isShared() &&
                    existingBudget.getDeliveryMethod() == budgetDetails.getDeliveryMethod()) {
                return existingBudget.getResourceName();
            }
        }
        // return an empty string if no budget currently exists
        return "";
    }

    /**
     * Retrieve a single [BudgetDetails] based on a resource name
     *
     * @param resourceName      the resource name of the campaign budget
     * @param budgetDetailsList a list of existing budgetDetails from the client account
     * @return [BudgetDetails] that matched the resource name given
     */
    public BudgetDetails findBudgetDetailsByResourceName(String resourceName, List<BudgetDetails> budgetDetailsList) {
        for (BudgetDetails budgetDetails : budgetDetailsList) {
            if (budgetDetails.getResourceName().equals(resourceName))
                return budgetDetails;
        }

        return null;
    }
}
