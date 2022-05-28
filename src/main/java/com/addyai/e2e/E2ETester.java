package com.addyai.e2e;

import com.addyai.ad.AdService;
import com.addyai.adgroup.AdGroupService;
import com.addyai.builder.GoogleAdsClientBuilder;
import com.addyai.campaign.CampaignService;
import com.addyai.models.AdGroupModel;
import com.addyai.models.CampaignModel;
import com.addyai.models.CampaignNetworkSettings;
import com.addyai.models.ResponsiveSearchAdModel;
import com.addyai.utils.ResponsiveSearchAdUtil;
import com.addyai.utils.Utils;
import com.google.ads.googleads.lib.GoogleAdsClient;
import com.google.ads.googleads.v10.common.AdTextAsset;
import com.google.ads.googleads.v10.enums.AdGroupStatusEnum;
import com.google.ads.googleads.v10.enums.AdvertisingChannelTypeEnum;
import com.google.ads.googleads.v10.enums.CampaignStatusEnum;
import com.google.ads.googleads.v10.resources.AdGroupCriterion;
import com.google.ads.googleads.v10.resources.CampaignBudget;
import com.google.ads.googleads.v9.errors.GoogleAdsException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is to used to test run the some or all methods on a Live Test Account
 * using a Test Manager Account
 */
public class E2ETester {

    public final static long MANAGER_ACCOUNT_ID = 2898332235L;
    public final static long CLIENT_ACCOUNT_ID = 9059845250L;

    private final GoogleAdsClientBuilder builder;

    private final CampaignService campaignService;

    private final AdGroupService adGroupService;

    private final AdService adService;

    private List<CampaignModel> campaignModelList = new ArrayList<>();

    private List<AdGroupModel> adGroupModelList = new ArrayList<>();

    private List<ResponsiveSearchAdModel> responsiveSearchAdModelList = new ArrayList<>();

    private int passedTests = 0;
    private int failedTests = 0;
    private final List<String> failedTestNames = new ArrayList<>();

    public E2ETester() {
        builder = new GoogleAdsClientBuilder();
        GoogleAdsClient googleAdsClient = builder.build();

        campaignService = new CampaignService(googleAdsClient);
        adGroupService = new AdGroupService(googleAdsClient);
        adService = new AdService(googleAdsClient);

        // runFullCampaignTests();
        //runFullAdGroupTests();
        runFullAdTests();
    }

    private void runFullCampaignTests() {
        createCampaign();
        updateCampaign();
        pauseCampaign();
        removeCampaign();

        printTestOutcome();
    }

    private void runFullAdGroupTests() {
        createCampaign();
        createAdGroup();
        updateAdGroup();
        pauseAdGroup();
        removeAdGroup();
        removeCampaign();

        printTestOutcome();
    }

    private void runFullAdTests() {
        createCampaign();
        createAdGroup();
        createResponsiveAds();

        updateResponsiveAds();

        printTestOutcome();
    }

