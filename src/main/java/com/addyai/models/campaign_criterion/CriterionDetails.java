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
    /**
     * The ID of the criterion resource
     */
    public long campaignCriterionId = 0L;

    /**
     * The resource name for the parent campaign
     * Format: <b>/customers/{customerId}/campaigns/{campaignId}</b>
     */
    private String campaignResourceName = "";

    /**
     * The resource name for the criterion
     * Format: <b>/customers/{customerId}/campaignCriterion/{campaignId~criterionId}</b>
     */
    private String criterionResourceName = "";

    /**
     * A flag declaring whether this criterion is a negative or not
     * Defaults to false
     */
    private boolean isNegative = false;

    /**
     * The bid modifier to be used with this criterion
     * Modify bids for a criterion from -100% - +900%
     * Range: 0.0 - 10.0 and -1.0 if not set
     */
    private float bidModifier = 0.0f;

    /**
     * The identifier for the type of criterion
     *
     * @see com.google.ads.googleads.v14.enums.CriterionTypeEnum.CriterionType
     */
    private int criterionType = 1;

    /**
     * The status of the criterion
     *
     * @see com.google.ads.googleads.v14.enums.CampaignCriterionStatusEnum.CampaignCriterionStatus
     */
    private int status = 2;

    public long getCampaignCriterionId() {
        return campaignCriterionId;
    }

    public void setCampaignCriterionId(long campaignCriterionId) {
        this.campaignCriterionId = campaignCriterionId;
    }

    public String getCriterionResourceName() {
        return criterionResourceName;
    }

    public void setCriterionResourceName(String criterionResourceName) {
        this.criterionResourceName = criterionResourceName;
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
