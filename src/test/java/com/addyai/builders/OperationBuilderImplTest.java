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

package com.addyai.builders;

import com.addyai.builder.OperationBuilder;
import com.addyai.builder.impl.OperationBuilderImpl;
import com.addyai.enums.OperationType;
import com.addyai.utils.TestUtils;
import com.google.ads.googleads.v12.enums.CampaignCriterionStatusEnum;
import com.google.ads.googleads.v12.enums.MinuteOfHourEnum;
import com.google.ads.googleads.v12.enums.NegativeGeoTargetTypeEnum;
import com.google.ads.googleads.v12.resources.Campaign;
import com.google.ads.googleads.v12.resources.CampaignBudget;
import com.google.ads.googleads.v12.services.CampaignBudgetOperation;
import com.google.ads.googleads.v12.services.CampaignCriterionOperation;
import com.google.ads.googleads.v12.services.CampaignOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.addyai.utils.TestUtils.*;
import static com.addyai.utils.misc.Constants.*;
import static com.google.ads.googleads.v12.enums.PositiveGeoTargetTypeEnum.PositiveGeoTargetType.PRESENCE;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = OperationBuilderImpl.class)
public class OperationBuilderImplTest {
    @Autowired
    private OperationBuilder operationBuilder;
    private TestUtils testUtils;

    @BeforeEach
    void setup() {
        testUtils = new TestUtils();
    }

    @Test
    void testBuildCampaignOperationListForCreateOperation() {
        List<CampaignOperation> campaignOperationList =
                operationBuilder.buildCampaignOperationList(testUtils.getMockCampaignDetailsList(), OperationType.CREATE);

        assertEquals(1, campaignOperationList.size());
        assertTrue(campaignOperationList.get(0).hasCreate());

        Campaign actualCampaign = campaignOperationList.get(0).getCreate();

        assertEquals(MOCK_CAMPAIGN_NAME, actualCampaign.getName());
        assertEquals(ADVERTISING_CHANNEL_TYPE_SEARCH, actualCampaign.getAdvertisingChannelTypeValue()); // ONLY IN CREATE
        assertEquals(MOCK_CAMPAIGN_START_DATE, actualCampaign.getStartDate());
        assertEquals(MOCK_CAMPAIGN_END_DATE, actualCampaign.getEndDate());
        assertEquals(PRESENCE, actualCampaign.getGeoTargetTypeSetting().getPositiveGeoTargetType());
        assertEquals(NegativeGeoTargetTypeEnum.NegativeGeoTargetType.PRESENCE, actualCampaign.getGeoTargetTypeSetting().getNegativeGeoTargetType());
        assertTrue(actualCampaign.getManualCpc().getEnhancedCpcEnabled());
        assertTrue(actualCampaign.getNetworkSettings().getTargetSearchNetwork());
        assertTrue(actualCampaign.getNetworkSettings().getTargetGoogleSearch());
        assertFalse(actualCampaign.getNetworkSettings().getTargetContentNetwork());
    }

    @Test
    void testBuildCampaignOperationListForUpdateOperation() {
        List<CampaignOperation> campaignOperationList =
                operationBuilder.buildCampaignOperationList(testUtils.getMockCampaignDetailsList(), OperationType.UPDATE);

        assertEquals(1, campaignOperationList.size());
        assertTrue(campaignOperationList.get(0).hasUpdate());

        Campaign actualCampaign = campaignOperationList.get(0).getUpdate();

        assertEquals(MOCK_CAMPAIGN_NAME, actualCampaign.getName());
        assertEquals(MOCK_CAMPAIGN_RESOURCE_NAME, actualCampaign.getResourceName()); // ONLY IN UPDATE
        assertEquals(MOCK_CAMPAIGN_START_DATE, actualCampaign.getStartDate());
        assertEquals(MOCK_CAMPAIGN_END_DATE, actualCampaign.getEndDate());
        assertEquals(PRESENCE, actualCampaign.getGeoTargetTypeSetting().getPositiveGeoTargetType());
        assertEquals(NegativeGeoTargetTypeEnum.NegativeGeoTargetType.PRESENCE, actualCampaign.getGeoTargetTypeSetting().getNegativeGeoTargetType());
        assertTrue(actualCampaign.getManualCpc().getEnhancedCpcEnabled());
        assertTrue(actualCampaign.getNetworkSettings().getTargetSearchNetwork());
        assertTrue(actualCampaign.getNetworkSettings().getTargetGoogleSearch());
        assertFalse(actualCampaign.getNetworkSettings().getTargetContentNetwork());
    }

    @Test
    void testBuildCampaignOperationListForRemoveOperation() {
        List<CampaignOperation> campaignOperationList =
                operationBuilder.buildCampaignOperationList(testUtils.getMockCampaignDetailsList(), OperationType.REMOVE);

        assertEquals(1, campaignOperationList.size());
        assertTrue(campaignOperationList.get(0).hasRemove());

        String resourceName = campaignOperationList.get(0).getRemove();

        assertEquals(resourceName, MOCK_CAMPAIGN_RESOURCE_NAME);
    }

