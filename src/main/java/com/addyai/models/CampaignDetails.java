package com.addyai.models;

import com.google.ads.googleads.v11.enums.AdvertisingChannelTypeEnum;
import com.google.ads.googleads.v11.enums.CampaignStatusEnum;
import com.google.ads.googleads.v11.enums.NegativeGeoTargetTypeEnum;
import com.google.ads.googleads.v11.enums.PositiveGeoTargetTypeEnum;

public class CampaignDetails {
    private long campaignId;

    private String campaignName;

    private CampaignStatusEnum.CampaignStatus status;

    private String budget;

    private AdvertisingChannelTypeEnum.AdvertisingChannelType advertisingChannelType;

    private String biddingStrategy;

    private PositiveGeoTargetTypeEnum.PositiveGeoTargetType positiveGeoTargetType;

    private NegativeGeoTargetTypeEnum.NegativeGeoTargetType negativeGeoTargetType;

    private boolean isEnhancedCpcEnabled;

    private String startDate;

    private String endDate;

    private boolean isTargetingGoogleSearch;

    private boolean isTargetingSearchNetwork;

    private boolean isTargetingContentNetwork;

    private boolean isTargetingPartnerSearchNetwork;

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

    public CampaignStatusEnum.CampaignStatus getStatus() {
        return status;
    }

    public void setStatus(CampaignStatusEnum.CampaignStatus status) {
        this.status = status;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public AdvertisingChannelTypeEnum.AdvertisingChannelType getAdvertisingChannelType() {
        return advertisingChannelType;
    }

    public void setAdvertisingChannelType(AdvertisingChannelTypeEnum.AdvertisingChannelType advertisingChannelType) {
        this.advertisingChannelType = advertisingChannelType;
    }

    public String getBiddingStrategy() {
        return biddingStrategy;
    }

    public void setBiddingStrategy(String biddingStrategy) {
        this.biddingStrategy = biddingStrategy;
    }

    public PositiveGeoTargetTypeEnum.PositiveGeoTargetType getPositiveGeoTargetType() {
        return positiveGeoTargetType;
    }

    public void setPositiveGeoTargetType(PositiveGeoTargetTypeEnum.PositiveGeoTargetType positiveGeoTargetType) {
        this.positiveGeoTargetType = positiveGeoTargetType;
    }

    public NegativeGeoTargetTypeEnum.NegativeGeoTargetType getNegativeGeoTargetType() {
        return negativeGeoTargetType;
    }

    public void setNegativeGeoTargetType(NegativeGeoTargetTypeEnum.NegativeGeoTargetType negativeGeoTargetType) {
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