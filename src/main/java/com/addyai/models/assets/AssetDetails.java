package com.addyai.models.assets;

import com.google.ads.googleads.v11.enums.AssetSourceEnum;
import com.google.ads.googleads.v11.enums.AssetTypeEnum;

import java.util.ArrayList;
import java.util.List;

public class AssetDetails {
    private long assetId = 0L;
    private String assetName = "";
    private int assetType = AssetTypeEnum.AssetType.UNKNOWN_VALUE;
    private int assetSource = AssetSourceEnum.AssetSource.UNKNOWN_VALUE;
    private List<String> finalUrlList = new ArrayList<>();
    private List<String> finalMobileUrlList = new ArrayList<>();
    private String finalUrlSuffix = "";

    public long getAssetId() {
        return assetId;
    }

    public void setAssetId(long assetId) {
        this.assetId = assetId;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public int getAssetType() {
        return assetType;
    }

    public void setAssetType(int assetType) {
        this.assetType = assetType;
    }

    public int getAssetSource() {
        return assetSource;
    }

    public void setAssetSource(int assetSource) {
        this.assetSource = assetSource;
    }

    public List<String> getFinalUrlList() {
        return finalUrlList;
    }

    public void setFinalUrlList(List<String> finalUrlList) {
        this.finalUrlList = finalUrlList;
    }

    public List<String> getFinalMobileUrlList() {
        return finalMobileUrlList;
    }

    public void setFinalMobileUrlList(List<String> finalMobileUrlList) {
        this.finalMobileUrlList = finalMobileUrlList;
    }

    public String getFinalUrlSuffix() {
        return finalUrlSuffix;
    }

    public void setFinalUrlSuffix(String finalUrlSuffix) {
        this.finalUrlSuffix = finalUrlSuffix;
    }
}
