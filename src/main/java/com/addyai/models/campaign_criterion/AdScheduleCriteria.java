package com.addyai.models.campaign_criterion;

import com.google.ads.googleads.v11.enums.CampaignCriterionStatusEnum;
import com.google.ads.googleads.v11.enums.CriterionTypeEnum;
import com.google.ads.googleads.v11.enums.DayOfWeekEnum;
import com.google.ads.googleads.v11.enums.MinuteOfHourEnum;

public class AdScheduleCriteria extends CampaignCriteria {
    private DayOfWeekEnum.DayOfWeek dayOfWeek = DayOfWeekEnum.DayOfWeek.UNKNOWN;

    // Must be between 0 and 24
    private int startHour = 0;

    // must be between 0 and 24
    private int endHour = 0;

    private MinuteOfHourEnum.MinuteOfHour startMinute = MinuteOfHourEnum.MinuteOfHour.UNKNOWN;

    private MinuteOfHourEnum.MinuteOfHour endMinute = MinuteOfHourEnum.MinuteOfHour.UNKNOWN;

    public DayOfWeekEnum.DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeekEnum.DayOfWeek dayOfWeek) {
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

    public MinuteOfHourEnum.MinuteOfHour getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(MinuteOfHourEnum.MinuteOfHour startMinute) {
        this.startMinute = startMinute;
    }

    public MinuteOfHourEnum.MinuteOfHour getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(MinuteOfHourEnum.MinuteOfHour endMinute) {
        this.endMinute = endMinute;
    }
}
