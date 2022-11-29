package com.addyai.models.assets;

import com.addyai.models.AdSchedulingDetails;

import java.util.ArrayList;
import java.util.List;

public class CallExtensionDetails extends AssetDetails {
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

    private String countryCode;
    private String phoneNumber;
    private List<AdSchedulingDetails> adSchedulingDetails = new ArrayList<>();

    private int conversionId;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<AdSchedulingDetails> getAdSchedulingDetails() {
        return adSchedulingDetails;
    }

    public void setAdSchedulingDetails(List<AdSchedulingDetails> adSchedulingDetails) {
        this.adSchedulingDetails = adSchedulingDetails;
    }

    public int getConversionId() {
        return conversionId;
    }

    public void setConversionId(int conversionId) {
        this.conversionId = conversionId;
    }
}
