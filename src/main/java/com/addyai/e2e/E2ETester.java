package com.addyai.e2e;

import com.addyai.builder.GoogleAdsClientBuilder;
import com.addyai.campaign.CampaignService;
import com.addyai.models.CampaignModel;
import com.addyai.models.CampaignNetworkSettings;
import com.addyai.utils.Utils;
import com.google.ads.googleads.lib.GoogleAdsClient;
import com.google.ads.googleads.v10.enums.AdvertisingChannelTypeEnum;
import com.google.ads.googleads.v10.enums.CampaignStatusEnum;
import com.google.ads.googleads.v10.resources.CampaignBudget;
import com.google.ads.googleads.v9.errors.GoogleAdsException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is to used to test run the some or all methods on a Live Test Account
 * using a Test Manager Account
 */
public class E2ETester {

    public final static long MANAGER_ACCOUNT_ID = 2898332235L;
    public final static long CLIENT_ACCOUNT_ID = 9059845250L;

    private final GoogleAdsClientBuilder builder;

    private final CampaignService campaignService;

    private List<CampaignModel> campaignModelList = new ArrayList<>();

    private int passedTests = 0;
    private int failedTests = 0;
    private final List<String> failedTestNames = new ArrayList<>();

    public E2ETester() {
        builder = new GoogleAdsClientBuilder();
        GoogleAdsClient googleAdsClient = builder.build();

        campaignService = new CampaignService(googleAdsClient);

        runFullCampaignTests();
    }

    private void runFullCampaignTests() {
        createCampaign();
        updateCampaign();
        pauseCampaign();
        removeCampaign();

        printTestOutcome();
    }

    /**
     * Create a dummy campaign in the test account
     */
    private void createCampaign() {
        // create networking settings for campaign
        CampaignNetworkSettings networkSettings = new CampaignNetworkSettings();
        networkSettings.setTargetContentNetwork(false);
        networkSettings.setTargetGoogleSearch(true);
        networkSettings.setTargetPartnerSearchNetwork(false);
        networkSettings.setTargetSearchNetwork(true);

        CampaignBudget budget = CampaignBudget
                .newBuilder()
                .setAmountMicros(Utils.convertMicrosValue("85.50"))
                .build();

        // generate a dummy CampaignModel
        CampaignModel campaign = new CampaignModel();
        campaign.setCampaignStatus(CampaignStatusEnum.CampaignStatus.ENABLED);
        campaign.setBudget(budget);
        campaign.setCustomerId(CLIENT_ACCOUNT_ID);
        campaign.setBudgetName("Dummy Budget");
        campaign.setChannelType(AdvertisingChannelTypeEnum.AdvertisingChannelType.SEARCH);
        campaign.setName("Dummy Test Campaign");

        // create the dummy campaign in test account
        try {
            CampaignService campaignService = new CampaignService(builder.build());
            campaignService.createSearchCampaign(campaign, networkSettings);
        } catch (GoogleAdsException adsException) {
            log("Error: " + adsException.getGoogleAdsFailure().toString());
        }

        // udpate campaignModelList with latest campaigns
        campaignModelList = campaignService.getCampaigns(CLIENT_ACCOUNT_ID);

        // verify the campaign name is matched
        if (isCampaignVerified("Dummy Test Campaign")) {
            passedTests = passedTests + 1;
        } else {
            failedTests = failedTests + 1;
            failedTestNames.add("Create Campaign");
        }
    }

    private void updateCampaign() {
        CampaignModel campaign = new CampaignModel();
        campaign.setName("Updated Test Campaign"); // Updating Name

        campaignService.updateCampaign(CLIENT_ACCOUNT_ID, campaignModelList.get(0).getId(), campaign);

        campaignModelList = campaignService.getCampaigns(CLIENT_ACCOUNT_ID);
        if (isCampaignVerified("Updated Test Campaign")) {
            passedTests = passedTests + 1;
        } else {
            failedTests = failedTests + 1;
            failedTestNames.add("Update Campaign");
        }
    }

    private void pauseCampaign() {
        campaignService.pauseCampaign(CLIENT_ACCOUNT_ID, campaignModelList.get(0).getId());

        CampaignModel model = campaignService.findCampaignById(CLIENT_ACCOUNT_ID, campaignModelList.get(0).getId());
        if (model.getCampaignStatus() == CampaignStatusEnum.CampaignStatus.PAUSED) {
            passedTests = passedTests + 1;
        } else {
            failedTests = failedTests + 1;
            failedTestNames.add("Pause Campaign");
        }
    }

    private void removeCampaign() {
        campaignService.removeCampaign(CLIENT_ACCOUNT_ID, campaignModelList.get(0).getId());

        CampaignModel model = campaignService.findCampaignById(CLIENT_ACCOUNT_ID, campaignModelList.get(0).getId());
        if (model == null) {
            passedTests = passedTests + 1;
        } else {
            failedTests = failedTests + 1;
            failedTestNames.add("Remove Campaign");
        }
    }

