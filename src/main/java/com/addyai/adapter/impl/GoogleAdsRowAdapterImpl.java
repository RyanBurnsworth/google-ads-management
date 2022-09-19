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

package com.addyai.adapter.impl;

import com.addyai.adapter.GoogleAdsRowAdapter;
import com.addyai.models.BudgetDetails;
import com.addyai.models.CampaignDetails;
import com.google.ads.googleads.v11.services.GoogleAdsRow;

import static com.addyai.utils.misc.Constants.MICRO_FACTOR;

public class GoogleAdsRowAdapterImpl implements GoogleAdsRowAdapter {
    @Override
    public CampaignDetails getCampaignDetails(GoogleAdsRow googleAdsRow) {
        CampaignDetails details = new CampaignDetails();

        details.setCampaignId(googleAdsRow.getCampaign().getId());
        details.setCampaignName(googleAdsRow.getCampaign().getName());
        details.setCampaignResourceName(googleAdsRow.getCampaign().getResourceName());
        details.setStatus(googleAdsRow.getCampaign().getStatus().toString());
        details.setAdvertisingChannelType(googleAdsRow.getCampaign().getAdvertisingChannelType().toString());
        details.setPositiveGeoTargetType(googleAdsRow.getCampaign().getGeoTargetTypeSetting().getPositiveGeoTargetTypeValue());
        details.setNegativeGeoTargetType(googleAdsRow.getCampaign().getGeoTargetTypeSetting().getNegativeGeoTargetTypeValue());
        details.setEnhancedCpcEnabled(googleAdsRow.getCampaign().getManualCpc().getEnhancedCpcEnabled());
        details.setStartDate(googleAdsRow.getCampaign().getStartDate());
        details.setEndDate(googleAdsRow.getCampaign().getEndDate());
        details.setTargetingSearchNetwork(googleAdsRow.getCampaign().getNetworkSettings().getTargetSearchNetwork());
        details.setTargetingContentNetwork(googleAdsRow.getCampaign().getNetworkSettings().getTargetContentNetwork());
        details.setTargetingGoogleSearchNetwork(googleAdsRow.getCampaign().getNetworkSettings().getTargetGoogleSearch());
        details.setBudgetResourceName(googleAdsRow.getCampaign().getCampaignBudget());

        return details;
    }

    @Override
    public BudgetDetails getBudgetDetails(GoogleAdsRow googleAdsRow) {
        BudgetDetails budgetDetails = new BudgetDetails();
        budgetDetails.setBudgetId(googleAdsRow.getCampaignBudget().getId());
        budgetDetails.setDailyBudgetAmount(Math.round((float) googleAdsRow.getCampaignBudget().getAmountMicros() / MICRO_FACTOR));
        budgetDetails.setName(googleAdsRow.getCampaignBudget().getName());
        budgetDetails.setResourceName(googleAdsRow.getCampaignBudget().getResourceName());
        budgetDetails.setDeliveryMethod(googleAdsRow.getCampaignBudget().getDeliveryMethodValue());
        budgetDetails.setShared(googleAdsRow.getCampaignBudget().getExplicitlyShared());
        budgetDetails.setStatus(googleAdsRow.getCampaignBudget().getStatusValue());

        return budgetDetails;
    }
}
