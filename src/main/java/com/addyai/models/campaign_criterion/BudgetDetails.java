package com.addyai.models.campaign_criterion;

public class BudgetDetails {
    private long budgetId;

    private String name;

    private int deliveryMethod;

    private boolean isShared;

    private String resourceName;

    private double dailyBudgetAmount;

    public long getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(long budgetId) {
        this.budgetId = budgetId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(int deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public boolean isShared() {
        return isShared;
    }

    public void setShared(boolean shared) {
        isShared = shared;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public double getDailyBudgetAmount() {
        return dailyBudgetAmount;
    }

    public void setDailyBudgetAmount(double dailyBudgetAmount) {
        this.dailyBudgetAmount = dailyBudgetAmount;
    }
}
