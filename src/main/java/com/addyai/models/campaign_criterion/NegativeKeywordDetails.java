package com.addyai.models.campaign_criterion;

import static com.addyai.utils.misc.Constants.CRITERION_TYPE_KEYWORD;
import static com.addyai.utils.misc.Constants.KEYWORD_MATCH_TYPE_BROAD;

public class NegativeKeywordDetails extends CriterionDetails {
    private String keywordText = "";

    private int keywordMatchType = KEYWORD_MATCH_TYPE_BROAD;

    @Override
    public int getCriterionType() {
        return CRITERION_TYPE_KEYWORD;
    }

    @Override
    public boolean isNegative() {
        return true;
    }

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
