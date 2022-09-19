package com.addyai.models.campaign_criterion;

import static com.addyai.utils.misc.Constants.DEFAULT_COUNTRY_CODE;
import static com.addyai.utils.misc.Constants.DEFAULT_LOCALE;

public class LocationDetails extends CriterionDetails {
    private String geoTargetingConstant = "";

    private String locale = DEFAULT_LOCALE;

    private String countryCode = DEFAULT_COUNTRY_CODE;

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
