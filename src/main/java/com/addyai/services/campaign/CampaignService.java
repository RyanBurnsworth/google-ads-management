package com.addyai.services.campaign;

import com.addyai.exceptions.CreateResourceException;
import com.addyai.exceptions.DeleteResourceException;
import com.addyai.exceptions.GetResourceException;
import com.addyai.exceptions.UpdateResourceException;
import com.addyai.models.CampaignDetails;

import java.util.List;

public interface CampaignService {

    void addCampaignsToAccount(long customerId, List<CampaignDetails> campaignDetailsList) throws CreateResourceException;

    List<CampaignDetails> getCampaignDetailsForAccount(long customerId) throws GetResourceException;

    void updateCampaign(long customerId, List<CampaignDetails> campaignDetails) throws UpdateResourceException;

    void deleteCampaigns(long customerId, List<Long> campaignIds) throws DeleteResourceException;
}
