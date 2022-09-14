package com.addyai.rest.impl;

import com.addyai.models.CampaignDetails;
import com.addyai.rest.CampaignController;
import com.addyai.services.campaign.CampaignService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/{customerId}/campaign/")
public class CampaignControllerImpl implements CampaignController {
    private final CampaignService campaignService;

    public CampaignControllerImpl(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @Override
    @GetMapping("details")
    public ResponseEntity<List<CampaignDetails>> fetchAllClientCampaigns(@PathVariable String customerId) throws Exception {
        List<CampaignDetails> campaignDetails = campaignService.findAllCampaignDetails(Long.parseLong(customerId));
        return new ResponseEntity<>(campaignDetails, HttpStatus.OK);
    }

    @Override
    @PostMapping("create")
    public ResponseEntity<Void> createCampaigns(@PathVariable String customerId,
                                                @RequestBody List<CampaignDetails> campaignDetails) throws Exception {
        campaignService.addCampaignsToAccount(Long.parseLong(customerId), campaignDetails);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @PutMapping("update")
    public ResponseEntity<Void> updateCampaigns(@PathVariable String customerId,
                                                @RequestBody List<CampaignDetails> campaignDetails) throws Exception {
        campaignService.updateCampaign(Long.parseLong(customerId), campaignDetails);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @PostMapping("remove")
    public ResponseEntity<Void> deleteCampaigns(@PathVariable String customerId,
                                                @RequestBody List<Long> campaignIds) throws Exception {
        campaignService.deleteCampaigns(Long.parseLong(customerId), campaignIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
