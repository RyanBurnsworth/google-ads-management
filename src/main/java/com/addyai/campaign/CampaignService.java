package com.addyai.campaign;

import com.addyai.models.CampaignModel;
import com.addyai.models.CampaignNetworkSettings;
import com.google.ads.googleads.lib.GoogleAdsClient;
import com.google.ads.googleads.v10.common.ManualCpc;
import com.google.ads.googleads.v10.enums.BudgetDeliveryMethodEnum;
import com.google.ads.googleads.v10.resources.Campaign;
import com.google.ads.googleads.v10.resources.CampaignBudget;
import com.google.ads.googleads.v10.services.*;
import com.google.api.gax.rpc.ServerStream;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

/**
 * Manage campaigns on a customer account
 */
public class CampaignService {
    private final GoogleAdsClient googleAdsClient;

    public CampaignService(GoogleAdsClient googleAdsClient) {
        this.googleAdsClient = googleAdsClient;
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
            String query = "SELECT campaign.id, campaign.name FROM campaign WHERE campaign.status IN ('ENABLED', 'PAUSED') ORDER BY campaign.id";
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
                    campaignModel.setEndDate(googleAdsRow.getCampaign().getEndDate());
                    campaignModel.setStartDate(googleAdsRow.getCampaign().getStartDate());
                    campaignModel.setChannelType(googleAdsRow.getCampaign().getAdvertisingChannelType());

                    if (!googleAdsRow.getCampaign().getCampaignBudget().equals(""))
                        campaignModel.setBudget(googleAdsRow.getCampaign().getCampaignBudget());
                    else
                        campaignModel.setBudget("0");

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
                Long.parseLong(newCampaign.getBudget()),
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
                        .setStartDate(newCampaign.getStartDate())
                        .setEndDate(newCampaign.getEndDate())
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
}
