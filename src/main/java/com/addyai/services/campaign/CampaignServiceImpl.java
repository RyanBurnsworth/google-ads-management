package com.addyai.services.campaign;

import com.addyai.exceptions.CreateResourceException;
import com.addyai.exceptions.DeleteResourceException;
import com.addyai.exceptions.GetResourceException;
import com.addyai.exceptions.UpdateResourceException;
import com.addyai.models.BudgetDetails;
import com.addyai.models.CampaignDetails;
import com.addyai.repos.campaigns.CampaignRepository;
import com.google.ads.googleads.lib.utils.FieldMasks;
import com.google.ads.googleads.v11.common.ManualCpc;
import com.google.ads.googleads.v11.enums.AdvertisingChannelTypeEnum;
import com.google.ads.googleads.v11.enums.CampaignStatusEnum;
import com.google.ads.googleads.v11.enums.NegativeGeoTargetTypeEnum;
import com.google.ads.googleads.v11.enums.PositiveGeoTargetTypeEnum;
import com.google.ads.googleads.v11.resources.Campaign;
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
     * @throws CreateResourceException
     */
    @Override
    public void addCampaignsToAccount(long customerId, List<CampaignDetails> campaignDetailsList) throws CreateResourceException {
        List<CampaignOperation> campaignOperations = new ArrayList<>();

        for (CampaignDetails campaignDetails : campaignDetailsList) {
            // create a Google Ads campaign object from campaign details
            Campaign campaign = buildCampaignFromDetails(customerId, campaignDetails);

            // create a CREATE campaign operation
            CampaignOperation op = CampaignOperation.newBuilder()
                    .setCreate(campaign)
                    .build();

            // add to campaign operations list
            campaignOperations.add(op);
        }

        // add all of the campaign to the client account
        campaignRepository.addCampaigns(customerId, campaignOperations);
    }

    /**
     * Fetch campaigns from a client account
     *
     * @param customerId the customer id of the client account
     * @return a list of complete campaign details containing all campaigns in a client's account
     * @throws GetResourceException
     */
    @Override
    public List<CampaignDetails> findAllCampaignDetails(long customerId) throws GetResourceException {
        return buildCompleteCampaignDetailsList(customerId, campaignRepository.getCampaignDetails(customerId));
    }

    /**
     * Update a campaign in a Google Ads account
     *
     * @param campaignDetailsList a list of updated campaignDetails
     * @throws UpdateResourceException
     */
    @Override
    public void updateCampaign(long customerId, List<CampaignDetails> campaignDetailsList) throws UpdateResourceException {
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
     * @throws DeleteResourceException
     */
    @Override
    public void deleteCampaigns(long customerId, List<Long> campaignIds) throws DeleteResourceException {
        campaignRepository.deleteCampaigns(customerId, campaignIds);
    }

    /**
     * Create a campaign object using a CampaignDetails object
     *
     * @param customerId      the customer id of the client account
     * @param campaignDetails the details of the campaign to be created
     * @return campaign object based on campaign details provided
     */
    private Campaign buildCampaignFromDetails(long customerId, CampaignDetails campaignDetails) {
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
    private List<CampaignDetails> buildCompleteCampaignDetailsList(long customerId, List<CampaignDetails> baseCampaignDetails) {
        List<CampaignDetails> completeCampaignDetailsList = new ArrayList<>();
        List<BudgetDetails> budgetDetailsList;

        try {
            // fetch the campaign budget details from the repository
            budgetDetailsList = campaignRepository.getCampaignBudgetDetails(customerId);
        } catch (GetResourceException e) {
            throw new RuntimeException(e);
        }

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
}
