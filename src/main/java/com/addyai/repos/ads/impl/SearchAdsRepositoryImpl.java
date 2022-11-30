package com.addyai.repos.ads.impl;

import com.addyai.builder.GoogleAdsClientBuilder;
import com.addyai.error_handling.ApiExceptionResolver;
import com.addyai.models.ads.AdDetails;
import com.addyai.repos.ads.SearchAdsRepository;
import com.addyai.repos.request.StreamRequest;
import com.addyai.repos.request.impl.StreamRequestImpl;
import com.addyai.utils.helpers.GAQLHelper;
import com.google.ads.googleads.v12.services.*;
import com.google.api.gax.rpc.ServerStream;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SearchAdsRepositoryImpl implements SearchAdsRepository {
    private final AdGroupAdServiceClient adGroupAdServiceClient;

    private final StreamRequest requestBuilder;

    public SearchAdsRepositoryImpl() {
        GoogleAdsClientBuilder googleAdsClientBuilder = GoogleAdsClientBuilder.INSTANCE;

        GoogleAdsServiceClient googleAdsServiceClient = googleAdsClientBuilder
                .getGoogleAdsClient()
                .getLatestVersion()
                .createGoogleAdsServiceClient();

        this.requestBuilder = new StreamRequestImpl(googleAdsServiceClient);

        adGroupAdServiceClient = googleAdsClientBuilder
                .getGoogleAdsClient()
                .getLatestVersion()
                .createAdGroupAdServiceClient();
    }

    @Override
    public List<AdDetails> fetchAdDetails(long customerId, String adGroupResName, String adType) throws Exception {
        List<AdDetails> adDetailsList;

        try {
            String query = GAQLHelper.getResponsiveSearchAdQuery(adGroupResName);

            SearchGoogleAdsStreamRequest request = requestBuilder.buildStreamRequest(customerId, query);
            ServerStream<SearchGoogleAdsStreamResponse> response = requestBuilder.callStreamRequest(request);

            adDetailsList = GAQLHelper.convertStreamToAdDetails(response, adType);

            return adDetailsList;
        } catch (Exception e) {
            throw ApiExceptionResolver.doResolveException(e);
        }
    }

    @Override
    public List<String> performSearchAdOperations(long customerId, List<AdGroupAdOperation> adGroupAdOperations) throws Exception {
        List<String> adResourceNameList = new ArrayList<>();

        try {
            MutateAdGroupAdsResponse response =
                    adGroupAdServiceClient.mutateAdGroupAds(Long.toString(customerId), adGroupAdOperations);

            for (MutateAdGroupAdResult result : response.getResultsList()) {
                adResourceNameList.add(result.getResourceName());
            }
            return adResourceNameList;
        } catch (Exception e) {
            throw ApiExceptionResolver.doResolveException(e);
        }
    }
}
