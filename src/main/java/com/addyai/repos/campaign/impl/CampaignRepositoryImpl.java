/*
 * Copyright (c) 2022.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. This program
 * is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 *
 */

package com.addyai.repos.campaign.impl;

import com.addyai.builder.GoogleAdsClientBuilder;
import com.addyai.error_handling.ApiExceptionResolver;
import com.addyai.models.CampaignDetails;
import com.addyai.repos.campaign.CampaignRepository;
import com.addyai.repos.request.StreamRequest;
import com.addyai.repos.request.impl.StreamRequestImpl;
import com.addyai.utils.helpers.GAQLHelper;
import com.google.ads.googleads.v12.services.*;
import com.google.api.gax.rpc.ServerStream;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

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
                return null;
        } catch (Exception e) {
            throw ApiExceptionResolver.doResolveException(e);
        }
    }

    @Override
    public List<String> performCampaignOperations(long customerId, List<CampaignOperation> campaignOperations) throws Exception {
        List<String> campaignResourceNameList = new ArrayList<>();

        try {
            MutateCampaignsResponse response =
                    campaignServiceClient.mutateCampaigns(Long.toString(customerId), campaignOperations);
            for (MutateCampaignResult result : response.getResultsList()) {
                campaignResourceNameList.add(result.getResourceName());
            }
        } catch (Exception e) {
            throw ApiExceptionResolver.doResolveException(e);
        }
        return campaignResourceNameList;
    }
}
