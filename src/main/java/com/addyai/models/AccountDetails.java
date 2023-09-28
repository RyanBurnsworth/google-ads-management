package com.addyai.models;

import com.google.ads.googleads.v14.enums.CustomerStatusEnum;

public class AccountDetails {
    String customerId = "";
    String resourceName = "";
    String descriptiveName = "";

    int status = CustomerStatusEnum.CustomerStatus.UNKNOWN_VALUE;
    String currencyCode = "";
    String timeZone = "";
    double optimizationScore = 0.0;
    boolean isManager = false;
    boolean isCallReportingEnabled = false;
    boolean isCallConversionReportingEnabled = false;
    String callConversionActionResourceName = "";
    String remarketingTag = "";

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getDescriptiveName() {
        return descriptiveName;
    }

    public void setDescriptiveName(String descriptiveName) {
        this.descriptiveName = descriptiveName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public double getOptimizationScore() {
        return optimizationScore;
    }

    public void setOptimizationScore(double optimizationScore) {
        this.optimizationScore = optimizationScore;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }

    public boolean isCallReportingEnabled() {
        return isCallReportingEnabled;
    }

    public void setCallReportingEnabled(boolean callReportingEnabled) {
        isCallReportingEnabled = callReportingEnabled;
    }

    public boolean isCallConversionReportingEnabled() {
        return isCallConversionReportingEnabled;
    }

    public void setCallConversionReportingEnabled(boolean callConversionReportingEnabled) {
        isCallConversionReportingEnabled = callConversionReportingEnabled;
    }

    public String getCallConversionActionResourceName() {
        return callConversionActionResourceName;
    }

    public void setCallConversionActionResourceName(String callConversionActionResourceName) {
        this.callConversionActionResourceName = callConversionActionResourceName;
    }

    public String getRemarketingTag() {
        return remarketingTag;
    }

    public void setRemarketingTag(String remarketingTag) {
        this.remarketingTag = remarketingTag;
    }
}
