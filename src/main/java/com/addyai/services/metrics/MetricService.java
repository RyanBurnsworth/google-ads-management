package com.addyai.services.metrics;

import com.addyai.models.metrics.Metrics;

import java.util.List;

public interface MetricService {
    List<Metrics> fetchCampaignMetricsByDate(String customerId, String campaignId, String startDate, String endDate) throws Exception;

    List<Metrics> fetchAdGroupMetricsByDate(String customerId, String adGroupId, String startDate, String endDate) throws Exception;

    List<Metrics> fetchAdMetricsByDate(String customerId, String adId, String startDate, String endDate) throws Exception;

    List<Metrics> fetchKeywordsByDate(String customerId, String adGroupId, String keywordId, String startDate, String endDate) throws Exception;
}
