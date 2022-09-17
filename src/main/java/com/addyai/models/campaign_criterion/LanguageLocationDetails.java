package com.addyai.models.campaign_criterion;

import com.google.ads.googleads.v11.enums.CampaignCriterionStatusEnum;
import com.google.ads.googleads.v11.enums.CriterionTypeEnum;

public class LanguageLocationDetails extends CampaignCriterionDetails {

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
