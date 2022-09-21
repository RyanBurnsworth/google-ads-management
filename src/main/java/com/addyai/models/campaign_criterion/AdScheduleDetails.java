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

import static com.addyai.utils.misc.Constants.*;

public class AdScheduleDetails extends CriterionDetails {
    /**
     * The day of the week the ad scheduling is targeting.
     * Defaults to DayOfWeekEnum.DayOfWeek.THURSDAY_VALUE
     *
     * @see com.google.ads.googleads.v11.enums.DayOfWeekEnum.DayOfWeek
     */
    private int dayOfWeek = DAY_OF_WEEK_THURSDAY;

    /**
     * The hour the ad schedule should start targeting.
     * Must be a value between 0 and 23
     * Defaults to 0
     */
    private int startHour = 0;

    /**
     * The hour the ad scheduling should stop targeting.
     * Must be a value between 0 and 23
     * Defaults to 23
     */
    private int endHour = 23;

    /**
     * The minute the ad scheduling should start targeting.
     * Defaults to MinuteOfHourEnum.MinuteOfHour.ZERO_VALUE
     *
     * @see com.google.ads.googleads.v11.enums.MinuteOfHourEnum.MinuteOfHour
     */
    private int startMinute = MINUTE_OF_HOUR_ZERO;

    /**
     * The minute the ad scheduling should stop targeting..
     * Defaults to MinuteOfHourEnum.MinuteOfHour.FORTY_FIVE_VALUE
     *
     * @see com.google.ads.googleads.v11.enums.MinuteOfHourEnum.MinuteOfHour
     */
    private int endMinute = MINUTE_OF_HOUR_FORTY_FIVE;

    @Override
    public int getCriterionType() {
        return CRITERION_TYPE_AD_SCHEDULE;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }
}
