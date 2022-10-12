package com.addyai.controllers.asset;

import com.addyai.models.assets.AssetDetails;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AssetController {
    ResponseEntity<List<AssetDetails>> fetchAllAssetDetails(long customerId) throws Exception;

    ResponseEntity<Void> addSitelinks(long customerId, List<AssetDetails> assetDetails) throws Exception;

    ResponseEntity<Void> updateAssets(long customerId, List<AssetDetails> assetDetailsList) throws Exception;
}
