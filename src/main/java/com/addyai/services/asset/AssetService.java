package com.addyai.services.asset;

import com.addyai.models.assets.AssetDetails;

import java.util.List;

public interface AssetService {
    List<AssetDetails> getAssetDetails(long customerId) throws Exception;

    void upsertAssets(long customerId, List<AssetDetails> assetDetailsList, boolean shouldCreate) throws Exception;
}
