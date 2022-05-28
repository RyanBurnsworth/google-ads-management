package com.addyai.adgroup;

import com.addyai.models.AdGroupModel;
import com.google.ads.googleads.lib.GoogleAdsClient;
import com.google.ads.googleads.lib.utils.FieldMasks;
import com.google.ads.googleads.v10.enums.AdGroupStatusEnum;
import com.google.ads.googleads.v10.resources.AdGroup;
import com.google.ads.googleads.v10.services.*;
import com.google.ads.googleads.v10.utils.ResourceNames;

import java.util.ArrayList;
import java.util.List;

/**
 * Manage AdGroup operations on a campaign for a client's account
 */
public class AdGroupService {
    private final GoogleAdsClient googleAdsClient;

    /**
     * Find a AdGroup given it's adGroupId
     *
     * @param customerId the id of the client's account
     * @param adGroupId  the id of the adGroup to find
     * @return the AdGroupModel if it's existing or null if not
     */
    public AdGroupModel findAdGroup(long customerId, long adGroupId) {
        List<AdGroupModel> adGroupModelList;
        adGroupModelList = getAdGroups(customerId, 100);
        for (AdGroupModel model : adGroupModelList) {
            if (model.getId() == adGroupId) {
                return model;
            }
        }
        return null;
    }

    public AdGroupService(GoogleAdsClient googleAdsClient) {
        this.googleAdsClient = googleAdsClient;
    }

    /**
     * Create aa batch of AdGroups within a campaign on a client's account
     *
     * @param customerId customerId of the client's account
     * @param modelList adgroupmodel to pull setup data from
     */
    public void createAdgroup(long customerId, List<AdGroupModel> modelList) {
        List<AdGroupOperation> operations = new ArrayList<>();

        for (AdGroupModel model : modelList) {
            AdGroup adGroup =
                    AdGroup.newBuilder()
                            .setName(model.getAdgroupName())
                            .setStatus(model.getStatus())
                            .setCampaign(model.getCampaignName())
                            .setCpcBidMicros(model.getMaxCPC())
                            .build();

            operations.add(AdGroupOperation.newBuilder().setCreate(adGroup).build());
        }
        try (AdGroupServiceClient adGroupServiceClient =
                     googleAdsClient.getLatestVersion().createAdGroupServiceClient()) {
            MutateAdGroupsResponse response =
                    adGroupServiceClient.mutateAdGroups(Long.toString(customerId), operations);

            for (MutateAdGroupResult result : response.getResultsList()) {
                System.out.println(result.getResourceName());
            }
        }
    }

