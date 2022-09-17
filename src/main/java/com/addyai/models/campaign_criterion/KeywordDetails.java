package com.addyai.models.campaign_criterion;

public class KeywordDetails extends CampaignCriterionDetails {
    private String keywordText = "";

    private int keywordMatchType = 1;

    public String getKeywordText() {
        return keywordText;
    }

    public void setKeywordText(String keywordText) {
        this.keywordText = keywordText;
    }

    public int getKeywordMatchType() {
        return keywordMatchType;
    }

    public void setKeywordMatchType(int keywordMatchType) {
        this.keywordMatchType = keywordMatchType;
    }
}
