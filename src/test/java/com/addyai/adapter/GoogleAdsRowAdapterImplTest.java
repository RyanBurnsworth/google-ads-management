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

package com.addyai.adapter;

import com.addyai.adapter.impl.GoogleAdsRowAdapterImpl;
import com.addyai.models.BudgetDetails;
import com.addyai.models.CampaignDetails;
import com.addyai.models.campaign_criterion.*;
import com.addyai.utils.TestUtils;
import com.google.ads.googleads.v11.enums.*;
import com.google.ads.googleads.v11.services.GoogleAdsRow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.addyai.utils.TestUtils.*;
import static com.addyai.utils.misc.Constants.*;
import static com.google.ads.googleads.v11.enums.PositiveGeoTargetTypeEnum.PositiveGeoTargetType.PRESENCE;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = GoogleAdsRowAdapterImpl.class)
public class GoogleAdsRowAdapterImplTest {
    @Autowired
    private GoogleAdsRowAdapter googleAdsRowAdapter;

    private TestUtils testUtils;

    @BeforeEach
    void setup() {
        testUtils = new TestUtils();
    }

    @Test
    void testGetCampaignDetails() {
        GoogleAdsRow googleAdsRow = GoogleAdsRow.newBuilder()
                .setCampaign(testUtils.getMockCampaign()).build();

        CampaignDetails campaignDetails = googleAdsRowAdapter.getCampaignDetails(googleAdsRow);

        assertEquals(campaignDetails.getCampaignId(), MOCK_CAMPAIGN_ID);
        assertEquals(campaignDetails.getCampaignName(), MOCK_CAMPAIGN_NAME);
        assertEquals(campaignDetails.getCampaignResourceName(), MOCK_CAMPAIGN_RESOURCE_NAME);
        assertEquals(campaignDetails.getBudgetResourceName(), MOCK_CAMPAIGN_BUDGET_RESOURCE_NAME);
        assertEquals(campaignDetails.getStatus(), CampaignStatusEnum.CampaignStatus.ENABLED_VALUE);
        assertEquals(campaignDetails.getAdvertisingChannelType(), ADVERTISING_CHANNEL_TYPE_SEARCH);
        assertEquals(campaignDetails.getStartDate(), MOCK_CAMPAIGN_START_DATE);
        assertEquals(campaignDetails.getEndDate(), MOCK_CAMPAIGN_END_DATE);
        assertEquals(campaignDetails.getPositiveGeoTargetType(), PRESENCE.getNumber());
        assertEquals(campaignDetails.getNegativeGeoTargetType(), NEGATIVE_GEO_TARGET_TYPE_PRESENCE);
        assertTrue(campaignDetails.isEnhancedCpcEnabled());
        assertTrue(campaignDetails.isTargetingSearchNetwork());
        assertTrue(campaignDetails.isTargetingGoogleSearchNetwork());
        assertFalse(campaignDetails.isTargetingContentNetwork());
    }

    @Test
    void testGetBudgetDetails() {
        GoogleAdsRow googleAdsRow = GoogleAdsRow.newBuilder()
                .setCampaignBudget(testUtils.getMockCampaignBudget())
                .build();

        BudgetDetails budgetDetails = googleAdsRowAdapter.getBudgetDetails(googleAdsRow);
        assertEquals(budgetDetails.getBudgetId(), MOCK_BUDGET_ID);
        assertEquals(budgetDetails.getName(), MOCK_BUDGET_NAME);
        assertEquals(budgetDetails.getResourceName(), MOCK_BUDGET_RESOURCE_NAME);
        assertEquals(budgetDetails.getDailyBudgetAmount(), MOCK_BUDGET_AMOUNT);
        assertEquals(budgetDetails.getDeliveryMethod(), BudgetDeliveryMethodEnum.BudgetDeliveryMethod.STANDARD_VALUE);
        assertEquals(budgetDetails.getStatus(), BudgetStatusEnum.BudgetStatus.ENABLED_VALUE);
        assertFalse(budgetDetails.isShared());
    }

    @Test
    void testGetAdScheduleDetails() {
        GoogleAdsRow googleAdsRow = GoogleAdsRow.newBuilder()
                .setCampaignCriterion(testUtils.getMockCampaignCriterion(CriterionTypeEnum.CriterionType.AD_SCHEDULE))
                .build();

        AdScheduleDetails adScheduleDetails = googleAdsRowAdapter.getAdScheduleDetails(googleAdsRow);
        assertEquals(adScheduleDetails.getCriterionType(), CRITERION_TYPE_AD_SCHEDULE);
        assertEquals(adScheduleDetails.getCampaignCriterionId(), MOCK_CRITERION_ID);
        assertEquals(adScheduleDetails.getCampaignResourceName(), MOCK_CAMPAIGN_RESOURCE_NAME);
        assertEquals(adScheduleDetails.getStatus(), CRITERION_STATUS_ENABLED);
        assertEquals(adScheduleDetails.getBidModifier(), MOCK_BID_MODIFIER);
        assertEquals(adScheduleDetails.getDayOfWeek(), DAY_OF_WEEK_MONDAY);
        assertEquals(adScheduleDetails.getStartHour(), 0);
        assertEquals(adScheduleDetails.getEndHour(), 23);
        assertEquals(adScheduleDetails.getStartMinute(), MINUTE_OF_HOUR_ZERO);
        assertEquals(adScheduleDetails.getEndMinute(), MINUTE_OF_HOUR_FORTY_FIVE);
    }

