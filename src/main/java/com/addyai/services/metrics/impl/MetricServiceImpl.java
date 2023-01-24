package com.addyai.services.metrics.impl;

import com.addyai.models.metrics.CampaignMetrics;
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
    public List<CampaignMetrics> fetchCampaignMetricsByDate(String customerId,
                                                            String campaignId,
                                                            String startDate,
                                                            String endDate) throws Exception {
        String campaignResourceName = "customers/" + customerId + "/campaigns/" + campaignId;
        return this.metricRepository.fetchCampaignMetricsByResourceName(customerId, campaignResourceName, startDate, endDate);
    }

    @Override
    public List<CampaignMetrics> fetchAllCampaignMetrics(String customerId, String campaignId) throws Exception {
        String campaignResourceName = "customers/" + customerId + "/campaigns/" + campaignId;
        return metricRepository.fetchCampaignMetricsByResourceName(customerId,
                campaignResourceName,
                null,
                null);
    }
}
