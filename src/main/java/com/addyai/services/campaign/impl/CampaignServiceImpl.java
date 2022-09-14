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
     * @param campaignDetailsList the campaign details to be used in campaign creation
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
     * @param campaignDetailsList a list of updated campaignDetails
     * @throws Exception
     */
    @Override
    public void updateCampaign(long customerId, List<CampaignDetails> campaignDetailsList) throws Exception {
        List<CampaignOperation> campaignOperations = new ArrayList<>();

        // create an UPDATE campaign operation for each campaign
        for (CampaignDetails campaignDetails : campaignDetailsList) {
            Campaign campaign = buildCampaignFromDetails(customerId, campaignDetails);
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
     * @param budgetDetails a list of budget details to be updated
     */
    @Override
    public void updateCampaignBudgets(long customerId, List<BudgetDetails> budgetDetails) throws Exception {
        List<CampaignBudgetOperation> campaignBudgetOperations = new ArrayList<>();

        // create an UPDATE campaign operation for each campaign
        for (BudgetDetails budgetDetail : budgetDetails) {
            // validate budget details before proceeding
            validateCampaignBudgetDetails(budgetDetail);

            CampaignBudget campaignBudget = buildCampaignBudgetFromDetails(budgetDetail);
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
     * Delete a campaign budget from a client's account
     *
     * @param customerId    the customer id of the client account
     * @param budgetDetails a list of budgets to be deleted
     */
    @Override
    public void deleteCampaignBudgets(long customerId, List<BudgetDetails> budgetDetails) {

    }

    /**
     * Create a campaign object using a CampaignDetails object
     *
     * @param customerId      the customer id of the client account
     * @param campaignDetails the details of the campaign to be created
     * @return campaign object based on campaign details provided
     */
    private Campaign buildCampaignFromDetails(long customerId, CampaignDetails campaignDetails) throws Exception {
        // create a Manual cpc object with or without enhanced CPC
        ManualCpc manualCpc = ManualCpc.newBuilder()
                .setEnhancedCpcEnabled(campaignDetails.isEnhancedCpcEnabled())
                .build();

        // create a GeoTargetTypeSetting using the negative and positive targets
        Campaign.GeoTargetTypeSetting geoTargetTypeSetting = Campaign.GeoTargetTypeSetting.newBuilder()
                .setNegativeGeoTargetType(NegativeGeoTargetTypeEnum.NegativeGeoTargetType
                        .forNumber(campaignDetails.getNegativeGeoTargetType()))
                .setPositiveGeoTargetType(PositiveGeoTargetTypeEnum.PositiveGeoTargetType
                        .forNumber(campaignDetails.getPositiveGeoTargetType()))
                .build();

        // extract network settings into its own object
        Campaign.NetworkSettings networkSettings = Campaign.NetworkSettings.newBuilder()
                .setTargetContentNetwork(campaignDetails.isTargetingContentNetwork())
                .setTargetSearchNetwork(campaignDetails.isTargetingSearchNetwork())
                .setTargetPartnerSearchNetwork(campaignDetails.isTargetingPartnerSearchNetwork())
                .build();

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
     * Create a list of complete campaign details models.
     *
     * @param customerId          the customer id of the client account
     * @param baseCampaignDetails a list of campaign details models sans BudgetDetails
     * @return a list of complete CampaignDetails
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

    private CampaignBudget buildCampaignBudgetFromDetails(BudgetDetails budgetDetails) {
        return CampaignBudget.newBuilder()
                .setName(budgetDetails.getName())
                .setStatusValue(budgetDetails.getStatus())
                .setAmountMicros(budgetDetails.getDailyBudgetAmount() * 1000000L)
                .setDeliveryMethodValue(budgetDetails.getDeliveryMethod())
                .build();
    }

    private void validateCampaignDetails(CampaignDetails campaignDetails) {
        // validate campaign details
        ValidationErrorResponse validationErrorResponse = EntityValidator.isCampaignDetailsValid(campaignDetails);
        if (validationErrorResponse != null)
            throw new InvalidRequestException(validationErrorResponse.getErrorType(),
                    validationErrorResponse.getErrorCode(),
                    validationErrorResponse.getErrorMessage());
    }

    private void validateCampaignBudgetDetails(BudgetDetails budgetDetails) {
        ValidationErrorResponse validationErrorResponse = EntityValidator.isBudgetDetailsValid(budgetDetails);
        // validate budget details
        validationErrorResponse = EntityValidator.isBudgetDetailsValid(budgetDetails);
        if (validationErrorResponse != null)
            throw new InvalidRequestException(validationErrorResponse.getErrorType(),
                    validationErrorResponse.getErrorCode(),
                    validationErrorResponse.getErrorMessage());
    }
}
