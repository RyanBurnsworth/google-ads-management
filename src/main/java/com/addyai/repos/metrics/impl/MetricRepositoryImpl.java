package com.addyai.repos.metrics.impl;

import com.addyai.builder.GoogleAdsClientBuilder;
import com.addyai.error_handling.ApiExceptionResolver;
import com.addyai.models.metrics.CampaignMetrics;
import com.addyai.repos.metrics.MetricRepository;
import com.addyai.repos.request.StreamRequest;
import com.addyai.repos.request.impl.StreamRequestImpl;
import com.addyai.utils.helpers.MetricsHelper;
import com.google.ads.googleads.v12.services.GoogleAdsServiceClient;
import com.google.ads.googleads.v12.services.SearchGoogleAdsStreamRequest;
import com.google.ads.googleads.v12.services.SearchGoogleAdsStreamResponse;
import com.google.api.gax.rpc.ServerStream;
import org.springframework.stereotype.Repository;

import javax.annotation.Nullable;
import java.util.List;

@Repository
public class MetricRepositoryImpl implements MetricRepository {
    private final GoogleAdsServiceClient googleAdsServiceClient;
    private final StreamRequest requestBuilder;

    public MetricRepositoryImpl() {
        GoogleAdsClientBuilder googleAdsClientBuilder = GoogleAdsClientBuilder.INSTANCE;

        this.googleAdsServiceClient = googleAdsClientBuilder
                .getGoogleAdsClient()
                .getLatestVersion()
                .createGoogleAdsServiceClient();

        this.requestBuilder = new StreamRequestImpl(googleAdsServiceClient);
    }

    @Override
    public List<CampaignMetrics> fetchCampaignMetricsByResourceName(String customerId,
                                                                    String campaignResourceName,
                                                                    @Nullable String startDate,
                                                                    @Nullable String endDate) throws Exception {
        try {
            String query = MetricsHelper.getCampaignMetrics(customerId, campaignResourceName, startDate, endDate);

            SearchGoogleAdsStreamRequest request = SearchGoogleAdsStreamRequest.newBuilder()
                    .setCustomerId(customerId)
                    .setQuery(query)
                    .build();

            ServerStream<SearchGoogleAdsStreamResponse> stream =
                    googleAdsServiceClient.searchStreamCallable().call(request);

            return MetricsHelper.convertStreamToCampaignMetrics(stream);
        } catch (Exception e) {
            throw ApiExceptionResolver.doResolveException(e);
        }
    }
}
