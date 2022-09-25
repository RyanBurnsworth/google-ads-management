/*
 * Copyright (c) 2022.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. This program
 * is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 *
 */

package com.addyai.models.campaign_criterion;

import static com.addyai.utils.misc.Constants.RADIUS_UNITS_MILES;

public class ProximityDetails extends CriterionDetails {
    /**
     * The city to be targeted with this criterion
     */
    private String cityName = "";

    /**
     * The country code to be targeted with this criterion
     */
    private String countryCode = "";

    /**
     * The postal code to be targeted with this criterion
     */
    private String postalCode = "";

    /**
     * The province code to be targeted (For non-US proximity targeting)
     */
    private String provinceCode = "";

    /**
     * The province name to be targeted (For non-US promixity targeting)
     */
    private String provinceName = "";

    /**
     * The street address for targeting
     */
    private String streetAddress = "";

    /**
     * The longitude value to be used for targeting
     */
    private float longitude = 0.0f;

    /**
     * The latitude value to be used for targeting
     */
    private float latitude = 0.0f;

    /**
     * The radius of the proximity to target
     */
    private double radius = 0.0;

    /**
     * The units of the radius value (Miles or Kilometers)
     */
    private int radiusUnits = RADIUS_UNITS_MILES;

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

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
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
