package com.addyai.models;

import com.google.ads.googleads.v10.enums.AdGroupStatusEnum;

public class AdGroupModel {
    private long id;

    private long customerId;

    private long campaignId;

    private String adgroupName;

    private AdGroupStatusEnum.AdGroupStatus status;

    private String campaignName;

    private long maxCPC;

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

    public String getAdgroupName() {
        return adgroupName;
    }

    public void setAdgroupName(String adgroupName) {
        this.adgroupName = adgroupName;
    }

    public AdGroupStatusEnum.AdGroupStatus getStatus() {
        return status;
    }

    public void setStatus(AdGroupStatusEnum.AdGroupStatus status) {
        this.status = status;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = "/customers/" + this.customerId + "/campaigns/" + this.campaignId;
    }

    public long getMaxCPC() {
        return maxCPC;
    }

    public void setMaxCPC(long maxCPC) {
        this.maxCPC = maxCPC;
    }

    public long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(long campaignId) {
        this.campaignId = campaignId;
    }
}
