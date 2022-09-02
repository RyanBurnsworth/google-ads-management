package com.addyai.services.campaign;

import com.addyai.exceptions.CreateResourceException;
import com.addyai.exceptions.DeleteResourceException;
import com.addyai.exceptions.GetResourceException;
import com.addyai.exceptions.UpdateResourceException;
import com.addyai.models.CampaignDetails;
import com.addyai.repos.campaigns.CampaignRepository;
import com.google.ads.googleads.lib.utils.FieldMasks;
import com.google.ads.googleads.v11.common.ManualCpc;
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
     * @param customerId      the customer id of the client account
     * @param campaignDetailsList the campaign details to be used in campaign creation
     * @throws CreateResourceException
     */
    @Override
    public void addCampaignsToAccount(long customerId, List<CampaignDetails> campaignDetailsList) throws CreateResourceException {
        List<CampaignOperation> campaignOperations = new ArrayList<>();

        for (CampaignDetails campaignDetails : campaignDetailsList) {
            Campaign campaign = extractCampaignFromDetails(campaignDetails);

            // create campaign operation and add to list
            CampaignOperation op = CampaignOperation.newBuilder()
                    .setCreate(campaign)
                    .build();

            campaignOperations.add(op);
        }

        campaignRepository.addCampaigns(customerId, campaignOperations);
    }

    /**
     * Fetch campaigns from a client account
     *
     * @param customerId the customer id of the client account
     * @return a list of campaign details containing all campaigns in a client's account
     * @throws GetResourceException
     */
    @Override
    public List<CampaignDetails> getCampaignDetailsForAccount(long customerId) throws GetResourceException {
        return campaignRepository.getCampaignDetails(customerId);
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

        for (CampaignDetails campaignDetails : campaignDetailsList) {
            Campaign campaign = extractCampaignFromDetails(campaignDetails);
            CampaignOperation operation = CampaignOperation.newBuilder()
                    .setUpdate(campaign)
                    .setUpdateMask(FieldMasks.allSetFieldsOf(campaign))
                    .build();

            campaignOperations.add(operation);
        }

        campaignRepository.updateCampaignDetails(customerId, campaignOperations);
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

    private Campaign extractCampaignFromDetails(CampaignDetails campaignDetails) {
        ManualCpc manualCpc = ManualCpc.newBuilder()
                .setEnhancedCpcEnabled(campaignDetails.isEnhancedCpcEnabled())
                .build();

        Campaign.GeoTargetTypeSetting geoTargetTypeSetting = Campaign.GeoTargetTypeSetting.newBuilder()
                .setNegativeGeoTargetType(campaignDetails.getNegativeGeoTargetType())
                .setPositiveGeoTargetType(campaignDetails.getPositiveGeoTargetType())
                .build();

        // extract network settings into it's own object
        Campaign.NetworkSettings networkSettings = Campaign.NetworkSettings.newBuilder()
                .setTargetContentNetwork(campaignDetails.isTargetingContentNetwork())
                .setTargetSearchNetwork(campaignDetails.isTargetingSearchNetwork())
                .setTargetGoogleSearch(campaignDetails.isTargetingGoogleSearch())
                .setTargetPartnerSearchNetwork(campaignDetails.isTargetingPartnerSearchNetwork())
                .build();

        return Campaign.newBuilder()
                .setManualCpc(manualCpc)
                .setStatus(campaignDetails.getStatus())
                .setId(campaignDetails.getCampaignId())
                .setStartDate(campaignDetails.getStartDate())
                .setEndDate(campaignDetails.getEndDate())
                .setName(campaignDetails.getCampaignName())
                .setGeoTargetTypeSetting(geoTargetTypeSetting)
                .setCampaignBudget(campaignDetails.getBudget())
                .setBiddingStrategy(campaignDetails.getBiddingStrategy())
                .setNetworkSettings(networkSettings)
                .setAdvertisingChannelType(campaignDetails.getAdvertisingChannelType())
                .build();
    }
}
