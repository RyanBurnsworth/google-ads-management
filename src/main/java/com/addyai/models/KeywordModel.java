package com.addyai.models;

import com.google.ads.googleads.v10.enums.KeywordMatchTypeEnum.KeywordMatchType;

public class KeywordModel {
    public long id;

    public long adGroupId;

    public String text;

    public long cpcBid;

    public KeywordMatchType matchType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAdGroupId() {
        return adGroupId;
    }

    public void setAdGroupId(long adGroupId) {
        this.adGroupId = adGroupId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getCpcBid() {
        return cpcBid * 1000000;
    }

    public void setCpcBid(long cpcBid) {
        this.cpcBid = cpcBid;
    }

    public KeywordMatchType getMatchType() {
        return matchType;
    }

    public void setMatchType(KeywordMatchType matchType) {
        this.matchType = matchType;
    }
}

