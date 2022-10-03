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

package com.addyai.utils.helpers;

import com.addyai.adapter.GoogleAdsRowAdapter;
import com.addyai.adapter.impl.GoogleAdsRowAdapterImpl;
import com.addyai.models.AdGroupDetails;
import com.addyai.models.BudgetDetails;
import com.addyai.models.CampaignDetails;
import com.addyai.models.KeywordDetails;
import com.addyai.models.campaign_criterion.*;
import com.google.ads.googleads.v11.enums.CriterionTypeEnum;
import com.google.ads.googleads.v11.services.GoogleAdsRow;
import com.google.ads.googleads.v11.services.SearchGoogleAdsStreamResponse;
import com.google.api.gax.rpc.ServerStream;

import java.util.ArrayList;
import java.util.List;

public class GAQLHelper {
    public static String getCampaignDetailsQuery() {
        return "SELECT campaign.id," +
                " campaign.name," +
                " campaign.resource_name, " +
                " campaign.status," +
                " campaign.advertising_channel_type," +
                " campaign.campaign_budget," +
                " campaign.bidding_strategy," +
                " campaign.geo_target_type_setting.positive_geo_target_type," +
                " campaign.geo_target_type_setting.negative_geo_target_type," +
                " campaign.manual_cpc.enhanced_cpc_enabled," +
                " campaign.start_date," +
                " campaign.end_date," +
                " campaign.campaign_budget, " +
                " campaign.network_settings.target_content_network," +
                " campaign.network_settings.target_google_search," +
                " campaign.network_settings.target_search_network" +
                " FROM campaign WHERE campaign.status IN ('ENABLED', 'PAUSED') ORDER BY campaign.id";
    }

    public static String getCampaignDetailsByNameQuery(String name) {
        return "SELECT campaign.id," +
                " campaign.name," +
                " campaign.resource_name, " +
                " campaign.status," +
                " campaign.advertising_channel_type," +
                " campaign.campaign_budget," +
                " campaign.bidding_strategy," +
                " campaign.geo_target_type_setting.positive_geo_target_type," +
                " campaign.geo_target_type_setting.negative_geo_target_type," +
                " campaign.manual_cpc.enhanced_cpc_enabled," +
                " campaign.optimization_score," +
                " campaign.start_date," +
                " campaign.end_date," +
                " campaign.campaign_budget, " +
                " campaign.network_settings.target_content_network," +
                " campaign.network_settings.target_google_search," +
                " campaign.network_settings.target_partner_search_network," +
                " campaign.network_settings.target_search_network" +
                " FROM campaign WHERE campaign.name = '" + name + "' ORDER BY campaign.id";
    }

    public static String getCampaignBudgetQuery() {
        return "SELECT" +
                " campaign_budget.status, " +
                " campaign_budget.amount_micros," +
                " campaign_budget.explicitly_shared," +
                " campaign_budget.delivery_method," +
                " campaign_budget.resource_name," +
                " campaign_budget.name," +
                " campaign_budget.id" +
                " FROM campaign_budget WHERE campaign_budget.status = 'ENABLED'";
    }

    public static String getAdScheduleCriterionQuery(String campaign) {
        return "SELECT " +
                "  campaign_criterion.criterion_id, " +
                "  campaign_criterion.resource_name, " +
                "  campaign_criterion.type, " +
                "  campaign_criterion.status, " +
                "  campaign_criterion.campaign, " +
                "  campaign_criterion.ad_schedule.start_minute, " +
                "  campaign_criterion.ad_schedule.start_hour, " +
                "  campaign_criterion.ad_schedule.end_minute, " +
                "  campaign_criterion.ad_schedule.day_of_week, " +
                "  campaign_criterion.ad_schedule.end_hour, " +
                "  campaign_criterion.bid_modifier " +
                "FROM campaign_criterion WHERE campaign_criterion.status = 'ENABLED' AND " +
                "campaign_criterion.campaign = '" + campaign + "' AND campaign_criterion.ad_schedule.day_of_week IN " +
                "('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY')";
    }

    public static String getNegativeKeywordQuery(String campaign) {
        return "SELECT " +
                "  campaign_criterion.criterion_id, " +
                "  campaign_criterion.resource_name, " +
                "  campaign_criterion.campaign, " +
                "  campaign_criterion.type, " +
                "  campaign_criterion.status, " +
                "  campaign_criterion.negative, " +
                "  campaign_criterion.keyword.text, " +
                "  campaign_criterion.keyword.match_type " +
                "FROM campaign_criterion WHERE campaign_criterion.status = 'ENABLED' AND " +
                "campaign_criterion.campaign ='" + campaign + "' AND campaign_criterion.keyword.text != '' AND " +
                "campaign_criterion.negative = True";
    }

