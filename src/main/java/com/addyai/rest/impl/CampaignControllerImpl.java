package com.addyai.rest.impl;

import com.addyai.models.BudgetDetails;
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
    public ResponseEntity<List<CampaignDetails>> fetchAllCampaignDetails(@PathVariable String customerId) throws Exception {
        List<CampaignDetails> campaignDetails = campaignService.findAllCampaignDetails(Long.parseLong(customerId));
        return new ResponseEntity<>(campaignDetails, HttpStatus.OK);
    }

    @Override
    @GetMapping("{campaignName}/details")
    public ResponseEntity<CampaignDetails> fetchCampaignDetailsByName(@PathVariable String customerId,
                                                                      @PathVariable String campaignName) throws Exception {
        CampaignDetails campaignDetails = campaignService
                .findCampaignDetailsByName(Long.parseLong(customerId), campaignName);

        return new ResponseEntity<>(campaignDetails, HttpStatus.OK);
    }

    @Override
    @PostMapping("/create")
    public ResponseEntity<Void> createCampaigns(@PathVariable String customerId,
                                                @RequestBody List<CampaignDetails> campaignDetails) throws Exception {
        campaignService.addCampaignsToAccount(Long.parseLong(customerId), campaignDetails);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    @PutMapping("/update")
    public ResponseEntity<Void> updateCampaigns(@PathVariable String customerId,
                                                @RequestBody List<CampaignDetails> campaignDetails) throws Exception {
        campaignService.updateCampaigns(Long.parseLong(customerId), campaignDetails);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @Override
    @PostMapping("/remove")
    public ResponseEntity<Void> deleteCampaigns(@PathVariable String customerId,
                                                @RequestBody List<Long> campaignIds) throws Exception {
        campaignService.deleteCampaigns(Long.parseLong(customerId), campaignIds);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @Override
    @PostMapping("/budget/update")
    public ResponseEntity<Void> updateCampaignBudgets(String customerId, List<BudgetDetails> budgetDetails) throws Exception {
        campaignService.updateCampaignBudgets(Long.parseLong(customerId), budgetDetails);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
