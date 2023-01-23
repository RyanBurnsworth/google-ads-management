package com.addyai.services.metrics;

import com.addyai.models.metrics.CampaignMetrics;

import java.util.List;

public interface MetricService {
    List<CampaignMetrics> fetchCampaignMetricsByDate(String customerId, String campaignResourceName, String startDate, String endDate) throws Exception;

    List<CampaignMetrics> fetchAllCampaignMetrics(String customerId, String campaignResourceName) throws Exception;
}
