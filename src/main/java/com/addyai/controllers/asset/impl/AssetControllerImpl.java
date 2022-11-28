package com.addyai.controllers.asset.impl;

import com.addyai.controllers.asset.AssetController;
import com.addyai.models.assets.AssetDetails;
import com.addyai.services.asset.AssetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/{customerId}/assets/")
public class AssetControllerImpl implements AssetController {
    private final AssetService assetService;

    public AssetControllerImpl(AssetService assetService) {
        this.assetService = assetService;
    }

    @Override
    @GetMapping("details")
    public ResponseEntity<List<AssetDetails>> fetchAllAssetDetails(@PathVariable long customerId) throws Exception {
        List<AssetDetails> assetDetails = assetService.getAssetDetails(customerId);
        return new ResponseEntity<>(assetDetails, HttpStatus.OK);
    }

    @Override
    @PostMapping("create")
    public ResponseEntity<Void> addSitelinks(@PathVariable long customerId,
                                          @RequestBody List<AssetDetails> assetDetails) throws Exception {
        assetService.upsertAssets(customerId, assetDetails, true);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @Override
    @PostMapping("update")
    public ResponseEntity<Void> updateAssets(@PathVariable long customerId,
                                             @RequestBody List<AssetDetails> assetDetailsList) throws Exception {
        assetService.upsertAssets(customerId, assetDetailsList, false);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
