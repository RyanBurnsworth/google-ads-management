package com.addyai.services.metrics;

import com.addyai.models.metrics.CampaignMetrics;

import java.util.List;

public interface MetricService {
    List<CampaignMetrics> fetchCampaignMetricsByDate(String customerId, String campaignId, String startDate, String endDate) throws Exception;

    List<CampaignMetrics> fetchAllCampaignMetrics(String customerId, String campaignId) throws Exception;
}
