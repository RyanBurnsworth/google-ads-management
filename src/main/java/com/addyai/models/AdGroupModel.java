package com.addyai.models;

import com.google.ads.googleads.v10.enums.AdGroupStatusEnum;
import com.google.ads.googleads.v10.enums.AdGroupTypeEnum;
import com.google.ads.googleads.v10.utils.ResourceNames;

public class AdGroupModel {
    private long customerId;

    private String adgroupName;

    private AdGroupStatusEnum.AdGroupStatus status;

    private AdGroupTypeEnum.AdGroupType type;

    private String campaignName;

    private long maxCPC;

    private long campaignId;

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

    public AdGroupTypeEnum.AdGroupType getType() {
        return type;
    }

    public void setType(AdGroupTypeEnum.AdGroupType type) {
        this.type = type;
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
        this.maxCPC = maxCPC * 1000000;
    }

    public long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(long campaignId) {
        this.campaignId = campaignId;
    }
}
