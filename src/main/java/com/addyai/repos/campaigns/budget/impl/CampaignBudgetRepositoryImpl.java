package com.addyai.repos.campaigns.budget.impl;

import com.addyai.builder.GoogleAdsClientBuilder;
import com.addyai.error_handling.ApiExceptionResolver;
import com.addyai.models.BudgetDetails;
import com.addyai.repos.campaigns.budget.CampaignBudgetRepository;
import com.addyai.repos.requests.StreamRequest;
import com.addyai.repos.requests.impl.StreamRequestImpl;
import com.addyai.utils.helpers.GAQLHelper;
import com.google.ads.googleads.v11.services.*;
import com.google.api.gax.rpc.ServerStream;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CampaignBudgetRepositoryImpl implements CampaignBudgetRepository {
    private final GoogleAdsServiceClient googleAdsServiceClient;
    private final StreamRequest requestBuilder;

    public CampaignBudgetRepositoryImpl() {
        GoogleAdsClientBuilder googleAdsClientBuilder = GoogleAdsClientBuilder.INSTANCE;

        this.googleAdsServiceClient = googleAdsClientBuilder
                .getGoogleAdsClient()
                .getLatestVersion()
                .createGoogleAdsServiceClient();

        this.requestBuilder = new StreamRequestImpl(googleAdsServiceClient);
    }

    @Override
    public List<BudgetDetails> fetchAllCampaignBudgetDetails(long customerId) throws Exception {
        try {
            String query = GAQLHelper.getCampaignBudgetQuery();

            // build and perform the search request on client account
            SearchGoogleAdsStreamRequest request = requestBuilder.buildStreamRequest(customerId, query);
            ServerStream<SearchGoogleAdsStreamResponse> response = requestBuilder.callStreamRequest(request);

            return GAQLHelper.convertStreamResponseToBudgetDetails(response);
        } catch (Exception e) {
            throw ApiExceptionResolver.doResolveException(e);
        }
    }

    @Override
    public List<String> createOrUpdateBudgets(long customerId, List<CampaignBudgetOperation> campaignBudgetOperationList) throws Exception {
        List<String> budgetResourceNameList = new ArrayList<>();
        try {
            GoogleAdsClientBuilder googleAdsClientBuilder = GoogleAdsClientBuilder.INSTANCE;

            CampaignBudgetServiceClient campaignBudgetServiceClient = googleAdsClientBuilder.getGoogleAdsClient()
                    .getLatestVersion().createCampaignBudgetServiceClient();

            // At this time we are going to assume the response is OK if no exception is thrown
            MutateCampaignBudgetsResponse budgetsResponse = campaignBudgetServiceClient
                    .mutateCampaignBudgets(Long.toString(customerId), campaignBudgetOperationList);

            // create budget details objects from the results and add to list
            for (MutateCampaignBudgetResult response : budgetsResponse.getResultsList()) {
                budgetResourceNameList.add(response.getResourceName());
            }
        } catch (Exception e) {
            throw ApiExceptionResolver.doResolveException(e);
        }

        return budgetResourceNameList;
    }

    @Override
    public void deleteCampaignBudgets(long customerId, List<Long> budgetIds) {

    }
}
