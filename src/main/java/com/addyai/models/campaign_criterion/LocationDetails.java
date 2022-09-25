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

import static com.addyai.utils.misc.Constants.DEFAULT_COUNTRY_CODE;
import static com.addyai.utils.misc.Constants.DEFAULT_LOCALE;

public class LocationDetails extends CriterionDetails {
    /**
     * The geo targeting constant associated to a location
     *
     * @see <a href="https://developers.google.com/google-ads/api/fields/v11/geo_target_constant">Geo Targeting Constants</a>
     */
    private String geoTargetingConstant = "";

    /**
     * The locale of the targeting criterion
     * Defaults to 'en'
     */
    private String locale = DEFAULT_LOCALE;

    /**
     * The country code of the targeting criterion
     * Defaults to 'US'
     */
    private String countryCode = DEFAULT_COUNTRY_CODE;

    /**
     * The location to be targeted with this criterion
     */
    private String location = "";

    public String getGeoTargetingConstant() {
        return geoTargetingConstant;
    }

    public void setGeoTargetingConstant(String geoTargetingConstant) {
        this.geoTargetingConstant = geoTargetingConstant;
    }

    public String getLocale() {
        return "en";
    } // currently only using 'en' for locale

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getCountryCode() {
        return "US";
    } // currently only using US for country code

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
