package com.addyai.models;

import static com.addyai.utils.Constants.ADVERTISING_TYPE_SEARCH;

public class CampaignDetails {
    private long campaignId = 0L;

    private String campaignName = "";

    private String campaignResourceName = "";

    private String status = "PAUSED";

    private String advertisingChannelType = ADVERTISING_TYPE_SEARCH;

    private int positiveGeoTargetType = 7;

    private int negativeGeoTargetType = 5;

    private boolean isEnhancedCpcEnabled = false;

    private String startDate = "";

    private String endDate = "";

    private boolean isTargetingSearchNetwork = true;

    private boolean isTargetingContentNetwork = false;

    private boolean isTargetingPartnerSearchNetwork = false;

    private String budgetResourceName;

    private BudgetDetails budgetDetails = new BudgetDetails();

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdvertisingChannelType() {
        return advertisingChannelType;
    }

    public void setAdvertisingChannelType(String advertisingChannelType) {
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

    public boolean isTargetingPartnerSearchNetwork() {
        return isTargetingPartnerSearchNetwork;
    }

    public void setTargetingPartnerSearchNetwork(boolean targetingPartnerSearchNetwork) {
        isTargetingPartnerSearchNetwork = targetingPartnerSearchNetwork;
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
}
