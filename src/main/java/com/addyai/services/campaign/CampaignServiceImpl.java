package com.addyai.services.campaign;

import com.addyai.exceptions.DeleteResourceException;
import com.addyai.exceptions.GetResourceException;
import com.addyai.exceptions.UpdateResourceException;
import com.addyai.models.CampaignDetails;
import com.addyai.repos.campaigns.CampaignRepository;
import com.google.ads.googleads.lib.utils.FieldMasks;
import com.google.ads.googleads.v11.common.ManualCpc;
import com.google.ads.googleads.v11.resources.Campaign;
import com.google.ads.googleads.v11.services.CampaignOperation;

import java.util.ArrayList;
import java.util.List;

public class CampaignServiceImpl implements CampaignService {
    private final CampaignRepository campaignRepository;

    public CampaignServiceImpl(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

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
    public void updateCampaign(List<CampaignDetails> campaignDetailsList) throws UpdateResourceException {
        List<CampaignOperation> campaignOperations = new ArrayList<>();

        for (CampaignDetails campaignDetails : campaignDetailsList) {
            ManualCpc manualCpc = ManualCpc.newBuilder()
                    .setEnhancedCpcEnabled(campaignDetails.isEnhancedCpcEnabled())
                    .build();

            Campaign.GeoTargetTypeSetting geoTargetTypeSetting = Campaign.GeoTargetTypeSetting.newBuilder()
                    .setNegativeGeoTargetType(campaignDetails.getNegativeGeoTargetType())
                    .setPositiveGeoTargetType(campaignDetails.getPositiveGeoTargetType())
                    .build();

            Campaign campaign = Campaign.newBuilder()
                    .setManualCpc(manualCpc)
                    .setStatus(campaignDetails.getStatus())
                    .setId(campaignDetails.getCampaignId())
                    .setEndDate(campaignDetails.getEndDate())
                    .setName(campaignDetails.getCampaignName())
                    .setGeoTargetTypeSetting(geoTargetTypeSetting)
                    .setCampaignBudget(campaignDetails.getBudget())
                    .setBiddingStrategy(campaignDetails.getBiddingStrategy())
                    .setAdvertisingChannelType(campaignDetails.getAdvertisingChannelType())
                    .build();

            CampaignOperation operation = CampaignOperation.newBuilder()
                    .setUpdate(campaign)
                    .setUpdateMask(FieldMasks.allSetFieldsOf(campaign))
                    .build();

            campaignOperations.add(operation);
        }

        campaignRepository.updateCampaignDetails(0L, campaignOperations);
    }

    @Override
    public void deleteCampaigns(long customerId, List<Long> campaignIds) throws DeleteResourceException {
        campaignRepository.deleteCampaigns(customerId, campaignIds);
    }
}
