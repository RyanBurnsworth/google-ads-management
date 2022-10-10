package com.addyai.models.assets;

import com.addyai.utils.helpers.DateTimeHelper;

public class SitelinkAssetDetails extends AssetDetails{
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
