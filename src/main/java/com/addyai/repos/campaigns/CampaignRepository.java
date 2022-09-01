package com.addyai.repos.campaigns;

import com.addyai.exceptions.CreateResourceException;
import com.addyai.exceptions.DeleteResourceException;
import com.addyai.exceptions.GetResourceException;
import com.addyai.exceptions.UpdateResourceException;
import com.addyai.models.CampaignDetails;
import com.addyai.models.OperationResponse;
import com.google.ads.googleads.v11.resources.Campaign;
import com.google.ads.googleads.v11.services.CampaignOperation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignRepository {
    List<CampaignDetails> getCampaignDetails(long customerId) throws GetResourceException;

    List<OperationResponse> updateCampaignDetails(long customerId, List<CampaignOperation> campaignOperations) throws UpdateResourceException;

    List<OperationResponse> deleteCampaigns(long customerId, List<Long> campaignOperations) throws DeleteResourceException;

    void addCampaigns(long customerId, List<CampaignDetails> campaignDetails, List<Campaign.NetworkSettings> networkSettingsList) throws CreateResourceException;
}