    @Test
    void testBuildCampaignBudgetOperationListForCreateOperation() {
        List<CampaignBudgetOperation> campaignBudgetOperationList =
                operationBuilder.buildCampaignBudgetOperationList(testUtils.getMockBudgetDetailsList(), OperationType.CREATE);

        assertEquals(1, campaignBudgetOperationList.size());

        CampaignBudget budget = campaignBudgetOperationList.get(0).getCreate();
        assertEquals(MOCK_BUDGET_NAME, budget.getName());
        assertEquals(BUDGET_STATUS_ENABLED, budget.getStatusValue());
        assertFalse(budget.getExplicitlyShared());
        assertEquals(100000000, budget.getAmountMicros());
    }

    @Test
    void testBuildCampaignBudgetOperationListForUpdateOperation() {
        List<CampaignBudgetOperation> campaignBudgetOperationList =
                operationBuilder.buildCampaignBudgetOperationList(testUtils.getMockBudgetDetailsList(), OperationType.UPDATE);

        assertEquals(1, campaignBudgetOperationList.size());

        CampaignBudget budget = campaignBudgetOperationList.get(0).getUpdate();
        assertEquals(MOCK_BUDGET_RESOURCE_NAME, budget.getResourceName());
        assertEquals(BUDGET_STATUS_ENABLED, budget.getStatusValue());
        assertEquals(100000000, budget.getAmountMicros());
    }

    @Test
    void testBuildCampaignBudgetOperationListForRemoveOperation() {
        List<CampaignBudgetOperation> campaignBudgetOperationList =
                operationBuilder.buildCampaignBudgetOperationList(testUtils.getMockBudgetDetailsList(), OperationType.REMOVE);

        assertEquals(1, campaignBudgetOperationList.size());
        assertTrue(campaignBudgetOperationList.get(0).hasRemove());

        String resourceName = campaignBudgetOperationList.get(0).getRemove();

        assertEquals(resourceName, MOCK_BUDGET_RESOURCE_NAME);
    }

