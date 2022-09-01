package com.addyai.repos.campaigns.impl;

import com.addyai.GoogleAdsManagementApplication;
import com.addyai.exceptions.CreateResourceException;
import com.addyai.exceptions.DeleteResourceException;
import com.addyai.exceptions.GetResourceException;
import com.addyai.exceptions.UpdateResourceException;
import com.addyai.models.CampaignDetails;
import com.addyai.models.OperationResponse;
import com.addyai.repos.campaigns.CampaignRepository;
import com.addyai.repos.requests.StreamRequest;
import com.addyai.repos.requests.impl.StreamRequestImpl;
import com.addyai.utils.GAQLUtils;
import com.google.ads.googleads.v11.common.ManualCpc;
import com.google.ads.googleads.v11.resources.Campaign;
import com.google.ads.googleads.v11.services.*;
import com.google.ads.googleads.v11.utils.ResourceNames;
import com.google.api.gax.rpc.ServerStream;

import java.util.ArrayList;
import java.util.List;

import static com.addyai.GoogleAdsManagementApplication.CLIENT_ACCOUNT_ID;

public class CampaignRepositoryImpl implements CampaignRepository {
    private final GoogleAdsServiceClient googleAdsServiceClient;

    public CampaignRepositoryImpl() {
        this.googleAdsServiceClient = GoogleAdsManagementApplication
                .getGoogleAdsClient()
                .getLatestVersion()
                .createGoogleAdsServiceClient();
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

            StreamRequest requestBuilder = new StreamRequestImpl(googleAdsServiceClient);
            SearchGoogleAdsStreamRequest request = requestBuilder.buildStreamRequest(CLIENT_ACCOUNT_ID, query);
            ServerStream<SearchGoogleAdsStreamResponse> response = requestBuilder.callStreamRequest(request);

            campaignDetailsList = GAQLUtils.convertStreamResponseToCampaignDetailsList(response);
        } catch (Exception e) {
            throw new GetResourceException("Failed to retrieve campaign details for account: " + customerId + " " + e);
        }
        return campaignDetailsList;
    }

    /**
     * Update a list of campaigns for a given customer account
     *
     * @param customerId         the customerId of the account to update
     * @param campaignOperations the list of campaign operations to be performed on the account
     * @return a response for each of the campaign operations
     * @throws UpdateResourceException
     */
    @Override
    public List<OperationResponse> updateCampaignDetails(long customerId,
                                                         List<CampaignOperation> campaignOperations) throws UpdateResourceException {
        List<OperationResponse> operationRespons = new ArrayList<>();

        try {
            CampaignServiceClient campaignServiceClient = GoogleAdsManagementApplication.getGoogleAdsClient()
                    .getLatestVersion().createCampaignServiceClient();

            MutateCampaignsResponse response = campaignServiceClient
                    .mutateCampaigns(Long.toString(customerId), campaignOperations);

            for (MutateCampaignResult result : response.getResultsList()) {
                OperationResponse operationResponse = new OperationResponse();
                operationResponse.setId(result.getCampaign().getId());
                operationResponse.setName(result.getCampaign().getName());
                operationResponse.setOperationSuccessful(true);
                operationRespons.add(operationResponse);
            }
        } catch (Exception e) {
            // TODO: Log error here for refrence
            throw new UpdateResourceException("Error updating campaigns. " + e);
        }
        return operationRespons;
    }

    /**
     * Delete campaigns from a client's account
     *
     * @param customerId  the customer id of the client account
     * @param campaignIds the ids of the campaigns to delete
     * @return response for each campaign deletion
     * @throws DeleteResourceException
     */
    @Override
    public List<OperationResponse> deleteCampaigns(long customerId, List<Long> campaignIds) throws DeleteResourceException {
        List<CampaignOperation> campaignOperations = new ArrayList<>();
        List<OperationResponse> operationResponses = new ArrayList<>();

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

            MutateCampaignsResponse response =
                    campaignServiceClient.mutateCampaigns(
                            Long.toString(customerId), campaignOperations);

            for (MutateCampaignResult mutateCampaignResult : response.getResultsList()) {
                OperationResponse operationResponse = new OperationResponse();
                operationResponse.setId(mutateCampaignResult.getCampaign().getId());
                operationResponse.setOperationSuccessful(true);
                operationResponse.setName(mutateCampaignResult.getCampaign().getName());
                operationResponses.add(operationResponse);
            }
        } catch (Exception e) {
            throw new DeleteResourceException("Failed to delete resources: " + e);
        }
        return operationResponses;
    }

    @Override
    public void addCampaigns(long customerId,
                             List<CampaignDetails> campaignDetails,
                             List<Campaign.NetworkSettings> networkSettingsList) throws CreateResourceException {
        List<CampaignOperation> campaignOperations = new ArrayList<>();
        for (int i = 0; i < campaignDetails.size(); i++) {
            Campaign campaign =
                    Campaign.newBuilder()
                            .setName(campaignDetails.get(i).getCampaignName())
                            .setAdvertisingChannelType(campaignDetails.get(i).getAdvertisingChannelType())
                            .setStatus(campaignDetails.get(i).getStatus())
                            .setManualCpc(ManualCpc.newBuilder()
                                    .setEnhancedCpcEnabled(campaignDetails.get(i).isEnhancedCpcEnabled()).build())
                            .setCampaignBudget(campaignDetails.get(i).getBudget())
                            .setNetworkSettings(networkSettingsList.get(i))
                            .setStartDate(campaignDetails.get(i).getStartDate())
                            .setEndDate(campaignDetails.get(i).getEndDate())
                            .build();
            CampaignOperation op = CampaignOperation.newBuilder().setCreate(campaign).build();
            campaignOperations.add(op);
        }

        try {
            CampaignServiceClient campaignServiceClient = GoogleAdsManagementApplication.getGoogleAdsClient()
                    .getLatestVersion().createCampaignServiceClient();

            MutateCampaignsResponse response =
                    campaignServiceClient.mutateCampaigns(Long.toString(customerId), campaignOperations);
            System.out.printf("Added %d campaigns:%n", response.getResultsCount());
            for (MutateCampaignResult result : response.getResultsList()) {
                System.out.println(result.getResourceName());
            }

            // TODO: Send a 200 response

        } catch (Exception e) {
            throw new CreateResourceException("Failed to create campaigns: " + e);
        }
    }
}
