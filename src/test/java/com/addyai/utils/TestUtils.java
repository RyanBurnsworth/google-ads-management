/*
 * Copyright (c) 2022.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. This program
 * is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 *
 */

package com.addyai.utils;

import com.addyai.models.AdGroupDetails;
import com.addyai.models.BudgetDetails;
import com.addyai.models.CampaignDetails;
import com.addyai.models.KeywordDetails;
import com.addyai.models.campaign_criterion.*;
import com.google.ads.googleads.lib.utils.FieldMasks;
import com.google.ads.googleads.v12.common.*;
import com.google.ads.googleads.v12.enums.*;
import com.google.ads.googleads.v12.resources.*;
import com.google.ads.googleads.v12.services.AdGroupCriterionOperation;
import com.google.ads.googleads.v12.services.AdGroupOperation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.addyai.mocks.CampaignMocks.MOCK_CAMPAIGN_RESOURCE_NAME;
import static com.addyai.mocks.CampaignMocks.MOCK_NEGATIVE_KEYWORD;
import static com.addyai.utils.misc.Constants.*;

public class TestUtils {
    public static final String MOCK_AD_GROUP_RESOURCE_NAME = "customers/9059845250/adgroups/39203940293";
    public static final String MOCK_AD_GROUP_CRITERION_RESOURCE_NAME = "customers/9059845250/adgroup_criterion/23402340234";
    public static final CampaignStatusEnum.CampaignStatus MOCK_CAMPAIGN_STATUS = CampaignStatusEnum.CampaignStatus.ENABLED;
    public static final AdvertisingChannelTypeEnum.AdvertisingChannelType MOCK_CAMPAIGN_ADVERTISING_CHANNEL = AdvertisingChannelTypeEnum.AdvertisingChannelType.SEARCH;
    public static final String MOCK_CAMPAIGN_BUDGET_RESOURCE_NAME = "customers/9059845250/campaignBudgets/11599749725";
    public static final String MOCK_CAMPAIGN_START_DATE = "2000-01-01";
    public static final String MOCK_CAMPAIGN_END_DATE = "2001-01-01";

    public static final long MOCK_BUDGET_ID = 1L;
    public static final long MOCK_BUDGET_AMOUNT = 100L;
    public static final String MOCK_BUDGET_NAME = "Test Budget 1";
    public static final String MOCK_BUDGET_RESOURCE_NAME = "customers/9059845250/campaignBudgets/1";

    public static final long MOCK_CRITERION_ID = 2L;
    public static final String MOCK_CRITERION_RESOURCE_NAME = "customers/905984520/criterion/2";
    public static final float MOCK_BID_MODIFIER = 2.0f;
    public static final String MOCK_NEGATIVE_KEYWORD = "testing sucks";
    public static final double PROXIMITY_RADIUS = 20.0;

    public static final String MOCK_CITY_NAME = "Java City";
    public static final String MOCK_PROVINCE_NAME = "Codeland";
    public static final String MOCK_STREET_ADDR = "123 Programming Lane";
    public static final String MOCK_POSTAL_CODE = "12345";
    public static final float MOCK_LATITUDE = 80.0f;
    public static final float MOCK_LONGITUDE = 80.0f;

    public static final String MOCK_LOCATION_GEO_TARGET_CONSTANT = "locations/1000";

    public static final String MOCK_LANGUAGE_CONSTANT = "languages/1000";

    public static final String MOCK_KEYWORD_TEXT = "KEYWORD_TEXT";
    public static final int MOCK_AD_GROUP_STATUS = AdGroupStatusEnum.AdGroupStatus.ENABLED_VALUE;

    public static final String MOCK_AD_GROUP_NAME = "Test AdGroup 1";

    public static final Long MOCK_AD_GROUP_CPC_BID = 30000L;

    public static final String MOCK_SITELINK_DESC_1 = "Sitelink Description 1";
    public static final String MOCK_SITELINK_DESC_2 = "Sitelink Description 2";
    public static final String MOCK_SITELINK_LINK = "http://windows.com";
    public static final String MOCK_SITELINK_START_DATE = "10/10/2022";
    public static final String MOCK_SITELINK_END_DATE = "10/10/2025";

    public static final String MOCK_CALL_ASSET_PHONE_NUMBER = "555-555-5555";
    public static final String MOCK_CALL_ASSET_COUNTRY_CODE = "US";

