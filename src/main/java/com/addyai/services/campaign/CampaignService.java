package com.addyai.services.campaign;

import com.addyai.models.CampaignDetails;

import java.util.List;

public interface CampaignService {

    void addCampaignsToAccount(long customerId, List<CampaignDetails> campaignDetailsList) throws Exception;

    List<CampaignDetails> findAllCampaignDetails(long customerId) throws Exception;

    CampaignDetails findCampaignDetailsByName(long customerId, String campaignName) throws Exception;

    void updateCampaigns(long customerId, List<CampaignDetails> campaignDetails) throws Exception;

    void deleteCampaigns(long customerId, List<Long> campaignIds) throws Exception;
}
