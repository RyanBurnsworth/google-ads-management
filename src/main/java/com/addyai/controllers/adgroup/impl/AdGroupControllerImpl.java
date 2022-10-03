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

package com.addyai.controllers.adgroup.impl;

import com.addyai.controllers.adgroup.AdGroupController;
import com.addyai.models.AdGroupDetails;
import com.addyai.services.adgroup.AdGroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/{customerId}/adgroup/")
public class AdGroupControllerImpl implements AdGroupController {
    private final AdGroupService adGroupService;

    public AdGroupControllerImpl(AdGroupService adGroupService) {
        this.adGroupService = adGroupService;
    }

    @Override
    @GetMapping("details")
    public ResponseEntity<List<AdGroupDetails>> fetchAllAdGroupDetails(@PathVariable long customerId,
                                                                       @RequestParam String campaignResName) throws Exception {
        List<AdGroupDetails> adGroupDetailsList = adGroupService.findAllAdGroupsByCampaign(customerId, campaignResName);
        return new ResponseEntity<>(adGroupDetailsList, HttpStatus.OK);
    }

    @Override
    @PostMapping("create")
    public ResponseEntity<Void> createAdGroups(@PathVariable String customerId,
                                               @RequestBody List<AdGroupDetails> adGroupDetailsList) throws Exception {
        adGroupService.upsertAdGroups(Long.parseLong(customerId), adGroupDetailsList, true);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    @PostMapping("update")
    public ResponseEntity<Void> updateAdGroups(@PathVariable String customerId,
                                               @RequestBody List<AdGroupDetails> adGroupDetailsList) throws Exception {
        adGroupService.upsertAdGroups(Long.parseLong(customerId), adGroupDetailsList, false);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @PostMapping("remove")
    public ResponseEntity<Void> deleteAdGroups(@PathVariable String customerId,
                                               @RequestBody List<AdGroupDetails> adGroupDetailsList) throws Exception {
        adGroupService.deleteAdGroups(Long.parseLong(customerId), adGroupDetailsList);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
