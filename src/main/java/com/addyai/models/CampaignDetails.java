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

package com.addyai.models;

import com.addyai.models.campaign_criterion.CriterionDetails;
import com.addyai.utils.helpers.DateHelper;
import com.google.ads.googleads.v11.enums.AdvertisingChannelTypeEnum;
import com.google.ads.googleads.v11.enums.CampaignStatusEnum;

import java.util.ArrayList;
import java.util.List;

import static com.addyai.utils.misc.Constants.*;

public class CampaignDetails {

    /**
     * The identifier for the campaign. Auto-generates when the campaign is created.
     */
    private long campaignId = 0L;

    /**
     * The name of the campaign.
     */
    private String campaignName = "";

    /**
     * The resource name of the campaign. Auto-generates when the campaign is created.
     * Format: <b>/customers/{customerId}/campaigns/{campaignId}</b>
     */
    private String campaignResourceName = "";

    /**
     * The current status of the campaign.
     * This status can be "ENABLED", "PAUSED" or "REMOVED".
     * Defaults to "PAUSED"
     */
    private int status = CampaignStatusEnum.CampaignStatus.ENABLED_VALUE;

    /**
     * The advertising channel type for the campaign.
     *
     * @see <a href="https://developers.google.com/google-ads/api/reference/rpc/v11/AdvertisingChannelTypeEnum.AdvertisingChannelType">Advertising Channel Types</a>
     * Defaults to "SEARCH"
     */
    private int advertisingChannelType = AdvertisingChannelTypeEnum.AdvertisingChannelType.SEARCH_VALUE;

    /**
     * The setting for including the showing of ads to those who are located within
     * or interested in the campaign's targeted areas or only located within the targeted area.
     * Defaults to PositiveGeoTargetTypeEnum.PositiveGeoTargetType.PRESENCE_OR_INTEREST_VALUE
     *
     * @see com.google.ads.googleads.v11.enums.PositiveGeoTargetTypeEnum.PositiveGeoTargetType
     */
    private int positiveGeoTargetType = POSITIVE_GEO_TARGET_TYPE_PRESENCE_OR_INTEREST;

    /**
     * The setting for excluding the showing of ads to those who are located within
     * or interested in the campaign's targeted areas or only located within the targeted area.
     * Defaults to NegativeGeoTargetTypeEnum.NegativeGeoTargetType.PRESENCE
     *
     * @see com.google.ads.googleads.v11.enums.NegativeGeoTargetTypeEnum.NegativeGeoTargetType
     */
    private int negativeGeoTargetType = NEGATIVE_GEO_TARGET_TYPE_PRESENCE;

    /**
     * The setting for enabling/disabling enhanced cost-per-click for the campaign.
     * Defaults to false
     */
    private boolean isEnhancedCpcEnabled = false;

    /**
     * The start date of the campaign.
     * Format: yyyy-MM-dd
     * Defaults to current date
     */
    private String startDate = DateHelper.getCurrentDate();

    /**
     * The end date of the campaign.
     * Format: yyyy-MM-dd
     * Defaults to current date + 10 years
     */
    private String endDate = DateHelper.getCurrentDatePlusYears(DEFAULT_ADDITIONAL_YEARS_CAMPAIGN_END_DATE);

    /**
     * Enabled if the campaign is to show ads on Google partner sites.
     * If enabled, isTargetingGoogleSearchNetwork must also be true
     * Defaults to false
     */
    private boolean isTargetingSearchNetwork = false;

    /**
     * Enabled if the campaign is to show ads on the Google Display network.
     * Defaults to false
     */
    private boolean isTargetingContentNetwork = false;

    /**
     * Enabled if the campaign will show ads on Google's search engine.
     * Defaults to true
     */
    private boolean isTargetingGoogleSearchNetwork = true;

    /**
     * The name of the budget resource. Auto-generates when the budget is created.
     */
    private String budgetResourceName = "";

    /**
     * The details of the budget for the campaign
     *
     * @see BudgetDetails
     */
    private BudgetDetails budgetDetails = new BudgetDetails();

    /**
     * The details of the targeting criterion for the campaign
     *
     * @see CriterionDetails
     */
    private List<CriterionDetails> criterionDetailsList = new ArrayList<>();

    public long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(long campaignId) {
        this.campaignId = campaignId;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getCampaignResourceName() {
        return campaignResourceName;
    }

    public void setCampaignResourceName(String campaignResourceName) {
        this.campaignResourceName = campaignResourceName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAdvertisingChannelType() {
        return advertisingChannelType;
    }

    public void setAdvertisingChannelType(int advertisingChannelType) {
        this.advertisingChannelType = advertisingChannelType;
    }

    public int getPositiveGeoTargetType() {
        return positiveGeoTargetType;
    }

    public void setPositiveGeoTargetType(int positiveGeoTargetType) {
        this.positiveGeoTargetType = positiveGeoTargetType;
    }

    public int getNegativeGeoTargetType() {
        return negativeGeoTargetType;
    }

    public void setNegativeGeoTargetType(int negativeGeoTargetType) {
        this.negativeGeoTargetType = negativeGeoTargetType;
    }

    public boolean isEnhancedCpcEnabled() {
        return isEnhancedCpcEnabled;
    }

    public void setEnhancedCpcEnabled(boolean enhancedCpcEnabled) {
        isEnhancedCpcEnabled = enhancedCpcEnabled;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean isTargetingSearchNetwork() {
        return isTargetingSearchNetwork;
    }

    public void setTargetingSearchNetwork(boolean targetingSearchNetwork) {
        isTargetingSearchNetwork = targetingSearchNetwork;
    }

    public boolean isTargetingContentNetwork() {
        return isTargetingContentNetwork;
    }

    public void setTargetingContentNetwork(boolean targetingContentNetwork) {
        isTargetingContentNetwork = targetingContentNetwork;
    }

    public boolean isTargetingGoogleSearchNetwork() {
        return isTargetingGoogleSearchNetwork;
    }

    public void setTargetingGoogleSearchNetwork(boolean targetingGoogleSearchNetwork) {
        isTargetingGoogleSearchNetwork = targetingGoogleSearchNetwork;
    }

    public String getBudgetResourceName() {
        return budgetResourceName;
    }

    public void setBudgetResourceName(String budgetResourceName) {
        this.budgetResourceName = budgetResourceName;
    }

    public BudgetDetails getBudgetDetails() {
        return budgetDetails;
    }

    public void setBudgetDetails(BudgetDetails budgetDetails) {
        this.budgetDetails = budgetDetails;
    }

    public List<CriterionDetails> getCampaignCriteriaList() {
        return criterionDetailsList;
    }

    public void setCampaignCriteriaList(List<CriterionDetails> criterionDetailsList) {
        this.criterionDetailsList = criterionDetailsList;
    }
}
