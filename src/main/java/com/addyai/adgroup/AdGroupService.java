package com.addyai.adgroup;

import com.addyai.models.AdGroupModel;
import com.google.ads.googleads.lib.GoogleAdsClient;
import com.google.ads.googleads.v10.resources.AdGroup;
import com.google.ads.googleads.v10.services.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Add an ad group to a campaign
 */
public class AdGroupService {
    private final GoogleAdsClient googleAdsClient;

    public AdGroupService(GoogleAdsClient googleAdsClient) {
        this.googleAdsClient = googleAdsClient;
    }

    /**
     * Create an adgroup within a campaign on a customer account
     *
     * @param model the adgroupmodel to pull setup data from
     */
    public void createAdgroup(AdGroupModel model) {
        AdGroup adGroup =
                AdGroup.newBuilder()
                        .setName(model.getAdgroupName())
                        .setStatus(model.getStatus())
                        .setCampaign(model.getCampaignName())
                        .setType(model.getType())
                        .setCpcBidMicros(model.getMaxCPC())
                        .build();

        List<AdGroupOperation> operations = new ArrayList<>();
        operations.add(AdGroupOperation.newBuilder().setCreate(adGroup).build());

        try (AdGroupServiceClient adGroupServiceClient =
                     googleAdsClient.getLatestVersion().createAdGroupServiceClient()) {
            MutateAdGroupsResponse response =
                    adGroupServiceClient.mutateAdGroups(Long.toString(model.getCustomerId()), operations);
            System.out.printf("Added %d ad groups:%n", response.getResultsCount());
            for (MutateAdGroupResult result : response.getResultsList()) {
                System.out.println(result.getResourceName());
            }
        }
    }

    /**
     * Return adgroups for a given campaign by a given page size
     *
     * @param customerId the client customer ID.
     * @param pageSize   the size of the page of results to return
     * @return a list of AdGroupModels containing all adgroups in account
     */
    public List<AdGroupModel> getAdGroups(long customerId, int pageSize) {
        List<AdGroupModel> adGroupModels = new ArrayList<>();
        try (GoogleAdsServiceClient googleAdsServiceClient =
                     googleAdsClient.getLatestVersion().createGoogleAdsServiceClient()) {
            String searchQuery = "SELECT campaign.id, ad_group.id, ad_group.name FROM ad_group WHERE campaign.status IN ('ENABLED', 'PAUSED')";

            // Creates a request that will retrieve all ad groups using pages of the specified page size.
            SearchGoogleAdsRequest request =
                    SearchGoogleAdsRequest.newBuilder()
                            .setCustomerId(Long.toString(customerId))
                            .setPageSize(pageSize)
                            .setQuery(searchQuery)
                            .build();

            // Issues the search request.
            GoogleAdsServiceClient.SearchPagedResponse searchPagedResponse = googleAdsServiceClient.search(request);
            // Iterates over all rows in all pages and prints the requested field values for the ad group
            // in each row.
            for (GoogleAdsRow googleAdsRow : searchPagedResponse.iterateAll()) {
                AdGroup adGroup = googleAdsRow.getAdGroup();

                AdGroupModel adGroupModel = new AdGroupModel();
                adGroupModel.setId(adGroup.getId());
                adGroupModel.setAdgroupName(adGroup.getName());
                adGroupModel.setMaxCPC(adGroup.getCpcBidMicros());
                adGroupModel.setStatus(adGroup.getStatus());
                adGroupModel.setType(adGroup.getType());
                adGroupModel.setCampaignId(googleAdsRow.getCampaign().getId());
                adGroupModel.setCampaignName(googleAdsRow.getCampaign().getName());

                adGroupModels.add(adGroupModel);
            }
            return adGroupModels;
        }
    }
}
