package com.addyai.utils.helpers;

import com.addyai.adapter.impl.GoogleAdsRowAdapterImpl;
import com.addyai.models.metrics.CampaignMetrics;
import com.google.ads.googleads.v12.services.GoogleAdsRow;
import com.google.ads.googleads.v12.services.SearchGoogleAdsStreamResponse;
import com.google.api.gax.rpc.ServerStream;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MetricsHelper {
    public static String getCampaignMetrics(String customerId,
                                            String campaignResourceName,
                                            @Nullable String startDate,
                                            @Nullable String endDate) {

        if (customerId.isEmpty() || campaignResourceName.isEmpty())
            return "";

        String query = "SELECT" +
                " metrics.all_conversions," +
                " metrics.all_conversions_value," +
                " metrics.average_cpc," +
                " metrics.clicks," +
                " metrics.cost_micros," +
                " metrics.cost_per_all_conversions," +
                " metrics.ctr," +
                " metrics.invalid_click_rate," +
                " metrics.invalid_clicks," +
                " metrics.phone_calls," +
                " metrics.phone_impressions," +
                " metrics.phone_through_rate," +
                " metrics.impressions," +
                " segments.date" +
                " FROM campaign" +
                " WHERE" +
                " campaign.resource_name = '" + campaignResourceName + "'";

        if (startDate != null && endDate != null) {
            query = query +
                    " AND segments.date >= '" + startDate + "'" +
                    " AND segments.date <= '" + endDate + "'";
        }

        return query;
    }

    public static List<CampaignMetrics> convertStreamToCampaignMetrics(ServerStream<SearchGoogleAdsStreamResponse> streamResponses) {
        GoogleAdsRowAdapterImpl googleAdsRowAdapter = new GoogleAdsRowAdapterImpl();
        List<CampaignMetrics> campaignMetricsList = new ArrayList<>();

        for (SearchGoogleAdsStreamResponse response : streamResponses) {
            for (GoogleAdsRow googleAdsRow : response.getResultsList()) {
                CampaignMetrics campaignMetrics = googleAdsRowAdapter.getCampaignMetrics(googleAdsRow);
                campaignMetricsList.add(campaignMetrics);
            }
        }
        return campaignMetricsList;
    }
}
