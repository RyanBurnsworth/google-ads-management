package com.addyai.models.campaign_criterion;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = KeywordDetails.class, name = "keyword"),
        @JsonSubTypes.Type(value = AdScheduleDetails.class, name = "ad_schedule"),
        @JsonSubTypes.Type(value = ProximityDetails.class, name = "proximity"),
        @JsonSubTypes.Type(value = LanguageDetails.class, name = "language"),
        @JsonSubTypes.Type(value = LocationDetails.class, name = "location"),
        @JsonSubTypes.Type(value = DeviceDetails.class, name = "device")
})
public abstract class CampaignCriterionDetails {
    public long campaignCriterionId = 0L;

    private long campaignId = 0L;

    private String campaignResourceName = "";

    private boolean isNegative = false;

    private float bidModifier = 0.0f;

    private int criterionType = 1;

    private int status = 2;

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

    public String getCampaignResourceName() {
        return campaignResourceName;
    }

    public void setCampaignResourceName(String campaignResourceName) {
        this.campaignResourceName = campaignResourceName;
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

    public int getCriterionType() {
        return criterionType;
    }

    public void setCriterionType(int criterionType) {
        this.criterionType = criterionType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
