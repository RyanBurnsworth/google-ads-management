package com.addyai.models.campaign_criterion;

import static com.addyai.utils.misc.Constants.LANGUAGE_CONSTANT_EN;

public class LanguageDetails extends CriterionDetails {
    private String languageCode = LANGUAGE_CONSTANT_EN;

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }
}
