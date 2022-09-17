package com.addyai.rest;

import com.addyai.models.CampaignDetails;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CampaignController {
    ResponseEntity<List<CampaignDetails>> fetchAllCampaignDetails(String customerId) throws Exception;

    ResponseEntity<CampaignDetails> fetchCampaignDetailsByName(String customerId,
                                                               String campaignName) throws Exception;

    ResponseEntity<Void> createCampaigns(String customerId,
                                         List<CampaignDetails> campaignDetails) throws Exception;

    ResponseEntity<Void> updateCampaigns(String customerId,
                                         List<CampaignDetails> campaignDetails) throws Exception;

    ResponseEntity<Void> deleteCampaigns(String customerId,
                                         List<Long> campaignIds) throws Exception;
}
