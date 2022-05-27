package com.addyai.models;

import com.google.ads.googleads.v10.common.AdTextAsset;
import com.google.protobuf.ProtocolStringList;

import java.util.List;

public class ResponsiveSearchAdModel {
    private long id;

    private long adGroupId;

    private List<AdTextAsset> headlinesList;

    private List<AdTextAsset> descriptionList;

    private String path1;

    private String path2;

    private String finalUrl;

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

    public List<AdTextAsset> getHeadlinesList() {
        return headlinesList;
    }

    public void setHeadlinesList(List<AdTextAsset> headlinesList) {
        this.headlinesList = headlinesList;
    }

    public List<AdTextAsset> getDescriptionList() {
        return descriptionList;
    }

    public void setDescriptionList(List<AdTextAsset> descriptionList) {
        this.descriptionList = descriptionList;
    }

    public String getPath1() {
        return path1;
    }

    public void setPath1(String path1) {
        this.path1 = path1;
    }

    public String getPath2() {
        return path2;
    }

    public void setPath2(String path2) {
        this.path2 = path2;
    }

    public String getFinalUrl() {
        return finalUrl;
    }

    public void setFinalUrl(String finalUrl) {
        this.finalUrl = finalUrl;
    }
}
