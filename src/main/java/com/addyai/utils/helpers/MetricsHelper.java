package com.addyai.utils.helpers;

import com.addyai.adapter.impl.GoogleAdsRowAdapterImpl;
import com.addyai.models.metrics.Metrics;
import com.google.ads.googleads.v12.services.GoogleAdsRow;
import com.google.ads.googleads.v12.services.SearchGoogleAdsStreamResponse;
import com.google.api.gax.rpc.ServerStream;

import java.util.ArrayList;
import java.util.List;

public class MetricsHelper {
    public static String getCampaignMetrics(String customerId,
                                            String campaignResourceName,
                                            String startDate,
                                            String endDate) {

        if (customerId.isEmpty() || campaignResourceName.isEmpty() || startDate.isEmpty() || endDate.isEmpty())
            return "";

        return "SELECT" +
                " segments.date" +
                " campaign.id," +
                " campaign.resource_name," +
                " metrics.clicks," +
                " metrics.impressions," +
                " metrics.ctr," +
                " metrics.average_cpc," +
                " metrics.cost_micros," +
                " metrics.conversions," +
                " metrics.conversions_value," +
                " metrics.cost_per_conversion," +
                " metrics.invalid_click_rate," +
                " metrics.invalid_clicks," +
                " metrics.phone_calls," +
                " metrics.phone_impressions," +
                " metrics.phone_through_rate," +
                " FROM campaign" +
                " WHERE" +
                " campaign.resource_name = '" + campaignResourceName + "'" +
                " AND segments.date >= '" + startDate + "'" +
                " AND segments.date <= '" + endDate + "'";
    }

    public static List<Metrics> convertStreamToCampaignMetrics(ServerStream<SearchGoogleAdsStreamResponse> streamResponses) {
        GoogleAdsRowAdapterImpl googleAdsRowAdapter = new GoogleAdsRowAdapterImpl();
        List<Metrics> campaignMetricsList = new ArrayList<>();

        for (SearchGoogleAdsStreamResponse response : streamResponses) {
            for (GoogleAdsRow googleAdsRow : response.getResultsList()) {
                Metrics campaignMetrics = googleAdsRowAdapter.getCampaignMetrics(googleAdsRow);
                campaignMetricsList.add(campaignMetrics);
            }
        }
        return campaignMetricsList;
    }

    public static String getAdGroupMetrics(String customerId,
                                           String adGroupResourceName,
                                           String campaignResourceName,
                                           String startDate,
                                           String endDate) {
        if (customerId.isEmpty() || adGroupResourceName.isEmpty() || startDate.isEmpty() || endDate.isEmpty())
            return "";

        return "SELECT" +
                " segments.date," +
                " ad_group.campaign," +
                " ad_group.id," +
                " ad_group.resource_name," +
                " metrics.clicks," +
                " metrics.impressions," +
                " metrics.ctr," +
                " metrics.average_cpc," +
                " metrics.cost_micros," +
                " metrics.phone_calls," +
                " metrics.phone_impressions," +
                " metrics.phone_through_rate," +
                " metrics.conversions," +
                " metrics.cost_per_conversion," +
                " metrics.conversions_value" +
                " FROM ad_group" +
                " WHERE" +
                " segments.date > '" + startDate + "'" +
                " AND segments.date < '" + endDate + "'" +
                " AND ad_group.resource_name = '" + adGroupResourceName + "'" +
                " AND ad_group.campaign = '" + campaignResourceName + "'";
    }

    public static List<Metrics> convertStreamToAdGroupMetrics(ServerStream<SearchGoogleAdsStreamResponse> streamResponses) {
        GoogleAdsRowAdapterImpl googleAdsRowAdapter = new GoogleAdsRowAdapterImpl();
        List<Metrics> adGroupMetricList = new ArrayList<>();

        for (SearchGoogleAdsStreamResponse response : streamResponses) {
            for (GoogleAdsRow googleAdsRow : response.getResultsList()) {
                Metrics adGroupMetrics = googleAdsRowAdapter.getAdGroupMetrics(googleAdsRow);
                adGroupMetricList.add(adGroupMetrics);
            }
        }
        return adGroupMetricList;
    }

    public static String getKeywordMetrics(String customerId,
                                           String keywordResourceName,
                                           String startDate,
                                           String endDate) {
        if (customerId.isEmpty() || keywordResourceName.isEmpty() || startDate.isEmpty() || endDate.isEmpty())
            return "";

        return "SELECT" +
                " segments.date," +
                " keyword_view.resource_name," +
                " ad_group_criterion.ad_group," +
                " ad_group_criterion.criterion_id," +
                " ad_group_criterion.quality_info.quality_score," +
                " metrics.clicks," +
                " metrics.impressions," +
                " metrics.ctr," +
                " metrics.average_cpc," +
                " metrics.cost_micros," +
                " metrics.conversions," +
                " metrics.conversion_rate," +
                " metrics.cost_per_conversion," +
                " metrics.conversions_value," +
                "FROM keyword_view" +
                "WHERE" +
                " segments.date > '" + startDate + "'" +
                " AND segments.date < '" + endDate + "'" +
                " AND keyword_view.resource_name = '" + keywordResourceName + "'";
    }

    public static List<Metrics> convertStreamToKeywordMetrics(ServerStream<SearchGoogleAdsStreamResponse> streamResponses) {
        GoogleAdsRowAdapterImpl googleAdsRowAdapter = new GoogleAdsRowAdapterImpl();
        List<Metrics> keywordMetricsList = new ArrayList<>();

        for (SearchGoogleAdsStreamResponse response : streamResponses) {
            for (GoogleAdsRow googleAdsRow : response.getResultsList()) {
                Metrics keywordMetrics = googleAdsRowAdapter.getKeywordMetrics(googleAdsRow);
                keywordMetricsList.add(keywordMetrics);
            }
        }
        return keywordMetricsList;
    }
}
