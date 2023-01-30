package com.addyai.utils.helpers;

import com.addyai.adapter.impl.GoogleAdsRowAdapterImpl;
import com.addyai.models.metrics.Metrics;
import com.google.ads.googleads.v12.services.GoogleAdsRow;
import com.google.ads.googleads.v12.services.SearchGoogleAdsStreamResponse;
import com.google.api.gax.rpc.ServerStream;

import java.util.ArrayList;
import java.util.List;

public class MetricsHelper {
    public static String getCampaignMetrics(String campaignResourceName,
                                            String startDate,
                                            String endDate) {

        if (campaignResourceName.isEmpty() || startDate.isEmpty() || endDate.isEmpty())
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
                " metrics.cost_per_conversion," +
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

    public static String getAdGroupMetrics(String adGroupResourceName,
                                           String startDate,
                                           String endDate) {
        if (adGroupResourceName.isEmpty() || startDate.isEmpty() || endDate.isEmpty())
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
                " AND ad_group.resource_name = '" + adGroupResourceName + "'";
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

    public static String getAdMetrics(String adResourceName,
                                      String startDate,
                                      String endDate) {
        if (adResourceName.isEmpty() || startDate.isEmpty() || endDate.isEmpty())
            return "";

        return "SELECT " +
                " ad_group_ad.ad.id," +
                " ad_group_ad.ad.resource_name," +
                " ad_group_ad.ad.type," +
                " ad_group_ad.ad_group," +
                " segments.date," +
                " metrics.clicks," +
                " metrics.impressions," +
                " metrics.ctr," +
                " metrics.average_cpc," +
                " metrics.cost_micros," +
                " metrics.conversions," +
                " metrics.cost_per_conversion," +
                " metrics.conversions_value" +
                " FROM ad_group_ad" +
                " WHERE" +
                " segments.date >= '" + startDate + "'" +
                " AND segments.date <= '" + endDate + "'" +
                " AND ad_group_ad.ad.resource_name = '" + adResourceName + "'";
    }

    public static List<Metrics> convertStreamToAdMetrics(ServerStream<SearchGoogleAdsStreamResponse> streamResponses) {
        GoogleAdsRowAdapterImpl googleAdsRowAdapter = new GoogleAdsRowAdapterImpl();
        List<Metrics> adMetricsList = new ArrayList<>();

        for (SearchGoogleAdsStreamResponse response : streamResponses) {
            for (GoogleAdsRow googleAdsRow : response.getResultsList()) {
                Metrics adMetrics = googleAdsRowAdapter.getAdMetrics(googleAdsRow);
                adMetricsList.add(adMetrics);
            }
        }
        return adMetricsList;
    }

    public static String getKeywordMetrics(String keywordResourceName,
                                           String startDate,
                                           String endDate) {
        if (keywordResourceName.isEmpty() || startDate.isEmpty() || endDate.isEmpty())
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
                " metrics.cost_per_conversion," +
                " metrics.conversions_value" +
                " FROM keyword_view" +
                " WHERE" +
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

    public static String getDeviceMetricsByCampaign(String campaignResourceName) {
        return "SELECT" +
                " segments.device," +
                " campaign.id," +
                " campaign.name," +
                " campaign.resource_name," +
                " metrics.clicks," +
                " metrics.impressions," +
                " metrics.ctr," +
                " metrics.average_cpc," +
                " metrics.cost_micros," +
                " metrics.conversions," +
                " metrics.cost_per_conversion," +
                " metrics.conversions_value," +
                " metrics.invalid_click_rate," +
                " metrics.invalid_clicks" +
                " FROM campaign" +
                " WHERE" +
                " campaign.resource_name = '" + campaignResourceName + "'";
    }

    public static List<Metrics> convertStreamToCampaignDeviceDetails(ServerStream<SearchGoogleAdsStreamResponse> streamResponses) {
        GoogleAdsRowAdapterImpl googleAdsRowAdapter = new GoogleAdsRowAdapterImpl();
        List<Metrics> campaignDeviceDataList = new ArrayList<>();

        for (SearchGoogleAdsStreamResponse response : streamResponses) {
            for (GoogleAdsRow googleAdsRow : response.getResultsList()) {
                Metrics deviceMetrics = googleAdsRowAdapter.getDeviceMetricsByCampaign(googleAdsRow);
                campaignDeviceDataList.add(deviceMetrics);
            }
        }
        return campaignDeviceDataList;
    }

    public static String getDeviceMetricsByAdGroup(String adGroupResourceName) {
        return "SELECT" +
                " segments.device," +
                " ad_group.id," +
                " ad_group.name," +
                " ad_group.resource_name," +
                " ad_group.campaign," +
                " metrics.clicks," +
                " metrics.impressions," +
                " metrics.ctr," +
                " metrics.average_cpc," +
                " metrics.cost_micros," +
                " metrics.conversions," +
                " metrics.cost_per_conversion," +
                " metrics.conversions_value," +
                " metrics.invalid_click_rate," +
                " metrics.invalid_clicks" +
                " FROM ad_group" +
                " WHERE" +
                " ad_group.resource_name = '" + adGroupResourceName + "'";
    }

    public static List<Metrics> convertStreamToAdGroupDeviceDetails(ServerStream<SearchGoogleAdsStreamResponse> streamResponses) {
        GoogleAdsRowAdapterImpl googleAdsRowAdapter = new GoogleAdsRowAdapterImpl();
        List<Metrics> adGroupDeviceDataList = new ArrayList<>();

        for (SearchGoogleAdsStreamResponse response : streamResponses) {
            for (GoogleAdsRow googleAdsRow : response.getResultsList()) {
                Metrics deviceMetrics = googleAdsRowAdapter.getDeviceMetricsByAdGroup(googleAdsRow);
                adGroupDeviceDataList.add(deviceMetrics);
            }
        }
        return adGroupDeviceDataList;
    }

    public static String getDeviceMetricsByAd(String adResourceName) {
        return "SELECT" +
                " segments.device," +
                " ad_group_ad.ad.id," +
                " ad_group_ad.ad.resource_name," +
                " ad_group_ad.ad.type," +
                " ad_group_ad.ad_group," +
                " metrics.clicks," +
                " metrics.impressions," +
                " metrics.ctr," +
                " metrics.average_cpc," +
                " metrics.cost_micros," +
                " metrics.conversions," +
                " metrics.cost_per_conversion," +
                " metrics.conversions_value," +
                " metrics.invalid_click_rate," +
                " metrics.invalid_clicks" +
                " FROM ad_group_ad" +
                " WHERE" +
                " ad_group_ad.ad.resource_name = '" + adResourceName + "'";
    }

    public static List<Metrics> convertStreamToAdDeviceDetails(ServerStream<SearchGoogleAdsStreamResponse> streamResponses) {
        GoogleAdsRowAdapterImpl googleAdsRowAdapter = new GoogleAdsRowAdapterImpl();
        List<Metrics> adDeviceDataList = new ArrayList<>();

        for (SearchGoogleAdsStreamResponse response : streamResponses) {
            for (GoogleAdsRow googleAdsRow : response.getResultsList()) {
                Metrics deviceMetrics = googleAdsRowAdapter.getDeviceMetricsByAd(googleAdsRow);
                adDeviceDataList.add(deviceMetrics);
            }
        }
        return adDeviceDataList;
    }

    public static String getDeviceMetricsByKeyword(String keywordResourceName) {
        return "SELECT" +
                " segments.device," +
                " keyword_view.resource_name," +
                " ad_group_criterion.ad_group," +
                " ad_group_criterion.criterion_id," +
                " metrics.clicks," +
                " metrics.impressions," +
                " metrics.ctr," +
                " metrics.average_cpc," +
                " metrics.cost_micros," +
                " metrics.conversions," +
                " metrics.cost_per_conversion," +
                " metrics.conversions_value" +
                " FROM keyword_view" +
                " WHERE" +
                " keyword_view.resource_name = '" + keywordResourceName + "'";
    }

    public static List<Metrics> convertStreamToKeywordDeviceDetails(ServerStream<SearchGoogleAdsStreamResponse> streamResponses) {
        GoogleAdsRowAdapterImpl googleAdsRowAdapter = new GoogleAdsRowAdapterImpl();
        List<Metrics> keywordDeviceDataList = new ArrayList<>();

        for (SearchGoogleAdsStreamResponse response : streamResponses) {
            for (GoogleAdsRow googleAdsRow : response.getResultsList()) {
                Metrics deviceMetrics = googleAdsRowAdapter.getDeviceMetricsByKeyword(googleAdsRow);
                keywordDeviceDataList.add(deviceMetrics);
            }
        }
        return keywordDeviceDataList;
    }
}
