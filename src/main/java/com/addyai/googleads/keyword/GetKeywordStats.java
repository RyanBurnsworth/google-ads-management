package com.addyai.googleads.keyword;

import com.addyai.models.KeywordStats;
import com.google.ads.googleads.lib.GoogleAdsClient;
import com.google.ads.googleads.v10.common.Metrics;
import com.google.ads.googleads.v10.resources.AdGroup;
import com.google.ads.googleads.v10.resources.AdGroupCriterion;
import com.google.ads.googleads.v10.resources.Campaign;
import com.google.ads.googleads.v10.services.GoogleAdsRow;
import com.google.ads.googleads.v10.services.GoogleAdsServiceClient;
import com.google.ads.googleads.v10.services.SearchGoogleAdsStreamRequest;
import com.google.ads.googleads.v10.services.SearchGoogleAdsStreamResponse;
import com.google.api.gax.rpc.ServerStream;

/**
 * Get detailed information on keywords for a given date range
 *
 */
public class GetKeywordStats {

    private final GoogleAdsClient googleAdsClient;

    public GetKeywordStats(GoogleAdsClient googleAdsClient) {
        this.googleAdsClient = googleAdsClient;
    }

    /**
     * Retrieves keyword statistics by pre-defined date range code:
     * # https://developers.google.com/google-ads/api/docs/query/date-ranges#predefined_date_range
     *
     * @param customerId    the client customer ID.
     * @param dateRangeCode the code for the selected date range
     */

    public KeywordStats getKeywordStatsByCode(long customerId, String dateRangeCode) {
        KeywordStats keywordStatsModel = new KeywordStats();

        try (GoogleAdsServiceClient googleAdsServiceClient =
                     this.googleAdsClient
                             .getLatestVersion()
                             .createGoogleAdsServiceClient()) {
            String searchQuery =
                    "SELECT campaign.id, "
                            + "campaign.name, "
                            + "ad_group.id, "
                            + "ad_group.name, "
                            + "ad_group_criterion.criterion_id, "
                            + "ad_group_criterion.keyword.text, "
                            + "ad_group_criterion.keyword.match_type, "
                            + "metrics.impressions, "
                            + "metrics.clicks, "
                            + "metrics.average_cpc, "
                            + "metrics.cost_micros, "
                            + "metrics.interaction_rate, "
                            + "metrics.conversions "
                            + "FROM keyword_view "
                            + "WHERE segments.date DURING " + dateRangeCode + " " // EX: LAST_7_DAYS
                            + "AND campaign.advertising_channel_type = 'SEARCH' "
                            + "AND ad_group.status = 'ENABLED' "
                            + "AND ad_group_criterion.status IN ('ENABLED', 'PAUSED') "
                            // Limits to the 50 keywords with the most impressions in the date range.
                            + "ORDER BY metrics.impressions DESC ";
            // Constructs the SearchGoogleAdsStreamRequest.
            SearchGoogleAdsStreamRequest request =
                    SearchGoogleAdsStreamRequest.newBuilder()
                            .setCustomerId(Long.toString(customerId))
                            .setQuery(searchQuery)
                            .build();

            // Creates and issues a search Google Ads stream request that will retrieve all of the
            // requested field values for the keyword.
            ServerStream<SearchGoogleAdsStreamResponse> stream =
                    googleAdsServiceClient.searchStreamCallable().call(request);

            // Iterates through the results in the stream response and prints all of the requested
            // field values for the keyword in each row.
            for (SearchGoogleAdsStreamResponse response : stream) {
                for (GoogleAdsRow googleAdsRow : response.getResultsList()) {
                    Campaign campaign = googleAdsRow.getCampaign();
                    AdGroup adGroup = googleAdsRow.getAdGroup();
                    AdGroupCriterion adGroupCriterion = googleAdsRow.getAdGroupCriterion();
                    Metrics metrics = googleAdsRow.getMetrics();

                    keywordStatsModel.setKeywordText(adGroupCriterion.getKeyword().getText());
                    keywordStatsModel.setAdgroupName(adGroup.getName());
                    keywordStatsModel.setCampaignName(campaign.getName());
                    keywordStatsModel.setMatchType(adGroupCriterion.getKeyword().getMatchType().toString());
                    keywordStatsModel.setImpressions(metrics.getImpressions());
                    keywordStatsModel.setClicks(metrics.getClicks());
                    keywordStatsModel.setCost(metrics.getCostMicros());
                    keywordStatsModel.setConversions(metrics.getConversions());
                    keywordStatsModel.setAvg_cpc(metrics.getAverageCpc());
                    keywordStatsModel.setInteraction_rate(metrics.getInteractionRate());
                }
            }
            return keywordStatsModel;
        }
    }