    @Test
    void testBuildCampaignCriterionOperationListForCreateOperation() {
        List<CampaignCriterionOperation> campaignCriterionOperationList =
                operationBuilder.buildCampaignCriterionOperationList(testUtils.getMockCriterionMapping(),
                        OperationType.CREATE);

        assertEquals(9, campaignCriterionOperationList.size());

        assertEquals(DAY_OF_WEEK_MONDAY,
                campaignCriterionOperationList.get(0).getCreate().getAdSchedule().getDayOfWeekValue());
        assertEquals(CampaignCriterionStatusEnum.CampaignCriterionStatus.ENABLED_VALUE,
                campaignCriterionOperationList.get(0).getCreate().getStatusValue());
        assertEquals(0,
                campaignCriterionOperationList.get(0).getCreate().getAdSchedule().getStartHour());
        assertEquals(23,
                campaignCriterionOperationList.get(0).getCreate().getAdSchedule().getEndHour());
        assertEquals(MinuteOfHourEnum.MinuteOfHour.ZERO,
                campaignCriterionOperationList.get(0).getCreate().getAdSchedule().getStartMinute());
        assertEquals(MinuteOfHourEnum.MinuteOfHour.FORTY_FIVE,
                campaignCriterionOperationList.get(0).getCreate().getAdSchedule().getEndMinute());
        assertEquals(MOCK_BID_MODIFIER, campaignCriterionOperationList.get(0).getCreate().getBidModifier());
        assertFalse(campaignCriterionOperationList.get(0).getCreate().getNegative());

        assertEquals(DAY_OF_WEEK_TUESDAY,
                campaignCriterionOperationList.get(1).getCreate().getAdSchedule().getDayOfWeekValue());
        assertEquals(CampaignCriterionStatusEnum.CampaignCriterionStatus.ENABLED_VALUE,
                campaignCriterionOperationList.get(1).getCreate().getStatusValue());
        assertEquals(0,
                campaignCriterionOperationList.get(1).getCreate().getAdSchedule().getStartHour());
        assertEquals(23,
                campaignCriterionOperationList.get(1).getCreate().getAdSchedule().getEndHour());
        assertEquals(MinuteOfHourEnum.MinuteOfHour.ZERO,
                campaignCriterionOperationList.get(1).getCreate().getAdSchedule().getStartMinute());
        assertEquals(MinuteOfHourEnum.MinuteOfHour.FORTY_FIVE,
                campaignCriterionOperationList.get(1).getCreate().getAdSchedule().getEndMinute());
        assertFalse(campaignCriterionOperationList.get(1).getCreate().hasBidModifier());
        assertFalse(campaignCriterionOperationList.get(1).getCreate().getNegative());

        assertEquals(MOCK_KEYWORD_TEXT, campaignCriterionOperationList.get(2).getCreate().getKeyword().getText());
        assertEquals(CampaignCriterionStatusEnum.CampaignCriterionStatus.ENABLED_VALUE,
                campaignCriterionOperationList.get(2).getCreate().getStatusValue());
        assertEquals(KEYWORD_MATCH_TYPE_BROAD, campaignCriterionOperationList.get(2).getCreate().getKeyword().getMatchTypeValue());
        assertTrue(campaignCriterionOperationList.get(2).getCreate().getNegative());

        assertEquals(MOCK_CITY_NAME,
                campaignCriterionOperationList.get(3).getCreate().getProximity().getAddress().getCityName());
        assertEquals(MOCK_STREET_ADDR,
                campaignCriterionOperationList.get(3).getCreate().getProximity().getAddress().getStreetAddress());
        assertEquals(MOCK_POSTAL_CODE,
                campaignCriterionOperationList.get(3).getCreate().getProximity().getAddress().getPostalCode());
        assertEquals(10,
                campaignCriterionOperationList.get(3).getCreate().getProximity().getRadius());
        assertEquals(RADIUS_UNITS_MILES,
                campaignCriterionOperationList.get(3).getCreate().getProximity().getRadiusUnitsValue());
        assertEquals(MOCK_BID_MODIFIER,
                campaignCriterionOperationList.get(3).getCreate().getBidModifier());
        assertEquals(CampaignCriterionStatusEnum.CampaignCriterionStatus.ENABLED_VALUE,
                campaignCriterionOperationList.get(3).getCreate().getStatusValue());
        assertFalse(campaignCriterionOperationList.get(3).getCreate().getNegative());


        assertEquals(MOCK_LONGITUDE * MICRO_FACTOR,
                campaignCriterionOperationList.get(4).getCreate().getProximity().getGeoPoint().getLongitudeInMicroDegrees());
        assertEquals(MOCK_LATITUDE * MICRO_FACTOR,
                campaignCriterionOperationList.get(4).getCreate().getProximity().getGeoPoint().getLatitudeInMicroDegrees());
        assertEquals(10,
                campaignCriterionOperationList.get(4).getCreate().getProximity().getRadius());
        assertEquals(RADIUS_UNITS_MILES,
                campaignCriterionOperationList.get(4).getCreate().getProximity().getRadiusUnitsValue());
        assertEquals(CampaignCriterionStatusEnum.CampaignCriterionStatus.ENABLED_VALUE,
                campaignCriterionOperationList.get(4).getCreate().getStatusValue());
        assertFalse(campaignCriterionOperationList.get(4).getCreate().getNegative());
        assertFalse(campaignCriterionOperationList.get(4).getCreate().hasBidModifier());

        assertEquals(DEVICE_TYPE_MOBILE, campaignCriterionOperationList.get(5).getCreate().getDevice().getTypeValue());
        assertEquals(MOCK_BID_MODIFIER, campaignCriterionOperationList.get(5).getCreate().getBidModifier());
        assertEquals(CampaignCriterionStatusEnum.CampaignCriterionStatus.ENABLED_VALUE,
                campaignCriterionOperationList.get(5).getCreate().getStatusValue());
        assertFalse(campaignCriterionOperationList.get(5).getCreate().getNegative());

        assertEquals(MOCK_LANGUAGE_CONSTANT,
                campaignCriterionOperationList.get(6).getCreate().getLanguage().getLanguageConstant());
        assertEquals(CampaignCriterionStatusEnum.CampaignCriterionStatus.ENABLED_VALUE,
                campaignCriterionOperationList.get(6).getCreate().getStatusValue());
        assertFalse(campaignCriterionOperationList.get(6).getCreate().getNegative());

        assertEquals(MOCK_LOCATION_GEO_TARGET_CONSTANT,
                campaignCriterionOperationList.get(7).getCreate().getLocation().getGeoTargetConstant());
        assertTrue(campaignCriterionOperationList.get(7).getCreate().getNegative());
        assertEquals(CampaignCriterionStatusEnum.CampaignCriterionStatus.ENABLED_VALUE,
                campaignCriterionOperationList.get(7).getCreate().getStatusValue());

        assertEquals(MOCK_LOCATION_GEO_TARGET_CONSTANT,
                campaignCriterionOperationList.get(8).getCreate().getLocation().getGeoTargetConstant());
        assertFalse(campaignCriterionOperationList.get(8).getCreate().getNegative());
        assertEquals(CampaignCriterionStatusEnum.CampaignCriterionStatus.ENABLED_VALUE,
                campaignCriterionOperationList.get(8).getCreate().getStatusValue());
        assertFalse(campaignCriterionOperationList.get(8).getCreate().hasBidModifier());
    }


    @Test
    void testBuildCampaignCriterionOperationListForRemoveOperation() {
        List<CampaignCriterionOperation> campaignCriterionOperationList =
                operationBuilder.buildCampaignCriterionOperationList(testUtils.getMockCriterionMapping(),
                        OperationType.REMOVE);

        assertEquals(9, campaignCriterionOperationList.size());
        assertTrue(campaignCriterionOperationList.get(0).hasRemove());
    }
}