    /**
     * Create a dummy campaign in the test account
     */
    private void createCampaign() {
        List<CampaignNetworkSettings> networkSettingsList = new ArrayList<>();
        List<CampaignModel> campaignModels = new ArrayList<>();

        // create networking settings for campaign
        CampaignNetworkSettings networkSettings = new CampaignNetworkSettings();
        networkSettings.setTargetContentNetwork(false);
        networkSettings.setTargetGoogleSearch(true);
        networkSettings.setTargetPartnerSearchNetwork(false);
        networkSettings.setTargetSearchNetwork(true);
        networkSettingsList.add(networkSettings);

        CampaignBudget budget = CampaignBudget
                .newBuilder()
                .setAmountMicros(Utils.convertDollarsToMicros("85.50"))
                .build();

        // generate a dummy CampaignModel
        CampaignModel campaign = new CampaignModel();
        campaign.setCampaignStatus(CampaignStatusEnum.CampaignStatus.ENABLED);
        campaign.setBudget(budget);
        campaign.setCustomerId(CLIENT_ACCOUNT_ID);
        campaign.setBudgetName("Dummy Budget");
        campaign.setChannelType(AdvertisingChannelTypeEnum.AdvertisingChannelType.SEARCH);
        campaign.setName("Dummy Test Campaign");
        campaignModels.add(campaign);

        // create the dummy campaign in test account
        try {
            CampaignService campaignService = new CampaignService(builder.build());
            campaignService.createSearchCampaign(CLIENT_ACCOUNT_ID, campaignModels, networkSettingsList);
        } catch (GoogleAdsException adsException) {
            log("Error: " + adsException.getGoogleAdsFailure().toString());
        }

        // update campaignModelList with the latest campaigns
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
        List<Long> campaignIds = new ArrayList<>();
        campaignIds.add(campaignModelList.get(0).getId());

        campaignService.pauseCampaign(CLIENT_ACCOUNT_ID, campaignIds);

        CampaignModel model = campaignService.findCampaignById(CLIENT_ACCOUNT_ID, campaignModelList.get(0).getId());
        if (model.getCampaignStatus() == CampaignStatusEnum.CampaignStatus.PAUSED) {
            passedTests = passedTests + 1;
        } else {
            failedTests = failedTests + 1;
            failedTestNames.add("Pause Campaign");
        }
    }

