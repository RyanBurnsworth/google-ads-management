package com.addyai.ad;

import com.addyai.models.ExpandedTextAdModel;
import com.addyai.models.ResponsiveSearchAdModel;
import com.google.ads.googleads.lib.GoogleAdsClient;
import com.google.ads.googleads.lib.utils.FieldMasks;
import com.google.ads.googleads.v10.common.ExpandedTextAdInfo;
import com.google.ads.googleads.v10.common.ResponsiveSearchAdInfo;
import com.google.ads.googleads.v10.enums.AdGroupAdStatusEnum;
import com.google.ads.googleads.v10.resources.Ad;
import com.google.ads.googleads.v10.resources.AdGroupAd;
import com.google.ads.googleads.v10.services.*;
import com.google.ads.googleads.v10.utils.ResourceNames;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages Ad Operations within a client's Google Ads campaigns
 */
public class AdService {
    private final GoogleAdsClient googleAdsClient;

    public AdService(GoogleAdsClient googleAdsClient) {
        this.googleAdsClient = googleAdsClient;
    }

    /**
     * Create Responsive Search Ads for a given AdGroup
     *
     * @param customerId               the client customer ID.
     * @param adGroupId                the ad group ID.
     * @param responsiveSearchAdModels a list of ResponsiveSearchAdModels to create ads with
     */
    public void createResponsiveSearchAds(long customerId,
                                          long adGroupId,
                                          List<ResponsiveSearchAdModel> responsiveSearchAdModels) {
        String adGroupResourceName = ResourceNames.adGroup(customerId, adGroupId);
        List<AdGroupAdOperation> adOperations = new ArrayList<>();

        for (ResponsiveSearchAdModel model : responsiveSearchAdModels) {
            // Creates the responsive search ad info.
            ResponsiveSearchAdInfo responsiveSearchAdInfo =
                    ResponsiveSearchAdInfo.newBuilder()
                            .addAllHeadlines(model.getHeadlinesList())
                            .addAllDescriptions(model.getDescriptionList())
                            .setPath1(model.getPath1())
                            .setPath2(model.getPath2())
                            .build();

            // Wraps the info in an Ad object.
            Ad ad = Ad.newBuilder()
                    .setResponsiveSearchAd(responsiveSearchAdInfo)
                    .addFinalUrls(model.getFinalUrl())
                    .build();

            // Builds the final ad group ad representation.
            AdGroupAd adGroupAd =
                    AdGroupAd.newBuilder()
                            .setAdGroup(adGroupResourceName)
                            .setStatus(AdGroupAdStatusEnum.AdGroupAdStatus.ENABLED)
                            .setAd(ad)
                            .build();

            // Creates the operation.
            AdGroupAdOperation operation = AdGroupAdOperation.newBuilder().setCreate(adGroupAd).build();
            adOperations.add(operation);
        }

        // Creates the AdGroupAdServiceClient.
        try (AdGroupAdServiceClient adGroupAdServiceClient =
                     googleAdsClient.getLatestVersion().createAdGroupAdServiceClient()) {
            // Adds the AdGroup.
            MutateAdGroupAdsResponse response =
                    adGroupAdServiceClient.mutateAdGroupAds(
                            Long.toString(customerId), adOperations);
            for (MutateAdGroupAdResult result : response.getResultsList()) {
                System.out.printf("Responsive search ad created with resource name: %s.%n", result.getResourceName());
            }
        }
    }

