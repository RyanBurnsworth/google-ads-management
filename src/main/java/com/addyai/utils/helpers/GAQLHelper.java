package com.addyai.utils.helpers;

import com.addyai.models.BudgetDetails;
import com.addyai.models.CampaignDetails;
import com.google.ads.googleads.v11.services.GoogleAdsRow;
import com.google.ads.googleads.v11.services.SearchGoogleAdsStreamResponse;
import com.google.api.gax.rpc.ServerStream;

import java.util.ArrayList;
import java.util.List;

import static com.addyai.utils.misc.Constants.MICRO_FACTOR;

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
                " FROM campaign ORDER BY campaign.id";
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
                " FROM campaign_budget";
    }

    public static List<CampaignDetails> convertStreamResponseToCampaignDetailsList(ServerStream<SearchGoogleAdsStreamResponse> streamResponse) {
        List<CampaignDetails> campaignDetailsList = new ArrayList<>();

        for (SearchGoogleAdsStreamResponse response : streamResponse) {
            for (GoogleAdsRow googleAdsRow : response.getResultsList()) {
                CampaignDetails details = new CampaignDetails();

                details.setCampaignId(googleAdsRow.getCampaign().getId());
                details.setCampaignName(googleAdsRow.getCampaign().getName());
                details.setCampaignResourceName(googleAdsRow.getCampaign().getResourceName());
                details.setStatus(googleAdsRow.getCampaign().getStatus().toString());
                details.setAdvertisingChannelType(googleAdsRow.getCampaign().getAdvertisingChannelType().toString());
                details.setPositiveGeoTargetType(googleAdsRow.getCampaign().getGeoTargetTypeSetting().getPositiveGeoTargetTypeValue());
                details.setNegativeGeoTargetType(googleAdsRow.getCampaign().getGeoTargetTypeSetting().getNegativeGeoTargetTypeValue());
                details.setEnhancedCpcEnabled(googleAdsRow.getCampaign().getManualCpc().getEnhancedCpcEnabled());
                details.setStartDate(googleAdsRow.getCampaign().getStartDate());
                details.setEndDate(googleAdsRow.getCampaign().getEndDate());
                details.setTargetingSearchNetwork(googleAdsRow.getCampaign().getNetworkSettings().getTargetSearchNetwork());
                details.setTargetingContentNetwork(googleAdsRow.getCampaign().getNetworkSettings().getTargetContentNetwork());
                details.setTargetingPartnerSearchNetwork(googleAdsRow.getCampaign().getNetworkSettings().getTargetPartnerSearchNetwork());
                details.setBudgetResourceName(googleAdsRow.getCampaign().getCampaignBudget());
                campaignDetailsList.add(details);
            }
        }

        return campaignDetailsList;
    }

    public static List<BudgetDetails> convertStreamResponseToBudgetDetails(ServerStream<SearchGoogleAdsStreamResponse> streamResponse) {
        List<BudgetDetails> budgetDetailsList = new ArrayList<>();

        for (SearchGoogleAdsStreamResponse response : streamResponse) {
            for (GoogleAdsRow googleAdsRow : response.getResultsList()) {
                BudgetDetails budgetDetails = new BudgetDetails();
                budgetDetails.setBudgetId(googleAdsRow.getCampaignBudget().getId());
                budgetDetails.setDailyBudgetAmount(Math.round((float) googleAdsRow.getCampaignBudget().getAmountMicros() / MICRO_FACTOR));
                budgetDetails.setName(googleAdsRow.getCampaignBudget().getName());
                budgetDetails.setResourceName(googleAdsRow.getCampaignBudget().getResourceName());
                budgetDetails.setDeliveryMethod(googleAdsRow.getCampaignBudget().getDeliveryMethodValue());
                budgetDetails.setShared(googleAdsRow.getCampaignBudget().getExplicitlyShared());
                budgetDetails.setStatus(googleAdsRow.getCampaignBudget().getStatusValue());

                budgetDetailsList.add(budgetDetails);
            }
        }
        return budgetDetailsList;
    }
}
