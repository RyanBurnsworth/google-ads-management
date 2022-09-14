package com.addyai.services.campaign.impl;

import com.addyai.error_handling.ValidationErrorResponse;
import com.addyai.error_handling.exceptions.InvalidRequestException;
import com.addyai.models.BudgetDetails;
import com.addyai.models.CampaignDetails;
import com.addyai.repos.campaigns.CampaignRepository;
import com.addyai.services.campaign.CampaignService;
import com.addyai.utils.EntityValidator;
import com.google.ads.googleads.lib.utils.FieldMasks;
import com.google.ads.googleads.v11.common.ManualCpc;
import com.google.ads.googleads.v11.enums.AdvertisingChannelTypeEnum;
import com.google.ads.googleads.v11.enums.CampaignStatusEnum;
import com.google.ads.googleads.v11.enums.NegativeGeoTargetTypeEnum;
import com.google.ads.googleads.v11.enums.PositiveGeoTargetTypeEnum;
import com.google.ads.googleads.v11.resources.Campaign;
import com.google.ads.googleads.v11.resources.CampaignBudget;
import com.google.ads.googleads.v11.services.CampaignBudgetOperation;
import com.google.ads.googleads.v11.services.CampaignOperation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CampaignServiceImpl implements CampaignService {
    private final CampaignRepository campaignRepository;

    public CampaignServiceImpl(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    /**
     * Add campaigns to a client's account
     *
     * @param customerId          the customer id of the client account
     * @param campaignDetailsList [CampaignDetails] to be used in campaign creation
     * @throws Exception
     */
    @Override
    public void addCampaignsToAccount(long customerId, List<CampaignDetails> campaignDetailsList) throws Exception {
        List<CampaignOperation> campaignOperations = new ArrayList<>();

        for (CampaignDetails campaignDetails : campaignDetailsList) {
            // validate campaign details before proceeding
            validateCampaignDetails(campaignDetails);

            // create a Google Ads campaign object from campaign details
            Campaign campaign = buildCampaignFromDetails(customerId, campaignDetails);

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
     * @throws Exception
     */
    @Override
    public List<CampaignDetails> findAllCampaignDetails(long customerId) throws Exception {
        return buildCompleteCampaignDetailsList(customerId, campaignRepository.getCampaignDetails(customerId));
    }

    /**
     * Update a campaign in a Google Ads account
     *
     * @param campaignDetailsList a list of updated [CampaignDetails]
     * @throws Exception
     */
    @Override
    public void updateCampaign(long customerId, List<CampaignDetails> campaignDetailsList) throws Exception {
        List<CampaignOperation> campaignOperations = new ArrayList<>();

        // create an UPDATE campaign operation for each campaign
        for (CampaignDetails campaignDetails : campaignDetailsList) {
            Campaign campaign = buildUpdatableCampaignFromDetails(campaignDetails);
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
     * @param customerId    the customer id of the client account
     * @param budgetDetails a list of [BudgetDetails] to be updated
     */
    @Override
    public void updateCampaignBudgets(long customerId, List<BudgetDetails> budgetDetails) throws Exception {
        List<CampaignBudgetOperation> campaignBudgetOperations = new ArrayList<>();

        // create an UPDATE campaign operation for each campaign
        for (BudgetDetails budgetDetail : budgetDetails) {
            // validate budget details before proceeding
            validateCampaignBudgetDetails(budgetDetail);

            CampaignBudget campaignBudget = buildUpdatedCampaignBudgetFromDetails(budgetDetail);
            CampaignBudgetOperation operation = CampaignBudgetOperation.newBuilder()
                    .setUpdate(campaignBudget)
                    .setUpdateMask(FieldMasks.allSetFieldsOf(campaignBudget))
                    .build();

            // add newly created operation to list
            campaignBudgetOperations.add(operation);
        }

        // perform update on all campaigns
        campaignRepository.updateCampaignBudgets(customerId, campaignBudgetOperations);
    }

    /**
     * Generate a campaign object using a CampaignDetails object (for use in creating new campaigns)
     *
     * @param customerId      the customer id of the client account
     * @param campaignDetails [CampaignDetails] to be parsed into a [Campaign]
     * @return [Campaign] parsed from [CampaignDetails] provided
     */
    private Campaign buildCampaignFromDetails(long customerId, CampaignDetails campaignDetails) throws Exception {
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

        // if needed, create the budget within the client account and set the resource name
        if (campaignDetails.getBudgetResourceName() == null ||
                campaignDetails.getBudgetResourceName().isEmpty())
            campaignDetails.setBudgetResourceName(campaignRepository
                    .createSingleCampaignBudget(customerId, campaignDetails.getBudgetDetails()));

        // create and return a campaign object with the above settings
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
                .setAdvertisingChannelType(advertisingChannelType)
                .build();
    }

    /**
     * Generate a campaign object using a CampaignDetails object (for use in updating existing campaigns)
     *
     * @param campaignDetails [CampaignDetails] to be parsed into a [Campaign]
     * @return [Campaign] parsed from [CampaignDetails] provided
     */
    private Campaign buildUpdatableCampaignFromDetails(CampaignDetails campaignDetails) {
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

        // create and return a campaign object with the above settings
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

    /**
     * Create a list of complete campaign details models.
     *
     * @param customerId          the customer id of the client account
     * @param baseCampaignDetails a list of [CampaignDetails] models sans BudgetDetails
     * @return a list of complete [CampaignDetails]
     */
    private List<CampaignDetails> buildCompleteCampaignDetailsList(long customerId, List<CampaignDetails> baseCampaignDetails) throws Exception {
        List<CampaignDetails> completeCampaignDetailsList = new ArrayList<>();
        List<BudgetDetails> budgetDetailsList;

        // fetch the campaign budget details from the repository
        budgetDetailsList = campaignRepository.getCampaignBudgetDetails(customerId);

        // associate each campaign with a budget by resource name
        for (CampaignDetails campaignDetails : baseCampaignDetails) {
            for (BudgetDetails budgetDetails : budgetDetailsList) {
                // set budgetDetails in campaignDetails model if matched
                if (campaignDetails.getBudgetResourceName().equals(budgetDetails.getResourceName())) {
                    campaignDetails.setBudgetDetails(budgetDetails);

                    // add updated campaign details model to completed list
                    completeCampaignDetailsList.add(campaignDetails);
                    break;
                }
            }
        }

        return completeCampaignDetailsList;
    }

    /**
     * Build a [CampaignBudgetObject] using [BudgetDetails] for use in updating campaign budgets
     *
     * @param budgetDetails the budget details to use in creating a campaign budget
     * @return [CampaignBudget]
     */
    private CampaignBudget buildUpdatedCampaignBudgetFromDetails(BudgetDetails budgetDetails) {
        return CampaignBudget.newBuilder()
                .setResourceName(budgetDetails.getResourceName())
                .setStatusValue(budgetDetails.getStatus())
                .setAmountMicros(budgetDetails.getDailyBudgetAmount() * 1000000L)
                .setDeliveryMethodValue(budgetDetails.getDeliveryMethod())
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
        ValidationErrorResponse validationErrorResponse = EntityValidator.isBudgetDetailsValid(budgetDetails);
        // validate budget details
        validationErrorResponse = EntityValidator.isBudgetDetailsValid(budgetDetails);
        if (validationErrorResponse != null)
            throw new InvalidRequestException(validationErrorResponse.getErrorType(),
                    validationErrorResponse.getErrorCode(),
                    validationErrorResponse.getErrorMessage());
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
     * @param isTargetingSearchNetwork        is this campaign tareting search networking
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
}
