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

package com.addyai.repos.campaigns.criterion.impl;

import com.addyai.builder.GoogleAdsClientBuilder;
import com.addyai.error_handling.ApiExceptionResolver;
import com.addyai.models.campaign_criterion.CriterionDetails;
import com.addyai.repos.campaigns.criterion.CriterionRepository;
import com.addyai.repos.requests.StreamRequest;
import com.addyai.repos.requests.impl.StreamRequestImpl;
import com.addyai.utils.helpers.GAQLHelper;
import com.google.ads.googleads.v11.enums.CriterionTypeEnum;
import com.google.ads.googleads.v11.services.*;
import com.google.api.gax.rpc.ServerStream;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.addyai.utils.misc.Constants.NUM_CRITERION_CLASSES_SUPPORTED;

@Repository
public class CriterionRepositoryImpl implements CriterionRepository {
    private final StreamRequest requestBuilder;

    private final CampaignCriterionServiceClient campaignCriterionServiceClient;

    public CriterionRepositoryImpl() {
        GoogleAdsClientBuilder googleAdsClientBuilder = GoogleAdsClientBuilder.INSTANCE;

        GoogleAdsServiceClient googleAdsServiceClient = googleAdsClientBuilder
                .getGoogleAdsClient()
                .getLatestVersion()
                .createGoogleAdsServiceClient();

        this.requestBuilder = new StreamRequestImpl(googleAdsServiceClient);

        this.campaignCriterionServiceClient = googleAdsClientBuilder.getGoogleAdsClient()
                .getLatestVersion().createCampaignCriterionServiceClient();
    }

    @Override
    public List<CriterionDetails> fetchCampaignCriterionDetails(long customerId, String campaignResourceName) {
        List<CriterionDetails> criterionDetailsList = new ArrayList<>();
        SearchGoogleAdsStreamRequest request;
        ServerStream<SearchGoogleAdsStreamResponse> response;
        String query;

        for (int i = 0; i < NUM_CRITERION_CLASSES_SUPPORTED; i++) {
            switch (i) {
                case 0:
                    query = GAQLHelper.getNegativeKeywordQuery(campaignResourceName);

                    request = requestBuilder.buildStreamRequest(customerId, query);
                    response = requestBuilder.callStreamRequest(request);

                    List<CriterionDetails> negativeKeywordDetails =
                            GAQLHelper.convertStreamResponseToCriterionDetails(
                                    response,
                                    CriterionTypeEnum.CriterionType.KEYWORD);
                    criterionDetailsList.addAll(negativeKeywordDetails);
                    break;
                case 1:
                    query = GAQLHelper.getAdScheduleCriterionQuery(campaignResourceName);

                    request = requestBuilder.buildStreamRequest(customerId, query);
                    response = requestBuilder.callStreamRequest(request);

                    List<CriterionDetails> adScheduleDetails =
                            GAQLHelper.convertStreamResponseToCriterionDetails(
                                    response,
                                    CriterionTypeEnum.CriterionType.AD_SCHEDULE);

                    criterionDetailsList.addAll(adScheduleDetails);
                    break;
                case 2:
                    query = GAQLHelper.getLanguageQuery(campaignResourceName);

                    request = requestBuilder.buildStreamRequest(customerId, query);
                    response = requestBuilder.callStreamRequest(request);

                    List<CriterionDetails> languageDetails =
                            GAQLHelper.convertStreamResponseToCriterionDetails(
                                    response,
                                    CriterionTypeEnum.CriterionType.LANGUAGE);

                    criterionDetailsList.addAll(languageDetails);
                    break;
                case 3:
                    query = GAQLHelper.getDeviceQuery(campaignResourceName);

                    request = requestBuilder.buildStreamRequest(customerId, query);
                    response = requestBuilder.callStreamRequest(request);

                    List<CriterionDetails> deviceDetails =
                            GAQLHelper.convertStreamResponseToCriterionDetails(
                                    response,
                                    CriterionTypeEnum.CriterionType.DEVICE);

                    criterionDetailsList.addAll(deviceDetails);
                    break;
                case 4:
                    query = GAQLHelper.getLocationQuery(campaignResourceName);

                    request = requestBuilder.buildStreamRequest(customerId, query);
                    response = requestBuilder.callStreamRequest(request);

                    List<CriterionDetails> locationDetails =
                            GAQLHelper.convertStreamResponseToCriterionDetails(
                                    response,
                                    CriterionTypeEnum.CriterionType.LOCATION);

                    criterionDetailsList.addAll(locationDetails);
                    break;
                case 5:
                    query = GAQLHelper.getProximityQuery(campaignResourceName);

                    request = requestBuilder.buildStreamRequest(customerId, query);
                    response = requestBuilder.callStreamRequest(request);

                    List<CriterionDetails> proximityDetails =
                            GAQLHelper.convertStreamResponseToCriterionDetails(
                                    response,
                                    CriterionTypeEnum.CriterionType.PROXIMITY);

                    criterionDetailsList.addAll(proximityDetails);
                    break;
                default:
                    break;
            }
        }
        return criterionDetailsList;
    }

    @Override
    public List<String> performCriterionOperations(long customerId,
                                                   List<CampaignCriterionOperation> campaignCriterionOperationList) throws Exception {
        List<String> criterionResourceNameList = new ArrayList<>();

        try {
            MutateCampaignCriteriaResponse response = campaignCriterionServiceClient
                    .mutateCampaignCriteria(Long.toString(customerId), campaignCriterionOperationList);

            for (MutateCampaignCriterionResult result : response.getResultsList()) {
                criterionResourceNameList.add(result.getResourceName());
            }
        } catch (Exception e) {
            throw ApiExceptionResolver.doResolveException(e);
        }
        return criterionResourceNameList;
    }

    @Override
    public String getGeoTargetConstant(String locale,
                                       String countryCode,
                                       String location) throws Exception {
        try {
            GoogleAdsClientBuilder googleAdsClientBuilder = GoogleAdsClientBuilder.INSTANCE;

            // create an instance of GeoTargetConstantServiceClient
            GeoTargetConstantServiceClient geoTargetClient =
                    googleAdsClientBuilder
                            .getGoogleAdsClient()
                            .getLatestVersion()
                            .createGeoTargetConstantServiceClient();

            // Create a SuggestGeoTargetConstantsRequest Builder.
            // Set the locale and countryCode
            SuggestGeoTargetConstantsRequest.Builder requestBuilder =
                    SuggestGeoTargetConstantsRequest.newBuilder()
                            .setLocale(locale)
                            .setCountryCode(countryCode);

            // Set the locations list in the request builder
            requestBuilder.getLocationNamesBuilder().addAllNames(Collections.singletonList(location));

            // perform the request and extract the response
            SuggestGeoTargetConstantsResponse response =
                    geoTargetClient.suggestGeoTargetConstants(requestBuilder.build());

            // if the geotarget exists in the response results. Return the first geo-target's resource name.
            if (response.getGeoTargetConstantSuggestionsList().size() > 0 &&
                    response.getGeoTargetConstantSuggestionsList().get(0).hasGeoTargetConstant()) {
                return response
                        .getGeoTargetConstantSuggestionsList().get(0)
                        .getGeoTargetConstant()
                        .getResourceName();
            }
        } catch (Exception e) {
            throw ApiExceptionResolver.doResolveException(e);
        }
        return "";
    }
}
