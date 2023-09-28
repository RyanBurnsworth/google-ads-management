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

package com.addyai.repos.adgroup.impl;

import com.addyai.builder.GoogleAdsClientBuilder;
import com.addyai.error_handling.ApiExceptionResolver;
import com.addyai.models.AdGroupDetails;
import com.addyai.repos.adgroup.AdGroupRepository;
import com.addyai.repos.request.StreamRequest;
import com.addyai.repos.request.impl.StreamRequestImpl;
import com.addyai.utils.helpers.GAQLHelper;
import com.google.ads.googleads.v14.services.*;
import com.google.api.gax.rpc.ServerStream;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AdGroupRepositoryImpl implements AdGroupRepository {
    private final AdGroupServiceClient adGroupServiceClient;

    private final StreamRequest requestBuilder;

    public AdGroupRepositoryImpl() {
        GoogleAdsClientBuilder googleAdsClientBuilder = GoogleAdsClientBuilder.INSTANCE;

        GoogleAdsServiceClient googleAdsServiceClient = googleAdsClientBuilder
                .getGoogleAdsClient()
                .getLatestVersion()
                .createGoogleAdsServiceClient();

        this.requestBuilder = new StreamRequestImpl(googleAdsServiceClient);

        adGroupServiceClient = googleAdsClientBuilder
                .getGoogleAdsClient()
                .getLatestVersion()
                .createAdGroupServiceClient();
    }

    @Override
    public List<AdGroupDetails> fetchAllAdGroupDetails(long customerId, String campaignResName) throws Exception {
        List<AdGroupDetails> adGroupDetailsList;

        try {
            String query = GAQLHelper.getAdGroupDetailsByCampaignQuery(campaignResName);

            SearchGoogleAdsStreamRequest request = requestBuilder.buildStreamRequest(customerId, query);
            ServerStream<SearchGoogleAdsStreamResponse> response = requestBuilder.callStreamRequest(request);

            adGroupDetailsList = GAQLHelper.convertStreamResponseToAdGroupDetails(response);

            return adGroupDetailsList;
        } catch (Exception e) {
            throw ApiExceptionResolver.doResolveException(e);
        }
    }

    @Override
    public List<String> performAdGroupOperations(long customerId, List<AdGroupOperation> adGroupOperationList) throws Exception {
        List<String> adGroupResourceNameList = new ArrayList<>();

        try {
            MutateAdGroupsResponse response = adGroupServiceClient
                    .mutateAdGroups(Long.toString(customerId), adGroupOperationList);

            for (MutateAdGroupResult result : response.getResultsList()) {
                adGroupResourceNameList.add(result.getResourceName());
            }
        } catch (Exception e) {
            throw ApiExceptionResolver.doResolveException(e);
        }
        return adGroupResourceNameList;
    }
}
