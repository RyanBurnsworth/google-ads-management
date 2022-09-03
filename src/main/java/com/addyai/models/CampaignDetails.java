package com.addyai.models;

import static com.addyai.utils.Constants.ADVERTISING_TYPE_SEARCH;
import static com.addyai.utils.Constants.GEO_TARGET_TYPE_UNKNOWN;

public class CampaignDetails {
    private long campaignId = 0L;

    private String campaignName = "";

    private String status = "";

    private String budget = "";

    private String advertisingChannelType = ADVERTISING_TYPE_SEARCH;

    private String biddingStrategy = "";

    private String positiveGeoTargetType = GEO_TARGET_TYPE_UNKNOWN;

    private String negativeGeoTargetType = GEO_TARGET_TYPE_UNKNOWN;

    private boolean isEnhancedCpcEnabled = false;

    private String startDate = "";

    private String endDate = "";

    private boolean isTargetingGoogleSearch = false;

    private boolean isTargetingSearchNetwork = false;

    private boolean isTargetingContentNetwork = false;

    private boolean isTargetingPartnerSearchNetwork = false;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getAdvertisingChannelType() {
        return advertisingChannelType;
    }

    public void setAdvertisingChannelType(String advertisingChannelType) {
        this.advertisingChannelType = advertisingChannelType;
    }

    public String getBiddingStrategy() {
        return biddingStrategy;
    }

    public void setBiddingStrategy(String biddingStrategy) {
        this.biddingStrategy = biddingStrategy;
    }

    public String getPositiveGeoTargetType() {
        return positiveGeoTargetType;
    }

    public void setPositiveGeoTargetType(String positiveGeoTargetType) {
        this.positiveGeoTargetType = positiveGeoTargetType;
    }

    public String getNegativeGeoTargetType() {
        return negativeGeoTargetType;
    }

    public void setNegativeGeoTargetType(String negativeGeoTargetType) {
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

    public boolean isTargetingGoogleSearch() {
        return isTargetingGoogleSearch;
    }

    public void setTargetingGoogleSearch(boolean targetingGoogleSearch) {
        isTargetingGoogleSearch = targetingGoogleSearch;
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

    public boolean isTargetingPartnerSearchNetwork() {
        return isTargetingPartnerSearchNetwork;
    }

    public void setTargetingPartnerSearchNetwork(boolean targetingPartnerSearchNetwork) {
        isTargetingPartnerSearchNetwork = targetingPartnerSearchNetwork;
    }
}
