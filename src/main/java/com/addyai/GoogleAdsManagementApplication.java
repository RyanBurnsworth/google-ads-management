package com.addyai;

import com.addyai.builder.GoogleAdsClientBuilder;
import com.addyai.googleads.adgroup.AdGroupService;
import com.addyai.googleads.campaign.CampaignManagementService;
import com.addyai.models.AdGroupModel;
import com.addyai.models.CampaignModel;
import com.addyai.models.CampaignNetworkSettings;
import com.addyai.utils.DateTimeUtils;
import com.google.ads.googleads.v10.enums.AdGroupStatusEnum;
import com.google.ads.googleads.v10.enums.AdGroupTypeEnum;
import com.google.ads.googleads.v10.enums.AdvertisingChannelTypeEnum;
import com.google.ads.googleads.v10.enums.CampaignStatusEnum;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
public class GoogleAdsManagementApplication {
    public final static long MANAGER_ACCOUNT_ID = 2898332235L;
    public final static long CLIENT_ACCOUNT_ID = 9059845250L;

    public static void main(String[] args) {
        SpringApplication.run(GoogleAdsManagementApplication.class, args);

        GoogleAdsClientBuilder builder = new GoogleAdsClientBuilder();

        String startDate = DateTimeUtils.getFutureDate(1);
        String endDate = DateTimeUtils.getFutureDate(31);

        CampaignNetworkSettings networkSettings = new CampaignNetworkSettings();
        networkSettings.setTargetContentNetwork(false);
        networkSettings.setTargetGoogleSearch(true);
        networkSettings.setTargetPartnerSearchNetwork(false);
        networkSettings.setTargetSearchNetwork(true);

        CampaignModel campaign = new CampaignModel();
        campaign.setCampaignStatus(CampaignStatusEnum.CampaignStatus.PAUSED);
        campaign.setBudget(1000000);
        campaign.setCustomerId(CLIENT_ACCOUNT_ID);
        campaign.setBudgetName("TEST BUDGET");
        campaign.setChannelType(AdvertisingChannelTypeEnum.AdvertisingChannelType.SEARCH);
        campaign.setName("FIRST TEST CAMPAIGN");
        campaign.setStartDate(startDate);
        campaign.setEndDate(endDate);

        CampaignManagementService campaignManagementService = new CampaignManagementService(builder.build());
//        campaignManagementService.createSearchCampaign(campaign, networkSettings);

        AdGroupModel adGroupModel = new AdGroupModel();
        Map<Long, String> campaignMap = campaignManagementService.getCampaigns(CLIENT_ACCOUNT_ID);
        adGroupModel.setCustomerId(CLIENT_ACCOUNT_ID);

        for (Long key : campaignMap.keySet()) {
            if (campaignMap.get(key).equals(campaign.getName())) {
                adGroupModel.setCampaignId(key);
                adGroupModel.setCampaignName(campaign.getName());
                break;
            }
        }

        adGroupModel.setAdgroupName("Test Adgroup3");
        adGroupModel.setStatus(AdGroupStatusEnum.AdGroupStatus.ENABLED);
        adGroupModel.setType(AdGroupTypeEnum.AdGroupType.SEARCH_STANDARD);
        adGroupModel.setMaxCPC(8);
        AdGroupService adGroupService = new AdGroupService(builder.build());
        // adGroupService.createAdgroup(adGroupModel);

        adGroupService.getAdGroups(CLIENT_ACCOUNT_ID, adGroupModel.getCampaignId(), 10);
    }
}