    /**
     * Retrieves keyword stats by date range
     * # https://developers.google.com/google-ads/api/docs/query/date-ranges#custom_date_range
     *
     * @param customerId the client customer ID.
     * @param startDate  the date to start accumulating stats Ex: 2022-05-19
     * @param stopDate   the date to stop accumulating stats Ex: 2022-05-19
     */
    public KeywordStats getKeywordStatsByDateRange(
            long customerId,
            String startDate,
            String stopDate) {
        KeywordStats keywordStatsModel = new KeywordStats();

        try (GoogleAdsServiceClient googleAdsServiceClient =
                     this.googleAdsClient
                             .getLatestVersion()
                             .createGoogleAdsServiceClient()) {
            String searchQuery =
                    "SELECT campaign.id, "
                            + "campaign.name, "
                            + "ad_group.id, "
                            + "ad_group.name, "
                            + "ad_group_criterion.criterion_id, "
                            + "ad_group_criterion.keyword.text, "
                            + "ad_group_criterion.keyword.match_type, "
                            + "metrics.impressions, "
                            + "metrics.clicks, "
                            + "metrics.average_cpc, "
                            + "metrics.cost_micros, "
                            + "metrics.interaction_rate, "
                            + "metrics.conversions "
                            + "FROM keyword_view "
                            + "WHERE segments.date BETWEEN '" + startDate + "' AND '" + stopDate + "' " // EX: LAST_7_DAYS
                            + "AND campaign.advertising_channel_type = 'SEARCH' "
                            + "AND ad_group.status = 'ENABLED' "
                            + "AND ad_group_criterion.status IN ('ENABLED', 'PAUSED') "
                            // Limits to the 50 keywords with the most impressions in the date range.
                            + "ORDER BY metrics.impressions DESC ";
            // Constructs the SearchGoogleAdsStreamRequest.
            SearchGoogleAdsStreamRequest request =
                    SearchGoogleAdsStreamRequest.newBuilder()
                            .setCustomerId(Long.toString(customerId))
                            .setQuery(searchQuery)
                            .build();

            // Creates and issues a search Google Ads stream request that will retrieve all of the
            // requested field values for the keyword.
            ServerStream<SearchGoogleAdsStreamResponse> stream =
                    googleAdsServiceClient.searchStreamCallable().call(request);

            // Iterates through the results in the stream response and prints all of the requested
            // field values for the keyword in each row.
            for (SearchGoogleAdsStreamResponse response : stream) {
                for (GoogleAdsRow googleAdsRow : response.getResultsList()) {
                    Campaign campaign = googleAdsRow.getCampaign();
                    AdGroup adGroup = googleAdsRow.getAdGroup();
                    AdGroupCriterion adGroupCriterion = googleAdsRow.getAdGroupCriterion();
                    Metrics metrics = googleAdsRow.getMetrics();

                    keywordStatsModel.setKeywordText(adGroupCriterion.getKeyword().getText());
                    keywordStatsModel.setAdgroupName(adGroup.getName());
                    keywordStatsModel.setCampaignName(campaign.getName());
                    keywordStatsModel.setMatchType(adGroupCriterion.getKeyword().getMatchType().toString());
                    keywordStatsModel.setImpressions(metrics.getImpressions());
                    keywordStatsModel.setClicks(metrics.getClicks());
                    keywordStatsModel.setCost(metrics.getCostMicros());
                    keywordStatsModel.setConversions(metrics.getConversions());
                    keywordStatsModel.setAvg_cpc(metrics.getAverageCpc());
                    keywordStatsModel.setInteraction_rate(metrics.getInteractionRate());
                }
            }
            return keywordStatsModel;
        }
    }

