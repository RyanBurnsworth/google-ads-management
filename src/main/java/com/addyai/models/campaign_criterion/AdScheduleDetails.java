package com.addyai.models.campaign_criterion;

import com.addyai.models.campaign_criterion.CampaignCriterionDetails;

public class AdScheduleDetails extends CampaignCriterionDetails {
    private int dayOfWeek = 1;

    // Must be between 0 and 24
    private int startHour = 0;

    // must be between 0 and 24
    private int endHour = 0;

    private int startMinute = 1;

    private int endMinute = 1;

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