    @Test
    void testGetNegativeKeywordDetails() {
        GoogleAdsRow googleAdsRow = GoogleAdsRow.newBuilder()
                .setCampaignCriterion(testUtils.getMockCampaignCriterion(CriterionTypeEnum.CriterionType.KEYWORD))
                .build();

        NegativeKeywordDetails negativeKeywordDetails = googleAdsRowAdapter.getKeywordDetails(googleAdsRow);
        assertEquals(negativeKeywordDetails.getCriterionType(), CRITERION_TYPE_KEYWORD);
        assertEquals(negativeKeywordDetails.getCampaignCriterionId(), MOCK_CRITERION_ID);
        assertEquals(negativeKeywordDetails.getCampaignResourceName(), MOCK_CAMPAIGN_RESOURCE_NAME);
        assertEquals(negativeKeywordDetails.getStatus(), CRITERION_STATUS_ENABLED);
        assertEquals(negativeKeywordDetails.getKeywordMatchType(), KEYWORD_MATCH_TYPE_BROAD);
        assertEquals(negativeKeywordDetails.getKeywordText(), MOCK_NEGATIVE_KEYWORD);
        assertTrue(negativeKeywordDetails.isNegative());
    }

    @Test
    void testGetProximityDetails() {
        GoogleAdsRow googleAdsRow = GoogleAdsRow.newBuilder()
                .setCampaignCriterion(testUtils.getMockCampaignCriterion(CriterionTypeEnum.CriterionType.PROXIMITY))
                .build();

        ProximityDetails proximityDetails = googleAdsRowAdapter.getProximityDetails(googleAdsRow);
        assertEquals(proximityDetails.getCriterionType(), CRITERION_TYPE_PROXIMITY);
        assertEquals(proximityDetails.getCampaignCriterionId(), MOCK_CRITERION_ID);
        assertEquals(proximityDetails.getCampaignResourceName(), MOCK_CAMPAIGN_RESOURCE_NAME);
        assertEquals(proximityDetails.getStatus(), CRITERION_STATUS_ENABLED);

        assertEquals(proximityDetails.getCityName(), MOCK_CITY_NAME);
        assertEquals(proximityDetails.getStreetAddress(), MOCK_STREET_ADDR);
        assertEquals(proximityDetails.getPostalCode(), MOCK_POSTAL_CODE);
        assertEquals(proximityDetails.getProvinceName(), MOCK_PROVINCE_NAME);
        assertEquals(proximityDetails.getRadius(), PROXIMITY_RADIUS);
        assertEquals(proximityDetails.getRadiusUnits(), RADIUS_UNITS_MILES);
        assertEquals(MOCK_LATITUDE, proximityDetails.getMicroLatitude());
        assertEquals(proximityDetails.getMicroLongitude(), MOCK_LONGITUDE);
    }

    @Test
    void testGetLocationDetails() {
        GoogleAdsRow googleAdsRow = GoogleAdsRow.newBuilder()
                .setCampaignCriterion(testUtils.getMockCampaignCriterion(CriterionTypeEnum.CriterionType.LOCATION))
                .build();

        LocationDetails locationDetails = googleAdsRowAdapter.getLocationDetails(googleAdsRow);
        assertEquals(locationDetails.getCriterionType(), CRITERION_TYPE_LOCATION);
        assertEquals(locationDetails.getCampaignCriterionId(), MOCK_CRITERION_ID);
        assertEquals(locationDetails.getCampaignResourceName(), MOCK_CAMPAIGN_RESOURCE_NAME);
        assertEquals(locationDetails.getStatus(), CRITERION_STATUS_ENABLED);

        assertEquals(locationDetails.getGeoTargetingConstant(), MOCK_LOCATION_GEO_TARGET_CONSTANT);
    }

    @Test
    void testGetLanguageDetails() {
        GoogleAdsRow googleAdsRow = GoogleAdsRow.newBuilder()
                .setCampaignCriterion(testUtils.getMockCampaignCriterion(CriterionTypeEnum.CriterionType.LANGUAGE))
                .build();

        LanguageDetails languageDetails = googleAdsRowAdapter.getLanguageDetails(googleAdsRow);
        assertEquals(languageDetails.getCriterionType(), CRITERION_TYPE_LANGUAGE);
        assertEquals(languageDetails.getCampaignCriterionId(), MOCK_CRITERION_ID);
        assertEquals(languageDetails.getCampaignResourceName(), MOCK_CAMPAIGN_RESOURCE_NAME);
        assertEquals(languageDetails.getStatus(), CRITERION_STATUS_ENABLED);

        assertEquals(languageDetails.getLanguageCode(), MOCK_LANGUAGE_CONSTANT);
    }

    @Test
    void testGetDeviceDetails() {
        GoogleAdsRow googleAdsRow = GoogleAdsRow.newBuilder()
                .setCampaignCriterion(testUtils.getMockCampaignCriterion(CriterionTypeEnum.CriterionType.DEVICE))
                .build();

        DeviceDetails deviceDetails = googleAdsRowAdapter.getDeviceDetails(googleAdsRow);
        assertEquals(deviceDetails.getCriterionType(), CRITERION_TYPE_DEVICE);
        assertEquals(deviceDetails.getCampaignCriterionId(), MOCK_CRITERION_ID);
        assertEquals(deviceDetails.getCampaignResourceName(), MOCK_CAMPAIGN_RESOURCE_NAME);
        assertEquals(deviceDetails.getStatus(), CRITERION_STATUS_ENABLED);
        assertEquals(deviceDetails.getBidModifier(), MOCK_BID_MODIFIER);
        assertEquals(deviceDetails.getDeviceType(), DeviceEnum.Device.MOBILE.getNumber());
    }
}
