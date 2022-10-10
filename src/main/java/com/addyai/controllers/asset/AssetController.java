package com.addyai.controllers.asset;

import com.addyai.models.assets.AssetDetails;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AssetController {
    ResponseEntity<List<AssetDetails>> fetchAllAssetDetails(long customerId) throws Exception;
}
