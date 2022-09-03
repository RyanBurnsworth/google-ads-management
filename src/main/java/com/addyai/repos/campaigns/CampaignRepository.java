package com.addyai.repos.campaigns;

import com.addyai.exceptions.CreateResourceException;
import com.addyai.exceptions.DeleteResourceException;
import com.addyai.exceptions.GetResourceException;
import com.addyai.exceptions.UpdateResourceException;
import com.addyai.models.CampaignDetails;
import com.google.ads.googleads.v11.services.CampaignOperation;

import java.util.List;

public interface CampaignRepository {
    List<CampaignDetails> getCampaignDetails(long customerId) throws GetResourceException;

    void updateCampaigns(long customerId, List<CampaignOperation> campaignOperations) throws UpdateResourceException;

    void deleteCampaigns(long customerId, List<Long> campaignOperations) throws DeleteResourceException;

    void addCampaigns(long customerId, List<CampaignOperation> campaignOperations) throws CreateResourceException;
}
