package com.addyai.rest;

import com.addyai.exceptions.CreateResourceException;
import com.addyai.exceptions.GetResourceException;
import com.addyai.models.CampaignDetails;
import com.addyai.services.campaign.CampaignService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/{customerId}/campaign/")
public class CampaignController {
    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @GetMapping("details")
    List<CampaignDetails> getCampaignDetails(@PathVariable String customerId) {
        try {
            return campaignService.getCampaignDetailsForAccount(Long.parseLong(customerId));
        } catch (GetResourceException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("create")
    void createCampaigns(@PathVariable String customerId, @RequestBody List<CampaignDetails> campaignDetails) {
        try {
            campaignService.addCampaignsToAccount(Long.parseLong(customerId), campaignDetails);
        } catch (CreateResourceException e) {
            throw new RuntimeException(e);
        }
    }
}
