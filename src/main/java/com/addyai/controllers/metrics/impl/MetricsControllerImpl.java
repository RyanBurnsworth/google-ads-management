package com.addyai.controllers.metrics.impl;

import com.addyai.controllers.metrics.MetricsController;
import com.addyai.enums.MetricType;
import com.addyai.models.metrics.Metrics;
import com.addyai.services.metrics.MetricService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/{customerId}")
public class MetricsControllerImpl implements MetricsController {
    private static final String RESOURCE_ACCOUNT = "account";
    private static final String RESOURCE_CAMPAIGN = "campaign";
    private static final String RESOURCE_ADGROUP = "adgroup";
    private static final String RESOURCE_AD = "ad";
    private static final String RESOURCE_KEYWORD = "keyword";

    private final MetricService metricService;

    public MetricsControllerImpl(MetricService metricService) {
        this.metricService = metricService;
    }

    @Override
    @GetMapping("/metrics/date")
    public ResponseEntity<List<Metrics>> getMetricsByDateRange(@PathVariable String customerId,
                                                               @RequestParam String resourceId,
                                                               @RequestParam String parentResourceId,
                                                               @RequestParam String startDate,
                                                               @RequestParam String endDate,
                                                               @RequestParam String resourceType) throws Exception {
        List<Metrics> metricsList = new ArrayList<>();

        switch (resourceType) {
            case RESOURCE_ACCOUNT:
                metricsList = metricService.fetchMetricsByDate(customerId, resourceId, parentResourceId, startDate,
                        endDate, MetricType.ACCOUNT);
                break;
            case RESOURCE_CAMPAIGN:
                metricsList = metricService.fetchMetricsByDate(customerId, resourceId, parentResourceId, startDate,
                        endDate, MetricType.CAMPAIGN);
                break;
            case RESOURCE_ADGROUP:
                metricsList = metricService.fetchMetricsByDate(customerId, resourceId, parentResourceId, startDate,
                        endDate, MetricType.ADGROUP);
                break;
            case RESOURCE_AD:
                metricsList = metricService.fetchMetricsByDate(customerId, resourceId, parentResourceId, startDate,
                        endDate, MetricType.AD);
                break;
            case RESOURCE_KEYWORD:
                metricsList = metricService.fetchMetricsByDate(customerId, resourceId, parentResourceId, startDate,
                        endDate, MetricType.KEYWORD);
                break;
            default:
                break;
        }

        return new ResponseEntity<>(metricsList, HttpStatus.OK);
    }

    @Override
    @GetMapping("/metrics/device")
    public ResponseEntity<List<Metrics>> getMetricsByDevice(@PathVariable String customerId,
                                                            @RequestParam String resourceId,
                                                            @RequestParam String parentResourceId,
                                                            @RequestParam String resourceType) throws Exception {
        List<Metrics> metricsList = new ArrayList<>();

        switch (resourceType) {
            case RESOURCE_ACCOUNT:
                metricsList = metricService.fetchMetricsByDevice(customerId, resourceId, parentResourceId, MetricType.ACCOUNT);
                break;
            case RESOURCE_CAMPAIGN:
                metricsList = metricService.fetchMetricsByDevice(customerId, resourceId, parentResourceId, MetricType.CAMPAIGN);
                break;
            case RESOURCE_ADGROUP:
                metricsList = metricService.fetchMetricsByDevice(customerId, resourceId, parentResourceId, MetricType.ADGROUP);
                break;
            case RESOURCE_AD:
                metricsList = metricService.fetchMetricsByDevice(customerId, resourceId, parentResourceId, MetricType.AD);
                break;
            case RESOURCE_KEYWORD:
                metricsList = metricService.fetchMetricsByDevice(customerId, resourceId, parentResourceId, MetricType.KEYWORD);
                break;
            default:
                break;
        }

        return new ResponseEntity<>(metricsList, HttpStatus.OK);
    }

    @Override
    @GetMapping("/metrics/date/demo")
    public ResponseEntity<Object> getDummyMetricsByDate(@PathVariable String customerId,
                                                        @RequestParam String resourceId,
                                                        @RequestParam String parentResourceId,
                                                        @RequestParam String startDate,
                                                        @RequestParam String endDate,
                                                        @RequestParam String resourceType) {
        Object obj = new Object();
        switch (resourceType) {
            case RESOURCE_ACCOUNT:
                obj = getDummyJsonObject(0);
                break;
            case RESOURCE_CAMPAIGN:
                obj = getDummyJsonObject(1);
                break;
            case RESOURCE_ADGROUP:
                obj = getDummyJsonObject(2);
                break;
            case RESOURCE_AD:
                obj = getDummyJsonObject(3);
                break;
            case RESOURCE_KEYWORD:
                obj = getDummyJsonObject(4);
                break;
            default:
                break;
        }
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @Override
    @GetMapping("/metrics/device/demo")
    public ResponseEntity<Object> getDummyMetricsByDevice(@PathVariable String customerId,
                                                          @Nullable @RequestParam String resourceId,
                                                          @Nullable@RequestParam String parentResourceId,
                                                          @RequestParam String resourceType) {
        return null;
    }

    private Object getDummyJsonObject(int type) {
        ClassPathResource resource;
        if (type == 1)
            resource = new ClassPathResource("dummy-campaign-metrics.json");
        else if (type == 2)
            resource = new ClassPathResource("dummy-adgroup-metrics.json");
        else if (type == 3)
            resource = new ClassPathResource("dummy-ad-metrics.json");
        else
            resource = new ClassPathResource("dummy-keyword-metrics.json");

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(resource.getInputStream(), Object.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
