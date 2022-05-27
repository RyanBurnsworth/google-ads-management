package com.addyai.e2e;

import com.addyai.ad.AdService;
import com.addyai.adgroup.AdGroupService;
import com.addyai.builder.GoogleAdsClientBuilder;
import com.addyai.campaign.CampaignService;
import com.addyai.keyword.KeywordService;
import com.addyai.models.*;
import com.addyai.utils.DateTimeUtils;
import com.addyai.utils.ResponsiveSearchAdUtil;
import com.google.ads.googleads.lib.GoogleAdsClient;
import com.google.ads.googleads.v10.common.AdTextAsset;
import com.google.ads.googleads.v10.enums.*;
import com.google.ads.googleads.v9.errors.GoogleAdsException;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

/**
 * This class is to run the Google Ads methods on a Live Test Account
 * using a Test Manager Account
 */
public class E2ETester {

    public final static long MANAGER_ACCOUNT_ID = 2898332235L;
    public final static long CLIENT_ACCOUNT_ID = 9059845250L;

    private final GoogleAdsClientBuilder builder;

    private final CampaignService campaignService;
    private AdGroupService adGroupService;
    private KeywordService keywordService;

    private AdService adService;

    private List<CampaignModel> campaignModelList = new ArrayList<>();
    private List<AdGroupModel> adGroupModelList = new ArrayList<>();
    private List<KeywordModel> keywordModelList = new ArrayList<>();

    private List<ResponsiveSearchAdModel> responsiveSearchAdModelList = new ArrayList<>();

    public E2ETester() {
        builder = new GoogleAdsClientBuilder();
        GoogleAdsClient googleAdsClient = builder.build();

        campaignService = new CampaignService(googleAdsClient);
        adGroupService = new AdGroupService(googleAdsClient);
        keywordService = new KeywordService(googleAdsClient);
        adService = new AdService(googleAdsClient);

        startE2ETesting();
    }

    private void startE2ETesting() {
//        createCampaign();
//        createAdGroup();
//        createResponsiveAds();
//        createKeywords();
        adService.getResponsiveSearchAds(CLIENT_ACCOUNT_ID, 100);

    }

    private void createCampaign() {
        String startDate = DateTimeUtils.getFutureDate(1);
        String endDate = DateTimeUtils.getFutureDate(31);

        CampaignNetworkSettings networkSettings = new CampaignNetworkSettings();
        networkSettings.setTargetContentNetwork(false);
        networkSettings.setTargetGoogleSearch(true);
        networkSettings.setTargetPartnerSearchNetwork(false);
        networkSettings.setTargetSearchNetwork(true);

        CampaignModel campaign = new CampaignModel();
        campaign.setCampaignStatus(CampaignStatusEnum.CampaignStatus.PAUSED);
        campaign.setBudget("1000000");
        campaign.setCustomerId(CLIENT_ACCOUNT_ID);
        campaign.setBudgetName("Dummy Budget1");
        campaign.setChannelType(AdvertisingChannelTypeEnum.AdvertisingChannelType.SEARCH);
        campaign.setName("Dummy Test Campaign");
        campaign.setStartDate(startDate);
        campaign.setEndDate(endDate);

        try {
            CampaignService campaignService = new CampaignService(builder.build());
            campaignService.createSearchCampaign(campaign, networkSettings);
        } catch (GoogleAdsException adsException) {
            log("Error: " + adsException.getGoogleAdsFailure().toString());
            exit(1);
        }

        campaignModelList = campaignService.getCampaigns(CLIENT_ACCOUNT_ID);

        for (CampaignModel model : campaignModelList) {
            if (model.getName().equals("Dummy Test Campaign")) {
                log("Campaign Created Successfully!");
                return;
            }
        }
        log("FAILED: Campaign Creation");
    }

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
        adService.createResponsiveSearchAds(CLIENT_ACCOUNT_ID, adGroupModelList.get(0).getId(),responsiveSearchAdModels);
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

    private void log(String message) {
        System.out.println(message);
    }
}
