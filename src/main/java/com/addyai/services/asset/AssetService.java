package com.addyai.services.asset;

import com.addyai.models.assets.AssetDetails;

import java.util.List;

public interface AssetService {
    List<AssetDetails> getAssetDetails(long customerId) throws Exception;
}
