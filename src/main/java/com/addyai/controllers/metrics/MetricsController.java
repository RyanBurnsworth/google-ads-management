package com.addyai.controllers.metrics;

import com.addyai.models.metrics.Metrics;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MetricsController {
    ResponseEntity<List<Metrics>> getMetricsByDateRange(String customerId, String resourceId, String parentResourceId,
                                                        String startDate, String endDate, String resourceType) throws Exception;

    ResponseEntity<Object> getDummyMetricsByDate(String customerId,
                                                 String resourceId,
                                                 String parentResourceId,
                                                 String startDate,
                                                 String endDate,
                                                 String resourceType);
}