    /**
     * Retrieves a list of ResponsiveSearchAdModels from the account
     *
     * @param customerId the client customer ID.
     * @param pageSize   the number of results per page
     * @return a list of ResponsiveSearchAdModel from the account
     */
    public List<ResponsiveSearchAdModel> getResponsiveSearchAds(long customerId, int pageSize) {
        List<ResponsiveSearchAdModel> responsiveSearchAdModelList = new ArrayList<>();

        try (GoogleAdsServiceClient googleAdsServiceClient =
                     googleAdsClient.getLatestVersion().createGoogleAdsServiceClient()) {
            // Constructs the search query.
            String searchQuery =
                    "SELECT ad_group.id, ad_group_ad.ad.id, "
                            + "ad_group_ad.ad.responsive_search_ad.headlines, "
                            + "ad_group_ad.ad.responsive_search_ad.descriptions, "
                            + "ad_group_ad.status "
                            + "FROM ad_group_ad "
                            + "WHERE ad_group_ad.ad.type = RESPONSIVE_SEARCH_AD "
                            + "AND ad_group_ad.status != 'REMOVED'";

            // Creates a request that will retrieve all ad group ads using pages of the specified page
            // size.
            SearchGoogleAdsRequest request =
                    SearchGoogleAdsRequest.newBuilder()
                            .setCustomerId(Long.toString(customerId))
                            .setPageSize(pageSize)
                            .setQuery(searchQuery)
                            .build();
            // Issues the search request.
            GoogleAdsServiceClient.SearchPagedResponse searchPagedResponse = googleAdsServiceClient.search(request);

            // Checks if the response contains any results and returns if the results set is empty.
            if (searchPagedResponse.getPage().getResponse().getResultsCount() == 0) {
                System.out.println("No responsive search ads were found.");
                return responsiveSearchAdModelList;
            }

            // Iterates over all rows in all pages and prints the requested field values for the ad
            // group ads in each row.
            for (GoogleAdsRow googleAdsRow : searchPagedResponse.iterateAll()) {
                AdGroupAd adGroupAd = googleAdsRow.getAdGroupAd();
                Ad ad = adGroupAd.getAd();
                System.out.printf(
                        "Responsive search ad with resource name '%s', status '%s' was found.%n",
                        ad.getResourceName(), adGroupAd.getStatus().getDescriptorForType().getName());
                // Prints the ad text asset detail.
                ResponsiveSearchAdInfo responsiveSearchAdInfo = ad.getResponsiveSearchAd();

                ResponsiveSearchAdModel searchAdModel = new ResponsiveSearchAdModel();
                searchAdModel.setHeadlinesList(responsiveSearchAdInfo.getHeadlinesList());
                searchAdModel.setDescriptionList(responsiveSearchAdInfo.getDescriptionsList());
                //searchAdModel.setFinalUrl(googleAdsRow.getAdGroupAd().getAd().getFinalUrls(0)); // TODO: research final urls
                searchAdModel.setPath1(responsiveSearchAdInfo.getPath1());
                searchAdModel.setPath2(responsiveSearchAdInfo.getPath2());
                searchAdModel.setId(googleAdsRow.getAdGroupAd().getAd().getId());
                searchAdModel.setAdGroupId(googleAdsRow.getAdGroup().getId());
                responsiveSearchAdModelList.add(searchAdModel);
            }
        }
        return responsiveSearchAdModelList;
    }

    /**
     * Update ResponsiveSearchAds
     *
     * @param customerId               the customer ID to update.
     * @param adId                     the ad ID to update.
     * @param responsiveSearchAdModels a list of updates to a list of ResponsiveSearchAds
     */
    private void updateResponsiveSearchAds(long customerId, long adId, List<ResponsiveSearchAdModel> responsiveSearchAdModels) {
        // Creates an AdOperation to update an ad.
        AdOperation.Builder adOperation = AdOperation.newBuilder();
        List<AdOperation> operationsList = new ArrayList<>();

        for (ResponsiveSearchAdModel model : responsiveSearchAdModels) {
            // Creates an Ad in the update field of the operation.
            Ad.Builder adBuilder =
                    adOperation
                            .getUpdateBuilder()
                            .setResourceName(ResourceNames.ad(customerId, adId))
                            .addFinalUrls(model.getFinalUrl());

            // Sets the expanded text ad properties to update on the ad.
            adBuilder.getResponsiveSearchAdBuilder()
                    .addAllHeadlines(model.getHeadlinesList())
                    .addAllDescriptions(model.getDescriptionList())
                    .build();

            // Sets the update mask (the fields which will be modified) to be all the fields we set above.
            adOperation.setUpdateMask(FieldMasks.allSetFieldsOf(adBuilder.build()));
            operationsList.add(adOperation.build());
        }

        // Creates a service client to connect to the API.
        try (AdServiceClient adServiceClient =
                     googleAdsClient.getLatestVersion().createAdServiceClient()) {
            // Issues the mutate request.
            MutateAdsResponse response =
                    adServiceClient.mutateAds(
                            String.valueOf(customerId), operationsList);

            // Displays the result.
            for (MutateAdResult result : response.getResultsList()) {
                System.out.printf("Ad with resource name '%s' was updated.%n", result.getResourceName());
            }
        }
    }

