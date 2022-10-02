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
import com.addyai.models.campaign_criterion.*;
import com.google.ads.googleads.v11.common.*;
import com.google.ads.googleads.v11.enums.*;
import com.google.ads.googleads.v11.resources.Campaign;
import com.google.ads.googleads.v11.resources.CampaignBudget;
import com.google.ads.googleads.v11.resources.CampaignCriterion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.addyai.utils.misc.Constants.*;

public class TestUtils {
    public static final long MOCK_CAMPAIGN_ID = 0L;
    public static final String MOCK_CAMPAIGN_NAME = "Test Campaign 1";
    public static final String MOCK_CAMPAIGN_RESOURCE_NAME = "customers/9059845250/campaigns/18357890301";
    public static final String MOCK_AD_GROUP_RESOURCE_NAME = "customers/9059845250/adgroups/39203940293";
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

    public Campaign getMockCampaign() {
        Campaign.Builder campaignBuilder = Campaign.newBuilder();
        return campaignBuilder
                .setId(MOCK_CAMPAIGN_ID)
                .setName(MOCK_CAMPAIGN_NAME)
                .setResourceName(MOCK_CAMPAIGN_RESOURCE_NAME)
                .setStatus(MOCK_CAMPAIGN_STATUS)
                .setCampaignBudget(MOCK_CAMPAIGN_BUDGET_RESOURCE_NAME)
                .setAdvertisingChannelType(MOCK_CAMPAIGN_ADVERTISING_CHANNEL)
                .setStartDate(MOCK_CAMPAIGN_START_DATE)
                .setEndDate(MOCK_CAMPAIGN_END_DATE)
                .setGeoTargetTypeSetting(getMockGeoTargetTypeSettings())
                .setNetworkSettings(getMockNetworkSettings())
                .setManualCpc(getMockManualCpc())
                .build();
    }

    public CampaignBudget getMockCampaignBudget() {
        return CampaignBudget.newBuilder()
                .setId(MOCK_BUDGET_ID)
                .setName(MOCK_BUDGET_NAME)
                .setResourceName(MOCK_BUDGET_RESOURCE_NAME)
                .setStatus(BudgetStatusEnum.BudgetStatus.ENABLED)
                .setAmountMicros(MOCK_BUDGET_AMOUNT * MICRO_FACTOR)
                .setExplicitlyShared(false)
                .setDeliveryMethod(BudgetDeliveryMethodEnum.BudgetDeliveryMethod.STANDARD)
                .build();
    }

    public CampaignCriterion getMockCampaignCriterion(CriterionTypeEnum.CriterionType criterionType) {
        CampaignCriterion.Builder campaignCriterion = CampaignCriterion.newBuilder();
        campaignCriterion.setCriterionId(MOCK_CRITERION_ID);
        campaignCriterion.setCampaign(getMockCampaign().getResourceName());
        campaignCriterion.setBidModifier(MOCK_BID_MODIFIER);
        campaignCriterion.setStatus(CampaignCriterionStatusEnum.CampaignCriterionStatus.ENABLED);
        campaignCriterion.setResourceName(MOCK_CRITERION_RESOURCE_NAME);

        switch (criterionType) {
            case AD_SCHEDULE:
                campaignCriterion.setType(CriterionTypeEnum.CriterionType.AD_SCHEDULE);
                campaignCriterion.setAdSchedule(getMockAdScheduleInfo());
                return campaignCriterion.build();
            case KEYWORD:
                campaignCriterion.setNegative(true);
                campaignCriterion.setType(CriterionTypeEnum.CriterionType.KEYWORD);
                campaignCriterion.setKeyword(getMockKeywordInfo());
                return campaignCriterion.build();
            case PROXIMITY:
                campaignCriterion.setType(CriterionTypeEnum.CriterionType.PROXIMITY);
                campaignCriterion.setProximity(getMockProximityInfo());
                return campaignCriterion.build();
            case LOCATION:
                campaignCriterion.setType(CriterionTypeEnum.CriterionType.LOCATION);
                campaignCriterion.setLocation(getMockLocationInfo());
                return campaignCriterion.build();
            case LANGUAGE:
                campaignCriterion.setType(CriterionTypeEnum.CriterionType.LANGUAGE);
                campaignCriterion.setLanguage(getMockLanguageInfo());
                return campaignCriterion.build();
            case DEVICE:
                campaignCriterion.setType(CriterionTypeEnum.CriterionType.DEVICE);
                campaignCriterion.setBidModifier(MOCK_BID_MODIFIER);
                campaignCriterion.setDevice(getMockDeviceInfo());
                return campaignCriterion.build();
            default:
                return null;
        }
    }

    public AdScheduleInfo getMockAdScheduleInfo() {
        return AdScheduleInfo.newBuilder()
                .setDayOfWeekValue(DAY_OF_WEEK_MONDAY)
                .setStartHour(0)
                .setEndHour(23)
                .setStartMinute(MinuteOfHourEnum.MinuteOfHour.ZERO)
                .setEndMinute(MinuteOfHourEnum.MinuteOfHour.FORTY_FIVE)
                .build();
    }

