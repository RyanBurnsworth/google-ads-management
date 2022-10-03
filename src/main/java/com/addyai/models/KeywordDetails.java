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

package com.addyai.models;

public class KeywordDetails {
    private long keywordId = 0L;

    private String keywordResourceName = "";

    private String adGroupResourceName = "";

    private String keywordText = "";

    private int keywordMatchType = -1;

    private int status = -1;

    private double cpcBid = 0.0;

    public long getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(long keywordId) {
        this.keywordId = keywordId;
    }

    public String getKeywordResourceName() {
        return keywordResourceName;
    }

    public void setKeywordResourceName(String keywordResourceName) {
        this.keywordResourceName = keywordResourceName;
    }

    public String getAdGroupResourceName() {
        return adGroupResourceName;
    }

    public void setAdGroupResourceName(String adGroupResourceName) {
        this.adGroupResourceName = adGroupResourceName;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getCpcBid() {
        return cpcBid;
    }

    public void setCpcBid(double cpcBid) {
        this.cpcBid = cpcBid;
    }
}
