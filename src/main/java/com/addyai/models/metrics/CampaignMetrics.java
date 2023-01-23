package com.addyai.models.metrics;

public class CampaignMetrics {

    private String campaignResourceId;

    private long clicks;

    private long impressions;

    private double ctr;

    private double averageCpc;

    private double conversions;

    private double conversionRate;

    private double costPerConversion;

    private double conversionValue;

    private double invalidClickRate;

    private long invalidClicks;

    private long phoneCalls;

    private long phoneImpressions;

    private double phoneThroughRate;

    private String date;

    public String getCampaignResourceId() {
        return campaignResourceId;
    }

    public void setCampaignResourceId(String campaignResourceId) {
        this.campaignResourceId = campaignResourceId;
    }

    public long getClicks() {
        return clicks;
    }

    public void setClicks(long clicks) {
        this.clicks = clicks;
    }

    public long getImpressions() {
        return impressions;
    }

    public void setImpressions(long impressions) {
        this.impressions = impressions;
    }

    public double getCtr() {
        return ctr;
    }

    public void setCtr(double ctr) {
        this.ctr = ctr;
    }

    public double getAverageCpc() {
        return averageCpc;
    }

    public void setAverageCpc(double averageCpc) {
        this.averageCpc = averageCpc;
    }

    public double getConversions() {
        return conversions;
    }

    public void setConversions(double conversions) {
        this.conversions = conversions;
    }

    public double getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(double conversionRate) {
        this.conversionRate = conversionRate;
    }

    public double getCostPerConversion() {
        return costPerConversion;
    }

    public void setCostPerConversion(double costPerConversion) {
        this.costPerConversion = costPerConversion;
    }

    public double getConversionValue() {
        return conversionValue;
    }

    public void setConversionValue(double conversionValue) {
        this.conversionValue = conversionValue;
    }

    public double getInvalidClickRate() {
        return invalidClickRate;
    }

    public void setInvalidClickRate(double invalidClickRate) {
        this.invalidClickRate = invalidClickRate;
    }

    public long getInvalidClicks() {
        return invalidClicks;
    }

    public void setInvalidClicks(long invalidClicks) {
        this.invalidClicks = invalidClicks;
    }

    public long getPhoneCalls() {
        return phoneCalls;
    }

    public void setPhoneCalls(long phoneCalls) {
        this.phoneCalls = phoneCalls;
    }

    public long getPhoneImpressions() {
        return phoneImpressions;
    }

    public void setPhoneImpressions(long phoneImpressions) {
        this.phoneImpressions = phoneImpressions;
    }

    public double getPhoneThroughRate() {
        return phoneThroughRate;
    }

    public void setPhoneThroughRate(double phoneThroughRate) {
        this.phoneThroughRate = phoneThroughRate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
