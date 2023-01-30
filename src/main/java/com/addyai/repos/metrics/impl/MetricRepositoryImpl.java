package com.addyai.repos.metrics.impl;

import com.addyai.builder.GoogleAdsClientBuilder;
import com.addyai.enums.MetricType;
import com.addyai.error_handling.ApiExceptionResolver;
import com.addyai.models.metrics.Metrics;
import com.addyai.repos.metrics.MetricRepository;
import com.addyai.utils.helpers.MetricsHelper;
import com.google.ads.googleads.v12.services.GoogleAdsServiceClient;
import com.google.ads.googleads.v12.services.SearchGoogleAdsStreamRequest;
import com.google.ads.googleads.v12.services.SearchGoogleAdsStreamResponse;
import com.google.api.gax.rpc.ServerStream;
import org.springframework.stereotype.Repository;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MetricRepositoryImpl implements MetricRepository {
    private final GoogleAdsServiceClient googleAdsServiceClient;

    public MetricRepositoryImpl() {
        GoogleAdsClientBuilder googleAdsClientBuilder = GoogleAdsClientBuilder.INSTANCE;

        this.googleAdsServiceClient = googleAdsClientBuilder
                .getGoogleAdsClient()
                .getLatestVersion()
                .createGoogleAdsServiceClient();
    }

    @Override
    public List<Metrics> fetchMetricsByResourceName(String customerId,
                                                    String resourceName,
                                                    @Nullable String parentResourceName,
                                                    @Nullable String startDate,
                                                    @Nullable String endDate,
                                                    MetricType metricType) throws Exception {
        String query = "";
        if (metricType == MetricType.CAMPAIGN) {
            query = MetricsHelper.getCampaignMetrics(resourceName, startDate, endDate);
        } else if (metricType == MetricType.ADGROUP) {
            query = MetricsHelper.getAdGroupMetrics(resourceName, startDate, endDate);
        } else if (metricType == MetricType.KEYWORD) {
            query = MetricsHelper.getKeywordMetrics(resourceName, startDate, endDate);
        } else if (metricType == MetricType.AD) {
            query = MetricsHelper.getAdMetrics(resourceName, startDate, endDate);
        } else if (metricType == MetricType.DEVICE_CAMPAIGN) {
            query = MetricsHelper.getDeviceMetricsByCampaign(resourceName);
        } else if (metricType == MetricType.DEVICE_ADGROUP) {
            query = MetricsHelper.getDeviceMetricsByAdGroup(resourceName);
        } else if (metricType == MetricType.DEVICE_AD) {
            query = MetricsHelper.getDeviceMetricsByAd(resourceName);
        } else if (metricType == MetricType.DEVICE_KEYWORD) {
            query = MetricsHelper.getDeviceMetricsByKeyword(resourceName);
        }

        SearchGoogleAdsStreamRequest request = SearchGoogleAdsStreamRequest.newBuilder()
                .setCustomerId(customerId)
                .setQuery(query)
                .build();

        try {
            ServerStream<SearchGoogleAdsStreamResponse> stream =
                    googleAdsServiceClient.searchStreamCallable().call(request);

            if (metricType == MetricType.CAMPAIGN) {
                return MetricsHelper.convertStreamToCampaignMetrics(stream);
            } else if (metricType == MetricType.ADGROUP) {
                return MetricsHelper.convertStreamToAdGroupMetrics(stream);
            } else if (metricType == MetricType.KEYWORD) {
                return MetricsHelper.convertStreamToKeywordMetrics(stream);
            } else if (metricType == MetricType.AD) {
                return MetricsHelper.convertStreamToAdMetrics(stream);
            } else if (metricType == MetricType.DEVICE_CAMPAIGN) {
                return MetricsHelper.convertStreamToCampaignDeviceDetails(stream);
            } else if (metricType == MetricType.DEVICE_ADGROUP) {
                return MetricsHelper.convertStreamToAdGroupDeviceDetails(stream);
            } else if (metricType == MetricType.DEVICE_AD) {
                return MetricsHelper.convertStreamToAdDeviceDetails(stream);
            } else if (metricType == MetricType.DEVICE_KEYWORD) {
                return MetricsHelper.convertStreamToKeywordDeviceDetails(stream);
            }

            return new ArrayList<>();
        } catch (Exception e) {
            throw ApiExceptionResolver.doResolveException(e);
        }
    }
}
