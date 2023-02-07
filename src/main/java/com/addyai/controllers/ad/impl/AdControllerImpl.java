package com.addyai.controllers.ad.impl;

import com.addyai.controllers.ad.AdController;
import com.addyai.models.ads.AdDetails;
import com.addyai.services.ads.SearchAdService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/{customerId}/ad/")
public class AdControllerImpl implements AdController {
    private final SearchAdService searchAdService;

    public AdControllerImpl(SearchAdService searchAdService) {
        this.searchAdService = searchAdService;
    }

    @Override
    @GetMapping("details")
    public ResponseEntity<List<AdDetails>> fetchAdDetailsByAdGroup(@PathVariable long customerId,
                                                                   @RequestParam String adGroupId) throws Exception {
        String adGroupResourceName = "customers/" + customerId + "/adGroups/" + adGroupId;
        List<AdDetails> adDetailsList = searchAdService.findAllAdsByAdGroup(customerId, adGroupResourceName);
        return new ResponseEntity<>(adDetailsList, HttpStatus.OK);
    }

    @Override
    @PostMapping("create")
    public ResponseEntity<Void> addAdsToAdGroup(@PathVariable long customerId,
                                                @RequestParam String adGroupId,
                                                @RequestBody List<AdDetails> adDetailsList) throws Exception {
        String adGroupResourceName = "customers/" + customerId + "/adGroups/" + adGroupId;
        searchAdService.upsertAds(customerId, adGroupResourceName, adDetailsList, true);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    @PutMapping("update")
    public ResponseEntity<Void> updateAdsInAdGroup(@PathVariable long customerId,
                                                   @RequestParam String adGroupId,
                                                   @RequestBody List<AdDetails> adDetailsList) throws Exception {
        String adGroupResourceName = "customers/" + customerId + "/adGroups/" + adGroupId;
        searchAdService.deleteAds(customerId, adDetailsList, adGroupResourceName);
        searchAdService.upsertAds(customerId, adGroupResourceName, adDetailsList, true);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @Override
    @PostMapping("remove")
    public ResponseEntity<Void> deleteAdsInAdGroup(@PathVariable long customerId,
                                                   @RequestParam String adGroupId,
                                                   @RequestBody List<AdDetails> adDetailsList) throws Exception {
        String adGroupResourceName = "customers/" + customerId + "/adGroups/" + adGroupId;
        searchAdService.deleteAds(customerId, adDetailsList, adGroupResourceName);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
