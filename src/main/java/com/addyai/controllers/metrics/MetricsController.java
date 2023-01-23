package com.addyai.controllers.metrics;

import com.addyai.models.metrics.CampaignMetrics;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MetricsController {
    ResponseEntity<List<CampaignMetrics>> getCampaignMetricsByDateRange(String customerId, String campaignResourceName,
                                                                  String startDate, String endDate) throws Exception;
}
