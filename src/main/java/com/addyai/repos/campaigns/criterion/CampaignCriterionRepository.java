package com.addyai.repos.campaigns.criterion;

import com.addyai.models.campaign_criterion.CampaignCriterionDetails;
import com.google.ads.googleads.v11.services.CampaignCriterionOperation;

import java.util.List;

public interface CampaignCriterionRepository {

    List<CampaignCriterionDetails> getCampaignCriterionDetails(long customerId, String campaignResourceName);

    void addCampaignCriterion(long customerId, List<CampaignCriterionOperation> campaignCriterionOperationList) throws Exception;

    String getGeoTargetConstant(String locale, String countryCode, String location) throws Exception;
}
