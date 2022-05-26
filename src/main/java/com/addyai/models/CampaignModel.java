package com.addyai.models;

import com.google.ads.googleads.v10.enums.AdvertisingChannelTypeEnum;
import com.google.ads.googleads.v10.enums.CampaignStatusEnum;

public class CampaignModel {

    private long customerId;

    private String name;

    private String budgetName;

    private String startDate;

    private String endDate;

    private long budget;

    private AdvertisingChannelTypeEnum.AdvertisingChannelType channelType;

    private CampaignStatusEnum.CampaignStatus campaignStatus;

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBudgetName() {
        return budgetName;
    }

    public void setBudgetName(String budgetName) {
        this.budgetName = budgetName;
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

    public long getBudget() {
        return budget;
    }

    public void setBudget(long budget) {
        this.budget = budget * 1000000;
    }

    public AdvertisingChannelTypeEnum.AdvertisingChannelType getChannelType() {
        return channelType;
    }

    public void setChannelType(AdvertisingChannelTypeEnum.AdvertisingChannelType channelType) {
        this.channelType = channelType;
    }

    public CampaignStatusEnum.CampaignStatus getCampaignStatus() {
        return campaignStatus;
    }

    public void setCampaignStatus(CampaignStatusEnum.CampaignStatus campaignStatus) {
        this.campaignStatus = campaignStatus;
    }
}
