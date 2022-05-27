package com.addyai.models;

public class KeywordStats {
    private long campaignId;
    private Long adGroupId;
    private Long keywordId;
    private String keywordText;
    private String matchType;
    private String adgroupName;
    private String campaignName;
    private Long impressions;
    private Long clicks;
    private Long cost;
    private double avg_cpc;
    private double interaction_rate;
    private double conversions;

    public long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(long campaignId) {
        this.campaignId = campaignId;
    }

    public Long getAdGroupId() {
        return adGroupId;
    }

    public void setAdGroupId(Long adGroupId) {
        this.adGroupId = adGroupId;
    }

    public Long getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(Long keywordId) {
        this.keywordId = keywordId;
    }

    public String getKeywordText() {
        return keywordText;
    }

    public void setKeywordText(String keywordText) {
        this.keywordText = keywordText;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public String getAdgroupName() {
        return adgroupName;
    }

    public void setAdgroupName(String adgroupName) {
        this.adgroupName = adgroupName;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public Long getImpressions() {
        return impressions;
    }

    public void setImpressions(Long impressions) {
        this.impressions = impressions;
    }

    public Long getClicks() {
        return clicks;
    }

    public void setClicks(Long clicks) {
        this.clicks = clicks;
    }

    public double getAvg_cpc() {
        return avg_cpc;
    }

    public void setAvg_cpc(double avg_cpc) {
        this.avg_cpc = avg_cpc;
    }

    public double getInteraction_rate() {
        return interaction_rate;
    }

    public void setInteraction_rate(double interaction_rate) {
        this.interaction_rate = interaction_rate;
    }

    public double getConversions() {
        return conversions;
    }

    public void setConversions(double conversions) {
        this.conversions = conversions;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }
}