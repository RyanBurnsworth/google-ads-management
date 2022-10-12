package com.addyai.repos.asset.impl;

import com.addyai.builder.GoogleAdsClientBuilder;
import com.addyai.error_handling.ApiExceptionResolver;
import com.addyai.models.assets.AssetDetails;
import com.addyai.repos.asset.AssetRepository;
import com.addyai.repos.request.StreamRequest;
import com.addyai.repos.request.impl.StreamRequestImpl;
import com.addyai.utils.helpers.GAQLHelper;
import com.google.ads.googleads.v11.enums.AssetTypeEnum;
import com.google.ads.googleads.v11.services.*;
import com.google.api.gax.rpc.ServerStream;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.addyai.utils.misc.Constants.NUM_ASSET_CLASSES_SUPPORTED;

@Repository
public class AssetRepositoryImpl implements AssetRepository {
    private final StreamRequest requestBuilder;

    private final AssetServiceClient assetServiceClient;

    public AssetRepositoryImpl() {
        GoogleAdsClientBuilder googleAdsClientBuilder = GoogleAdsClientBuilder.INSTANCE;

        GoogleAdsServiceClient googleAdsServiceClient = googleAdsClientBuilder
                .getGoogleAdsClient()
                .getLatestVersion()
                .createGoogleAdsServiceClient();

        this.requestBuilder = new StreamRequestImpl(googleAdsServiceClient);

        this.assetServiceClient = googleAdsClientBuilder.getGoogleAdsClient()
                .getLatestVersion().createAssetServiceClient();
    }

    @Override
    public List<AssetDetails> fetchAssets(long customerId) throws Exception {
        List<AssetDetails> assetDetailsList = new ArrayList<>();
        SearchGoogleAdsStreamRequest request;
        ServerStream<SearchGoogleAdsStreamResponse> response;
        String query;

        try {
            for (int i = 0; i < NUM_ASSET_CLASSES_SUPPORTED; i++) {
                switch (i) {
                    case 0:
                        query = GAQLHelper.getSitelinksAssetQuery();

                        request = requestBuilder.buildStreamRequest(customerId, query);
                        response = requestBuilder.callStreamRequest(request);

                        List<AssetDetails> sitelinkDetails =
                                GAQLHelper.convertStreamResponseToAssetDetails(
                                        response,
                                        AssetTypeEnum.AssetType.SITELINK);
                        assetDetailsList.addAll(sitelinkDetails);
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            throw ApiExceptionResolver.doResolveException(e);
        }
        return assetDetailsList;
    }

    @Override
    public List<String> performAssetOperations(long customerId, List<AssetOperation> assetOperationList) throws Exception {
        try {
            MutateAssetsResponse assetsResponse = assetServiceClient.mutateAssets(
                    Long.toString(customerId), assetOperationList);
        } catch (Exception e) {
            throw ApiExceptionResolver.doResolveException(e);
        }
        return null; // TODO: update when knowing more
    }
}
