package com.addyai.controllers.metrics;

import com.addyai.models.metrics.CampaignMetrics;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MetricsController {
    ResponseEntity<List<CampaignMetrics>> getCampaignMetricsByDateRange(String customerId, String campaignId,
                                                                  String startDate, String endDate) throws Exception;

    ResponseEntity<Object> getDummyCampaignMetrics(String customerId, String campaignResourceName) throws Exception;
}
