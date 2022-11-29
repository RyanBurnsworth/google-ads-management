package com.addyai.controllers.asset;

import com.addyai.models.assets.AssetDetails;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AssetController {
    ResponseEntity<List<AssetDetails>> fetchAllAssetDetails(long customerId) throws Exception;

    ResponseEntity<Void> addAssets(long customerId,
                                   String assetLevel,
                                   String campaignResName,
                                   List<AssetDetails> assetDetails) throws Exception;

    ResponseEntity<Void> updateAssets(long customerId,
                                      String assetLevel,
                                      String campaignResName,
                                      List<AssetDetails> assetDetailsList) throws Exception;

    ResponseEntity<Void> unlinkAssets(long customerId,
                                      String assetLevel,
                                      String campaignResName,
                                      String assetFieldType,
                                      List<String> assetResourceNames) throws Exception;
}
