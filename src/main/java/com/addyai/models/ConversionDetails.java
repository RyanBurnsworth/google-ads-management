package com.addyai.models;

public class ConversionDetails {
    private String actionId;
    private String actionName;
    private String resourceName;
    private int status;
    private int type;
    private double defaultValue;
    private String currencyCode;
    private long callDurationSeconds;

    private int actionCountingType;
    private int category;

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(double defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public long getCallDurationSeconds() {
        return callDurationSeconds;
    }

    public void setCallDurationSeconds(long callDurationSeconds) {
        this.callDurationSeconds = callDurationSeconds;
    }

    public int getActionCountingType() {
        return actionCountingType;
    }

    public void setActionCountingType(int actionCountingType) {
        this.actionCountingType = actionCountingType;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