    public static String getProximityQuery(String campaign) {
        return "SELECT " +
                "  campaign_criterion.criterion_id, " +
                "  campaign_criterion.resource_name, " +
                "  campaign_criterion.campaign, " +
                "  campaign_criterion.type, " +
                "  campaign_criterion.status, " +
                "  campaign_criterion.negative, " +
                "  campaign_criterion.proximity.address.city_name, " +
                "  campaign_criterion.proximity.address.country_code, " +
                "  campaign_criterion.proximity.address.postal_code, " +
                "  campaign_criterion.proximity.address.province_code, " +
                "  campaign_criterion.proximity.address.province_name, " +
                "  campaign_criterion.proximity.address.street_address, " +
                "  campaign_criterion.proximity.geo_point.latitude_in_micro_degrees, " +
                "  campaign_criterion.proximity.geo_point.longitude_in_micro_degrees, " +
                "  campaign_criterion.proximity.radius, " +
                "  campaign_criterion.proximity.radius_units, " +
                "  campaign_criterion.bid_modifier " +
                "FROM campaign_criterion WHERE campaign_criterion.status = 'ENABLED' AND " +
                "campaign_criterion.campaign ='" + campaign + "' AND campaign_criterion.proximity.radius != 0";
    }

    public static String getDeviceQuery(String campaign) {
        return "SELECT " +
                "  campaign_criterion.criterion_id, " +
                "  campaign_criterion.resource_name, " +
                "  campaign_criterion.campaign, " +
                "  campaign_criterion.type, " +
                "  campaign_criterion.status, " +
                "  campaign_criterion.negative, " +
                "  campaign_criterion.device.type, " +
                "  campaign_criterion.bid_modifier " +
                "FROM campaign_criterion WHERE campaign_criterion.status = 'ENABLED' AND " +
                "campaign_criterion.campaign ='" + campaign + "' AND campaign_criterion.device.type " +
                "IN ('DESKTOP', 'MOBILE', 'TABLET')";
    }

    public static String getLanguageQuery(String campaign) {
        return "SELECT " +
                "  campaign_criterion.criterion_id, " +
                "  campaign_criterion.resource_name, " +
                "  campaign_criterion.campaign, " +
                "  campaign_criterion.type, " +
                "  campaign_criterion.status, " +
                "  campaign_criterion.negative, " +
                "  campaign_criterion.bid_modifier, " +
                "  campaign_criterion.language.language_constant " +
                "FROM campaign_criterion WHERE campaign_criterion.status = 'ENABLED' AND " +
                "campaign_criterion.campaign ='" + campaign + "'";
    }

    public static String getLocationQuery(String campaign) {
        return "SELECT " +
                "  campaign_criterion.criterion_id, " +
                "  campaign_criterion.resource_name, " +
                "  campaign_criterion.campaign, " +
                "  campaign_criterion.type, " +
                "  campaign_criterion.status, " +
                "  campaign_criterion.negative, " +
                "  campaign_criterion.bid_modifier, " +
                "  campaign_criterion.location.geo_target_constant " +
                "FROM campaign_criterion WHERE campaign_criterion.status = 'ENABLED' AND " +
                "campaign_criterion.campaign ='" + campaign + "'";
    }

    public static String getAdGroupDetailsQuery() {
        return "SELECT " +
                "ad_group.id, " +
                "ad_group.name, " +
                "ad_group.resource_name, " +
                "ad_group.campaign, " +
                "ad_group.type, " +
                "ad_group.status, " +
                "ad_group.cpc_bid_micros " +
                "FROM ad_group WHERE ad_group.status IN ('ENABLED', 'PAUSED') ORDER BY ad_group.id";
    }

    public static String getAdGroupDetailsByCampaignQuery(String campaignResName) {
        return "SELECT " +
                "ad_group.id, " +
                "ad_group.name, " +
                "ad_group.resource_name, " +
                "ad_group.campaign, " +
                "ad_group.type, " +
                "ad_group.status, " +
                "ad_group.cpc_bid_micros " +
                "FROM ad_group WHERE ad_group.campaign ='" + campaignResName +
                "' AND ad_group.status IN ('ENABLED', 'PAUSED')";
    }

    public static String getKeywordDetailsByAdGroup(String adGroupResName) {
        return "SELECT " +
                "ad_group_criterion.criterion_id, " +
                "ad_group_criterion.resource_name, " +
                "ad_group_criterion.ad_group, " +
                "ad_group_criterion.type, " +
                "ad_group_criterion.status, " +
                "ad_group_criterion.keyword.match_type, " +
                "ad_group_criterion.keyword.text, " +
                "ad_group_criterion.cpc_bid_micros, " +
                "ad_group_criterion.bid_modifier " +
                "FROM ad_group_criterion WHERE ad_group_criterion.ad_group ='" + adGroupResName +
                "' AND ad_group_criterion.status IN ('ENABLED', 'PAUSED')";
    }

