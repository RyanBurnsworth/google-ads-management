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
import com.addyai.enums.MetricType;
import com.addyai.models.*;
import com.addyai.models.ads.ResponsiveSearchAdDetails;
import com.addyai.models.assets.CallExtensionDetails;
import com.addyai.models.assets.SitelinkDetails;
import com.addyai.models.campaign_criterion.*;
import com.addyai.models.metrics.Metrics;
import com.addyai.utils.helpers.DateTimeHelper;
import com.google.ads.googleads.v12.common.AdScheduleInfo;
import com.google.ads.googleads.v12.services.GoogleAdsRow;

import java.util.ArrayList;
import java.util.List;

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
        keywordDetails.setAdGroupResourceName(googleAdsRow.getAdGroupCriterion().getAdGroup());
        keywordDetails.setKeywordResourceName(googleAdsRow.getAdGroupCriterion().getResourceName());
        keywordDetails.setKeywordId(googleAdsRow.getAdGroupCriterion().getCriterionId());
        keywordDetails.setKeywordMatchType(googleAdsRow.getAdGroupCriterion().getKeyword().getMatchTypeValue());
        keywordDetails.setKeywordText(googleAdsRow.getAdGroupCriterion().getKeyword().getText());
        keywordDetails.setStatus(googleAdsRow.getAdGroupCriterion().getStatusValue());
        keywordDetails.setCpcBid((double) (googleAdsRow.getAdGroupCriterion().getCpcBidMicros() / MICRO_FACTOR));
        return keywordDetails;
    }

    @Override
    public SitelinkDetails getSitelinkDetails(GoogleAdsRow googleAdsRow) {
        SitelinkDetails sitelinkDetails = new SitelinkDetails();
        sitelinkDetails.setAssetId(googleAdsRow.getAsset().getId());
        sitelinkDetails.setAssetName(googleAdsRow.getAsset().getResourceName());
        sitelinkDetails.setAssetSource(googleAdsRow.getAsset().getSourceValue());
        sitelinkDetails.setAssetType(googleAdsRow.getAsset().getTypeValue());
        sitelinkDetails.setStartDate(googleAdsRow.getAsset().getSitelinkAsset().getStartDate());
        sitelinkDetails.setEndDate(googleAdsRow.getAsset().getSitelinkAsset().getEndDate());
        sitelinkDetails.setDescription1(googleAdsRow.getAsset().getSitelinkAsset().getDescription1());
        sitelinkDetails.setDescription2(googleAdsRow.getAsset().getSitelinkAsset().getDescription2());
        sitelinkDetails.setLinkText(googleAdsRow.getAsset().getSitelinkAsset().getLinkText());
        return sitelinkDetails;
    }

    @Override
    public CallExtensionDetails getCallExtensionDetails(GoogleAdsRow googleAdsRow) {
        CallExtensionDetails callExtensionDetails = new CallExtensionDetails();
        callExtensionDetails.setAssetId(googleAdsRow.getAsset().getId());
        callExtensionDetails.setAssetName(googleAdsRow.getAsset().getResourceName());
        callExtensionDetails.setAssetSource(googleAdsRow.getAsset().getSourceValue());
        callExtensionDetails.setAssetType(googleAdsRow.getAsset().getTypeValue());
        callExtensionDetails.setPhoneNumber(googleAdsRow.getAsset().getCallAsset().getPhoneNumber());
        callExtensionDetails.setCountryCode(googleAdsRow.getAsset().getCallAsset().getCountryCode());
        if (googleAdsRow.getAsset().getCallAsset().getAdScheduleTargetsCount() > 0) {
            List<AdSchedulingDetails> adSchedulingDetailsList = new ArrayList<>();
            for (AdScheduleInfo adscheduleInfo : googleAdsRow.getAsset().getCallAsset().getAdScheduleTargetsList()) {
                AdSchedulingDetails adSchedulingDetails = new AdSchedulingDetails();
                adSchedulingDetails.setDayOfWeek(adscheduleInfo.getDayOfWeekValue());
                adSchedulingDetails.setEndHour(adscheduleInfo.getEndHour());
                adSchedulingDetails.setEndMinute(adscheduleInfo.getEndMinuteValue());
                adSchedulingDetails.setStartMinute(adscheduleInfo.getStartMinuteValue());
                adSchedulingDetails.setStartHour(adscheduleInfo.getStartHour());

                adSchedulingDetailsList.add(adSchedulingDetails);
            }
            callExtensionDetails.setAdSchedulingDetails(adSchedulingDetailsList);
        }

        return callExtensionDetails;
    }

    @Override
    public ResponsiveSearchAdDetails getResponsiveSearchAdDetails(GoogleAdsRow googleAdsRow) {
        List<String> headlines = new ArrayList<>();
        List<String> descriptions = new ArrayList<>();
        List<String> paths = new ArrayList<>();

        googleAdsRow.getAdGroupAd().getAd().getResponsiveSearchAd().getHeadlinesList().forEach((headline) -> {
            headlines.add(headline.getText());
        });

        googleAdsRow.getAdGroupAd().getAd().getResponsiveSearchAd().getDescriptionsList().forEach((description) -> {
            descriptions.add(description.getText());
        });

        if (googleAdsRow.getAdGroupAd().getAd().getResponsiveSearchAd().hasPath1())
            paths.add(googleAdsRow.getAdGroupAd().getAd().getResponsiveSearchAd().getPath1());

        if (googleAdsRow.getAdGroupAd().getAd().getResponsiveSearchAd().hasPath2())
            paths.add(googleAdsRow.getAdGroupAd().getAd().getResponsiveSearchAd().getPath2());

        ResponsiveSearchAdDetails responsiveSearchAdDetails = new ResponsiveSearchAdDetails();
        responsiveSearchAdDetails.setAdName(googleAdsRow.getAdGroupAd().getAd().getResourceName());
        responsiveSearchAdDetails.setAdStatus(googleAdsRow.getAdGroupAd().getStatusValue());
        responsiveSearchAdDetails.setAdGroupResourceName(googleAdsRow.getAdGroupAd().getAdGroup());
        responsiveSearchAdDetails.setHeadlines(headlines);
        responsiveSearchAdDetails.setDescriptions(descriptions);
        responsiveSearchAdDetails.setPaths(paths);
        if (!googleAdsRow.getAdGroupAd().getAd().getFinalUrlsList().isEmpty())
            responsiveSearchAdDetails.setFinalUrl(googleAdsRow.getAdGroupAd().getAd().getFinalUrls(0));

        return responsiveSearchAdDetails;
    }

    /*
        End of Details Adaptions

        Start Metrics Adaptions
     */

    @Override
    public Metrics getAccountMetrics(GoogleAdsRow googleAdsRow) {
        Metrics accountMetrics = new Metrics();
        accountMetrics.setType(MetricType.ACCOUNT);
        accountMetrics.setResourceId(String.valueOf(googleAdsRow.getCustomer().getId()));
        accountMetrics.setResourceName(googleAdsRow.getCustomer().getResourceName());

        accountMetrics.setDate(googleAdsRow.getSegments().getDate());
        accountMetrics.setClicks(googleAdsRow.getMetrics().getClicks());
        accountMetrics.setImpressions(googleAdsRow.getMetrics().getImpressions());
        accountMetrics.setCtr(googleAdsRow.getMetrics().getCtr());
        accountMetrics.setCost((double) googleAdsRow.getMetrics().getCostMicros() / MICRO_FACTOR);
        accountMetrics.setAverageCpc(googleAdsRow.getMetrics().getAverageCpc());
        accountMetrics.setConversions(googleAdsRow.getMetrics().getConversions());
        accountMetrics.setConversionValue(googleAdsRow.getMetrics().getConversionsValue());
        accountMetrics.setCostPerConversion(googleAdsRow.getMetrics().getCostPerConversion());
        accountMetrics.setInvalidClickRate(googleAdsRow.getMetrics().getInvalidClickRate());
        accountMetrics.setInvalidClicks(googleAdsRow.getMetrics().getInvalidClicks());
        accountMetrics.setLastUpdated(DateTimeHelper.getCurrentTimestamp().toString());
        return accountMetrics;
    }

    @Override
    public Metrics getCampaignMetrics(GoogleAdsRow googleAdsRow) {
        Metrics campaignMetrics = new Metrics();
        campaignMetrics.setType(MetricType.CAMPAIGN);
        campaignMetrics.setResourceId(String.valueOf(googleAdsRow.getCampaign().getId()));
        campaignMetrics.setResourceName(googleAdsRow.getCampaign().getResourceName());

        campaignMetrics.setDate(googleAdsRow.getSegments().getDate());
        campaignMetrics.setClicks(googleAdsRow.getMetrics().getClicks());
        campaignMetrics.setImpressions(googleAdsRow.getMetrics().getImpressions());
        campaignMetrics.setCtr(googleAdsRow.getMetrics().getCtr());
        campaignMetrics.setCost((double) googleAdsRow.getMetrics().getCostMicros() / MICRO_FACTOR);
        campaignMetrics.setAverageCpc(googleAdsRow.getMetrics().getAverageCpc());
        campaignMetrics.setConversions(googleAdsRow.getMetrics().getConversions());
        campaignMetrics.setConversionValue(googleAdsRow.getMetrics().getConversionsValue());
        campaignMetrics.setCostPerConversion(googleAdsRow.getMetrics().getCostPerConversion());
        campaignMetrics.setInvalidClickRate(googleAdsRow.getMetrics().getInvalidClickRate());
        campaignMetrics.setInvalidClicks(googleAdsRow.getMetrics().getInvalidClicks());
        campaignMetrics.setPhoneImpressions(googleAdsRow.getMetrics().getPhoneImpressions());
        campaignMetrics.setPhoneCalls(googleAdsRow.getMetrics().getPhoneCalls());
        campaignMetrics.setPhoneThroughRate(googleAdsRow.getMetrics().getPhoneThroughRate());
        campaignMetrics.setLastUpdated(DateTimeHelper.getCurrentTimestamp().toString());
        return campaignMetrics;
    }

    @Override
    public Metrics getAdGroupMetrics(GoogleAdsRow googleAdsRow) {
        Metrics adGroupMetrics = new Metrics();
        adGroupMetrics.setType(MetricType.ADGROUP);
        adGroupMetrics.setResourceId(String.valueOf(googleAdsRow.getAdGroup().getId()));
        adGroupMetrics.setParentId(googleAdsRow.getAdGroup().getCampaign());
        adGroupMetrics.setResourceName(googleAdsRow.getAdGroup().getResourceName());

        adGroupMetrics.setDate(googleAdsRow.getSegments().getDate());
        adGroupMetrics.setClicks(googleAdsRow.getMetrics().getClicks());
        adGroupMetrics.setImpressions(googleAdsRow.getMetrics().getImpressions());
        adGroupMetrics.setCtr(googleAdsRow.getMetrics().getCtr());
        adGroupMetrics.setCost((double) googleAdsRow.getMetrics().getCostMicros() / MICRO_FACTOR);
        adGroupMetrics.setAverageCpc(googleAdsRow.getMetrics().getAverageCpc());
        adGroupMetrics.setConversions(googleAdsRow.getMetrics().getConversions());
        adGroupMetrics.setConversionValue(googleAdsRow.getMetrics().getConversionsValue());
        adGroupMetrics.setCostPerConversion(googleAdsRow.getMetrics().getCostPerConversion());
        adGroupMetrics.setPhoneImpressions(googleAdsRow.getMetrics().getPhoneImpressions());
        adGroupMetrics.setPhoneCalls(googleAdsRow.getMetrics().getPhoneCalls());
        adGroupMetrics.setPhoneThroughRate(googleAdsRow.getMetrics().getPhoneThroughRate());
        adGroupMetrics.setLastUpdated(DateTimeHelper.getCurrentTimestamp().toString());
        return adGroupMetrics;
    }

    @Override
    public Metrics getAdMetrics(GoogleAdsRow googleAdsRow) {
        Metrics adMetrics = new Metrics();
        adMetrics.setType(MetricType.AD);
        adMetrics.setResourceId(String.valueOf(googleAdsRow.getAdGroupAd().getAd().getId()));
        adMetrics.setParentId(googleAdsRow.getAdGroupAd().getAdGroup());
        adMetrics.setResourceName(googleAdsRow.getAdGroupAd().getAd().getResourceName());

        adMetrics.setDate(googleAdsRow.getSegments().getDate());
        adMetrics.setClicks(googleAdsRow.getMetrics().getClicks());
        adMetrics.setImpressions(googleAdsRow.getMetrics().getImpressions());
        adMetrics.setCtr(googleAdsRow.getMetrics().getCtr());
        adMetrics.setCost((double) googleAdsRow.getMetrics().getCostMicros() / MICRO_FACTOR);
        adMetrics.setAverageCpc(googleAdsRow.getMetrics().getAverageCpc());
        adMetrics.setConversions(googleAdsRow.getMetrics().getConversions());
        adMetrics.setConversionValue(googleAdsRow.getMetrics().getConversionsValue());
        adMetrics.setCostPerConversion(googleAdsRow.getMetrics().getCostPerConversion());
        adMetrics.setLastUpdated(DateTimeHelper.getCurrentTimestamp().toString());

        return adMetrics;
    }

    @Override
    public Metrics getKeywordMetrics(GoogleAdsRow googleAdsRow) {
        Metrics keywordMetrics = new Metrics();
        keywordMetrics.setType(MetricType.KEYWORD);
        keywordMetrics.setResourceId(String.valueOf(googleAdsRow.getAdGroupCriterion().getCriterionId()));
        keywordMetrics.setParentId(googleAdsRow.getAdGroupCriterion().getAdGroup());
        keywordMetrics.setResourceName(googleAdsRow.getAdGroupCriterion().getResourceName());

        keywordMetrics.setDate(googleAdsRow.getSegments().getDate());
        keywordMetrics.setClicks(googleAdsRow.getMetrics().getClicks());
        keywordMetrics.setImpressions(googleAdsRow.getMetrics().getImpressions());
        keywordMetrics.setCtr(googleAdsRow.getMetrics().getCtr());
        keywordMetrics.setCost((double) googleAdsRow.getMetrics().getCostMicros() / MICRO_FACTOR);
        keywordMetrics.setAverageCpc(googleAdsRow.getMetrics().getAverageCpc());
        keywordMetrics.setConversions(googleAdsRow.getMetrics().getConversions());
        keywordMetrics.setConversionValue(googleAdsRow.getMetrics().getConversionsValue());
        keywordMetrics.setCostPerConversion(googleAdsRow.getMetrics().getCostPerConversion());
        keywordMetrics.setQualityScore(googleAdsRow.getAdGroupCriterion().getQualityInfo().getQualityScore());
        keywordMetrics.setLastUpdated(DateTimeHelper.getCurrentTimestamp().toString());

        return keywordMetrics;
    }

    @Override
    public Metrics getDeviceMetricsByCampaign(GoogleAdsRow googleAdsRow) {
        Metrics campaignDeviceMetrics = new Metrics();
        campaignDeviceMetrics.setType(MetricType.DEVICE_CAMPAIGN);
        campaignDeviceMetrics.setResourceId(String.valueOf(googleAdsRow.getAdGroupCriterion().getCriterionId()));
        campaignDeviceMetrics.setParentId(googleAdsRow.getAdGroupCriterion().getAdGroup());
        campaignDeviceMetrics.setResourceName(googleAdsRow.getAdGroupCriterion().getResourceName());

        campaignDeviceMetrics.setDeviceType(googleAdsRow.getSegments().getDevice().name());
        campaignDeviceMetrics.setClicks(googleAdsRow.getMetrics().getClicks());
        campaignDeviceMetrics.setImpressions(googleAdsRow.getMetrics().getImpressions());
        campaignDeviceMetrics.setCtr(googleAdsRow.getMetrics().getCtr());
        campaignDeviceMetrics.setCost((double) googleAdsRow.getMetrics().getCostMicros() / MICRO_FACTOR);
        campaignDeviceMetrics.setAverageCpc(googleAdsRow.getMetrics().getAverageCpc());
        campaignDeviceMetrics.setConversions(googleAdsRow.getMetrics().getConversions());
        campaignDeviceMetrics.setConversionValue(googleAdsRow.getMetrics().getConversionsValue());
        campaignDeviceMetrics.setCostPerConversion(googleAdsRow.getMetrics().getCostPerConversion());
        campaignDeviceMetrics.setInvalidClickRate(googleAdsRow.getMetrics().getInvalidClickRate());
        campaignDeviceMetrics.setInvalidClicks(googleAdsRow.getMetrics().getInvalidClicks());
        campaignDeviceMetrics.setLastUpdated(DateTimeHelper.getCurrentTimestamp().toString());

        return campaignDeviceMetrics;
    }

    @Override
    public Metrics getDeviceMetricsByAdGroup(GoogleAdsRow googleAdsRow) {
        Metrics adGroupDeviceMetrics = new Metrics();
        adGroupDeviceMetrics.setType(MetricType.DEVICE_ADGROUP);
        adGroupDeviceMetrics.setResourceId(String.valueOf(googleAdsRow.getAdGroupCriterion().getCriterionId()));
        adGroupDeviceMetrics.setParentId(googleAdsRow.getAdGroupCriterion().getAdGroup());
        adGroupDeviceMetrics.setResourceName(googleAdsRow.getAdGroupCriterion().getResourceName());

        adGroupDeviceMetrics.setDeviceType(googleAdsRow.getSegments().getDevice().name());
        adGroupDeviceMetrics.setClicks(googleAdsRow.getMetrics().getClicks());
        adGroupDeviceMetrics.setImpressions(googleAdsRow.getMetrics().getImpressions());
        adGroupDeviceMetrics.setCtr(googleAdsRow.getMetrics().getCtr());
        adGroupDeviceMetrics.setCost((double) googleAdsRow.getMetrics().getCostMicros() / MICRO_FACTOR);
        adGroupDeviceMetrics.setAverageCpc(googleAdsRow.getMetrics().getAverageCpc());
        adGroupDeviceMetrics.setConversions(googleAdsRow.getMetrics().getConversions());
        adGroupDeviceMetrics.setConversionValue(googleAdsRow.getMetrics().getConversionsValue());
        adGroupDeviceMetrics.setCostPerConversion(googleAdsRow.getMetrics().getCostPerConversion());
        adGroupDeviceMetrics.setLastUpdated(DateTimeHelper.getCurrentTimestamp().toString());

        return adGroupDeviceMetrics;
    }

    @Override
    public Metrics getDeviceMetricsByAd(GoogleAdsRow googleAdsRow) {
        Metrics adDeviceMetrics = new Metrics();
        adDeviceMetrics.setType(MetricType.DEVICE_AD);
        adDeviceMetrics.setResourceId(String.valueOf(googleAdsRow.getAdGroupAd().getAd().getId()));
        adDeviceMetrics.setParentId(googleAdsRow.getAdGroupAd().getAdGroup());
        adDeviceMetrics.setResourceName(googleAdsRow.getAdGroupAd().getAd().getResourceName());

        adDeviceMetrics.setDeviceType(googleAdsRow.getSegments().getDevice().name());
        adDeviceMetrics.setClicks(googleAdsRow.getMetrics().getClicks());
        adDeviceMetrics.setImpressions(googleAdsRow.getMetrics().getImpressions());
        adDeviceMetrics.setCtr(googleAdsRow.getMetrics().getCtr());
        adDeviceMetrics.setCost((double) googleAdsRow.getMetrics().getCostMicros() / MICRO_FACTOR);
        adDeviceMetrics.setAverageCpc(googleAdsRow.getMetrics().getAverageCpc());
        adDeviceMetrics.setConversions(googleAdsRow.getMetrics().getConversions());
        adDeviceMetrics.setConversionValue(googleAdsRow.getMetrics().getConversionsValue());
        adDeviceMetrics.setCostPerConversion(googleAdsRow.getMetrics().getCostPerConversion());
        adDeviceMetrics.setLastUpdated(DateTimeHelper.getCurrentTimestamp().toString());

        return adDeviceMetrics;
    }

    @Override
    public Metrics getDeviceMetricsByKeyword(GoogleAdsRow googleAdsRow) {
        Metrics keywordDeviceMetrics = new Metrics();
        keywordDeviceMetrics.setType(MetricType.DEVICE_KEYWORD);
        keywordDeviceMetrics.setResourceId(String.valueOf(googleAdsRow.getAdGroupCriterion().getCriterionId()));
        keywordDeviceMetrics.setParentId(googleAdsRow.getAdGroupCriterion().getAdGroup());
        keywordDeviceMetrics.setResourceName(googleAdsRow.getKeywordView().getResourceName());

        keywordDeviceMetrics.setDeviceType(googleAdsRow.getSegments().getDevice().name());
        keywordDeviceMetrics.setClicks(googleAdsRow.getMetrics().getClicks());
        keywordDeviceMetrics.setImpressions(googleAdsRow.getMetrics().getImpressions());
        keywordDeviceMetrics.setCtr(googleAdsRow.getMetrics().getCtr());
        keywordDeviceMetrics.setCost((double) googleAdsRow.getMetrics().getCostMicros() / MICRO_FACTOR);
        keywordDeviceMetrics.setAverageCpc(googleAdsRow.getMetrics().getAverageCpc());
        keywordDeviceMetrics.setConversions(googleAdsRow.getMetrics().getConversions());
        keywordDeviceMetrics.setConversionValue(googleAdsRow.getMetrics().getConversionsValue());
        keywordDeviceMetrics.setCostPerConversion(googleAdsRow.getMetrics().getCostPerConversion());
        keywordDeviceMetrics.setLastUpdated(DateTimeHelper.getCurrentTimestamp().toString());

        return keywordDeviceMetrics;
    }
}
