package com.addyai.repos.campaigns;

import com.addyai.models.campaign_criterion.CampaignCriteria;

import java.util.List;

public interface CampaignCriteriaRepository {

    void getCampaignCriteria(long customerId, long campaignId);

    void createCampaignCriterion(long customerId, List<CampaignCriteria> campaignCriteriaList);
}
