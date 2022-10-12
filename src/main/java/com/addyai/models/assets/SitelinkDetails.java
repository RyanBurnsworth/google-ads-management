package com.addyai.models.assets;

import com.addyai.utils.helpers.DateTimeHelper;

import java.util.List;

public class SitelinkDetails extends AssetDetails{
    @Override
    public long getAssetId() {
        return super.getAssetId();
    }

    @Override
    public void setAssetId(long assetId) {
        super.setAssetId(assetId);
    }

    @Override
    public String getAssetName() {
        return super.getAssetName();
    }

    @Override
    public void setAssetName(String assetName) {
        super.setAssetName(assetName);
    }

    @Override
    public int getAssetType() {
        return super.getAssetType();
    }

    @Override
    public void setAssetType(int assetType) {
        super.setAssetType(assetType);
    }

    @Override
    public int getAssetSource() {
        return super.getAssetSource();
    }

    @Override
    public void setAssetSource(int assetSource) {
        super.setAssetSource(assetSource);
    }

    @Override
    public List<String> getFinalUrlList() {
        return super.getFinalUrlList();
    }

    @Override
    public void setFinalUrlList(List<String> finalUrlList) {
        super.setFinalUrlList(finalUrlList);
    }

    @Override
    public List<String> getFinalMobileUrlList() {
        return super.getFinalMobileUrlList();
    }

    @Override
    public void setFinalMobileUrlList(List<String> finalMobileUrlList) {
        super.setFinalMobileUrlList(finalMobileUrlList);
    }

    @Override
    public String getFinalUrlSuffix() {
        return super.getFinalUrlSuffix();
    }

    @Override
    public void setFinalUrlSuffix(String finalUrlSuffix) {
        super.setFinalUrlSuffix(finalUrlSuffix);
    }

    private String description1 = "";
    private String description2 = "";
    private String linkText = "";
    private String startDate = DateTimeHelper.getCurrentDate();
    private String endDate = DateTimeHelper.getCurrentDatePlusYears(10);

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

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
