package com.addyai.repos.campaigns.criterion.impl;

import com.addyai.GoogleAdsManagementApplication;
import com.addyai.error_handling.ApiExceptionResolver;
import com.addyai.models.campaign_criterion.CampaignCriterionDetails;
import com.addyai.repos.campaigns.criterion.CampaignCriterionRepository;
import com.addyai.repos.requests.StreamRequest;
import com.addyai.repos.requests.impl.StreamRequestImpl;
import com.google.ads.googleads.v11.services.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CampaignCriterionRepositoryImpl implements CampaignCriterionRepository {
    private final GoogleAdsServiceClient googleAdsServiceClient;
    private final StreamRequest requestBuilder;

    private final CampaignCriterionServiceClient campaignCriterionServiceClient;

    public CampaignCriterionRepositoryImpl() {
        this.googleAdsServiceClient = GoogleAdsManagementApplication
                .getGoogleAdsClient()
                .getLatestVersion()
                .createGoogleAdsServiceClient();

        this.requestBuilder = new StreamRequestImpl(googleAdsServiceClient);

        this.campaignCriterionServiceClient = GoogleAdsManagementApplication.getGoogleAdsClient()
                .getLatestVersion().createCampaignCriterionServiceClient();
    }

    @Override
    public List<CampaignCriterionDetails> getCampaignCriterionDetails(long customerId, String campaignResourceName) {
        return null;
    }

    @Override
    public void addCampaignCriterion(long customerId,
                                     List<CampaignCriterionOperation> campaignCriterionOperationList) throws Exception {
        try {
            MutateCampaignCriteriaResponse response = campaignCriterionServiceClient
                    .mutateCampaignCriteria(Long.toString(customerId), campaignCriterionOperationList);
        } catch (Exception e) {
            throw ApiExceptionResolver.doResolveException(e);
        }
    }
}
