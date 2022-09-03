package com.addyai.models.campaign_criterion;

import com.google.ads.googleads.v11.enums.CampaignCriterionStatusEnum;
import com.google.ads.googleads.v11.enums.CriterionTypeEnum;

public class LanguageLocationCriteria extends CampaignCriteria {

    private String geoTargetingConstant = "";

    private String languageCode = "EN";

    public String getGeoTargetingConstant() {
        return geoTargetingConstant;
    }

    public void setGeoTargetingConstant(String geoTargetingConstant) {
        this.geoTargetingConstant = geoTargetingConstant;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    @Override
    public long getCampaignCriterionId() {
        return super.getCampaignCriterionId();
    }

    @Override
    public void setCampaignCriterionId(long campaignCriterionId) {
        super.setCampaignCriterionId(campaignCriterionId);
    }

    @Override
    public long getCampaignId() {
        return super.getCampaignId();
    }

    @Override
    public void setCampaignId(long campaignId) {
        super.setCampaignId(campaignId);
    }

    @Override
    public String getCampaignName() {
        return super.getCampaignName();
    }

    @Override
    public void setCampaignName(String campaignName) {
        super.setCampaignName(campaignName);
    }

    @Override
    public boolean isNegative() {
        return super.isNegative();
    }

    @Override
    public void setNegative(boolean negative) {
        super.setNegative(negative);
    }

    @Override
    public float getBidModifier() {
        return super.getBidModifier();
    }

    @Override
    public void setBidModifier(float bidModifier) {
        super.setBidModifier(bidModifier);
    }

    @Override
    public CriterionTypeEnum.CriterionType getCriterionType() {
        return super.getCriterionType();
    }

    @Override
    public void setCriterionType(CriterionTypeEnum.CriterionType criterionType) {
        super.setCriterionType(criterionType);
    }

    @Override
    public CampaignCriterionStatusEnum.CampaignCriterionStatus getStatus() {
        return super.getStatus();
    }

    @Override
    public void setStatus(CampaignCriterionStatusEnum.CampaignCriterionStatus status) {
        super.setStatus(status);
    }
}
