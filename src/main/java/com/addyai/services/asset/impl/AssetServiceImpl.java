package com.addyai.services.asset.impl;

import com.addyai.models.assets.AssetDetails;
import com.addyai.repos.asset.AssetRepository;
import com.addyai.services.asset.AssetService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetServiceImpl implements AssetService {
    private final AssetRepository assetRepository;

    public AssetServiceImpl(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    @Override
    public List<AssetDetails> getAssetDetails(long customerId) throws Exception {
        return assetRepository.fetchAssets(customerId);
    }
}
