package com.addyai.repos.campaigns;

import com.addyai.models.CampaignDetails;
import com.google.ads.googleads.v11.services.CampaignOperation;

import java.util.List;

public interface CampaignRepository {
    List<CampaignDetails> fetchAllCampaignDetails(long customerId) throws Exception;

    CampaignDetails fetchCampaignDetailsByName(long customerId, String campaignName) throws Exception;

    void updateCampaigns(long customerId, List<CampaignOperation> campaignOperations) throws Exception;

    void deleteCampaigns(long customerId, List<Long> campaignOperations) throws Exception;

    List<String> addCampaigns(long customerId, List<CampaignOperation> campaignOperations) throws Exception;
}
