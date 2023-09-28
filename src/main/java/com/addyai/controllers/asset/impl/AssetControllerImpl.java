package com.addyai.controllers.asset.impl;

import com.addyai.controllers.asset.AssetController;
import com.addyai.enums.AssetLevel;
import com.addyai.models.assets.AssetDetails;
import com.addyai.services.asset.AssetService;
import com.google.ads.googleads.v14.enums.AssetFieldTypeEnum;
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
    public ResponseEntity<Void> addAssets(@PathVariable long customerId,
                                          @RequestParam String assetLevel,
                                          @RequestParam String campaignResName,
                                          @RequestBody List<AssetDetails> assetDetails) throws Exception {
        assetService.upsertAssets(customerId, assetDetails, assetLevel, campaignResName, true);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    @PutMapping("update")
    public ResponseEntity<Void> updateAssets(@PathVariable long customerId,
                                             @RequestParam String assetLevel,
                                             @RequestParam String campaignResName,
                                             @RequestBody List<AssetDetails> assetDetailsList) throws Exception {
        assetService.upsertAssets(customerId, assetDetailsList, assetLevel, campaignResName, false);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @PostMapping("remove")
    public ResponseEntity<Void> unlinkAssets(@PathVariable long customerId,
                                             @RequestParam String assetLevel,
                                             @RequestParam String campaignResName,
                                             @RequestParam String assetFieldType,
                                             @RequestBody List<String> assetResourceNames) throws Exception {
        AssetFieldTypeEnum.AssetFieldType fieldType =
                AssetFieldTypeEnum.AssetFieldType.valueOf(assetFieldType);

        if (AssetLevel.valueOf(assetLevel).equals(AssetLevel.ACCOUNT_LEVEL)) {
            assetService.performCustomerAssetOperation(customerId,
                    assetResourceNames,
                    fieldType,
                    false);
        } else if (AssetLevel.valueOf(assetLevel).equals(AssetLevel.CAMPAIGN_LEVEL)) {
            assetService.performCampaignAssetOperation(customerId,
                    assetResourceNames,
                    fieldType,
                    campaignResName,
                    false);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
