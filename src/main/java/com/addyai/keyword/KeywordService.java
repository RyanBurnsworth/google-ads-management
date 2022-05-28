package com.addyai.keyword;

import com.addyai.models.KeywordModel;
import com.google.ads.googleads.lib.GoogleAdsClient;
import com.google.ads.googleads.lib.utils.FieldMasks;
import com.google.ads.googleads.v10.common.KeywordInfo;
import com.google.ads.googleads.v10.enums.AdGroupCriterionStatusEnum;
import com.google.ads.googleads.v10.resources.AdGroup;
import com.google.ads.googleads.v10.resources.AdGroupCriterion;
import com.google.ads.googleads.v10.services.*;
import com.google.ads.googleads.v10.utils.ResourceNames;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

import static com.google.ads.googleads.v10.enums.AdGroupCriterionStatusEnum.AdGroupCriterionStatus.PAUSED;

public class KeywordService {
    private final GoogleAdsClient googleAdsClient;

    public KeywordService(GoogleAdsClient googleAdsClient) {
        this.googleAdsClient = googleAdsClient;
    }

    /**
     * Add keywords to a given adgroup for a client account.
     *
     * @param customerId       the client customer ID.
     * @param adGroupId        the ad group ID.
     * @param keywordModelList a list of keywordsModels to add to Adgroup
     */
    public void addKeywords(long customerId, long adGroupId, List<KeywordModel> keywordModelList) {
        List<AdGroupCriterionOperation> adGroupCriterionOperationList = new ArrayList<>();

        for (KeywordModel model : keywordModelList) {
            // Configures the keywordText text and match type settings.
            KeywordInfo keywordInfo =
                    KeywordInfo.newBuilder()
                            .setText(model.getText())
                            .setMatchType(model.getMatchType())
                            .build();

            String adGroupResourceName = ResourceNames.adGroup(customerId, adGroupId);

            // Constructs an ad group criterion using the keywordText configuration above.
            AdGroupCriterion criterion =
                    AdGroupCriterion.newBuilder()
                            .setAdGroup(adGroupResourceName)
                            .setStatus(AdGroupCriterionStatusEnum.AdGroupCriterionStatus.ENABLED)
                            .setKeyword(keywordInfo)
                            .setCpcBidMicros(Long.parseLong(model.getCpcBid()))
                            .build();

            AdGroupCriterionOperation op =
                    AdGroupCriterionOperation.newBuilder().setCreate(criterion).build();
            adGroupCriterionOperationList.add(op);
        }

        try (AdGroupCriterionServiceClient agcServiceClient =
                     googleAdsClient.getLatestVersion().createAdGroupCriterionServiceClient()) {
            MutateAdGroupCriteriaResponse response =
                    agcServiceClient.mutateAdGroupCriteria(Long.toString(customerId), adGroupCriterionOperationList);
            System.out.printf("Added %d ad group criteria:%n", response.getResultsCount());
            for (MutateAdGroupCriterionResult result : response.getResultsList()) {
                System.out.println(result.getResourceName());
            }
        }
    }

    /**
     * Get keywords from a given adgroup
     *
     * @param customerId the client customer ID.
     * @param pageSize   the number of keywords per page
     * @return a list of KeywordModels containing all keyords in account
     */
    public List<KeywordModel> getKeywords(
            long customerId,
            int pageSize) {
        List<KeywordModel> keywordModelList = new ArrayList<>();

        try (GoogleAdsServiceClient googleAdsServiceClient =
                     googleAdsClient.getLatestVersion().createGoogleAdsServiceClient()) {
            String searchQuery =
                    "SELECT ad_group.id, "
                            + "ad_group_criterion.type, "
                            + "ad_group_criterion.criterion_id, "
                            + "ad_group_criterion.keyword.text, "
                            + "ad_group_criterion.keyword.match_type "
                            // + "ad_group_criterion.keyword.cpc_bid_micros, " //TODO: This is failing
                            //  + "ad_group_criterion.keyword.bid_modifier "
                            + "FROM ad_group_criterion "
                            + "WHERE ad_group_criterion.type = KEYWORD ";
            searchQuery += " PARAMETERS omit_unselected_resource_names=true";

            // Creates a request that will retrieve all keywords using pages of the specified page size.
            SearchGoogleAdsRequest request =
                    SearchGoogleAdsRequest.newBuilder()
                            .setCustomerId(Long.toString(customerId))
                            .setPageSize(pageSize)
                            .setQuery(searchQuery)
                            .build();

            // Issues the search request.
            GoogleAdsServiceClient.SearchPagedResponse searchPagedResponse = googleAdsServiceClient.search(request);
            // Iterates over all rows in all pages and prints the requested field values for the keyword
            // in each row.
            for (GoogleAdsRow googleAdsRow : searchPagedResponse.iterateAll()) {
                AdGroup adGroup = googleAdsRow.getAdGroup();
                AdGroupCriterion adGroupCriterion = googleAdsRow.getAdGroupCriterion();
                KeywordInfo keywordInfo = adGroupCriterion.getKeyword();

                KeywordModel keywordModel = new KeywordModel();
                keywordModel.setId(adGroupCriterion.getCriterionId());
                keywordModel.setAdGroupId(adGroup.getId());
                keywordModel.setText(keywordInfo.getText());
                keywordModel.setMatchType(keywordInfo.getMatchType());

                keywordModelList.add(keywordModel);
            }
            return keywordModelList;
        }
    }

    /**
     * Pause a Campaign in the specified client account
     *
     * @param customerId the id of the client account
     * @param adGroupId  the id of the adgroup the keyword is within
     * @param keywordId  the id of the keyword to be paused
     */
    public void pauseKeyword(long customerId, long adGroupId, long keywordId) {
        String keywordResource = ResourceNames.adGroupCriterion(customerId, adGroupId, keywordId);
        AdGroupCriterion adGroupCriterion = AdGroupCriterion.newBuilder()
                .setResourceName(keywordResource)
                .setStatus(PAUSED)
                .build();

        AdGroupCriterionOperation op = AdGroupCriterionOperation.newBuilder()
                .setUpdate(adGroupCriterion)
                .setUpdateMask(FieldMasks.allSetFieldsOf(adGroupCriterion))
                .build();

        try (AdGroupCriterionServiceClient adGroupCriterionServiceClient =
                     googleAdsClient.getLatestVersion().createAdGroupCriterionServiceClient()) {
            MutateAdGroupCriteriaResponse response =
                    adGroupCriterionServiceClient.mutateAdGroupCriteria(Long.toString(customerId), ImmutableList.of(op));
            for (MutateAdGroupCriterionResult result : response.getResultsList()) {
                System.out.printf("Keyword with resource name '%s' is paused. %n", result.getResourceName());
            }
        }
    }
}
