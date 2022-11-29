package com.addyai.models.assets;

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
    private int dayOfWeek;
    private int startHour;
    private int endHour;
    private int startMinute;
    private int endMinute;

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

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    public int getConversionId() {
        return conversionId;
    }

    public void setConversionId(int conversionId) {
        this.conversionId = conversionId;
    }
}
