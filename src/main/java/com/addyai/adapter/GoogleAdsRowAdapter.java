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

import com.addyai.models.AdGroupDetails;
import com.addyai.models.BudgetDetails;
import com.addyai.models.CampaignDetails;
import com.addyai.models.campaign_criterion.*;
import com.google.ads.googleads.v11.services.GoogleAdsRow;

public interface GoogleAdsRowAdapter {
    CampaignDetails getCampaignDetails(GoogleAdsRow googleAdsRow);

    BudgetDetails getBudgetDetails(GoogleAdsRow googleAdsRow);

    AdScheduleDetails getAdScheduleDetails(GoogleAdsRow googleAdsRow);

    NegativeKeywordDetails getKeywordDetails(GoogleAdsRow googleAdsRow);

    ProximityDetails getProximityDetails(GoogleAdsRow googleAdsRow);

    LocationDetails getLocationDetails(GoogleAdsRow googleAdsRow);

    LanguageDetails getLanguageDetails(GoogleAdsRow googleAdsRow);

    DeviceDetails getDeviceDetails(GoogleAdsRow googleAdsRow);

    AdGroupDetails getAdGroupDetails(GoogleAdsRow googleAdsRow);
}
