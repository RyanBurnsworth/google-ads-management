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
import com.addyai.models.AdGroupDetails;
import com.addyai.models.BudgetDetails;
import com.addyai.models.CampaignDetails;
import com.addyai.models.KeywordDetails;
import com.addyai.models.campaign_criterion.*;
import com.google.ads.googleads.v11.services.GoogleAdsRow;

import static com.addyai.utils.misc.Constants.*;

public class GoogleAdsRowAdapterImpl implements GoogleAdsRowAdapter {
    @Override
    public CampaignDetails getCampaignDetails(GoogleAdsRow googleAdsRow) {
        CampaignDetails details = new CampaignDetails();

        details.setCampaignId(googleAdsRow.getCampaign().getId());
        details.setCampaignName(googleAdsRow.getCampaign().getName());
        details.setCampaignResourceName(googleAdsRow.getCampaign().getResourceName());
        details.setStatus(googleAdsRow.getCampaign().getStatusValue());
        details.setAdvertisingChannelType(googleAdsRow.getCampaign().getAdvertisingChannelTypeValue());
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

    @Override
    public AdScheduleDetails getAdScheduleDetails(GoogleAdsRow googleAdsRow) {
        AdScheduleDetails adScheduleDetails = new AdScheduleDetails();
        adScheduleDetails.setCriterionType(CRITERION_TYPE_AD_SCHEDULE);
        adScheduleDetails.setCriterionResourceName(googleAdsRow.getCampaignCriterion().getResourceName());
        adScheduleDetails.setCampaignCriterionId(googleAdsRow.getCampaignCriterion().getCriterionId());
        adScheduleDetails.setCampaignResourceName(googleAdsRow.getCampaignCriterion().getCampaign());
        adScheduleDetails.setStatus(googleAdsRow.getCampaignCriterion().getStatusValue());
        adScheduleDetails.setDayOfWeek(googleAdsRow.getCampaignCriterion().getAdSchedule().getDayOfWeekValue());
        adScheduleDetails.setStartHour(googleAdsRow.getCampaignCriterion().getAdSchedule().getStartHour());
        adScheduleDetails.setEndHour(googleAdsRow.getCampaignCriterion().getAdSchedule().getEndHour());
        adScheduleDetails.setStartMinute(googleAdsRow.getCampaignCriterion().getAdSchedule().getStartMinuteValue());
        adScheduleDetails.setEndMinute(googleAdsRow.getCampaignCriterion().getAdSchedule().getEndMinuteValue());

        // if no bid modifier is set, set to -1.0
        adScheduleDetails.setBidModifier(
                googleAdsRow.getCampaignCriterion().hasBidModifier() ?
                        googleAdsRow.getCampaignCriterion().getBidModifier() : -1.0f);

        return adScheduleDetails;
    }

    @Override
    public NegativeKeywordDetails getNegativeKeywordDetails(GoogleAdsRow googleAdsRow) {
        NegativeKeywordDetails negativeKeywordDetails = new NegativeKeywordDetails();
        negativeKeywordDetails.setCriterionType(CRITERION_TYPE_KEYWORD);
        negativeKeywordDetails.setCampaignCriterionId(googleAdsRow.getCampaignCriterion().getCriterionId());
        negativeKeywordDetails.setCriterionResourceName(googleAdsRow.getCampaignCriterion().getResourceName());
        negativeKeywordDetails.setCampaignResourceName(googleAdsRow.getCampaignCriterion().getCampaign());
        negativeKeywordDetails.setNegative(googleAdsRow.getCampaignCriterion().getNegative());
        negativeKeywordDetails.setStatus(googleAdsRow.getCampaignCriterion().getStatusValue());
        negativeKeywordDetails.setKeywordText(googleAdsRow.getCampaignCriterion().getKeyword().getText());
        negativeKeywordDetails.setKeywordMatchType(googleAdsRow.getCampaignCriterion().getKeyword().getMatchTypeValue());

        return negativeKeywordDetails;
    }

    @Override
    public ProximityDetails getProximityDetails(GoogleAdsRow googleAdsRow) {
        ProximityDetails proximityDetails = new ProximityDetails();
        proximityDetails.setCriterionType(CRITERION_TYPE_PROXIMITY);
        proximityDetails.setCampaignCriterionId(googleAdsRow.getCampaignCriterion().getCriterionId());
        proximityDetails.setCriterionResourceName(googleAdsRow.getCampaignCriterion().getResourceName());
        proximityDetails.setCampaignResourceName(googleAdsRow.getCampaignCriterion().getCampaign());
        proximityDetails.setStatus(googleAdsRow.getCampaignCriterion().getStatusValue());

        if (googleAdsRow.getCampaignCriterion().getProximity().hasAddress()) {
            proximityDetails.setCityName(googleAdsRow.getCampaignCriterion().getProximity().getAddress().getCityName());
            proximityDetails.setProvinceName(googleAdsRow.getCampaignCriterion().getProximity().getAddress().getProvinceName());
            proximityDetails.setStreetAddress(googleAdsRow.getCampaignCriterion().getProximity().getAddress().getStreetAddress());
            proximityDetails.setPostalCode(googleAdsRow.getCampaignCriterion().getProximity().getAddress().getPostalCode());
            proximityDetails.setProvinceCode(googleAdsRow.getCampaignCriterion().getProximity().getAddress().getProvinceCode());
            proximityDetails.setCountryCode(googleAdsRow.getCampaignCriterion().getProximity().getAddress().getCountryCode());
        }

        // if no bid modifier is set, set to -1.0
        proximityDetails.setBidModifier(
                googleAdsRow.getCampaignCriterion().hasBidModifier() ?
                        googleAdsRow.getCampaignCriterion().getBidModifier() : -1.0f);

        if (googleAdsRow.getCampaignCriterion().getProximity().hasGeoPoint()) {
            proximityDetails.setLongitude(
                    Math.round((float) googleAdsRow
                            .getCampaignCriterion()
                            .getProximity()
                            .getGeoPoint()
                            .getLongitudeInMicroDegrees() / MICRO_FACTOR));
            proximityDetails.setLatitude(
                    Math.round((float) googleAdsRow
                            .getCampaignCriterion()
                            .getProximity()
                            .getGeoPoint()
                            .getLatitudeInMicroDegrees() / MICRO_FACTOR));
        }

        proximityDetails.setRadius(googleAdsRow.getCampaignCriterion().getProximity().getRadius());
        proximityDetails.setRadiusUnits(googleAdsRow.getCampaignCriterion().getProximity().getRadiusUnitsValue());

        return proximityDetails;
    }

    @Override
    public LocationDetails getLocationDetails(GoogleAdsRow googleAdsRow) {
        LocationDetails locationDetails = new LocationDetails();
        locationDetails.setCriterionType(CRITERION_TYPE_LOCATION);
        locationDetails.setCampaignCriterionId(googleAdsRow.getCampaignCriterion().getCriterionId());
        locationDetails.setCriterionResourceName(googleAdsRow.getCampaignCriterion().getResourceName());
        locationDetails.setCampaignResourceName(googleAdsRow.getCampaignCriterion().getCampaign());
        locationDetails.setStatus(googleAdsRow.getCampaignCriterion().getStatusValue());
        locationDetails.setNegative(googleAdsRow.getCampaignCriterion().getNegative());
        locationDetails.setGeoTargetingConstant(googleAdsRow.getCampaignCriterion().getLocation().getGeoTargetConstant());

        // Google Ads adds location targets that the user doesn't specify. These cannot be removed or updated.
        // These will have an empty geo-targeting constant. Do not add to list to refrain from confusion.
        if (locationDetails.getGeoTargetingConstant().isEmpty())
            return null;

        // if no bid modifier is set, set to -1.0
        locationDetails.setBidModifier(
                googleAdsRow.getCampaignCriterion().hasBidModifier() ?
                        googleAdsRow.getCampaignCriterion().getBidModifier() : -1.0f);

        return locationDetails;
    }

    @Override
    public LanguageDetails getLanguageDetails(GoogleAdsRow googleAdsRow) {
        LanguageDetails languageDetails = new LanguageDetails();
        languageDetails.setCriterionType(CRITERION_TYPE_LANGUAGE);
        languageDetails.setCampaignCriterionId(googleAdsRow.getCampaignCriterion().getCriterionId());
        languageDetails.setCriterionResourceName(googleAdsRow.getCampaignCriterion().getResourceName());
        languageDetails.setCampaignResourceName(googleAdsRow.getCampaignCriterion().getCampaign());
        languageDetails.setStatus(googleAdsRow.getCampaignCriterion().getStatusValue());
        languageDetails.setLanguageCode(googleAdsRow.getCampaignCriterion().getLanguage().getLanguageConstant());
        languageDetails.setBidModifier(-1.0f); // language cannot have a bid modifier

        // Google Ads adds language targets that the user doesn't specify. These cannot be removed or updated.
        // These will have an empty language code. Do not add to list to refrain from confusion.
        if (languageDetails.getLanguageCode().isEmpty())
            return null;

        return languageDetails;
    }

    @Override
    public DeviceDetails getDeviceDetails(GoogleAdsRow googleAdsRow) {
        DeviceDetails deviceDetails = new DeviceDetails();
        deviceDetails.setCriterionType(CRITERION_TYPE_DEVICE);
        deviceDetails.setCampaignCriterionId(googleAdsRow.getCampaignCriterion().getCriterionId());
        deviceDetails.setCriterionResourceName(googleAdsRow.getCampaignCriterion().getResourceName());
        deviceDetails.setCampaignResourceName(googleAdsRow.getCampaignCriterion().getCampaign());
        deviceDetails.setStatus(googleAdsRow.getCampaignCriterion().getStatusValue());
        deviceDetails.setDeviceType(googleAdsRow.getCampaignCriterion().getDevice().getTypeValue());

        // if no bid modifier is set, set to -1.0
        deviceDetails.setBidModifier(
                googleAdsRow.getCampaignCriterion().hasBidModifier() ?
                        googleAdsRow.getCampaignCriterion().getBidModifier() : -1.0f);

        return deviceDetails;
    }

    @Override
    public AdGroupDetails getAdGroupDetails(GoogleAdsRow googleAdsRow) {
        AdGroupDetails adGroupDetails = new AdGroupDetails();
        adGroupDetails.setCpcBid((int) (googleAdsRow.getAdGroup().getCpcBidMicros() / MICRO_FACTOR));
        adGroupDetails.setAdGroupResourceName(googleAdsRow.getAdGroup().getResourceName());
        adGroupDetails.setCampaignResourceName(googleAdsRow.getAdGroup().getCampaign());
        adGroupDetails.setStatus(googleAdsRow.getAdGroup().getStatusValue());
        adGroupDetails.setType(googleAdsRow.getAdGroup().getTypeValue());
        adGroupDetails.setAdGroupName(googleAdsRow.getAdGroup().getName());

        return adGroupDetails;
    }

    @Override
    public KeywordDetails getKeywordDetails(GoogleAdsRow googleAdsRow) {
        KeywordDetails keywordDetails = new KeywordDetails();
        keywordDetails.setKeywordResourceName(googleAdsRow.getAdGroupCriterion().getResourceName());
        keywordDetails.setKeywordId(googleAdsRow.getAdGroupCriterion().getCriterionId());
        keywordDetails.setKeywordMatchType(googleAdsRow.getAdGroupCriterion().getKeyword().getMatchTypeValue());
        keywordDetails.setKeywordText(googleAdsRow.getAdGroupCriterion().getKeyword().getText());
        keywordDetails.setStatus(googleAdsRow.getAdGroupCriterion().getStatusValue());
        keywordDetails.setCpcBid((double) (googleAdsRow.getAdGroupCriterion().getCpcBidMicros() / MICRO_FACTOR));
        return keywordDetails;
    }
}
