package com.addyai.repos.metrics;

import com.addyai.models.metrics.CampaignMetrics;

import javax.annotation.Nullable;
import java.util.List;

public interface MetricRepository {
    List<CampaignMetrics> fetchCampaignMetricsByResourceName(String customerId,
                                                             String campaignResourceName,
                                                             @Nullable String startDate,
                                                             @Nullable String endDate) throws Exception;
}
