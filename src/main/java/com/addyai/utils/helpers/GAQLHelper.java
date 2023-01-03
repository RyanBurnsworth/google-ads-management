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
import com.addyai.models.ads.AdDetails;
import com.addyai.models.ads.ResponsiveSearchAdDetails;
import com.addyai.models.assets.AssetDetails;
import com.addyai.models.assets.CallExtensionDetails;
import com.addyai.models.assets.SitelinkDetails;
import com.addyai.models.campaign_criterion.*;
import com.google.ads.googleads.v12.enums.AssetTypeEnum;
import com.google.ads.googleads.v12.enums.CriterionTypeEnum;
import com.google.ads.googleads.v12.services.GoogleAdsRow;
import com.google.ads.googleads.v12.services.SearchGoogleAdsStreamResponse;
import com.google.api.gax.rpc.ServerStream;

import java.util.ArrayList;
import java.util.List;

import static com.addyai.utils.misc.Constants.RESPONSIVE_AD_TYPE;

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

    /**
     * Return a search query for retrieving the keywords from an AdGroup
     *
     * @param adGroupResName the adGroup to search for keywords within
     * @return a GAQL search query
     */
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

    /**
     * Return a string for retrieving the site link assets from an account
     *
     * @return a GAQL search query
     */
    public static String getSitelinksAssetQuery() {
        return "SELECT " +
                "asset.id, " +
                "asset.name, " +
                "asset.type, " +
                "asset.source, " +
                "asset.sitelink_asset.description1, " +
                "asset.sitelink_asset.description2, " +
                "asset.sitelink_asset.link_text, " +
                "asset.sitelink_asset.start_date, " +
                "asset.sitelink_asset.end_date " +
                "FROM asset WHERE asset.sitelink_asset.link_text != ''";
    }

    /**
     * Return a search query string for retrieving extension assets from an account
     *
     * @return a GAQL search query
     */
    public static String getCallExtensionAssetQuery() {
        return "SELECT " +
                "asset.id, " +
                "asset.name, " +
                "asset.type, " +
                "asset.source, " +
                "asset.call_asset.ad_schedule_targets, " +
                "asset.call_asset.country_code, " +
                "asset.call_asset.phone_number " +
                "FROM asset WHERE asset.call_asset.phone_number != ''";
    }

    /**
     * Return a search query string for retrieving the responsive search ads in an adgroup
     *
     * @param adGroupResourceName the name of the adGroup to search within
     * @return a search query string for responsive search ads
     */
    public static String getResponsiveSearchAdQuery(String adGroupResourceName) {
        return "SELECT " +
                "ad_group_ad.ad.resource_name, " +
                "ad_group_ad.status, " +
                "ad_group_ad.ad.display_url, " +
                "ad_group_ad.ad.final_urls, " +
                "ad_group_ad.ad.responsive_search_ad.headlines, " +
                "ad_group_ad.ad.responsive_search_ad.descriptions, " +
                "ad_group_ad.ad.responsive_search_ad.path1, " +
                "ad_group_ad.ad.responsive_search_ad.path2 " +
                "FROM ad_group_ad WHERE " +
                "ad_group_ad.status IN ('ENABLED', 'PAUSED') " +
                "AND ad_group_ad.ad_group='" + adGroupResourceName + "'";
    }

    /**
     * Create a query for retrieving metrics by the metric's name
     *
     * @param name the name of the metric
     * @param metricType the type of metric (campaign, adgroup, ad, keyword)
     * @return the query for metrics by name
     */
    public static String getMetricsByNameQuery(String name, String metricType) {
        return "SELECT " +
                " " + metricType + ".resource_name, " +
                " " + metricType + ".name, " +
                "  metrics.clicks, " +
                "  metrics.impressions, " +
                "  metrics.conversions, " +
                "  metrics.phone_calls, " +
                "  metrics.invalid_clicks, " +
                "  metrics.average_cpc, " +
                "  metrics.ctr, " +
                "  metrics.invalid_click_rate, " +
                "  metrics.cost_micros, " +
                "  metrics.cost_per_conversion, " +
                "  metrics.conversions_value " +
                "FROM " + metricType + " WHERE " + metricType + ".name='" + name + "'";
    }

    /**
     * Get a query for metrics related [campaigns, adgroups] for a given custom date range
     *
     * @param metricType the type of metric to get a query for
     * @param startDate  the date to start reporting from. Use YYYY-MM-DD format
     * @param endDate    the date to end reporting from. Use YYYY-MM-DD format.
     * @return a query for  a given metric and date ranges
     */
    public static String getMetricsByCustomDateRange(String metricType, String startDate, String endDate) {
        metricType = metricType.toLowerCase();
        return "SELECT " +
                " " + metricType + ".resource_name, " +
                " " + metricType + ".name, " +
                "  metrics.clicks, " +
                "  metrics.impressions, " +
                "  metrics.conversions, " +
                "  metrics.phone_calls, " +
                "  metrics.invalid_clicks, " +
                "  metrics.average_cpc, " +
                "  metrics.ctr, " +
                "  metrics.invalid_click_rate, " +
                "  metrics.cost_micros, " +
                "  metrics.cost_per_conversion, " +
                "  metrics.conversions_value " +
                "FROM " + metricType + " WHERE segments.date >= " + startDate + " AND segments.date <=" + endDate;
    }


    /**
     * Convert an incoming ServerStream response into campaign details
     *
     * @param streamResponse a stream response from Google's server
     * @return a list of {@link CampaignDetails}
     */

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


    /**
     * Convert an incoming ServerStream response into budgete details
     *
     * @param streamResponse a stream response from Google's server
     * @return a list of {@link BudgetDetails}
     */
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


    /**
     * Convert an incoming ServerStream response into AdGroup details
     *
     * @param streamResponse a stream response from Google's server
     * @return a list of {@link AdGroupDetails}
     */
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


    /**
     * Convert an incoming ServerStream response into keyword details
     *
     * @param streamResponses a stream response from Google's server
     * @return a list of {@link KeywordDetails}
     */
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


    /**
     * Convert an incoming ServerStream response into criterion details
     *
     * @param streamResponse a stream response from Google's server
     * @param criterionType  the type of criterion to return details for
     * @return a list of {@link CriterionDetails}
     */
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


    /**
     * Convert an incoming ServerStream response into asset details
     *
     * @param streamResponse a stream response from Google's server
     * @param assetType      the type of asset to convert to
     * @return a list of {@link AssetDetails}
     */
    public static List<AssetDetails> convertStreamResponseToAssetDetails(
            ServerStream<SearchGoogleAdsStreamResponse> streamResponse,
            AssetTypeEnum.AssetType assetType) {
        GoogleAdsRowAdapter googleAdsRowAdapter = new GoogleAdsRowAdapterImpl();
        List<AssetDetails> assetDetailsList = new ArrayList<>();

        for (SearchGoogleAdsStreamResponse searchGoogleAdsStreamResponse : streamResponse) {
            for (GoogleAdsRow googleAdsRow : searchGoogleAdsStreamResponse.getResultsList()) {
                switch (assetType) {
                    case SITELINK:
                        SitelinkDetails sitelinkDetails =
                                googleAdsRowAdapter.getSitelinkDetails(googleAdsRow);
                        assetDetailsList.add(sitelinkDetails);
                        break;
                    case CALL:
                        CallExtensionDetails callExtensionDetails =
                                googleAdsRowAdapter.getCallExtensionDetails(googleAdsRow);
                        assetDetailsList.add(callExtensionDetails);
                        break;
                    default:
                        break;
                }
            }
        }
        return assetDetailsList;
    }


    /**
     * Convert an incoming ServerStream response into ad details
     *
     * @param streamResponse a stream response from Google's server
     * @return a list of {@link AdDetails}
     */
    public static List<AdDetails> convertStreamToAdDetails(
            ServerStream<SearchGoogleAdsStreamResponse> streamResponse,
            String adType
    ) {
        GoogleAdsRowAdapter googleAdsRowAdapter = new GoogleAdsRowAdapterImpl();
        List<AdDetails> adDetailsList = new ArrayList<>();

        for (SearchGoogleAdsStreamResponse searchGoogleAdsStreamResponse : streamResponse) {
            for (GoogleAdsRow googleAdsRow : searchGoogleAdsStreamResponse.getResultsList()) {
                switch (adType.toLowerCase()) {
                    case RESPONSIVE_AD_TYPE:
                        ResponsiveSearchAdDetails adDetails =
                                googleAdsRowAdapter.getResponsiveSearchAdDetails(googleAdsRow);
                        adDetailsList.add(adDetails);
                        break;
                    default:
                        break;
                }
            }
        }
        return adDetailsList;
    }

    /**
     * Convert an incoming ServerStream response into metric details
     *
     * @param streamResponse a stream response from Google's server
     * @return a list of {@link }
     */
/*    public static List<BaseMetrics> convertStreamToMetrics(
            ServerStream<SearchGoogleAdsStreamResponse> streamResponse
    ) {
        GoogleAdsRowAdapter googleAdsRowAdapter = new GoogleAdsRowAdapterImpl();
        List<BaseMetrics> baseMetricsList = new ArrayList<>();

        for (SearchGoogleAdsStreamResponse searchGoogleAdsStreamResponse : streamResponse) {
            for (GoogleAdsRow googleAdsRow : searchGoogleAdsStreamResponse.getResultsList()) {
                BaseMetrics baseMetrics =
                        googleAdsRowAdapter.getMetrics(googleAdsRow);
                baseMetricsList.add(baseMetrics);
            }
        }
        return baseMetricsList;
    }*/
}
