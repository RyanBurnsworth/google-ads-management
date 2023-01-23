package com.addyai.controllers.metrics;

import com.addyai.models.metrics.CampaignMetrics;
import com.addyai.services.metrics.MetricService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/campaign/{customerId}")
public class MetricsControllerImpl implements MetricsController {
    private final MetricService metricService;

    public MetricsControllerImpl(MetricService metricService) {
        this.metricService = metricService;
    }

    @Override
    @GetMapping("/metrics")
    public ResponseEntity<List<CampaignMetrics>> getCampaignMetricsByDateRange(@PathVariable String customerId,
                                                                         @RequestParam String campaignResourceName,
                                                                         @RequestParam String startDate,
                                                                         @RequestParam String endDate) throws Exception {
        List<CampaignMetrics> campaignMetrics = metricService.fetchCampaignMetricsByDate(
                customerId,
                campaignResourceName,
                startDate,
                endDate);
        return new ResponseEntity<>(campaignMetrics, HttpStatus.OK);
    }
}