    public AdGroupDetails getMockAdGroupDetails() {
        AdGroupDetails adGroupDetails = new AdGroupDetails();
        adGroupDetails.setAdGroupName("Test Ad Group");
        adGroupDetails.setAdGroupId(2L);
        adGroupDetails.setType(AdGroupTypeEnum.AdGroupType.SEARCH_STANDARD_VALUE);
        adGroupDetails.setCpcBid(10 * MICRO_FACTOR);
        adGroupDetails.setCampaignResourceName(MOCK_CAMPAIGN_RESOURCE_NAME);
        adGroupDetails.setAdGroupResourceName(MOCK_AD_GROUP_RESOURCE_NAME);
        adGroupDetails.setStatus(AdGroupStatusEnum.AdGroupStatus.ENABLED_VALUE);

        return adGroupDetails;
    }

    private Campaign.NetworkSettings getMockNetworkSettings() {
        return Campaign.NetworkSettings.newBuilder()
                .setTargetContentNetwork(false)
                .setTargetSearchNetwork(true)
                .setTargetGoogleSearch(true)
                .build();
    }

    private Campaign.GeoTargetTypeSetting getMockGeoTargetTypeSettings() {
        return Campaign.GeoTargetTypeSetting.newBuilder()
                .setNegativeGeoTargetType(NegativeGeoTargetTypeEnum.NegativeGeoTargetType.PRESENCE)
                .setPositiveGeoTargetType(PositiveGeoTargetTypeEnum.PositiveGeoTargetType.PRESENCE)
                .build();
    }

    private ManualCpc getMockManualCpc() {
        return ManualCpc.newBuilder().setEnhancedCpcEnabled(true).build();
    }

    private AddressInfo getMockAddressInfo() {
        return AddressInfo.newBuilder()
                .setCityName(MOCK_CITY_NAME)
                .setProvinceName(MOCK_PROVINCE_NAME)
                .setPostalCode(MOCK_POSTAL_CODE)
                .setStreetAddress(MOCK_STREET_ADDR)
                .build();
    }

    private GeoPointInfo getMockGeoPointInfo() {
        return GeoPointInfo.newBuilder()
                .setLatitudeInMicroDegrees(Math.round(MOCK_LATITUDE * MICRO_FACTOR))
                .setLongitudeInMicroDegrees(Math.round(MOCK_LONGITUDE * MICRO_FACTOR))
                .build();
    }

    public AdGroup getMockAdGroupContainingResourceName() {
        return AdGroup.newBuilder()
                .setStatus(AdGroupStatusEnum.AdGroupStatus.PAUSED)
                .setResourceName(MOCK_AD_GROUP_RESOURCE_NAME)
                .setCpcBidMicros(MOCK_AD_GROUP_CPC_BID)
                .setName(MOCK_AD_GROUP_NAME)
                .build();
    }

    public AdGroup getMockAdGroupWithoutResourceName() {
        return AdGroup.newBuilder()
                .setCampaign(MOCK_CAMPAIGN_RESOURCE_NAME)
                .setStatus(AdGroupStatusEnum.AdGroupStatus.PAUSED)
                .setResourceName(MOCK_AD_GROUP_RESOURCE_NAME)
                .setCpcBidMicros(MOCK_AD_GROUP_CPC_BID)
                .setType(AdGroupTypeEnum.AdGroupType.SEARCH_STANDARD)
                .setName(MOCK_AD_GROUP_NAME)
                .build();
    }

    public AdGroupCriterion getMockAdGroupCriterion() {
        return AdGroupCriterion.newBuilder()
                .setKeyword(getMockKeywordInfo())
                .setCpcBidMicros(MOCK_AD_GROUP_CPC_BID)
                .setAdGroup(MOCK_AD_GROUP_RESOURCE_NAME)
                .setStatus(AdGroupCriterionStatusEnum.AdGroupCriterionStatus.ENABLED)
                .setResourceName(MOCK_AD_GROUP_CRITERION_RESOURCE_NAME)
                .build();
    }

    public Asset getMockSitelinkAsset() {
        SitelinkAsset sitelinkAsset = SitelinkAsset.newBuilder()
                .setDescription1(MOCK_SITELINK_DESC_1)
                .setDescription2(MOCK_SITELINK_DESC_2)
                .setStartDate(MOCK_SITELINK_START_DATE)
                .setEndDate(MOCK_SITELINK_END_DATE)
                .setLinkText(MOCK_SITELINK_LINK)
                .build();

        return Asset.newBuilder()
                .setSitelinkAsset(sitelinkAsset)
                .build();
    }

    public Asset getMockCallAsset() {
        CallAsset callAsset = CallAsset.newBuilder()
                .setPhoneNumber(MOCK_CALL_ASSET_PHONE_NUMBER)
                .setCountryCode(MOCK_CALL_ASSET_COUNTRY_CODE)
                .build();

        return Asset.newBuilder().setCallAsset(callAsset).build();
    }

