package com.addyai.controllers.metrics;

import com.addyai.models.metrics.Metrics;
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
    public ResponseEntity<List<Metrics>> getCampaignMetricsByDateRange(@PathVariable String customerId,
                                                                       @RequestParam String campaignId,
                                                                       @RequestParam String startDate,
                                                                       @RequestParam String endDate) throws Exception {
        List<Metrics> campaignMetrics = metricService.fetchCampaignMetricsByDate(
                customerId,
                campaignId,
                startDate,
                endDate);
        return new ResponseEntity<>(campaignMetrics, HttpStatus.OK);
    }

    @Override
    @GetMapping("/adgroup/metrics")
    public ResponseEntity<List<Metrics>> getAdGroupMetricsByDateRange(@PathVariable String customerId,
                                                                      @RequestParam String adGroupId,
                                                                      @RequestParam String startDate,
                                                                      @RequestParam String endDate) throws Exception {
        List<Metrics> adGroupMetrics = metricService.fetchAdGroupMetricsByDate(customerId,
                adGroupId,
                startDate,
                endDate);

        return new ResponseEntity<>(adGroupMetrics, HttpStatus.OK);
    }

    @Override
    @GetMapping("/ad/metrics")
    public ResponseEntity<List<Metrics>> getAdMetricsByDateRange(@PathVariable String customerId,
                                                                 @RequestParam String adId,
                                                                 @RequestParam String startDate,
                                                                 @RequestParam String endDate) throws Exception {
        List<Metrics> adMetrics = metricService.fetchAdMetricsByDate(customerId, adId, startDate, endDate);
        return new ResponseEntity<>(adMetrics, HttpStatus.OK);
    }

    @Override
    @GetMapping("/keyword/metrics")
    public ResponseEntity<List<Metrics>> getKeywordMetricsByDateRange(@PathVariable String customerId,
                                                                      @RequestParam String keywordId,
                                                                      @RequestParam String adGroupId,
                                                                      @RequestParam String startDate,
                                                                      @RequestParam String endDate) throws Exception {
        List<Metrics> keywordMetrics = metricService.fetchKeywordsByDate(customerId,
                adGroupId, keywordId, startDate, endDate);
        return new ResponseEntity<>(keywordMetrics, HttpStatus.OK);
    }

    @Override
    @GetMapping("/campaign/metrics/dummy")
    public ResponseEntity<Object> getDummyCampaignMetrics(@PathVariable String customerId,
                                                          @RequestParam String campaignResourceName) {
        Object obj = getDummyJsonObject(1);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @Override
    @GetMapping("/adgroup/metrics/dummy")
    public ResponseEntity<Object> getDummyAdGroupMetrics(@PathVariable String customerId,
                                                         @RequestParam String campaignId,
                                                         @RequestParam String adGroupId) {
        Object obj = getDummyJsonObject(2);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    private Object getDummyJsonObject(int type) {
        ClassPathResource resource;
        if (type == 1)
            resource = new ClassPathResource("dummy-campaign-metrics.json");
        else
            resource = new ClassPathResource("dummy-adgroup-metrics.json");

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(resource.getInputStream(), Object.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
