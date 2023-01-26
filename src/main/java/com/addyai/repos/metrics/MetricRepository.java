package com.addyai.repos.metrics;

import com.addyai.enums.MetricType;
import com.addyai.models.metrics.Metrics;

import java.util.List;

public interface MetricRepository {
    List<Metrics> fetchMetricsByResourceName(String customerId,
                                             String resourceName,
                                             String parentResourceName,
                                             String startDate,
                                             String endDate,
                                             MetricType metricType) throws Exception;
}
