package com.addyai.models.ads;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static com.addyai.utils.misc.Constants.RESPONSIVE_AD_TYPE;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ResponsiveSearchAdDetails.class, name = RESPONSIVE_AD_TYPE)
})
public abstract class AdDetails {
    private long adId = 0;

    private String adType = "";
    private String adName = "";

    private String adGroupResourceName = "";

    private int adStatus = 0;

    public long getAdId() {
        return adId;
    }

    public void setAdId(long adId) {
        this.adId = adId;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public String getAdGroupResourceName() {
        return adGroupResourceName;
    }

    public void setAdGroupResourceName(String adGroupResourceName) {
        this.adGroupResourceName = adGroupResourceName;
    }

    public int getAdStatus() {
        return adStatus;
    }

    public void setAdStatus(int adStatus) {
        this.adStatus = adStatus;
    }
}
