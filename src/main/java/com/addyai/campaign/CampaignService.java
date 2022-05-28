package com.addyai.campaign;

import com.addyai.models.CampaignModel;
import com.addyai.models.CampaignNetworkSettings;
import com.google.ads.googleads.lib.GoogleAdsClient;
import com.google.ads.googleads.lib.utils.FieldMasks;
import com.google.ads.googleads.v10.common.ManualCpc;
import com.google.ads.googleads.v10.enums.BudgetDeliveryMethodEnum;
import com.google.ads.googleads.v10.enums.CampaignStatusEnum;
import com.google.ads.googleads.v10.resources.Campaign;
import com.google.ads.googleads.v10.resources.CampaignBudget;
import com.google.ads.googleads.v10.services.*;
import com.google.ads.googleads.v10.utils.ResourceNames;
import com.google.api.gax.rpc.ServerStream;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

/**
 * Manage campaigns within a client's account
 */
public class CampaignService {
    private final GoogleAdsClient googleAdsClient;

    public CampaignService(GoogleAdsClient googleAdsClient) {
        this.googleAdsClient = googleAdsClient;
    }

    /**
     * Find a campaign given it's campaignId
     *
     * @param customerId the id of the client's account
     * @param campaignId the id of the campaign to find
     * @return the CampaignModel if it existing or null if not
     */
    public CampaignModel findCampaignById(long customerId, long campaignId) {
        List<CampaignModel> campaignModelList = getCampaigns(customerId);
        for (CampaignModel model : campaignModelList) {
            if (model.getId() == campaignId) {
                return model;
            }
        }
        return null;
    }

    /**
     * Retrieve campaigns from a customer campaign
     *
     * @param customerId the customer ID
     * @return a list of CampaignModel containing all campaigns in account
     */
    public List<CampaignModel> getCampaigns(long customerId) {
        List<CampaignModel> campaignModels = new ArrayList<>();

        try (GoogleAdsServiceClient googleAdsServiceClient =
                     googleAdsClient.getLatestVersion().createGoogleAdsServiceClient()) {
            String query = "SELECT campaign.id, " +
                    "campaign.name, campaign.status, " +
                    "campaign.advertising_channel_type, " +
                    "campaign.campaign_budget " +
                    "FROM campaign " +
                    "WHERE campaign.status IN ('ENABLED', 'PAUSED') ORDER BY campaign.id";

            // Constructs the SearchGoogleAdsStreamRequest.
            SearchGoogleAdsStreamRequest request =
                    SearchGoogleAdsStreamRequest.newBuilder()
                            .setCustomerId(Long.toString(customerId))
                            .setQuery(query)
                            .build();

            // Creates and issues a search Google Ads stream request that will retrieve all campaigns.
            ServerStream<SearchGoogleAdsStreamResponse> stream =
                    googleAdsServiceClient.searchStreamCallable().call(request);

            // Iterates through and prints all the results in the stream response.
            for (SearchGoogleAdsStreamResponse response : stream) {
                for (GoogleAdsRow googleAdsRow : response.getResultsList()) {
                    CampaignModel campaignModel = new CampaignModel();
                    campaignModel.setId(googleAdsRow.getCampaign().getId());
                    campaignModel.setCampaignStatus(googleAdsRow.getCampaign().getStatus());
                    campaignModel.setName(googleAdsRow.getCampaign().getName());
                    campaignModel.setChannelType(googleAdsRow.getCampaign().getAdvertisingChannelType());
                    campaignModel.setBudget(googleAdsRow.getCampaignBudget());

                    campaignModels.add(campaignModel);

                    System.out.printf(
                            "Campaign with ID %d and name '%s' was found.%n",
                            googleAdsRow.getCampaign().getId(), googleAdsRow.getCampaign().getName());
                }
            }
        }

        return campaignModels;
    }

