package com.addyai.repos.campaigns.impl;

import com.addyai.GoogleAdsManagementApplication;
import com.addyai.error_handling.ApiExceptionResolver;
import com.addyai.error_handling.ValidationErrorResponse;
import com.addyai.error_handling.exceptions.InvalidRequestException;
import com.addyai.models.BudgetDetails;
import com.addyai.models.CampaignDetails;
import com.addyai.repos.campaigns.CampaignRepository;
import com.addyai.repos.requests.StreamRequest;
import com.addyai.repos.requests.impl.StreamRequestImpl;
import com.addyai.utils.EntityValidator;
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

import static com.addyai.utils.Constants.GET_RES_EXCEPTION_MSG;

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
     * @throws Exception
     */
    @Override
    public List<CampaignDetails> getCampaignDetails(long customerId) throws Exception {
        List<CampaignDetails> campaignDetailsList;

        try {
            String query = GAQLUtils.getCampaignDetailsQuery();

            SearchGoogleAdsStreamRequest request = requestBuilder.buildStreamRequest(customerId, query);

            ServerStream<SearchGoogleAdsStreamResponse> response = requestBuilder.callStreamRequest(request);

            campaignDetailsList = GAQLUtils.convertStreamResponseToCampaignDetailsList(response);

            return campaignDetailsList;
        } catch (Exception e) {
            throw ApiExceptionResolver.doResolveException(e);
        }
    }

    /**
     * Update a list of campaigns within a given customer account
     *
     * @param customerId         the customerId of the account to update
     * @param campaignOperations the list of campaign operations to be performed on the account
     * @throws Exception
     */
    @Override
    public void updateCampaigns(long customerId,
                                List<CampaignOperation> campaignOperations) throws Exception {
        try {
            CampaignServiceClient campaignServiceClient = GoogleAdsManagementApplication.getGoogleAdsClient()
                    .getLatestVersion().createCampaignServiceClient();

            // At this time we are going to assume the response is OK if no exception is thrown
            MutateCampaignsResponse response = campaignServiceClient
                    .mutateCampaigns(Long.toString(customerId), campaignOperations);
        } catch (Exception e) {
            throw ApiExceptionResolver.doResolveException(e);
        }
    }

    /**
     * Delete campaigns within a client's account
     *
     * @param customerId  the customer id of the client account
     * @param campaignIds the ids of the campaigns to delete
     * @throws Exception
     */
    @Override
    public void deleteCampaigns(long customerId, List<Long> campaignIds) throws Exception {
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
            throw ApiExceptionResolver.doResolveException(e);
        }
    }

    /**
     * Add campaigns to a client's campaign
     *
     * @param customerId         the id of the customer account
     * @param campaignOperations a list of [CampaignOperation] for processing
     * @throws Exception
     */
    @Override
    public void addCampaigns(long customerId, List<CampaignOperation> campaignOperations) throws Exception {
        try {
            CampaignServiceClient campaignServiceClient = GoogleAdsManagementApplication.getGoogleAdsClient()
                    .getLatestVersion().createCampaignServiceClient();

            MutateCampaignsResponse response =
                    campaignServiceClient.mutateCampaigns(Long.toString(customerId), campaignOperations);
        } catch (Exception e) {
            throw ApiExceptionResolver.doResolveException(e);
        }
    }

    /**
     * Create a campaign budget within a client's account
     *
     * @param customerId    the customer id for the client account
     * @param budgetDetails the details of the budget
     * @return campaign budget's resource name
     */
    @Override
    public String createSingleCampaignBudget(final long customerId,
                                             final BudgetDetails budgetDetails) throws Exception {
        MutateCampaignBudgetResult campaignBudgetResult;

        // validate budget details before proceeding
        ValidationErrorResponse validationErrorResponse = EntityValidator.isBudgetDetailsValid(budgetDetails);

        if (validationErrorResponse != null)
            throw new InvalidRequestException(validationErrorResponse.getErrorType(),
                    validationErrorResponse.getErrorCode(),
                    validationErrorResponse.getErrorMessage());

        try {
            // convert budget to micros
            long budgetValue = budgetDetails.getDailyBudgetAmount() * 1000000L;

            CampaignBudgetServiceClient campaignBudgetServiceClient = GoogleAdsManagementApplication.getGoogleAdsClient()
                    .getLatestVersion().createCampaignBudgetServiceClient();

            CampaignBudget budget = CampaignBudget.newBuilder()
                    .setName(budgetDetails.getName())
                    .setAmountMicros(budgetValue)
                    .setStatus(BudgetStatusEnum.BudgetStatus.forNumber(budgetDetails.getStatus()))
                    .setDeliveryMethod(BudgetDeliveryMethodEnum.BudgetDeliveryMethod.forNumber(budgetDetails.getDeliveryMethod()))
                    .setExplicitlyShared(budgetDetails.isShared())
                    .build();

            CampaignBudgetOperation operation = CampaignBudgetOperation.newBuilder()
                    .setCreate(budget)
                    .build();

            MutateCampaignBudgetsResponse budgetsResponse = campaignBudgetServiceClient.mutateCampaignBudgets(
                    Long.toString(customerId), Lists.newArrayList(operation));

            campaignBudgetResult = budgetsResponse.getResults(0);
        } catch (Exception exception) {
            throw ApiExceptionResolver.doResolveException(exception);
        }
        return campaignBudgetResult.getResourceName();
    }

    @Override
    public List<BudgetDetails> getCampaignBudgetDetails(long customerId) throws Exception {
        try {
            String query = GAQLUtils.getCampaignBudgetQuery();

            SearchGoogleAdsStreamRequest request = requestBuilder.buildStreamRequest(customerId, query);

            ServerStream<SearchGoogleAdsStreamResponse> response = requestBuilder.callStreamRequest(request);

            return GAQLUtils.convertStreamResponseToBudgetDetails(response);
        } catch (Exception e) {
            throw ApiExceptionResolver.doResolveException(e);
        }
    }

    @Override
    public void updateCampaignBudgets(long customerId, List<CampaignBudgetOperation> campaignBudgetOperations) throws Exception {
        try {
            CampaignBudgetServiceClient campaignBudgetServiceClient = GoogleAdsManagementApplication.getGoogleAdsClient()
                    .getLatestVersion().createCampaignBudgetServiceClient();

            // At this time we are going to assume the response is OK if no exception is thrown
            MutateCampaignBudgetsResponse response = campaignBudgetServiceClient
                    .mutateCampaignBudgets(Long.toString(customerId), campaignBudgetOperations);
        } catch (Exception e) {
            throw ApiExceptionResolver.doResolveException(e);
        }
    }

    @Override
    public void deleteCampaignBudgets(long customerId, List<Long> campaignBudgetIds) {

    }
}
