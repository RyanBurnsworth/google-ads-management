package com.addyai.services.campaign;

import com.addyai.exceptions.DeleteResourceException;
import com.addyai.exceptions.GetResourceException;
import com.addyai.exceptions.UpdateResourceException;
import com.addyai.models.CampaignDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CampaignService {

    List<CampaignDetails> getCampaignDetailsForAccount(long customerId) throws GetResourceException;

    void updateCampaign(List<CampaignDetails> campaignDetails) throws UpdateResourceException;

    void deleteCampaigns(long customerId, List<Long> campaignIds) throws DeleteResourceException;
}
