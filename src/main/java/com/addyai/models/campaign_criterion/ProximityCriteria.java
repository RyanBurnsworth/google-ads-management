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
}
