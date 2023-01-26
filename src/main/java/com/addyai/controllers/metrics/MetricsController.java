package com.addyai.controllers.metrics;

import com.addyai.models.metrics.Metrics;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MetricsController {
    ResponseEntity<List<Metrics>> getCampaignMetricsByDateRange(String customerId, String campaignId,
                                                                String startDate, String endDate) throws Exception;

    ResponseEntity<List<Metrics>> getAdGroupMetricsByDateRange(String customerId, String campaignId,
                                                               String adGroupId, String startDate, String endDate) throws Exception;

    ResponseEntity<Object> getDummyCampaignMetrics(String customerId, String campaignResourceName);

    ResponseEntity<Object> getDummyAdGroupMetrics(String customerId, String campaignId, String adGroupId);
}