    public KeywordInfo getMockKeywordInfo() {
        return KeywordInfo.newBuilder()
                .setMatchTypeValue(KEYWORD_MATCH_TYPE_BROAD)
                .setText(MOCK_NEGATIVE_KEYWORD)
                .build();
    }

    public ProximityInfo getMockProximityInfo() {
        return ProximityInfo.newBuilder()
                .setAddress(getMockAddressInfo())
                .setGeoPoint(getMockGeoPointInfo())
                .setRadiusUnitsValue(RADIUS_UNITS_MILES)
                .setRadius(PROXIMITY_RADIUS)
                .build();
    }

    public LocationInfo getMockLocationInfo() {
        return LocationInfo.newBuilder()
                .setGeoTargetConstant(MOCK_LOCATION_GEO_TARGET_CONSTANT)
                .build();
    }

    public LanguageInfo getMockLanguageInfo() {
        return LanguageInfo.newBuilder()
                .setLanguageConstant(MOCK_LANGUAGE_CONSTANT)
                .build();
    }

    public DeviceInfo getMockDeviceInfo() {
        return DeviceInfo.newBuilder()
                .setType(DeviceEnum.Device.MOBILE)
                .build();
    }

    public List<CampaignDetails> getMockCampaignDetailsList() {
        List<CampaignDetails> mockCampaignDetailsList = new ArrayList<>();
        mockCampaignDetailsList.add(getMockCampaignDetails());
        return mockCampaignDetailsList;
    }

    public CampaignDetails getMockCampaignDetails() {
        CampaignDetails mockCampaignDetails = new CampaignDetails();
        mockCampaignDetails.setCampaignName(MOCK_CAMPAIGN_NAME);
        mockCampaignDetails.setStatus(CampaignStatusEnum.CampaignStatus.ENABLED_VALUE);
        mockCampaignDetails.setNegativeGeoTargetType(NegativeGeoTargetTypeEnum.NegativeGeoTargetType.PRESENCE_VALUE);
        mockCampaignDetails.setPositiveGeoTargetType(PositiveGeoTargetTypeEnum.PositiveGeoTargetType.PRESENCE_VALUE);
        mockCampaignDetails.setAdvertisingChannelType(AdvertisingChannelTypeEnum.AdvertisingChannelType.SEARCH_VALUE);
        mockCampaignDetails.setTargetingContentNetwork(false);
        mockCampaignDetails.setTargetingSearchNetwork(true);
        mockCampaignDetails.setTargetingGoogleSearchNetwork(true);
        mockCampaignDetails.setEndDate(MOCK_CAMPAIGN_END_DATE);
        mockCampaignDetails.setStartDate((MOCK_CAMPAIGN_START_DATE));
        mockCampaignDetails.setCampaignResourceName(MOCK_CAMPAIGN_RESOURCE_NAME);
        mockCampaignDetails.setBudgetResourceName(MOCK_BUDGET_RESOURCE_NAME);
        mockCampaignDetails.setEnhancedCpcEnabled(true);
        mockCampaignDetails.setCampaignCriteriaList(new ArrayList<>()); //TODO:
        mockCampaignDetails.setBudgetDetails(getMockBudgetDetails());

        return mockCampaignDetails;
    }

    public List<BudgetDetails> getMockBudgetDetailsList() {
        List<BudgetDetails> mockBudgetDetailsList = new ArrayList<>();
        mockBudgetDetailsList.add(getMockBudgetDetails());
        return mockBudgetDetailsList;
    }

    public BudgetDetails getMockBudgetDetails() {
        BudgetDetails mockBudgetDetails = new BudgetDetails();
        mockBudgetDetails.setName(MOCK_BUDGET_NAME);
        mockBudgetDetails.setDailyBudgetAmount(100);
        mockBudgetDetails.setResourceName(MOCK_BUDGET_RESOURCE_NAME);
        mockBudgetDetails.setShared(false);
        mockBudgetDetails.setStatus(BUDGET_STATUS_ENABLED);

        return mockBudgetDetails;
    }

