package com.addyai.repos.campaigns.impl;

import com.addyai.builder.GoogleAdsClientBuilder;
import com.addyai.error_handling.ApiExceptionResolver;
import com.addyai.error_handling.exceptions.NotFoundException;
import com.addyai.models.CampaignDetails;
import com.addyai.repos.campaigns.CampaignRepository;
import com.addyai.repos.requests.StreamRequest;
import com.addyai.repos.requests.impl.StreamRequestImpl;
import com.addyai.utils.helpers.GAQLHelper;
import com.google.ads.googleads.v11.services.*;
import com.google.ads.googleads.v11.utils.ResourceNames;
import com.google.api.gax.rpc.ServerStream;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.addyai.utils.misc.Constants.RECORD_NOT_FOUND;

@Repository
public class CampaignRepositoryImpl implements CampaignRepository {

    private final CampaignServiceClient campaignServiceClient;

    private final StreamRequest requestBuilder;

    public CampaignRepositoryImpl() {
        GoogleAdsClientBuilder googleAdsClientBuilder = GoogleAdsClientBuilder.INSTANCE;

        GoogleAdsServiceClient googleAdsServiceClient = googleAdsClientBuilder
                .getGoogleAdsClient()
                .getLatestVersion()
                .createGoogleAdsServiceClient();

        this.requestBuilder = new StreamRequestImpl(googleAdsServiceClient);

        campaignServiceClient = googleAdsClientBuilder
                .getGoogleAdsClient()
                .getLatestVersion()
                .createCampaignServiceClient();
    }

    /**
     * Get a list of campaign details for each of the campaigns in a customer's account
     *
     * @param customerId the customer id of the account
     * @return list of campaign details
     * @throws Exception
     */
    @Override
    public List<CampaignDetails> fetchAllCampaignDetails(long customerId) throws Exception {
        List<CampaignDetails> campaignDetailsList;

        try {
            String query = GAQLHelper.getCampaignDetailsQuery();

            SearchGoogleAdsStreamRequest request = requestBuilder.buildStreamRequest(customerId, query);
            ServerStream<SearchGoogleAdsStreamResponse> response = requestBuilder.callStreamRequest(request);

            campaignDetailsList = GAQLHelper.convertStreamResponseToCampaignDetailsList(response);

            return campaignDetailsList;
        } catch (Exception e) {
            throw ApiExceptionResolver.doResolveException(e);
        }
    }

    /**
     * Get a campaign details object by campaign name
     *
     * @param customerId   the customer id of the account
     * @param campaignName the name of the campaign to be fetched
     * @return list of campaign details
     * @throws Exception
     */
    @Override
    public CampaignDetails fetchCampaignDetailsByName(long customerId, String campaignName) throws Exception {
        List<CampaignDetails> campaignDetailsList;

        try {
            String query = GAQLHelper.getCampaignDetailsByNameQuery(campaignName);

            SearchGoogleAdsStreamRequest request = requestBuilder.buildStreamRequest(customerId, query);
            ServerStream<SearchGoogleAdsStreamResponse> response = requestBuilder.callStreamRequest(request);

            campaignDetailsList = GAQLHelper.convertStreamResponseToCampaignDetailsList(response);

            if (campaignDetailsList.size() > 0)
                return campaignDetailsList.get(0);
            else
                throw new NotFoundException(RECORD_NOT_FOUND, "Campaign Not Found", "Campaign " + campaignName + " could not be found!");
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
     * @return a list of campaign Resource names
     * @throws Exception
     */
    @Override
    public List<String> addCampaigns(long customerId, List<CampaignOperation> campaignOperations) throws Exception {
        List<String> campaignResourceNameList = new ArrayList<>();
        try {
            MutateCampaignsResponse response =
                    campaignServiceClient.mutateCampaigns(Long.toString(customerId), campaignOperations);

            // populate a list of campaign resource names
            for (MutateCampaignResult result : response.getResultsList()) {
                campaignResourceNameList.add(result.getResourceName());
            }
        } catch (Exception e) {
            throw ApiExceptionResolver.doResolveException(e);
        }
        return campaignResourceNameList;
    }
}
