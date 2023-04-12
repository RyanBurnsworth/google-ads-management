package com.addyai.models.assets;

import com.addyai.models.AdSchedulingDetails;

import java.util.List;

public class CalloutExtensionDetails extends AssetDetails {
    private String text;

    private String startDate;

    private String endDate;

    private List<AdSchedulingDetails> adSchedulingDetailsList;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public List<AdSchedulingDetails> getAdSchedulingDetailsList() {
        return adSchedulingDetailsList;
    }

    public void setAdSchedulingDetailsList(List<AdSchedulingDetails> adSchedulingDetailsList) {
        this.adSchedulingDetailsList = adSchedulingDetailsList;
    }
}
