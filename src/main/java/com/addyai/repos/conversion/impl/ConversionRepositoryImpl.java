package com.addyai.repos.conversion.impl;

import com.addyai.builder.GoogleAdsClientBuilder;
import com.addyai.error_handling.ApiExceptionResolver;
import com.addyai.models.ConversionDetails;
import com.addyai.repos.conversion.ConversionRepository;
import com.addyai.repos.request.StreamRequest;
import com.addyai.repos.request.impl.StreamRequestImpl;
import com.addyai.utils.helpers.GAQLHelper;
import com.google.ads.googleads.v14.services.ConversionActionServiceClient;
import com.google.ads.googleads.v14.services.GoogleAdsServiceClient;
import com.google.ads.googleads.v14.services.SearchGoogleAdsStreamRequest;
import com.google.ads.googleads.v14.services.SearchGoogleAdsStreamResponse;
import com.google.api.gax.rpc.ServerStream;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ConversionRepositoryImpl implements ConversionRepository {
    private final ConversionActionServiceClient conversionActionServiceClient;

    private final StreamRequest requestBuilder;

    public ConversionRepositoryImpl() {
        GoogleAdsClientBuilder googleAdsClientBuilder = GoogleAdsClientBuilder.INSTANCE;

        GoogleAdsServiceClient googleAdsServiceClient = googleAdsClientBuilder
                .getGoogleAdsClient()
                .getLatestVersion()
                .createGoogleAdsServiceClient();

        this.requestBuilder = new StreamRequestImpl(googleAdsServiceClient);

        conversionActionServiceClient = googleAdsClientBuilder
                .getGoogleAdsClient()
                .getLatestVersion()
                .createConversionActionServiceClient();
    }

    @Override
    public List<ConversionDetails> getConversionDetails(String customerId) throws Exception {
        try {
            String query = GAQLHelper.getConversionDetailsQuery();

            SearchGoogleAdsStreamRequest request = requestBuilder.buildStreamRequest(Long.parseLong(customerId), query);
            ServerStream<SearchGoogleAdsStreamResponse> response = requestBuilder.callStreamRequest(request);

            return GAQLHelper.convertStreamResponseToConversionDetails(response);
        } catch (Exception e) {
            throw ApiExceptionResolver.doResolveException(e);
        }
    }
}
