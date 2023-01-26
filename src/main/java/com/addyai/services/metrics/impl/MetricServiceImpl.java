package com.addyai.services.metrics.impl;

import com.addyai.enums.MetricType;
import com.addyai.models.metrics.Metrics;
import com.addyai.repos.metrics.MetricRepository;
import com.addyai.services.metrics.MetricService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetricServiceImpl implements MetricService {
    private final MetricRepository metricRepository;

    public MetricServiceImpl(MetricRepository metricRepository) {
        this.metricRepository = metricRepository;
    }

    @Override
    public List<Metrics> fetchCampaignMetricsByDate(String customerId,
                                                    String campaignId,
                                                    String startDate,
                                                    String endDate) throws Exception {
        String campaignResourceName = "customers/" + customerId + "/campaigns/" + campaignId;
        return this.metricRepository.fetchMetricsByResourceName(customerId, campaignResourceName, null,
                startDate, endDate, MetricType.CAMPAIGN);
    }

    @Override
    public List<Metrics> fetchAdGroupMetricsByDate(String customerId,
                                                   String adGroupId,
                                                   String startDate,
                                                   String endDate) throws Exception {
        String adGroupResourceName = "customers/" + customerId + "/adGroups/" + adGroupId;

        return this.metricRepository.fetchMetricsByResourceName(customerId, adGroupResourceName, null,
                startDate, endDate, MetricType.ADGROUP);
    }

    @Override
    public List<Metrics> fetchAdMetricsByDate(String customerId,
                                              String adId,
                                              String startDate,
                                              String endDate) throws Exception {
        String adResourceName = "customers/" + customerId + "/ads/" + adId;

        return this.metricRepository.fetchMetricsByResourceName(customerId, adResourceName, null,
                startDate, endDate, MetricType.AD);
    }

    @Override
    public List<Metrics> fetchKeywordsByDate(String customerId,
                                             String adGroupId,
                                             String keywordId,
                                             String startDate,
                                             String endDate) throws Exception {
        String adGroupResourceName = "customers/" + customerId + "/adGroups/" + adGroupId;
        String keywordResourceName = "customers/" + customerId + "/keywordViews/" + adGroupId + "~" + keywordId;

        return this.metricRepository.fetchMetricsByResourceName(customerId, keywordResourceName, adGroupResourceName,
                startDate, endDate, MetricType.KEYWORD);
    }
}
