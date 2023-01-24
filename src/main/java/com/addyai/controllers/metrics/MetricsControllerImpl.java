package com.addyai.controllers.metrics;

import com.addyai.models.metrics.CampaignMetrics;
import com.addyai.services.metrics.MetricService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/{customerId}")
public class MetricsControllerImpl implements MetricsController {
    private final MetricService metricService;

    public MetricsControllerImpl(MetricService metricService) {
        this.metricService = metricService;
    }

    @Override
    @GetMapping("/campaign/metrics")
    public ResponseEntity<List<CampaignMetrics>> getCampaignMetricsByDateRange(@PathVariable String customerId,
                                                                               @RequestParam String campaignId,
                                                                               @RequestParam String startDate,
                                                                               @RequestParam String endDate) throws Exception {
        List<CampaignMetrics> campaignMetrics = metricService.fetchCampaignMetricsByDate(
                customerId,
                campaignId,
                startDate,
                endDate);
        return new ResponseEntity<>(campaignMetrics, HttpStatus.OK);
    }

    @Override
    @GetMapping("/campaign/metrics/dummy")
    public ResponseEntity<Object> getDummyCampaignMetrics(String customerId, String campaignResourceName) throws Exception {
        Object obj = getDummyJsonObject();
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    private Object getDummyJsonObject() {
        ClassPathResource resource = new ClassPathResource("dummy-campaign-metrics.json");
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(resource.getInputStream(), Object.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
