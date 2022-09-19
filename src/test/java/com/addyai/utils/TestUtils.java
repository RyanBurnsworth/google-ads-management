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

import com.google.ads.googleads.v11.common.ManualCpc;
import com.google.ads.googleads.v11.enums.*;
import com.google.ads.googleads.v11.resources.Campaign;
import com.google.ads.googleads.v11.resources.CampaignBudget;

import static com.addyai.utils.misc.Constants.MICRO_FACTOR;

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
}
