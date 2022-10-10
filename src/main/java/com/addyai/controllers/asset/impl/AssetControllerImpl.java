package com.addyai.controllers.asset.impl;

import com.addyai.controllers.asset.AssetController;
import com.addyai.models.assets.AssetDetails;
import com.addyai.services.asset.AssetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