    /**
     * Create a Search CampaignModel
     *
     * @param newCampaign a campaign model with details for new campaign
     * @param settings    a networking settings model with details for new campaign
     */
    public void createSearchCampaign(CampaignModel newCampaign, CampaignNetworkSettings settings) {

        // Creates a single shared budget to be used by the campaigns added below.
        String budgetResourceName = addStandardCampaignBudget(
                newCampaign.getCustomerId(),
                newCampaign.getBudget().getAmountMicros(),
                newCampaign.getBudgetName()
        );

        // Setup campaign network settings
        Campaign.NetworkSettings networkSettings =
                Campaign.NetworkSettings.newBuilder()
                        .setTargetGoogleSearch(settings.isTargetGoogleSearch())
                        .setTargetSearchNetwork(settings.isTargetSearchNetwork())
                        .setTargetContentNetwork(settings.isTargetContentNetwork())
                        .setTargetPartnerSearchNetwork(settings.isTargetPartnerSearchNetwork())
                        .build();

        // Creates the campaign.
        Campaign campaign =
                Campaign.newBuilder()
                        .setName(newCampaign.getName())
                        .setAdvertisingChannelType(newCampaign.getChannelType())
                        .setStatus(newCampaign.getCampaignStatus())
                        .setManualCpc(ManualCpc.newBuilder().build())
                        .setCampaignBudget(budgetResourceName)
                        .setNetworkSettings(networkSettings)
                        .build();

        List<CampaignOperation> campaignOperations = new ArrayList<>();
        CampaignOperation op = CampaignOperation.newBuilder().setCreate(campaign).build();
        campaignOperations.add(op);

        try (CampaignServiceClient campaignServiceClient =
                     googleAdsClient.getLatestVersion().createCampaignServiceClient()) {
            MutateCampaignsResponse response =
                    campaignServiceClient.mutateCampaigns(Long.toString(newCampaign.getCustomerId()), campaignOperations);
            System.out.printf("Added %d campaigns:%n", response.getResultsCount());
            for (MutateCampaignResult result : response.getResultsList()) {
                System.out.println(result.getResourceName());
            }
        }
    }

    /**
     * Pause a Campaign in the specified client account
     *
     * @param customerId the id of the client account
     * @param campaignId the id of the campaign to be paused
     */
    public void pauseCampaign(long customerId, long campaignId) {
        String campaignResourceName = ResourceNames.campaign(customerId, campaignId);
        Campaign campaign = Campaign.newBuilder()
                .setResourceName(campaignResourceName)
                .setStatus(CampaignStatusEnum.CampaignStatus.PAUSED)
                .build();

        CampaignOperation op = CampaignOperation.newBuilder()
                .setUpdate(campaign)
                .setUpdateMask(FieldMasks.allSetFieldsOf(campaign))
                .build();

        try (CampaignServiceClient campaignServiceClient =
                     googleAdsClient.getLatestVersion().createCampaignServiceClient()) {
            MutateCampaignsResponse response =
                    campaignServiceClient.mutateCampaigns(Long.toString(customerId), ImmutableList.of(op));
            for (MutateCampaignResult result : response.getResultsList()) {
                System.out.printf("Campaign with resource name '%s' is paused. %n", result.getResourceName());
            }
        }
    }

    /**
     * Remove a campaign from a client account
     *
     * @param customerId the id of the client account
     * @param campaignId the id of the campaign to be removed
     */
    public void removeCampaign(long customerId, long campaignId) {
        try (CampaignServiceClient campaignServiceClient =
                     googleAdsClient.getLatestVersion().createCampaignServiceClient()) {
            String campaignResourceName = ResourceNames.campaign(customerId, campaignId);
            // Constructs an operation that will remove the campaign with the specified resource name.
            CampaignOperation operation =
                    CampaignOperation.newBuilder().setRemove(campaignResourceName).build();
            // Sends the operation in a mutate request.
            MutateCampaignsResponse response =
                    campaignServiceClient.mutateCampaigns(
                            Long.toString(customerId), ImmutableList.of(operation));
            // Prints the resource name of each removed object.
            for (MutateCampaignResult mutateCampaignResult : response.getResultsList()) {
                System.out.printf(
                        "Removed campaign with resource name: '%s'.%n", mutateCampaignResult.getResourceName());
            }
        }
    }

