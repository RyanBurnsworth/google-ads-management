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

package com.addyai.repos.keyword.impl;

import com.addyai.builder.GoogleAdsClientBuilder;
import com.addyai.error_handling.ApiExceptionResolver;
import com.addyai.models.KeywordDetails;
import com.addyai.repos.keyword.KeywordRepository;
import com.addyai.repos.request.StreamRequest;
import com.addyai.repos.request.impl.StreamRequestImpl;
import com.addyai.utils.helpers.GAQLHelper;
import com.google.ads.googleads.v11.services.*;
import com.google.api.gax.rpc.ServerStream;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class KeywordRepositoryImpl implements KeywordRepository {
    private final AdGroupCriterionServiceClient adGroupCriterionServiceClient;

    private final StreamRequest requestBuilder;

    public KeywordRepositoryImpl() {
        GoogleAdsClientBuilder googleAdsClientBuilder = GoogleAdsClientBuilder.INSTANCE;

        GoogleAdsServiceClient googleAdsServiceClient = googleAdsClientBuilder
                .getGoogleAdsClient()
                .getLatestVersion()
                .createGoogleAdsServiceClient();

        this.requestBuilder = new StreamRequestImpl(googleAdsServiceClient);

        adGroupCriterionServiceClient = googleAdsClientBuilder
                .getGoogleAdsClient()
                .getLatestVersion()
                .createAdGroupCriterionServiceClient();

    }

    @Override
    public List<KeywordDetails> fetchKeywordDetailsByAdGroup(long customerId, String adGroupResName) throws Exception {
        List<KeywordDetails> keywordDetailsList;

        try {
            String query = GAQLHelper.getKeywordDetailsByAdGroup(adGroupResName);

            SearchGoogleAdsStreamRequest request = requestBuilder.buildStreamRequest(customerId, query);
            ServerStream<SearchGoogleAdsStreamResponse> response = requestBuilder.callStreamRequest(request);

            keywordDetailsList = GAQLHelper.convertStreamResponseToKeywordDetails(response);

            return keywordDetailsList;
        } catch (Exception e) {
            throw ApiExceptionResolver.doResolveException(e);
        }
    }

    @Override
    public List<String> performKeywordOperations(long customerId,
                                                 List<AdGroupCriterionOperation> adGroupCriterionOperationList) throws Exception {
        List<String> keywordResourceNameList = new ArrayList<>();

        try {
            MutateAdGroupCriteriaResponse response = adGroupCriterionServiceClient
                    .mutateAdGroupCriteria(Long.toString(customerId), adGroupCriterionOperationList);

            for (MutateAdGroupCriterionResult result : response.getResultsList()) {
                keywordResourceNameList.add(result.getResourceName());
            }
        } catch (Exception e) {
            throw ApiExceptionResolver.doResolveException(e);
        }
        return keywordResourceNameList;
    }
}
