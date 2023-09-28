package com.addyai.models.assets;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.ads.googleads.v14.enums.AssetSourceEnum;
import com.google.ads.googleads.v14.enums.AssetTypeEnum;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SitelinkDetails.class, name = "sitelinks"),
        @JsonSubTypes.Type(value = CallExtensionDetails.class, name = "call"),
        @JsonSubTypes.Type(value = CalloutExtensionDetails.class, name = "callout"),
})
public abstract class AssetDetails {
    private long assetId = 0L;
    private String assetName = "";
    private int assetType = AssetTypeEnum.AssetType.UNKNOWN_VALUE;
    private int assetSource = AssetSourceEnum.AssetSource.UNKNOWN_VALUE;

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
}
