package com.addyai.utils;

import com.addyai.models.CampaignDetails;
import com.google.ads.googleads.v11.services.GoogleAdsRow;
import com.google.ads.googleads.v11.services.SearchGoogleAdsStreamResponse;
import com.google.api.gax.rpc.ServerStream;

import java.util.ArrayList;
import java.util.List;

public class GAQLUtils {
    public static String getCampaignDetailsQuery() {
        return "SELECT campaign.id," +
                " campaign.name," +
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
                " campaign.network_settings.target_content_network," +
                " campaign.network_settings.target_google_search," +
                " campaign.network_settings.target_partner_search_network," +
                " campaign.network_settings.target_search_network" +
                " FROM campaign ORDER BY campaign.id";
    }

    public static List<CampaignDetails> convertStreamResponseToCampaignDetailsList(ServerStream<SearchGoogleAdsStreamResponse> streamResponse) {
        List<CampaignDetails> campaignDetailsList = new ArrayList<>();

        for (SearchGoogleAdsStreamResponse response : streamResponse) {
            for (GoogleAdsRow googleAdsRow : response.getResultsList()) {
                CampaignDetails details = new CampaignDetails();
                details.setCampaignId(googleAdsRow.getCampaign().getId());
                details.setCampaignName(googleAdsRow.getCampaign().getName());
                details.setStatus(googleAdsRow.getCampaign().getStatus().toString());
                details.setAdvertisingChannelType(googleAdsRow.getCampaign().getAdvertisingChannelType().toString());
                details.setBudget(googleAdsRow.getCampaign().getCampaignBudget());
                details.setBiddingStrategy(googleAdsRow.getCampaign().getBiddingStrategyType().toString());
                details.setPositiveGeoTargetType(googleAdsRow.getCampaign().getGeoTargetTypeSetting().getPositiveGeoTargetType().toString());
                details.setNegativeGeoTargetType(googleAdsRow.getCampaign().getGeoTargetTypeSetting().getNegativeGeoTargetType().toString());
                details.setEnhancedCpcEnabled(googleAdsRow.getCampaign().getManualCpc().getEnhancedCpcEnabled());
                details.setStartDate(googleAdsRow.getCampaign().getStartDate());
                details.setEndDate(googleAdsRow.getCampaign().getEndDate());
                details.setTargetingSearchNetwork(googleAdsRow.getCampaign().getNetworkSettings().getTargetSearchNetwork());
                details.setTargetingContentNetwork(googleAdsRow.getCampaign().getNetworkSettings().getTargetContentNetwork());
                details.setTargetingGoogleSearch(googleAdsRow.getCampaign().getNetworkSettings().getTargetGoogleSearch());
                details.setTargetingPartnerSearchNetwork(googleAdsRow.getCampaign().getNetworkSettings().getTargetPartnerSearchNetwork());

                campaignDetailsList.add(details);
            }
        }

        return campaignDetailsList;
    }
}
