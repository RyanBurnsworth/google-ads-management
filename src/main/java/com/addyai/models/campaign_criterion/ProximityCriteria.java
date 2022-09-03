package com.addyai.models.campaign_criterion;

import com.google.ads.googleads.v11.enums.CampaignCriterionStatusEnum;
import com.google.ads.googleads.v11.enums.CriterionTypeEnum;
import com.google.ads.googleads.v11.enums.ProximityRadiusUnitsEnum;

public class ProximityCriteria extends CampaignCriteria {
    private String cityName = "";

    private String countryCode = "";

    private String postalCode = "";

    private String provinceCode = "";

    private String provinceName = "";

    private String streetAddress = "";

    private int microLongitude = 0;

    private int microLatitude = 0;

    private double radius = 0.0;

    private ProximityRadiusUnitsEnum.ProximityRadiusUnits radiusUnits;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public int getMicroLongitude() {
        return microLongitude;
    }

    public void setMicroLongitude(int microLongitude) {
        this.microLongitude = microLongitude;
    }

    public int getMicroLatitude() {
        return microLatitude;
    }

    public void setMicroLatitude(int microLatitude) {
        this.microLatitude = microLatitude;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public ProximityRadiusUnitsEnum.ProximityRadiusUnits getRadiusUnits() {
        return radiusUnits;
    }

    public void setRadiusUnits(ProximityRadiusUnitsEnum.ProximityRadiusUnits radiusUnits) {
        this.radiusUnits = radiusUnits;
    }

    @Override
    public long getCampaignCriterionId() {
        return super.getCampaignCriterionId();
    }

    @Override
    public void setCampaignCriterionId(long campaignCriterionId) {
        super.setCampaignCriterionId(campaignCriterionId);
    }

    @Override
    public long getCampaignId() {
        return super.getCampaignId();
    }

    @Override
    public void setCampaignId(long campaignId) {
        super.setCampaignId(campaignId);
    }

    @Override
    public String getCampaignName() {
        return super.getCampaignName();
    }

    @Override
    public void setCampaignName(String campaignName) {
        super.setCampaignName(campaignName);
    }

    @Override
    public boolean isNegative() {
        return super.isNegative();
    }

    @Override
    public void setNegative(boolean negative) {
        super.setNegative(negative);
    }

    @Override
    public float getBidModifier() {
        return super.getBidModifier();
    }

    @Override
    public void setBidModifier(float bidModifier) {
        super.setBidModifier(bidModifier);
    }

    @Override
    public CriterionTypeEnum.CriterionType getCriterionType() {
        return super.getCriterionType();
    }

    @Override
    public void setCriterionType(CriterionTypeEnum.CriterionType criterionType) {
        super.setCriterionType(criterionType);
    }

    @Override
    public CampaignCriterionStatusEnum.CampaignCriterionStatus getStatus() {
        return super.getStatus();
    }

    @Override
    public void setStatus(CampaignCriterionStatusEnum.CampaignCriterionStatus status) {
        super.setStatus(status);
    }
}
