package com.addyai.models.ads;

import java.util.List;

public class ResponsiveSearchAdDetails extends AdDetails {
    private List<String> headlines;
    private List<String> descriptions;
    private List<String> paths;
    private String finalUrl;

    @Override
    public long getAdId() {
        return super.getAdId();
    }

    @Override
    public void setAdId(long adId) {
        super.setAdId(adId);
    }

    @Override
    public String getAdName() {
        return super.getAdName();
    }

    @Override
    public void setAdName(String adName) {
        super.setAdName(adName);
    }

    @Override
    public String getAdGroupResourceName() {
        return super.getAdGroupResourceName();
    }

    @Override
    public void setAdGroupResourceName(String adGroupResourceName) {
        super.setAdGroupResourceName(adGroupResourceName);
    }

    @Override
    public int getAdStatus() {
        return super.getAdStatus();
    }

    @Override
    public void setAdStatus(int adStatus) {
        super.setAdStatus(adStatus);
    }

    public List<String> getHeadlines() {
        return headlines;
    }

    public void setHeadlines(List<String> headlines) {
        this.headlines = headlines;
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<String> descriptions) {
        this.descriptions = descriptions;
    }

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }

    public String getFinalUrl() {
        return finalUrl;
    }

    public void setFinalUrl(String finalUrl) {
        this.finalUrl = finalUrl;
    }

}