    private void removeCampaign() {
        List<Long> campaignIds = new ArrayList<>();
        campaignIds.add(campaignModelList.get(0).getId());

        campaignService.removeCampaign(CLIENT_ACCOUNT_ID, campaignIds);

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

    private void createAdGroup() {
        List<AdGroupModel> adGroupModels = new ArrayList<>();

        AdGroupModel adGroupModel = new AdGroupModel();
        adGroupModel.setCustomerId(CLIENT_ACCOUNT_ID);

        // Set the campaign id and name in the AdGroupModel
        for (CampaignModel campaignModel : campaignModelList) {
            if (campaignModel.getName().equals(campaignModelList.get(0).getName())) {
                adGroupModel.setCampaignId(campaignModel.getId());
                adGroupModel.setCampaignName(campaignModelList.get(0).getName());
            }
        }

        // create your ad group bid
        long bidValueMicros = Utils.convertDollarsToMicros("8.43");
        AdGroupCriterion adGroupCriterion = AdGroupCriterion
                .newBuilder()
                .setCpcBidMicros(bidValueMicros)
                .build();

        adGroupModel.setAdgroupName("Dummy Adgroup");
        adGroupModel.setStatus(AdGroupStatusEnum.AdGroupStatus.ENABLED);
        adGroupModel.setMaxCPC(adGroupCriterion.getCpcBidMicros());
        adGroupModels.add(adGroupModel);

        AdGroupService adGroupService = new AdGroupService(builder.build());
        adGroupService.createAdgroup(CLIENT_ACCOUNT_ID, adGroupModels);

        adGroupModelList = adGroupService.getAdGroups(CLIENT_ACCOUNT_ID, 1);

        for (AdGroupModel model : adGroupModelList) {
            if (model.getAdgroupName().equals("Dummy Adgroup")) {
                log("Ad group created successfully!");
                return;
            }
        }
        log("FAILED: AdGroup creation");
    }

    private void pauseAdGroup() {
        List<Long> adGroupIds = new ArrayList<>();
        adGroupIds.add(adGroupModelList.get(0).getId());

        adGroupService.pauseAdGroup(CLIENT_ACCOUNT_ID, adGroupIds);

        AdGroupModel model = adGroupService.findAdGroup(CLIENT_ACCOUNT_ID, adGroupModelList.get(0).getId());
        if (model.getStatus() == AdGroupStatusEnum.AdGroupStatus.PAUSED) {
            passedTests = passedTests + 1;
        } else {
            failedTests = failedTests + 1;
            failedTestNames.add("Pause AdGroup");
        }
    }

    private void updateAdGroup() {
        List<AdGroupModel> updatedAdGroupModelList = new ArrayList<>();
        List<Long> adGroupIdList = new ArrayList<>();

        AdGroupModel adGroupModel = new AdGroupModel();
        adGroupModel.setAdgroupName("TesterGroup");
        updatedAdGroupModelList.add(adGroupModel);
        adGroupIdList.add(adGroupModelList.get(0).getId());

        adGroupService.updateAdGroup(
                CLIENT_ACCOUNT_ID,
                adGroupIdList,
                updatedAdGroupModelList
        );

        adGroupModelList = adGroupService.getAdGroups(CLIENT_ACCOUNT_ID, 1);
        if (adGroupModelList.get(0).getAdgroupName().equals("TesterGroup")) {
            passedTests = passedTests + 1;
        } else {
            failedTests = failedTests + 1;
            failedTestNames.add("Update AdGroup");
        }
    }

    private void removeAdGroup() {
        List<Long> adGroupIds = new ArrayList<>();
        adGroupIds.add(adGroupModelList.get(0).getId());

        adGroupService.removeAdGroup(CLIENT_ACCOUNT_ID, adGroupIds);

        AdGroupModel model = adGroupService.findAdGroup(CLIENT_ACCOUNT_ID, adGroupModelList.get(0).getId());
        if (model == null) {
            passedTests = passedTests + 1;
        } else {
            failedTests = failedTests + 1;
            failedTestNames.add("Remove AdGroup");
        }
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
        adService.createResponsiveSearchAd(CLIENT_ACCOUNT_ID, adGroupModelList.get(0).getId(), responsiveSearchAdModels);

        responsiveSearchAdModelList = adService.getResponsiveSearchAds(CLIENT_ACCOUNT_ID, 1);
        ResponsiveSearchAdModel model = responsiveSearchAdModelList.get(responsiveSearchAdModelList.size() - 1);

        if (model.getHeadlinesList().get(0).getText().equals("AddyAI Improves Your Campaigns")) {
            passedTests = passedTests + 1;
        } else {
            System.out.println("PATH: " + model.getPath1());
            failedTests = failedTests + 1;
            failedTestNames.add("Create Ad");
        }
    }

    private void updateResponsiveAds() {
        List<ResponsiveSearchAdModel> responsiveSearchAdModels = new ArrayList<>();
        List<String> headlineList = new ArrayList<>();
        List<String> descriptionList = new ArrayList<>();

        headlineList.add("AddyAI Manages Your Ads");
        headlineList.add("AI Manages Your Ads Perfectly");
        headlineList.add("Let Our AI Improve Your Ads");

        descriptionList.add("AddyAI Can Manage Your PPC Campaigns For You! Sign Up For Our Free 30 Day Trial Now!");
        descriptionList.add("AddyAI Can Improve Your Ads Campaigns For You! Sign Up For A No-Obligation 30 Day Trial!");

        ResponsiveSearchAdUtil responsiveSearchAdUtil = new ResponsiveSearchAdUtil();
        List<AdTextAsset> headlines = responsiveSearchAdUtil.createHeadlinesList(headlineList);
        List<AdTextAsset> descriptions = responsiveSearchAdUtil.createDescriptionList(descriptionList);

        ResponsiveSearchAdModel searchAdModel = new ResponsiveSearchAdModel();
        searchAdModel.setAdGroupId(adGroupModelList.get(adGroupModelList.size()-1).getId());
        searchAdModel.setHeadlinesList(headlines);
        searchAdModel.setDescriptionList(descriptions);
        searchAdModel.setPath1("improve-roas");
        searchAdModel.setPath2("AI For Ads");
        searchAdModel.setFinalUrl("http://www.addyaiz.com");

        responsiveSearchAdModels.add(searchAdModel);

        adService.updateResponsiveSearchAds(CLIENT_ACCOUNT_ID,
                responsiveSearchAdModelList.get(0).getId(),
                responsiveSearchAdModels);
    }

    private void removeResponsiveSearchAds() {
        List<Long> adGroupIdList = new ArrayList<>();
        List<Long> adIdList = new ArrayList<>();

        adIdList.stream()
                .filter(adId -> adGroupIdList.contains(adId))
                .collect(Collectors.toList());
    }
/*
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
    }*/

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
