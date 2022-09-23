/*
 * Copyright (c) 2022.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. This program
 * is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 *
 */

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
        campaignService.upsertCampaigns(Long.parseLong(customerId), campaignDetails, true);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    @PutMapping("/update")
    public ResponseEntity<Void> updateCampaigns(@PathVariable String customerId,
                                                @RequestBody List<CampaignDetails> campaignDetails) throws Exception {
        campaignService.upsertCampaigns(Long.parseLong(customerId), campaignDetails, false);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @PostMapping("/remove")
    public ResponseEntity<Void> deleteCampaigns(@PathVariable String customerId,
                                                @RequestBody List<CampaignDetails> campaignDetails) throws Exception {
        campaignService.deleteCampaigns(Long.parseLong(customerId), campaignDetails);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
