package com.addyai.repos.campaigns.budget.impl;

import com.addyai.GoogleAdsManagementApplication;
import com.addyai.error_handling.ApiExceptionResolver;
import com.addyai.models.BudgetDetails;
import com.addyai.repos.campaigns.budget.CampaignBudgetRepository;
import com.addyai.repos.requests.StreamRequest;
import com.addyai.repos.requests.impl.StreamRequestImpl;
import com.addyai.utils.GAQLUtils;
import com.google.ads.googleads.v11.services.*;
import com.google.api.gax.rpc.ServerStream;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.addyai.utils.Constants.MICRO_FACTOR;
import static java.lang.Math.round;

@Repository
public class CampaignBudgetRepositoryImpl implements CampaignBudgetRepository {
    private final GoogleAdsServiceClient googleAdsServiceClient;
    private final StreamRequest requestBuilder;

    public CampaignBudgetRepositoryImpl() {
        this.googleAdsServiceClient = GoogleAdsManagementApplication
                .getGoogleAdsClient()
                .getLatestVersion()
                .createGoogleAdsServiceClient();

        this.requestBuilder = new StreamRequestImpl(googleAdsServiceClient);
    }

    @Override
    public List<BudgetDetails> fetchAllCampaignBudgetDetails(long customerId) throws Exception {
        try {
            String query = GAQLUtils.getCampaignBudgetQuery();

            // build and perform the search request on client account
            SearchGoogleAdsStreamRequest request = requestBuilder.buildStreamRequest(customerId, query);
            ServerStream<SearchGoogleAdsStreamResponse> response = requestBuilder.callStreamRequest(request);

            return GAQLUtils.convertStreamResponseToBudgetDetails(response);
        } catch (Exception e) {
            throw ApiExceptionResolver.doResolveException(e);
        }
    }

    @Override
    public List<BudgetDetails> createOrUpdateBudgets(long customerId, List<CampaignBudgetOperation> campaignBudgetOperationList) throws Exception {
        // TODO: Update to return a list of resourceNames. MutateResponse only returns res name correctly

        List<BudgetDetails> budgetDetailsList = new ArrayList<>();
        try {
            CampaignBudgetServiceClient campaignBudgetServiceClient = GoogleAdsManagementApplication.getGoogleAdsClient()
                    .getLatestVersion().createCampaignBudgetServiceClient();

            // At this time we are going to assume the response is OK if no exception is thrown
            MutateCampaignBudgetsResponse budgetsResponse = campaignBudgetServiceClient
                    .mutateCampaignBudgets(Long.toString(customerId), campaignBudgetOperationList);

            // create budget details objects from the results and add to list
            for (MutateCampaignBudgetResult response : budgetsResponse.getResultsList()) {
                BudgetDetails budgetDetails = new BudgetDetails();
                budgetDetails.setShared(response.getCampaignBudget().getExplicitlyShared());
                budgetDetails.setStatus(response.getCampaignBudget().getStatusValue());
                budgetDetails.setBudgetId(response.getCampaignBudget().getId());
                budgetDetails.setName(response.getCampaignBudget().getName());
                budgetDetails.setDailyBudgetAmount(
                        round(response.getCampaignBudget().getAmountMicros() * MICRO_FACTOR));
                budgetDetails.setResourceName(response.getResourceName());

                budgetDetailsList.add(budgetDetails);
            }
        } catch (Exception e) {
            throw ApiExceptionResolver.doResolveException(e);
        }

        return budgetDetailsList;
    }

    @Override
    public void deleteCampaignBudgets(long customerId, List<Long> budgetIds) {

    }
}