    /**
     * Retrieves keyword statistics by pre-defined date range code:
     * # https://developers.google.com/google-ads/api/docs/query/date-ranges#predefined_date_range
     *
     * @param campaignName  the name of the campaign you are searching upon
     * @param customerId    the client customer ID.
     * @param dateRangeCode the code for the selected date range
     */
    public KeywordStats getKeywordStatsForCampaignByCode(
            String campaignName,
            long customerId,
            String dateRangeCode) {
        KeywordStats keywordStatsModel = new KeywordStats();

        try (GoogleAdsServiceClient googleAdsServiceClient =
                     this.googleAdsClient
                             .getLatestVersion()
                             .createGoogleAdsServiceClient()) {
            String searchQuery =
                    "SELECT campaign.id, "
                            + "campaign.name, "
                            + "ad_group.id, "
                            + "ad_group.name, "
                            + "ad_group_criterion.criterion_id, "
                            + "ad_group_criterion.keyword.text, "
                            + "ad_group_criterion.keyword.match_type, "
                            + "metrics.impressions, "
                            + "metrics.clicks, "
                            + "metrics.average_cpc, "
                            + "metrics.cost_micros, "
                            + "metrics.interaction_rate, "
                            + "metrics.conversions "
                            + "FROM keyword_view "
                            + "WHERE segments.date DURING " + dateRangeCode + " " // EX: LAST_7_DAYS
                            + "AND campaign.advertising_channel_type = 'SEARCH' "
                            + "AND campaign.name == '" + campaignName + "' "
                            + "AND ad_group.status = 'ENABLED' "
                            + "AND ad_group_criterion.status IN ('ENABLED', 'PAUSED') "
                            // Limits to the 50 keywords with the most impressions in the date range.
                            + "ORDER BY metrics.impressions DESC ";
            // Constructs the SearchGoogleAdsStreamRequest.
            SearchGoogleAdsStreamRequest request =
                    SearchGoogleAdsStreamRequest.newBuilder()
                            .setCustomerId(Long.toString(customerId))
                            .setQuery(searchQuery)
                            .build();

            // Creates and issues a search Google Ads stream request that will retrieve all of the
            // requested field values for the keyword.
            ServerStream<SearchGoogleAdsStreamResponse> stream =
                    googleAdsServiceClient.searchStreamCallable().call(request);

            // Iterates through the results in the stream response and prints all of the requested
            // field values for the keyword in each row.
            for (SearchGoogleAdsStreamResponse response : stream) {
                for (GoogleAdsRow googleAdsRow : response.getResultsList()) {
                    Campaign campaign = googleAdsRow.getCampaign();
                    AdGroup adGroup = googleAdsRow.getAdGroup();
                    AdGroupCriterion adGroupCriterion = googleAdsRow.getAdGroupCriterion();
                    Metrics metrics = googleAdsRow.getMetrics();

                    keywordStatsModel.setKeywordText(adGroupCriterion.getKeyword().getText());
                    keywordStatsModel.setAdgroupName(adGroup.getName());
                    keywordStatsModel.setCampaignName(campaign.getName());
                    keywordStatsModel.setMatchType(adGroupCriterion.getKeyword().getMatchType().toString());
                    keywordStatsModel.setImpressions(metrics.getImpressions());
                    keywordStatsModel.setClicks(metrics.getClicks());
                    keywordStatsModel.setCost(metrics.getCostMicros());
                    keywordStatsModel.setConversions(metrics.getConversions());
                    keywordStatsModel.setAvg_cpc(metrics.getAverageCpc());
                    keywordStatsModel.setInteraction_rate(metrics.getInteractionRate());
                }
            }
            return keywordStatsModel;
        }
    }

