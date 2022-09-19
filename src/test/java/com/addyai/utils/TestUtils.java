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

import com.google.ads.googleads.v11.common.*;
import com.google.ads.googleads.v11.enums.*;
import com.google.ads.googleads.v11.resources.Campaign;
import com.google.ads.googleads.v11.resources.CampaignBudget;
import com.google.ads.googleads.v11.resources.CampaignCriterion;

import static com.addyai.utils.misc.Constants.*;

public class TestUtils {
    public static final long MOCK_CAMPAIGN_ID = 0L;
    public static final String MOCK_CAMPAIGN_NAME = "Test Campaign 1";
    public static final String MOCK_CAMPAIGN_RESOURCE_NAME = "customers/9059845250/campaigns/18357890301";
    public static final CampaignStatusEnum.CampaignStatus MOCK_CAMPAIGN_STATUS = CampaignStatusEnum.CampaignStatus.ENABLED;
    public static final AdvertisingChannelTypeEnum.AdvertisingChannelType MOCK_CAMPAIGN_ADVERTISING_CHANNEL = AdvertisingChannelTypeEnum.AdvertisingChannelType.SEARCH;
    public static final String MOCK_CAMPAIGN_BUDGET_RESOURCE_NAME = "customers/9059845250/campaignBudgets/11599749725";
    public static final String MOCK_CAMPAIGN_START_DATE = "2000-01-01";
    public static final String MOCK_CAMPAIGN_END_DATE = "2001-01-01";

    public static final long MOCK_BUDGET_ID = 1L;
    public static final long MOCK_BUDGET_AMOUNT = 100L;
    public static final String MOCK_BUDGET_NAME = "Test Budget 1";
    public static final String MOCK_BUDGET_RESOURCE_NAME = "customers/9059845250/campaignBudgets/11599749725";

    public static final long MOCK_CRITERION_ID = 2L;
    public static final float MOCK_BID_MODIFIER = 2.0f;
    public static final String MOCK_NEGATIVE_KEYWORD = "testing sucks";
    public static final double PROXIMITY_RADIUS = 20.0;

    public static final String MOCK_CITY_NAME = "Java City";
    public static final String MOCK_PROVINCE_NAME = "Codeland";
    public static final String MOCK_STREET_ADDR = "123 Programming Lane";
    public static final String MOCK_POSTAL_CODE = "12345";
    public static final int MOCK_LATITUDE = 80;
    public static final int MOCK_LONGITUDE = 80;

    public static final String MOCK_LOCATION_GEO_TARGET_CONSTANT = "locations/1000";

    public static final String MOCK_LANGUAGE_CONSTANT = "languages/1000";

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
                .setLatitudeInMicroDegrees(MOCK_LATITUDE)
                .setLongitudeInMicroDegrees(MOCK_LONGITUDE)
                .build();
    }
}
