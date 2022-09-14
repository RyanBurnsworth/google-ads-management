package com.addyai.rest;

import com.addyai.models.CampaignDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface CampaignController {
    ResponseEntity<List<CampaignDetails>> fetchAllClientCampaigns(@PathVariable String customerId);

    ResponseEntity<Void> createCampaigns(@PathVariable String customerId,
                                         @RequestBody List<CampaignDetails> campaignDetails);

    ResponseEntity<Void> updateCampaigns(@PathVariable String customerId,
                                        @RequestBody List<CampaignDetails> campaignDetails);

    ResponseEntity<Void> deleteCampaigns(@PathVariable String customerId,
                                         @RequestBody List<Long> campaignIds);
}