    public List<AdGroupDetails> getValidMockAdGroupDetailsWithoutAdGroupResName() {
        List<AdGroupDetails> adGroupDetailsList = new ArrayList<>();

        AdGroupDetails adGroupDetails = new AdGroupDetails();
        adGroupDetails.setAdGroupName(MOCK_AD_GROUP_NAME);
        adGroupDetails.setCampaignResourceName(MOCK_CAMPAIGN_RESOURCE_NAME);
        adGroupDetails.setStatus(CAMPAIGN_STATUS_PAUSED);
        adGroupDetails.setType(2);
        adGroupDetails.setCpcBid(0.03);

        adGroupDetailsList.add(adGroupDetails);

        return adGroupDetailsList;
    }


    public List<AdGroupDetails> getValidMockAdGroupDetailsWithAdGroupResName() {
        List<AdGroupDetails> adGroupDetailsList = new ArrayList<>();

        AdGroupDetails adGroupDetails = new AdGroupDetails();
        adGroupDetails.setAdGroupName(MOCK_AD_GROUP_NAME);
        adGroupDetails.setAdGroupResourceName(MOCK_AD_GROUP_RESOURCE_NAME);
        adGroupDetails.setCampaignResourceName(MOCK_CAMPAIGN_RESOURCE_NAME);
        adGroupDetails.setStatus(CAMPAIGN_STATUS_PAUSED);
        adGroupDetails.setType(2);
        adGroupDetails.setCpcBid(0.03);

        adGroupDetailsList.add(adGroupDetails);

        return adGroupDetailsList;
    }


    public List<AdGroupDetails> getInvalidMockAdGroupDetailsBidTooLow() {
        List<AdGroupDetails> adGroupDetailsList = new ArrayList<>();

        AdGroupDetails adGroupDetails = new AdGroupDetails();
        adGroupDetails.setAdGroupName(MOCK_AD_GROUP_NAME);
        adGroupDetails.setCampaignResourceName(MOCK_CAMPAIGN_RESOURCE_NAME);
        adGroupDetails.setStatus(CAMPAIGN_STATUS_PAUSED);
        adGroupDetails.setType(2);
        adGroupDetails.setCpcBid(0.0);

        adGroupDetailsList.add(adGroupDetails);

        return adGroupDetailsList;
    }

    public List<AdGroupOperation> getValidMockAdGroupCreateOperationsList() {
        List<AdGroupOperation> adGroupOperationList = new ArrayList<>();

        AdGroupOperation.Builder adGroupOperationBuilder = AdGroupOperation.newBuilder();
        adGroupOperationBuilder.setCreate(getMockAdGroupWithoutResourceName());

        adGroupOperationList.add(adGroupOperationBuilder.build());

        return adGroupOperationList;
    }

    public List<AdGroupOperation> getValidMockAdGroupUpdateOperationsList() {
        List<AdGroupOperation> adGroupOperationList = new ArrayList<>();

        AdGroupOperation.Builder adGroupOperationBuilder = AdGroupOperation.newBuilder();
        adGroupOperationBuilder.setUpdate(getMockAdGroupContainingResourceName());
        adGroupOperationBuilder.setUpdateMask(FieldMasks.allSetFieldsOf(getMockAdGroupContainingResourceName()));
        adGroupOperationList.add(adGroupOperationBuilder.build());

        return adGroupOperationList;
    }

    public List<KeywordDetails> getMockKeywordDetails() {
        List<KeywordDetails> keywordDetails = new ArrayList<>();
        KeywordDetails keywordDetails1 = new KeywordDetails();
        keywordDetails1.setKeywordId(2L);
        keywordDetails1.setKeywordText("testing sucks");
        keywordDetails1.setAdGroupResourceName(MOCK_AD_GROUP_RESOURCE_NAME);
        keywordDetails1.setCpcBid(0.03);
        keywordDetails1.setStatus(2);
        keywordDetails1.setKeywordMatchType(4);

        keywordDetails.add(keywordDetails1);

        return keywordDetails;
    }

    public List<AdGroupCriterionOperation> getMockAdGroupCriterionOperationsList() {
        List<AdGroupCriterionOperation> adGroupCriterionOperations = new ArrayList<>();
        AdGroupCriterionOperation adGroupCriterionOperation = AdGroupCriterionOperation.newBuilder()
                .setCreate(getMockAdGroupCriterion())
                .build();
        adGroupCriterionOperations.add(adGroupCriterionOperation);
        return adGroupCriterionOperations;
    }

    public KeywordInfo getMockKeywordInfo() {
        return KeywordInfo.newBuilder()
                .setMatchTypeValue(KEYWORD_MATCH_TYPE_BROAD)
                .setText(MOCK_NEGATIVE_KEYWORD)
                .build();
    }
}
