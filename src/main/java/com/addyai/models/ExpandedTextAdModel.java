package com.addyai.models;

import com.google.ads.googleads.v10.enums.AdGroupAdStatusEnum;

public class ExpandedTextAdModel {
    private long id;

    private long adGroupId;

    private String headlinePart1;

    private String headlinePart2;

    private String headlinePart3;

    private String description1;

    private String description2;

    private String path1;

    private String path2;

    private AdGroupAdStatusEnum.AdGroupAdStatus status;

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

    public String getHeadlinePart1() {
        return headlinePart1;
    }

    public void setHeadlinePart1(String headlinePart1) {
        this.headlinePart1 = headlinePart1;
    }

    public String getHeadlinePart2() {
        return headlinePart2;
    }

    public void setHeadlinePart2(String headlinePart2) {
        this.headlinePart2 = headlinePart2;
    }

    public String getHeadlinePart3() {
        return headlinePart3;
    }

    public void setHeadlinePart3(String headlinePart3) {
        this.headlinePart3 = headlinePart3;
    }

    public String getDescription1() {
        return description1;
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
    }

    public String getDescription2() {
        return description2;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
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

    public AdGroupAdStatusEnum.AdGroupAdStatus getStatus() {
        return status;
    }

    public void setStatus(AdGroupAdStatusEnum.AdGroupAdStatus status) {
        this.status = status;
    }
}