    /**
     * Verify there is a campaign with the same name in the account
     *
     * @param expectedCampaignName the name of the campaign expected to be in the list
     * @return true if the campaign name exists, false if it does not
     */
    private boolean isCampaignVerified(String expectedCampaignName) {
        for (CampaignModel model : campaignModelList) {
            if (model.getName().equals(expectedCampaignName)) {
                return true;
            }
        }
        return false;
    }

/*

    private void createAdGroup() {
        AdGroupModel adGroupModel = new AdGroupModel();
        adGroupModel.setCustomerId(CLIENT_ACCOUNT_ID);

        // Set the campaign id and name in the AdGroupModel
        for (CampaignModel campaignModel : campaignModelList) {
            if (campaignModel.getName().equals(campaignModelList.get(0).getName())) {
                adGroupModel.setCampaignId(campaignModel.getId());
                adGroupModel.setCampaignName(campaignModelList.get(0).getName());
            }
        }

        adGroupModel.setAdgroupName("Dummy Adgroup");
        adGroupModel.setStatus(AdGroupStatusEnum.AdGroupStatus.ENABLED);
        adGroupModel.setType(AdGroupTypeEnum.AdGroupType.SEARCH_STANDARD);
        adGroupModel.setMaxCPC(8);
        AdGroupService adGroupService = new AdGroupService(builder.build());
        adGroupService.createAdgroup(adGroupModel);

        adGroupModelList = adGroupService.getAdGroups(CLIENT_ACCOUNT_ID, 10);

        for (AdGroupModel model : adGroupModelList) {
            if (model.getAdgroupName().equals("Dummy Adgroup")) {
                log("Ad group created successfully!");
                return;
            }
        }
        log("FAILED: AdGroup creation");
    }

    private void createResponsiveAds() {
        List<ResponsiveSearchAdModel> responsiveSearchAdModels = new ArrayList<>();
        List<String> headlineList = new ArrayList<>();
        List<String> descriptionList = new ArrayList<>();

        headlineList.add("AddyAI Improves Your Campaigns");
        headlineList.add("AI Manages Your Ads Perfectly");
        headlineList.add("Let Our AI Improve Your Ads");

        descriptionList.add("AddyAI Can Manage Your Ads Campaigns For You! Sign Up For Our Free 30 Day Trial Now!");
        descriptionList.add("AddyAI Can Improve Your Ads Campaigns For You! Sign Up For A No-Obligation 30 Day Trial!");

        ResponsiveSearchAdUtil responsiveSearchAdUtil = new ResponsiveSearchAdUtil();
        List<AdTextAsset> headlines = responsiveSearchAdUtil.createHeadlinesList(headlineList);
        List<AdTextAsset> descriptions = responsiveSearchAdUtil.createDescriptionList(descriptionList);

        ResponsiveSearchAdModel searchAdModel = new ResponsiveSearchAdModel();
        searchAdModel.setAdGroupId(adGroupModelList.get(0).getId());
        searchAdModel.setHeadlinesList(headlines);
        searchAdModel.setDescriptionList(descriptions);
        searchAdModel.setPath1("Improve ROAS");
        searchAdModel.setPath2("AI For Ads");
        searchAdModel.setFinalUrl("http://www.addyaiz.com");

        responsiveSearchAdModels.add(searchAdModel);
        adService.createResponsiveSearchAds(CLIENT_ACCOUNT_ID, adGroupModelList.get(0).getId(), responsiveSearchAdModels);
    }

    private void createKeywords() {
        List<KeywordModel> newKeywordsList = new ArrayList();

        KeywordModel model1 = new KeywordModel();
        model1.setMatchType(KeywordMatchTypeEnum.KeywordMatchType.EXACT);
        model1.setText("AI Adwords Manager");
        model1.setCpcBid(1);
        newKeywordsList.add(model1);

        KeywordModel model2 = new KeywordModel();
        model2.setMatchType(KeywordMatchTypeEnum.KeywordMatchType.PHRASE);
        model2.setText("AI Adwords Manager");
        model2.setCpcBid(2);
        newKeywordsList.add(model2);

        KeywordModel model3 = new KeywordModel();
        model3.setMatchType(KeywordMatchTypeEnum.KeywordMatchType.BROAD);
        model3.setText("AI Adwords Manager");
        model3.setCpcBid(3);
        newKeywordsList.add(model3);

        keywordService.addKeywords(CLIENT_ACCOUNT_ID, adGroupModelList.get(0).getId(), newKeywordsList);
        keywordModelList = keywordService.getKeywords(CLIENT_ACCOUNT_ID, 10);

        for (KeywordModel k : keywordModelList) {
            log(k.getText());
        }
    }
*/

    private void log(String message) {
        System.out.println(message);
    }

    private void printTestOutcome() {
        System.out.println("\n");
        System.out.println("Success Tests: " + passedTests);
        System.out.println("Failed Tests Count: " + failedTests);
        System.out.println("Failed Tests Cases" + failedTestNames.toString());
    }
}
