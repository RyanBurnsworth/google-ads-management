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
import com.addyai.utils.TestUtils;
import com.google.ads.googleads.v11.enums.BudgetDeliveryMethodEnum;
import com.google.ads.googleads.v11.enums.BudgetStatusEnum;
import com.google.ads.googleads.v11.services.GoogleAdsRow;
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

    @Test
    void testGetCampaignDetails() {
        TestUtils testUtils = new TestUtils();

        GoogleAdsRow googleAdsRow = GoogleAdsRow.newBuilder()
                .setCampaign(testUtils.getMockCampaign()).build();

        CampaignDetails campaignDetails = googleAdsRowAdapter.getCampaignDetails(googleAdsRow);

        assertEquals(campaignDetails.getCampaignId(), MOCK_CAMPAIGN_ID);
        assertEquals(campaignDetails.getCampaignName(), MOCK_CAMPAIGN_NAME);
        assertEquals(campaignDetails.getCampaignResourceName(), MOCK_CAMPAIGN_RESOURCE_NAME);
        assertEquals(campaignDetails.getBudgetResourceName(), MOCK_CAMPAIGN_BUDGET_RESOURCE_NAME);
        assertEquals(campaignDetails.getStatus(), CAMPAIGN_ENABLED_STATUS);
        assertEquals(campaignDetails.getAdvertisingChannelType(), ADVERTISING_TYPE_SEARCH);
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
        TestUtils testUtils = new TestUtils();

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
}