    /**
     * Update a campaign in a client account
     *
     * @param customerId           the id of the client account
     * @param campaignId           the id of the campaign to update
     * @param updatedCampaignModel the updated campaign values
     */
    public void updateCampaign(long customerId, long campaignId, CampaignModel updatedCampaignModel) {
        try (CampaignServiceClient campaignServiceClient =
                     googleAdsClient.getLatestVersion().createCampaignServiceClient()) {

            // Get a fully completed updated campaign model
            CampaignModel updatedModel = buildUpdatedCampaignModel(customerId, campaignId, updatedCampaignModel);

            // Creates a Campaign object with the proper resource name and any other changes.
            Campaign campaign =
                    Campaign.newBuilder()
                            .setResourceName(ResourceNames.campaign(customerId, campaignId))
                            .setName(updatedModel.getName())
                            .setStatus(updatedModel.getCampaignStatus())
                            // .setCampaignBudget(updatedCampaign.getBudget()) // TODO: Update budget
                            .build();
            // Constructs an operation that will update the campaign, using the FieldMasks utility to
            // derive the update mask. This mask tells the Google Ads API which attributes of the
            // campaign you want to change.
            CampaignOperation operation =
                    CampaignOperation.newBuilder()
                            .setUpdate(campaign)
                            .setUpdateMask(FieldMasks.allSetFieldsOf(campaign))
                            .build();
            // Sends the operation in a mutate request.
            MutateCampaignsResponse response =
                    campaignServiceClient.mutateCampaigns(
                            String.valueOf(customerId), ImmutableList.of(operation));
            // Prints the resource name of each updated object.
            for (MutateCampaignResult mutateCampaignResult : response.getResultsList()) {
                System.out.printf(
                        "Updated campaign with resourceName: %s.%n", mutateCampaignResult.getResourceName());
            }
        }
    }

    /**
     * Creates a new CampaignBudget in the specified client account.
     *
     * @param customerId the client customer ID.
     * @return resource name of the newly created budget.
     */
    private String addStandardCampaignBudget(long customerId, long budgetAmount, String budgetName) {
        CampaignBudget budget =
                CampaignBudget.newBuilder()
                        .setName(budgetName)
                        .setDeliveryMethod(BudgetDeliveryMethodEnum.BudgetDeliveryMethod.STANDARD)
                        .setAmountMicros(budgetAmount)
                        .setExplicitlyShared(false)
                        .build();

        CampaignBudgetOperation op = CampaignBudgetOperation.newBuilder().setCreate(budget).build();

        try (CampaignBudgetServiceClient campaignBudgetServiceClient =
                     googleAdsClient.getLatestVersion().createCampaignBudgetServiceClient()) {
            MutateCampaignBudgetsResponse response =
                    campaignBudgetServiceClient.mutateCampaignBudgets(
                            Long.toString(customerId), ImmutableList.of(op));
            String budgetResourceName = response.getResults(0).getResourceName();
            System.out.printf("Added budget: %s%n", budgetResourceName);
            return budgetResourceName;
        }
    }

    /**
     * Builds an updated CampaignModel given a fully or partially completed CampaignModel
     *
     * @param customer_id the id of the client's account
     * @param campaignId  the id of the campaign to update
     * @param model       a fully or partially completed CampaignModel
     * @return an updated CampaignModel composed of the existing model and updated changes
     */
    private CampaignModel buildUpdatedCampaignModel(long customer_id, long campaignId, CampaignModel model) {
        CampaignModel existingModel = findCampaignById(customer_id, campaignId);

        // if the campaign name is not updated, set to existing name
        if (model.getName() == null || model.getName().equals(""))
            model.setName(existingModel.getName());

        if (model.getCampaignStatus() == null)
            model.setCampaignStatus(existingModel.getCampaignStatus());

        if (model.getBudget() == null || model.getBudget().equals(""))
            model.setBudget(existingModel.getBudget());

        if (model.getBudgetName() == null || model.getBudgetName().equals(""))
            model.setBudgetName(existingModel.getBudgetName());

        if (model.getChannelType() == null)
            model.setChannelType(existingModel.getChannelType());

        return model;
    }
}
