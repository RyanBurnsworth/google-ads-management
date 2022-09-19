/*
 * Copyright (c) 2022.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. This program
 * is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 *
 */

package com.addyai.models.campaign_criterion;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = NegativeKeywordDetails.class, name = "keyword"),
        @JsonSubTypes.Type(value = AdScheduleDetails.class, name = "ad_schedule"),
        @JsonSubTypes.Type(value = ProximityDetails.class, name = "proximity"),
        @JsonSubTypes.Type(value = LanguageDetails.class, name = "language"),
        @JsonSubTypes.Type(value = LocationDetails.class, name = "location"),
        @JsonSubTypes.Type(value = DeviceDetails.class, name = "device")
})
public abstract class CriterionDetails {
    public long campaignCriterionId = 0L;

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
