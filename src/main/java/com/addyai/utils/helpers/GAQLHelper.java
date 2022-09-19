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
import com.addyai.models.BudgetDetails;
import com.addyai.models.CampaignDetails;
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
                " campaign.network_settings.target_partner_search_network," +
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
}
