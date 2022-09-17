package com.addyai.models.campaign_criterion;

public class ProximityDetails extends CampaignCriterionDetails {
    private String cityName = "";

    private String countryCode = "";

    private String postalCode = "";

    private String provinceCode = "";

    private String provinceName = "";

    private String streetAddress = "";

    private float microLongitude = 0.0f;

    private float microLatitude = 0.0f;

    private double radius = 0.0;

    private int radiusUnits;

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

    public float getMicroLongitude() {
        return microLongitude;
    }

    public void setMicroLongitude(int microLongitude) {
        this.microLongitude = microLongitude;
    }

    public float getMicroLatitude() {
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

    public int getRadiusUnits() {
        return radiusUnits;
    }

    public void setRadiusUnits(int radiusUnits) {
        this.radiusUnits = radiusUnits;
    }
}
