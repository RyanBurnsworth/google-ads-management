package com.addyai.models.campaign_criterion;

import com.google.ads.googleads.v11.enums.KeywordMatchTypeEnum;

public class KeywordDetails extends CampaignCriterionDetails {
    private String keywordText = "";

    private KeywordMatchTypeEnum.KeywordMatchType keywordMatchType = KeywordMatchTypeEnum.KeywordMatchType.UNKNOWN;

    public String getKeywordText() {
        return keywordText;
    }

    public void setKeywordText(String keywordText) {
        this.keywordText = keywordText;
    }

    public KeywordMatchTypeEnum.KeywordMatchType getKeywordMatchType() {
        return keywordMatchType;
    }

    public void setKeywordMatchType(KeywordMatchTypeEnum.KeywordMatchType keywordMatchType) {
        this.keywordMatchType = keywordMatchType;
    }
}
