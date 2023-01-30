package com.addyai.repos.metrics;

import com.addyai.enums.MetricType;
import com.addyai.models.metrics.Metrics;

import javax.annotation.Nullable;
import java.util.List;

public interface MetricRepository {
    List<Metrics> fetchMetricsByResourceName(String customerId,
                                             String resourceName,
                                             @Nullable String parentResourceName,
                                             @Nullable String startDate,
                                             @Nullable String endDate,
                                             MetricType metricType) throws Exception;
}
