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

package com.addyai.models;

import com.addyai.utils.helpers.DateHelper;

import static com.addyai.utils.misc.Constants.BUDGET_DELIVERY_METHOD_STANDARD;
import static com.addyai.utils.misc.Constants.BUDGET_STATUS_ENABLED;

public class BudgetDetails {
    private long budgetId = 0L;

    private String name = DateHelper.getCurrentEpochTimeAsString();

    private String resourceName = "";

    private int deliveryMethod = BUDGET_DELIVERY_METHOD_STANDARD;

    private boolean isShared = false;

    private int dailyBudgetAmount = 0;

    private int status = BUDGET_STATUS_ENABLED;

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

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
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

    public int getDailyBudgetAmount() {
        return dailyBudgetAmount;
    }

    public void setDailyBudgetAmount(int dailyBudgetAmount) {
        this.dailyBudgetAmount = dailyBudgetAmount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
