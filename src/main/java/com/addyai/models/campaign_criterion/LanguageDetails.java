package com.addyai.models.campaign_criterion;

import com.addyai.models.campaign_criterion.CampaignCriterionDetails;

public class LanguageDetails extends CampaignCriterionDetails {
    private String languageCode = "EN";

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }
}