    /**
     * Retrieves keyword stats by date range
     * # https://developers.google.com/google-ads/api/docs/query/date-ranges#custom_date_range
     *
     * @param campaignName the name of the campaign you are searching upon
     * @param customerId   the client customer ID.
     * @param startDate    the date to start accumulating stats Ex: 2022-05-19
     * @param stopDate     the date to stop accumulating stats Ex: 2022-05-19
     */
    public KeywordStats getKeywordStatsForCampaignByDateRange(
            String campaignName,
            long customerId,
            String startDate,
            String stopDate) {
        KeywordStats keywordStatsModel = new KeywordStats();

        try (GoogleAdsServiceClient googleAdsServiceClient =
                     this.googleAdsClient
                             .getLatestVersion()
                             .createGoogleAdsServiceClient()) {
            String searchQuery =
                    "SELECT campaign.id, "
                            + "campaign.name, "
                            + "ad_group.id, "
                            + "ad_group.name, "
                            + "ad_group_criterion.criterion_id, "
                            + "ad_group_criterion.keyword.text, "
                            + "ad_group_criterion.keyword.match_type, "
                            + "metrics.impressions, "
                            + "metrics.clicks, "
                            + "metrics.average_cpc, "
                            + "metrics.cost_micros, "
                            + "metrics.interaction_rate, "
                            + "metrics.conversions "
                            + "FROM keyword_view "
                            + "WHERE segments.date BETWEEN '" + startDate + "' AND '" + stopDate + "' " // EX: LAST_7_DAYS
                            + "AND campaign.advertising_channel_type = 'SEARCH' "
                            + "AND campaign.name == '" + campaignName + "' "
                            + "AND ad_group.status = 'ENABLED' "
                            + "AND ad_group_criterion.status IN ('ENABLED', 'PAUSED') "
                            // Limits to the 50 keywords with the most impressions in the date range.
                            + "ORDER BY metrics.impressions DESC ";
            // Constructs the SearchGoogleAdsStreamRequest.
            SearchGoogleAdsStreamRequest request =
                    SearchGoogleAdsStreamRequest.newBuilder()
                            .setCustomerId(Long.toString(customerId))
                            .setQuery(searchQuery)
                            .build();

            // Creates and issues a search Google Ads stream request that will retrieve all of the
            // requested field values for the keyword.
            ServerStream<SearchGoogleAdsStreamResponse> stream =
                    googleAdsServiceClient.searchStreamCallable().call(request);

            // Iterates through the results in the stream response and prints all of the requested
            // field values for the keyword in each row.
            for (SearchGoogleAdsStreamResponse response : stream) {
                for (GoogleAdsRow googleAdsRow : response.getResultsList()) {
                    Campaign campaign = googleAdsRow.getCampaign();
                    AdGroup adGroup = googleAdsRow.getAdGroup();
                    AdGroupCriterion adGroupCriterion = googleAdsRow.getAdGroupCriterion();
                    Metrics metrics = googleAdsRow.getMetrics();

                    keywordStatsModel.setKeywordText(adGroupCriterion.getKeyword().getText());
                    keywordStatsModel.setAdgroupName(adGroup.getName());
                    keywordStatsModel.setCampaignName(campaign.getName());
                    keywordStatsModel.setMatchType(adGroupCriterion.getKeyword().getMatchType().toString());
                    keywordStatsModel.setImpressions(metrics.getImpressions());
                    keywordStatsModel.setClicks(metrics.getClicks());
                    keywordStatsModel.setCost(metrics.getCostMicros());
                    keywordStatsModel.setConversions(metrics.getConversions());
                    keywordStatsModel.setAvg_cpc(metrics.getAverageCpc());
                    keywordStatsModel.setInteraction_rate(metrics.getInteractionRate());
                }
            }
            return keywordStatsModel;
        }
    }
}
