package com.addyai.services.metrics;

import com.addyai.enums.MetricType;
import com.addyai.models.metrics.Metrics;

import java.util.List;

public interface MetricService {
    List<Metrics> fetchMetricsByDate(String customerId, String resourceId, String parentResourceId, String startDate, String endDate, MetricType type) throws Exception;
    List<Metrics> fetchMetricsByDevice(String customerId, String resourceId, String parentResourceId, MetricType type) throws Exception;
}
