package com.addyai.repos.asset;

import com.addyai.models.assets.AssetDetails;
import com.google.ads.googleads.v14.services.AssetOperation;

import java.util.List;

public interface AssetRepository {
    List<AssetDetails> fetchAssets(long customerId) throws Exception;

    List<String> performAssetOperations(long customerId, List<AssetOperation> assetOperationList) throws Exception;
}
