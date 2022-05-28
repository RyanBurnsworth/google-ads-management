package com.addyai.models;

import com.addyai.utils.Utils;
import com.google.ads.googleads.v10.enums.AdGroupStatusEnum;
import com.google.ads.googleads.v10.enums.AdGroupTypeEnum;

public class AdGroupModel {
    private long id;

    private long customerId;

    private String adgroupName;

    private AdGroupStatusEnum.AdGroupStatus status;

    private AdGroupTypeEnum.AdGroupType type;

    private String campaignName;

    private String maxCPC;

    private long campaignId;

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

    public String getMaxCPC() {
        return maxCPC;
    }

    public void setMaxCPC(String maxCPC) {
        this.maxCPC = String.valueOf(Utils.convertMicrosValue(maxCPC));
    }

    public long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(long campaignId) {
        this.campaignId = campaignId;
    }
}