    public Map<String, List<CriterionDetails>> getMockCriterionMapping() {
        Map<String, List<CriterionDetails>> mapping = new HashMap<>();
        List<CriterionDetails> criterionDetailsList = new ArrayList<>();

        AdScheduleDetails adScheduleDetails = new AdScheduleDetails();
        adScheduleDetails.setCriterionType(CRITERION_TYPE_AD_SCHEDULE);
        adScheduleDetails.setDayOfWeek(DAY_OF_WEEK_MONDAY);
        adScheduleDetails.setStartHour(0);
        adScheduleDetails.setEndHour(23);
        adScheduleDetails.setStartMinute(MinuteOfHourEnum.MinuteOfHour.ZERO_VALUE);
        adScheduleDetails.setEndMinute(MinuteOfHourEnum.MinuteOfHour.FORTY_FIVE_VALUE);
        adScheduleDetails.setStatus(CampaignCriterionStatusEnum.CampaignCriterionStatus.ENABLED_VALUE);
        adScheduleDetails.setBidModifier(MOCK_BID_MODIFIER);
        adScheduleDetails.setCriterionResourceName(MOCK_CRITERION_RESOURCE_NAME);
        adScheduleDetails.setCampaignResourceName(MOCK_CAMPAIGN_RESOURCE_NAME);
        criterionDetailsList.add(adScheduleDetails);

        AdScheduleDetails adScheduleDetails2 = new AdScheduleDetails();
        adScheduleDetails.setCriterionType(CRITERION_TYPE_AD_SCHEDULE);
        adScheduleDetails2.setDayOfWeek(DAY_OF_WEEK_TUESDAY);
        adScheduleDetails2.setStartHour(0);
        adScheduleDetails2.setEndHour(23);
        adScheduleDetails2.setStartMinute(MinuteOfHourEnum.MinuteOfHour.ZERO_VALUE);
        adScheduleDetails2.setEndMinute(MinuteOfHourEnum.MinuteOfHour.FORTY_FIVE_VALUE);
        adScheduleDetails2.setStatus(CampaignCriterionStatusEnum.CampaignCriterionStatus.ENABLED_VALUE);
        criterionDetailsList.add(adScheduleDetails2);

        NegativeKeywordDetails negativeKeywordDetails = new NegativeKeywordDetails();
        negativeKeywordDetails.setCriterionType(CRITERION_TYPE_KEYWORD);
        negativeKeywordDetails.setKeywordText(MOCK_KEYWORD_TEXT);
        negativeKeywordDetails.setKeywordMatchType(KEYWORD_MATCH_TYPE_BROAD);
        criterionDetailsList.add(negativeKeywordDetails);

        ProximityDetails proximityDetails1 = new ProximityDetails();
        proximityDetails1.setCriterionType(CRITERION_TYPE_PROXIMITY);
        proximityDetails1.setBidModifier(MOCK_BID_MODIFIER);
        proximityDetails1.setCountryCode("US");
        proximityDetails1.setRadius(10);
        proximityDetails1.setRadiusUnits(RADIUS_UNITS_MILES);
        proximityDetails1.setStreetAddress(MOCK_STREET_ADDR);
        proximityDetails1.setProvinceName(MOCK_PROVINCE_NAME);
        proximityDetails1.setPostalCode(MOCK_POSTAL_CODE);
        proximityDetails1.setCityName(MOCK_CITY_NAME);
        criterionDetailsList.add(proximityDetails1);

        ProximityDetails proximityDetails2 = new ProximityDetails();
        proximityDetails2.setCriterionType(CRITERION_TYPE_PROXIMITY);
        proximityDetails2.setCountryCode("US");
        proximityDetails2.setRadius(10);
        proximityDetails2.setRadiusUnits(RADIUS_UNITS_MILES);
        proximityDetails2.setLatitude(MOCK_LATITUDE);
        proximityDetails2.setLongitude(MOCK_LONGITUDE);
        criterionDetailsList.add(proximityDetails2);

        DeviceDetails deviceDetails = new DeviceDetails();
        deviceDetails.setDeviceType(DEVICE_TYPE_MOBILE);
        deviceDetails.setBidModifier(MOCK_BID_MODIFIER);
        criterionDetailsList.add(deviceDetails);

        LanguageDetails languageDetails = new LanguageDetails();
        languageDetails.setCriterionType(CRITERION_TYPE_LANGUAGE);
        languageDetails.setLanguageCode(MOCK_LANGUAGE_CONSTANT);
        criterionDetailsList.add(languageDetails);

        LocationDetails locationDetails = new LocationDetails();
        locationDetails.setCriterionType(CRITERION_TYPE_LOCATION);
        locationDetails.setNegative(true);
        locationDetails.setGeoTargetingConstant(MOCK_LOCATION_GEO_TARGET_CONSTANT);
        locationDetails.setLocale("en");
        locationDetails.setLocation(MOCK_CITY_NAME);
        criterionDetailsList.add(locationDetails);

        LocationDetails locationDetails2 = new LocationDetails();
        locationDetails2.setCriterionType(CRITERION_TYPE_LOCATION);
        locationDetails2.setNegative(false);
        locationDetails2.setGeoTargetingConstant(MOCK_LOCATION_GEO_TARGET_CONSTANT);
        locationDetails2.setLocale("en");
        locationDetails2.setLocation(MOCK_CITY_NAME);
        criterionDetailsList.add(locationDetails2);

        mapping.put(MOCK_CAMPAIGN_RESOURCE_NAME, criterionDetailsList);
        return mapping;
    }

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
}
