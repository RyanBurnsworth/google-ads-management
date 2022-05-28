package com.addyai.models;

import com.addyai.utils.Utils;
import com.google.ads.googleads.v10.enums.AdvertisingChannelTypeEnum;
import com.google.ads.googleads.v10.enums.CampaignStatusEnum;
import com.google.ads.googleads.v10.resources.CampaignBudget;

public class CampaignModel {
    private long id;

    private long customerId;

    private String name;

    private String budgetName;

    private CampaignBudget budget;

    private AdvertisingChannelTypeEnum.AdvertisingChannelType channelType;

    private CampaignStatusEnum.CampaignStatus campaignStatus;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public CampaignBudget getBudget() {
        return budget;
    }

    public void setBudget(CampaignBudget budget) {
        this.budget = budget;
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
