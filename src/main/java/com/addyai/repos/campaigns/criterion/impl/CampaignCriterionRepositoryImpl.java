package com.addyai.repos.campaigns.criterion.impl;

import com.addyai.models.campaign_criterion.CampaignCriterionDetails;
import com.addyai.repos.campaigns.criterion.CampaignCriterionRepository;
import com.google.ads.googleads.v11.services.CampaignCriterionOperation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CampaignCriterionRepositoryImpl implements CampaignCriterionRepository {
    @Override
    public List<CampaignCriterionDetails> getCampaignCriterionDetails(long customerId, String campaignResourceName) {
        return null;
    }

    @Override
    public void addCampaignCriterion(long customerId, List<CampaignCriterionOperation> campaignCriterionOperationList) {

    }
}