    public static List<CampaignDetails> convertStreamResponseToCampaignDetailsList(ServerStream<SearchGoogleAdsStreamResponse> streamResponse) {
        GoogleAdsRowAdapter googleAdsRowAdapter = new GoogleAdsRowAdapterImpl();
        List<CampaignDetails> campaignDetailsList = new ArrayList<>();

        for (SearchGoogleAdsStreamResponse response : streamResponse) {
            for (GoogleAdsRow googleAdsRow : response.getResultsList()) {
                CampaignDetails details = googleAdsRowAdapter.getCampaignDetails(googleAdsRow);
                campaignDetailsList.add(details);
            }
        }

        return campaignDetailsList;
    }

    public static List<BudgetDetails> convertStreamResponseToBudgetDetails(ServerStream<SearchGoogleAdsStreamResponse> streamResponse) {
        GoogleAdsRowAdapter googleAdsRowAdapter = new GoogleAdsRowAdapterImpl();
        List<BudgetDetails> budgetDetailsList = new ArrayList<>();

        for (SearchGoogleAdsStreamResponse response : streamResponse) {
            for (GoogleAdsRow googleAdsRow : response.getResultsList()) {
                BudgetDetails budgetDetails = googleAdsRowAdapter.getBudgetDetails(googleAdsRow);
                budgetDetailsList.add(budgetDetails);
            }
        }
        return budgetDetailsList;
    }

    public static List<AdGroupDetails> convertStreamResponseToAdGroupDetails(ServerStream<SearchGoogleAdsStreamResponse> streamResponse) {
        GoogleAdsRowAdapterImpl googleAdsRowAdapter = new GoogleAdsRowAdapterImpl();
        List<AdGroupDetails> adGroupDetailsList = new ArrayList<>();

        for (SearchGoogleAdsStreamResponse response : streamResponse) {
            for (GoogleAdsRow googleAdsRow : response.getResultsList()) {
                AdGroupDetails adGroupDetails = googleAdsRowAdapter.getAdGroupDetails(googleAdsRow);
                adGroupDetailsList.add(adGroupDetails);
            }
        }
        return adGroupDetailsList;
    }

    public static List<KeywordDetails> convertStreamResponseToKeywordDetails(ServerStream<SearchGoogleAdsStreamResponse> streamResponses) {
        GoogleAdsRowAdapterImpl googleAdsRowAdapter = new GoogleAdsRowAdapterImpl();
        List<KeywordDetails> keywordDetailsList = new ArrayList<>();

        for (SearchGoogleAdsStreamResponse response : streamResponses) {
            for (GoogleAdsRow googleAdsRow : response.getResultsList()) {
                KeywordDetails keywordDetails = googleAdsRowAdapter.getKeywordDetails(googleAdsRow);
                keywordDetailsList.add(keywordDetails);
            }
        }
        return keywordDetailsList;
    }

    public static List<CriterionDetails> convertStreamResponseToCriterionDetails(
            ServerStream<SearchGoogleAdsStreamResponse> streamResponse, CriterionTypeEnum.CriterionType criterionType) {
        GoogleAdsRowAdapter googleAdsRowAdapter = new GoogleAdsRowAdapterImpl();
        List<CriterionDetails> criterionDetailsList = new ArrayList<>();

        for (SearchGoogleAdsStreamResponse response : streamResponse) {
            for (GoogleAdsRow googleAdsRow : response.getResultsList()) {
                switch (criterionType) {
                    case AD_SCHEDULE:
                        AdScheduleDetails adScheduleDetails = googleAdsRowAdapter.getAdScheduleDetails(googleAdsRow);
                        criterionDetailsList.add(adScheduleDetails);
                        break;
                    case KEYWORD:
                        NegativeKeywordDetails negativeKeywordDetails = googleAdsRowAdapter.getNegativeKeywordDetails(googleAdsRow);
                        criterionDetailsList.add(negativeKeywordDetails);
                        break;
                    case DEVICE:
                        DeviceDetails deviceDetails = googleAdsRowAdapter.getDeviceDetails(googleAdsRow);
                        criterionDetailsList.add(deviceDetails);
                        break;
                    case LANGUAGE:
                        LanguageDetails languageDetails = googleAdsRowAdapter.getLanguageDetails(googleAdsRow);
                        if (languageDetails != null)
                            criterionDetailsList.add(languageDetails);
                        break;
                    case LOCATION:
                        LocationDetails locationDetails = googleAdsRowAdapter.getLocationDetails(googleAdsRow);
                        if (locationDetails != null)
                            criterionDetailsList.add(locationDetails);
                        break;
                    case PROXIMITY:
                        ProximityDetails proximityDetails =
                                googleAdsRowAdapter.getProximityDetails(googleAdsRow);
                        criterionDetailsList.add(proximityDetails);
                        break;
                    default:
                        break;
                }
            }
        }
        return criterionDetailsList;
    }
}
