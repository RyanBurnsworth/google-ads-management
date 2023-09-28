package com.addyai.models;

import com.google.ads.googleads.v14.enums.AssetFieldTypeEnum;

public class AssetLinkerDetails {
    private String assetResourceName;
    private String campaignResourceName = "";
    private AssetFieldTypeEnum.AssetFieldType assetType;

    public String getAssetResourceName() {
        return assetResourceName;
    }

    public void setAssetResourceName(String assetResourceName) {
        this.assetResourceName = assetResourceName;
    }

    public String getCampaignResourceName() {
        return campaignResourceName;
    }

    public void setCampaignResourceName(String campaignResourceName) {
        this.campaignResourceName = campaignResourceName;
    }

    public AssetFieldTypeEnum.AssetFieldType getAssetType() {
        return assetType;
    }

    public void setAssetType(AssetFieldTypeEnum.AssetFieldType assetType) {
        this.assetType = assetType;
    }
}
