package com.addyai.repos.campaigns.impl;

import com.addyai.GoogleAdsManagementApplication;
import com.addyai.exceptions.CreateResourceException;
import com.addyai.exceptions.DeleteResourceException;
import com.addyai.exceptions.GetResourceException;
import com.addyai.exceptions.UpdateResourceException;
import com.addyai.models.CampaignDetails;
import com.addyai.repos.campaigns.CampaignRepository;
import com.addyai.repos.requests.StreamRequest;
import com.addyai.repos.requests.impl.StreamRequestImpl;
import com.addyai.utils.GAQLUtils;
import com.google.ads.googleads.v11.enums.BudgetDeliveryMethodEnum;
import com.google.ads.googleads.v11.enums.BudgetStatusEnum;
import com.google.ads.googleads.v11.resources.CampaignBudget;
import com.google.ads.googleads.v11.services.*;
import com.google.ads.googleads.v11.utils.ResourceNames;
import com.google.api.gax.rpc.ServerStream;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.addyai.utils.Constants.*;

@Repository
public class CampaignRepositoryImpl implements CampaignRepository {
    private final GoogleAdsServiceClient googleAdsServiceClient;

    private final StreamRequest requestBuilder;

    public CampaignRepositoryImpl() {
        this.googleAdsServiceClient = GoogleAdsManagementApplication
                .getGoogleAdsClient()
                .getLatestVersion()
                .createGoogleAdsServiceClient();

        this.requestBuilder = new StreamRequestImpl(googleAdsServiceClient);
    }

    /**
     * Get a list of campaign details for each of the campaigns in a customer's account
     *
     * @param customerId the customer id of the account
     * @return list of campaign details
     * @throws GetResourceException
     */
    @Override
    public List<CampaignDetails> getCampaignDetails(long customerId) throws GetResourceException {
        List<CampaignDetails> campaignDetailsList;

        try {
            String query = GAQLUtils.getCampaignDetailsQuery();

            SearchGoogleAdsStreamRequest request = requestBuilder.buildStreamRequest(customerId, query);

            ServerStream<SearchGoogleAdsStreamResponse> response = requestBuilder.callStreamRequest(request);

            campaignDetailsList = GAQLUtils.convertStreamResponseToCampaignDetailsList(response);

            return campaignDetailsList;
        } catch (Exception e) {
            throw new GetResourceException(GET_RES_EXCEPTION_MSG + customerId);
        }
    }

    /**
     * Update a list of campaigns within a given customer account
     *
     * @param customerId         the customerId of the account to update
     * @param campaignOperations the list of campaign operations to be performed on the account
     * @throws UpdateResourceException
     */
    @Override
    public void updateCampaigns(long customerId,
                                List<CampaignOperation> campaignOperations) throws UpdateResourceException {
        try {
            CampaignServiceClient campaignServiceClient = GoogleAdsManagementApplication.getGoogleAdsClient()
                    .getLatestVersion().createCampaignServiceClient();

            // At this time we are going to assume the response is OK if no exception is thrown
            MutateCampaignsResponse response = campaignServiceClient
                    .mutateCampaigns(Long.toString(customerId), campaignOperations);
        } catch (Exception e) {
            // TODO: Log error here for reference
            throw new UpdateResourceException(UPDATE_RES_EXCEPTION_MSG + customerId);
        }
    }

    /**
     * Delete campaigns within a client's account
     *
     * @param customerId  the customer id of the client account
     * @param campaignIds the ids of the campaigns to delete
     * @throws DeleteResourceException
     */
    @Override
    public void deleteCampaigns(long customerId, List<Long> campaignIds) throws DeleteResourceException {
        List<CampaignOperation> campaignOperations = new ArrayList<>();

        try {
            CampaignServiceClient campaignServiceClient = GoogleAdsManagementApplication.getGoogleAdsClient()
                    .getLatestVersion().createCampaignServiceClient();

            for (long campaignId : campaignIds) {
                String campaignResourceName = ResourceNames.campaign(customerId, campaignId);
                // Constructs an operation that will remove the campaign with the specified resource name.
                CampaignOperation operation =
                        CampaignOperation.newBuilder().setRemove(campaignResourceName).build();
                campaignOperations.add(operation);
            }

            // At this time we are going to assume the response is OK if no exception is thrown
            MutateCampaignsResponse response =
                    campaignServiceClient.mutateCampaigns(
                            Long.toString(customerId), campaignOperations);
        } catch (Exception e) {
            throw new DeleteResourceException(DELETE_RES_EXCEPTION_MSG + customerId);
        }
    }

    /**
     * Add campaigns to a client's campaign
     *
     * @param customerId         the id of the customer account
     * @param campaignOperations a list of [CampaignOperation] for processing
     * @throws CreateResourceException
     */
    @Override
    public void addCampaigns(long customerId, List<CampaignOperation> campaignOperations) throws CreateResourceException {
        try {
            CampaignServiceClient campaignServiceClient = GoogleAdsManagementApplication.getGoogleAdsClient()
                    .getLatestVersion().createCampaignServiceClient();

            MutateCampaignsResponse response =
                    campaignServiceClient.mutateCampaigns(Long.toString(customerId), campaignOperations);
        } catch (Exception e) {
            throw new CreateResourceException(ADD_RES_EXCEPTION_MSG + customerId);
        }
    }

    /**
     * Create a campaign budget within a client's account
     *
     * @param customerId  the customer id for the client account
     * @param budgetValue the amount for the daily budget
     * @param isShared    is a shared budget
     * @return campaign budget's resource name
     */
    @Override
    public String createCampaignBudget(final long customerId,
                                       long budgetValue,
                                       final boolean isShared) {
        budgetValue = budgetValue * 1000000;

        CampaignBudgetServiceClient campaignBudgetServiceClient = GoogleAdsManagementApplication.getGoogleAdsClient()
                .getLatestVersion().createCampaignBudgetServiceClient();

        CampaignBudget budget = CampaignBudget.newBuilder()
                .setName("Budget # 902923")
                .setAmountMicros(budgetValue)
                .setStatus(BudgetStatusEnum.BudgetStatus.ENABLED)
                .setDeliveryMethod(BudgetDeliveryMethodEnum.BudgetDeliveryMethod.STANDARD)
                .setExplicitlyShared(isShared)
                .build();

        CampaignBudgetOperation operation = CampaignBudgetOperation.newBuilder()
                .setCreate(budget)
                .build();

        MutateCampaignBudgetsResponse budgetsResponse = campaignBudgetServiceClient.mutateCampaignBudgets(
                Long.toString(customerId), Lists.newArrayList(operation));

        MutateCampaignBudgetResult campaignBudgetResult = budgetsResponse.getResults(0);

        return campaignBudgetResult.getResourceName();
    }
}
