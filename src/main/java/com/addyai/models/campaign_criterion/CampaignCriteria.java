package com.addyai.models.campaign_criterion;

import com.google.ads.googleads.v11.enums.CampaignCriterionStatusEnum;
import com.google.ads.googleads.v11.enums.CriterionTypeEnum;

public abstract class CampaignCriteria {
    public long campaignCriterionId = 0L;

    private long campaignId = 0L;

    private String campaignName = "";

    private boolean isNegative = false;

    private float bidModifier = 0.0f;

    private CriterionTypeEnum.CriterionType criterionType = CriterionTypeEnum.CriterionType.UNKNOWN;

    private CampaignCriterionStatusEnum.CampaignCriterionStatus status = CampaignCriterionStatusEnum.CampaignCriterionStatus.ENABLED;

    public long getCampaignCriterionId() {
        return campaignCriterionId;
    }

    public void setCampaignCriterionId(long campaignCriterionId) {
        this.campaignCriterionId = campaignCriterionId;
    }

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

    public boolean isNegative() {
        return isNegative;
    }

    public void setNegative(boolean negative) {
        isNegative = negative;
    }

    public float getBidModifier() {
        return bidModifier;
    }

    public void setBidModifier(float bidModifier) {
        this.bidModifier = bidModifier;
    }

    public CriterionTypeEnum.CriterionType getCriterionType() {
        return criterionType;
    }

    public void setCriterionType(CriterionTypeEnum.CriterionType criterionType) {
        this.criterionType = criterionType;
    }

    public CampaignCriterionStatusEnum.CampaignCriterionStatus getStatus() {
        return status;
    }

    public void setStatus(CampaignCriterionStatusEnum.CampaignCriterionStatus status) {
        this.status = status;
    }
}
