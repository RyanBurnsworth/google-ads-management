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
import com.addyai.models.KeywordDetails;
import com.addyai.models.ads.ResponsiveSearchAdDetails;
import com.addyai.models.assets.CallExtensionDetails;
import com.addyai.models.assets.SitelinkDetails;
import com.addyai.models.campaign_criterion.*;
import com.addyai.models.metrics.Metrics;
import com.google.ads.googleads.v12.services.GoogleAdsRow;

public interface GoogleAdsRowAdapter {
    CampaignDetails getCampaignDetails(GoogleAdsRow googleAdsRow);

    BudgetDetails getBudgetDetails(GoogleAdsRow googleAdsRow);

    AdScheduleDetails getAdScheduleDetails(GoogleAdsRow googleAdsRow);

    NegativeKeywordDetails getNegativeKeywordDetails(GoogleAdsRow googleAdsRow);

    ProximityDetails getProximityDetails(GoogleAdsRow googleAdsRow);

    LocationDetails getLocationDetails(GoogleAdsRow googleAdsRow);

    LanguageDetails getLanguageDetails(GoogleAdsRow googleAdsRow);

    DeviceDetails getDeviceDetails(GoogleAdsRow googleAdsRow);

    AdGroupDetails getAdGroupDetails(GoogleAdsRow googleAdsRow);

    KeywordDetails getKeywordDetails(GoogleAdsRow googleAdsRow);

    SitelinkDetails getSitelinkDetails(GoogleAdsRow googleAdsRow);

    CallExtensionDetails getCallExtensionDetails(GoogleAdsRow googleAdsRow);

    ResponsiveSearchAdDetails getResponsiveSearchAdDetails(GoogleAdsRow googleAdsRow);

    Metrics getCampaignMetrics(GoogleAdsRow googleAdsRow);

    Metrics getAdGroupMetrics(GoogleAdsRow googleAdsRow);

    Metrics getAdMetrics(GoogleAdsRow googleAdsRow);

    Metrics getKeywordMetrics(GoogleAdsRow googleAdsRow);

    Metrics getDeviceMetricsByCampaign(GoogleAdsRow googleAdsRow);

    Metrics getDeviceMetricsByAdGroup(GoogleAdsRow googleAdsRow);

    Metrics getDeviceMetricsByAd(GoogleAdsRow googleAdsRow);

    Metrics getDeviceMetricsByKeyword(GoogleAdsRow googleAdsRow);
}