    /**
     * Return AdGroups for a given campaign by a given page size
     *
     * @param customerId the client customer ID.
     * @param pageSize   the size of the page of results to return
     * @return a list of AdGroupModels containing all adgroups in account
     */
    public List<AdGroupModel> getAdGroups(long customerId, int pageSize) {
        List<AdGroupModel> adGroupModels = new ArrayList<>();
        try (GoogleAdsServiceClient googleAdsServiceClient =
                     googleAdsClient.getLatestVersion().createGoogleAdsServiceClient()) {
            String searchQuery = "SELECT campaign.id, " +
                    "ad_group.id, " +
                    "ad_group.name, " +
                    "ad_group.cpc_bid_micros, " +
                    "ad_group.status " +
                    "FROM ad_group " +
                    "WHERE campaign.status IN ('ENABLED', 'PAUSED') " +
                    "AND ad_group.status IN ('ENABLED', 'PAUSED')";

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
                adGroupModel.setCampaignId(googleAdsRow.getCampaign().getId());
                adGroupModel.setCampaignName(googleAdsRow.getCampaign().getName());

                adGroupModels.add(adGroupModel);
            }
            return adGroupModels;
        }
    }

    /**
     * Pause AdGroups in the specified client account in batches
     *
     * @param customerId    the id of the client account
     * @param adGroupIdList a list of AdGroup Ids to be paused
     */
    public void pauseAdGroup(long customerId, List<Long> adGroupIdList) {
        List<AdGroupOperation> operations = new ArrayList<>();

        for (Long adGroupId : adGroupIdList) {
            String adgroupResourceName = ResourceNames.adGroup(customerId, adGroupId);
            AdGroup adGroup = AdGroup.newBuilder()
                    .setResourceName(adgroupResourceName)
                    .setStatus(AdGroupStatusEnum.AdGroupStatus.PAUSED)
                    .build();

            AdGroupOperation op = AdGroupOperation.newBuilder()
                    .setUpdate(adGroup)
                    .setUpdateMask(FieldMasks.allSetFieldsOf(adGroup))
                    .build();
            operations.add(op);
        }

        try (AdGroupServiceClient adGroupServiceClient =
                     googleAdsClient.getLatestVersion().createAdGroupServiceClient()) {
            MutateAdGroupsResponse response =
                    adGroupServiceClient.mutateAdGroups(Long.toString(customerId), operations);
            for (MutateAdGroupResult result : response.getResultsList()) {
                System.out.printf("AdGroup with resource name '%s' is paused. %n", result.getResourceName());
            }
        }
    }

    /**
     * Remove adGroups from a client account in batches
     *
     * @param customerId  the id of the client account
     * @param adGroupList list of AdGroup Ids to be removed
     */
    public void removeAdGroup(long customerId, List<Long> adGroupList) {
        List<AdGroupOperation> operations = new ArrayList<>();
        try (AdGroupServiceClient adGroupServiceClient =
                     googleAdsClient.getLatestVersion().createAdGroupServiceClient()) {

            for (Long adGroupId : adGroupList) {
                String adGroupResource = ResourceNames.adGroup(customerId, adGroupId);
                // Constructs an operation that will remove the campaign with the specified resource name.
                AdGroupOperation operation =
                        AdGroupOperation.newBuilder().setRemove(adGroupResource).build();
                operations.add(operation);
            }

            // Sends the operation in a mutate request.
            MutateAdGroupsResponse response =
                    adGroupServiceClient.mutateAdGroups(
                            Long.toString(customerId), operations);
            // Prints the resource name of each removed object.
            for (MutateAdGroupResult mutateAdGroupResult : response.getResultsList()) {
                System.out.printf(
                        "Removed adGroup with resource name: '%s'.%n", mutateAdGroupResult.getResourceName());
            }
        }
    }

    /**
     * Update a batch of AdGroups in a client account
     *
     * @param customerId             the id of the client account
     * @param campaignId             the id of the campaign containing the adGroup
     * @param updateAdGroupModelList list of AdGroupModels with updated values
     */
    public void updateAdGroup(long customerId, long campaignId, List<AdGroupModel> updateAdGroupModelList) {
        List<AdGroupOperation> operations = new ArrayList<>();
        try (AdGroupServiceClient adGroupServiceClient =
                     googleAdsClient.getLatestVersion().createAdGroupServiceClient()) {

            for (AdGroupModel updatedAdGroupModel : updateAdGroupModelList) {
                // Get a fully completed updated campaign model
                AdGroupModel updatedModel = buildUpdatedAdGroupModel(customerId, campaignId, updatedAdGroupModel);

                // Creates a Campaign object with the proper resource name and any other changes.
                AdGroup adGroup =
                        AdGroup.newBuilder()
                                .setResourceName(ResourceNames.adGroup(customerId, campaignId))
                                .setName(updatedModel.getAdgroupName())
                                .setStatus(updatedModel.getStatus())
                                .setCpcBidMicros(updatedModel.getMaxCPC())
                                .build();
                // Constructs an operation that will update the campaign, using the FieldMasks utility to
                // derive the update mask. This mask tells the Google Ads API which attributes of the
                // campaign you want to change.
                AdGroupOperation operation =
                        AdGroupOperation.newBuilder()
                                .setUpdate(adGroup)
                                .setUpdateMask(FieldMasks.allSetFieldsOf(adGroup))
                                .build();
            }
            // Sends the operation in a mutate request.
            MutateAdGroupsResponse response =
                    adGroupServiceClient.mutateAdGroups(
                            String.valueOf(customerId), operations);
            // Prints the resource name of each updated object.
            for (MutateAdGroupResult mutateAdGroupResult : response.getResultsList()) {
                System.out.printf(
                        "Updated adGroup with resourceName: %s.%n", mutateAdGroupResult.getResourceName());
            }
        }
    }

    /**
     * Builds a list of updated AdGroupModels fully completed
     *
     * @param customer_id the id of the client's account
     * @param adGroupId   the id of the adGroup to update
     * @param model       a fully or partially completed AdGroupModel
     * @return an updated AdGroupModel composed of the existing model and updated changes
     */
    private AdGroupModel buildUpdatedAdGroupModel(long customer_id, long adGroupId, AdGroupModel model) {
        AdGroupModel existingModel = findAdGroup(customer_id, adGroupId);

        // if the campaign name is not updated, set to existing name
        if (model.getAdgroupName() == null || model.getAdgroupName().equals(""))
            model.setAdgroupName(existingModel.getAdgroupName());

        if (model.getStatus() == null)
            model.setStatus(existingModel.getStatus());

        if (model.getMaxCPC() == 0L)
            model.setMaxCPC(existingModel.getMaxCPC());

        if (model.getCampaignName() == null || model.getCampaignName().equals(""))
            model.setCampaignName(existingModel.getCampaignName());

        if (model.getCampaignId() == 0L)
            model.setCampaignId(existingModel.getCampaignId());

        if (model.getId() == 0L)
            model.setId(existingModel.getId());

        return model;
    }
}
