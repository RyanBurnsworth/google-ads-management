package com.addyai.models.campaign_criterion;

import static com.addyai.utils.misc.Constants.CRITERION_TYPE_DEVICE;
import static com.addyai.utils.misc.Constants.DEVICE_TYPE_DESKTOP;

public class DeviceDetails extends CriterionDetails {
    private int deviceType = DEVICE_TYPE_DESKTOP;

    @Override
    public int getCriterionType() {
        return CRITERION_TYPE_DEVICE;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }
}
