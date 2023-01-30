package com.addyai.services.metrics.impl;

import com.addyai.enums.MetricType;
import com.addyai.models.metrics.Metrics;
import com.addyai.repos.metrics.MetricRepository;
import com.addyai.services.metrics.MetricService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.addyai.enums.MetricType.KEYWORD;

@Service
public class MetricServiceImpl implements MetricService {
    private final MetricRepository metricRepository;

    public MetricServiceImpl(MetricRepository metricRepository) {
        this.metricRepository = metricRepository;
    }

    @Override
    public List<Metrics> fetchMetricsByDate(String customerId, String resourceId, String parentResourceId, String startDate, String endDate, MetricType type) throws Exception {
        String resourceName;
        String parentResourceName;

        switch (type) {
            case ACCOUNT:
                return this.metricRepository.fetchMetricsByResourceName(customerId, "",
                        null, startDate, endDate, MetricType.ACCOUNT);
            case CAMPAIGN:
                resourceName = "customers/" + customerId + "/campaigns/" + resourceId;
                return this.metricRepository.fetchMetricsByResourceName(customerId, resourceName, null,
                        startDate, endDate, MetricType.CAMPAIGN);
            case ADGROUP:
                resourceName = "customers/" + customerId + "/adGroups/" + resourceId;

                return this.metricRepository.fetchMetricsByResourceName(customerId, resourceName, null,
                        startDate, endDate, MetricType.ADGROUP);
            case AD:
                resourceName = "customers/" + customerId + "/ads/" + resourceId;

                return this.metricRepository.fetchMetricsByResourceName(customerId, resourceName, null,
                        startDate, endDate, MetricType.AD);
            case KEYWORD:
                resourceName = "customers/" + customerId + "/keywordViews/" + parentResourceId + "~" + resourceId;
                parentResourceName = "customers/" + customerId + "/adGroups/" + parentResourceId;

                return this.metricRepository.fetchMetricsByResourceName(customerId, resourceName, parentResourceName,
                        startDate, endDate, KEYWORD);
            default:
                break;
        }
        return new ArrayList<>();
    }

    @Override
    public List<Metrics> fetchMetricsByDevice(String customerId,
                                              String resourceId,
                                              String parentResourceId,
                                              MetricType type) throws Exception {
        String resourceName = "";
        String parentResourceName = "";
        switch (type) {
            case DEVICE_CAMPAIGN:
                resourceName = "customers/" + customerId + "/campaigns/" + resourceId;

                return this.metricRepository.fetchMetricsByResourceName(customerId,
                        resourceName, "", null, null, MetricType.DEVICE_CAMPAIGN);
            case DEVICE_ADGROUP:
                resourceName = "customers/" + customerId + "/adGroups/" + resourceId;
                parentResourceName = "customers/" + customerId + "/campaigns/" + parentResourceId;

                return this.metricRepository.fetchMetricsByResourceName(customerId,
                        resourceName, parentResourceName, null, null, MetricType.DEVICE_ADGROUP);
            case DEVICE_AD:
                resourceName = "customers/" + customerId + "/ads/" + resourceId;
                parentResourceName = "customers/" + customerId + "/adGroups/" + parentResourceId;

                return this.metricRepository.fetchMetricsByResourceName(customerId,
                        resourceName, parentResourceName, null, null, MetricType.DEVICE_AD);
            case DEVICE_KEYWORD:
                resourceName = "customers/" + customerId + "/keywordViews/" + parentResourceId + "~" + resourceId;
                parentResourceName = "customers/" + customerId + "/adGroups/" + parentResourceId;

                return this.metricRepository.fetchMetricsByResourceName(customerId,
                        resourceName, parentResourceName, null, null, MetricType.DEVICE_KEYWORD);
            default:
                return new ArrayList<>();
        }
    }
}
