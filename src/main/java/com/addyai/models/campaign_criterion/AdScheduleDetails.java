package com.addyai.models.campaign_criterion;

import static com.addyai.utils.misc.Constants.*;

public class AdScheduleDetails extends CriterionDetails {
    private int dayOfWeek = DAY_OF_WEEK_SUNDAY;

    // Must be between 0 and 24
    private int startHour = 0;

    // must be between 0 and 24
    private int endHour = 23;

    private int startMinute = MINUTE_OF_HOUR_ZERO;

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