    /**
     * Retrieve a list of ExpandedTextAds from the account or adgroup
     *
     * @param customerId the client customer ID.
     * @param adGroupId  the adgroup id from to get the ExpandedTextAds from
     * @param pageSize   the number of ExpandedTextAds to return within a page
     * @return list of ExpandedTextAdModel containing all expanded ads in account
     */
    public List<ExpandedTextAdModel> getExpandedTextAds(long customerId, long adGroupId, int pageSize) {
        List<ExpandedTextAdModel> expandedTextAdModelList = new ArrayList<>();

        try (GoogleAdsServiceClient googleAdsServiceClient =
                     googleAdsClient.getLatestVersion().createGoogleAdsServiceClient()) {
            String searchQuery =
                    "SELECT ad_group.id, "
                            + "ad_group_ad.ad.id, "
                            + "ad_group_ad.ad.expanded_text_ad.headline_part1, "
                            + "ad_group_ad.ad.expanded_text_ad.headline_part2, "
                            + "ad_group_ad.ad.expanded_text_ad.headline_part3, "
                            + "ad_group_ad.ad.expanded_text_ad.description, "
                            + "ad_group_ad.ad.expanded_text_ad.description2, "
                            + "ad_group_ad.ad.expanded_text_ad.path1, "
                            + "ad_group_ad.ad.expanded_text_ad.path2, "
                            + "ad_group_ad.status "
                            + "FROM ad_group_ad "
                            + "WHERE ad_group_ad.ad.type = EXPANDED_TEXT_AD ";
            if (adGroupId != 0L) {
                searchQuery += String.format(" AND ad_group.id = %d", adGroupId);
            }
            // Creates a request that will retrieve all ads using pages of the specified page size.
            SearchGoogleAdsRequest request =
                    SearchGoogleAdsRequest.newBuilder()
                            .setCustomerId(Long.toString(customerId))
                            .setPageSize(pageSize)
                            .setQuery(searchQuery)
                            .build();
            // Issues the search request.
            GoogleAdsServiceClient.SearchPagedResponse searchPagedResponse = googleAdsServiceClient.search(request);
            // Iterates over all rows in all pages and prints the requested field values for the ad
            // in each row.
            for (GoogleAdsRow googleAdsRow : searchPagedResponse.iterateAll()) {
                Ad ad = googleAdsRow.getAdGroupAd().getAd();
                ExpandedTextAdInfo expandedTextAdInfo = ad.getExpandedTextAd();
                ExpandedTextAdModel expandedTextAdModel = new ExpandedTextAdModel();
                expandedTextAdModel.setId(googleAdsRow.getAdGroupAd().getAd().getId());
                expandedTextAdModel.setAdGroupId(googleAdsRow.getAdGroup().getId());
                expandedTextAdModel.setHeadlinePart1(expandedTextAdInfo.getHeadlinePart1());
                expandedTextAdModel.setHeadlinePart2(expandedTextAdInfo.getHeadlinePart2());
                expandedTextAdModel.setHeadlinePart3(expandedTextAdInfo.getHeadlinePart3());
                expandedTextAdModel.setDescription1(expandedTextAdInfo.getDescription());
                expandedTextAdModel.setDescription2(expandedTextAdInfo.getDescription2());
                expandedTextAdModel.setPath1(expandedTextAdInfo.getPath1());
                expandedTextAdModel.setPath2(expandedTextAdInfo.getPath2());
                expandedTextAdModel.setStatus(googleAdsRow.getAdGroupAd().getStatus());
                expandedTextAdModelList.add(expandedTextAdModel);

                System.out.printf(
                        "Expanded text ad with ID %d, status '%s', and headline '%s - %s' was found in ad "
                                + "group with ID %d.%n",
                        ad.getId(),
                        googleAdsRow.getAdGroupAd().getStatus(),
                        expandedTextAdInfo.getHeadlinePart1(),
                        expandedTextAdInfo.getHeadlinePart2(),
                        expandedTextAdInfo.getHeadlinePart3(),
                        expandedTextAdInfo.getDescription(),
                        expandedTextAdInfo.getDescription2(),
                        expandedTextAdInfo.getPath1(),
                        expandedTextAdInfo.getPath2(),
                        googleAdsRow.getAdGroupAd().getAd().getId(),
                        googleAdsRow.getAdGroup().getId());
            }
        }
        return expandedTextAdModelList;
    }
}
