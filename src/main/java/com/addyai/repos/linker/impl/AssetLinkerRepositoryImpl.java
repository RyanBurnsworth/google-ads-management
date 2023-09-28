package com.addyai.repos.linker.impl;

import com.addyai.builder.GoogleAdsClientBuilder;
import com.addyai.error_handling.ApiExceptionResolver;
import com.addyai.repos.linker.AssetLinkerRepository;
import com.google.ads.googleads.v14.services.CampaignAssetOperation;
import com.google.ads.googleads.v14.services.CampaignAssetServiceClient;
import com.google.ads.googleads.v14.services.CustomerAssetOperation;
import com.google.ads.googleads.v14.services.CustomerAssetServiceClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AssetLinkerRepositoryImpl implements AssetLinkerRepository {
    private final CustomerAssetServiceClient customerAssetServiceClient;

    private final CampaignAssetServiceClient campaignAssetServiceClient;

    public AssetLinkerRepositoryImpl() {
        GoogleAdsClientBuilder googleAdsClientBuilder = GoogleAdsClientBuilder.INSTANCE;

        customerAssetServiceClient = googleAdsClientBuilder
                .getGoogleAdsClient()
                .getLatestVersion()
                .createCustomerAssetServiceClient();

        campaignAssetServiceClient = googleAdsClientBuilder
                .getGoogleAdsClient()
                .getLatestVersion()
                .createCampaignAssetServiceClient();
    }

    @Override
    public void performCampaignAssetOperation(long customerId, List<CampaignAssetOperation> campaignAssetOperationList) throws Exception {
        try {
            campaignAssetServiceClient
                    .mutateCampaignAssets(Long.toString(customerId), campaignAssetOperationList);
        } catch (Exception e) {
            throw ApiExceptionResolver.doResolveException(e);
        }
    }

    @Override
    public void performCustomerAssetOperation(long customerId, List<CustomerAssetOperation> customerAssetOperationList) throws Exception {
        try {
            customerAssetServiceClient
                    .mutateCustomerAssets(Long.toString(customerId), customerAssetOperationList);
        } catch (Exception e) {
            throw ApiExceptionResolver.doResolveException(e);
        }
    }
}
