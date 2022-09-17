package com.addyai.models.campaign_criterion.ext;

import com.addyai.models.campaign_criterion.CampaignCriterionDetails;

public class LocationDetails extends CampaignCriterionDetails {
    private String geoTargetingConstant = "";

    public String getGeoTargetingConstant() {
        return geoTargetingConstant;
    }

    public void setGeoTargetingConstant(String geoTargetingConstant) {
        this.geoTargetingConstant = geoTargetingConstant;
    }
}
