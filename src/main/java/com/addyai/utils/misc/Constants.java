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

package com.addyai.utils.misc;

import com.google.ads.googleads.v11.enums.*;

public class Constants {
    public static final long MICRO_FACTOR = 1000000L;

    public static final int DEFAULT_ADDITIONAL_YEARS_CAMPAIGN_END_DATE = 10;

    public static final int NUM_CRITERION_CLASSES_SUPPORTED = 6;

    public static final String DEFAULT_LOCALE = "en";
    public static final String DEFAULT_COUNTRY_CODE = "US";
    public static final String LANGUAGE_CONSTANT_EN = "languageConstants/1000";

    public static final String INVALID_REQUEST_ERROR = "INVALID_REQUEST";
    public static final String INTERNAL_SERVICE_ERROR = "INTERNAL_SERVICE_ERROR";
    public static final String UNKNOWN_SERVICE_ERROR = "UNKNOWN_SERVICE_ERROR";
    public static final String RESOURCE_NOT_FOUND_ERR_CODE = "RESOURCE_NOT_FOUND";
    public static final String RESOURCE_NOT_FOUND_ERROR_MSG = "The requested resource was not found";
    public static final String LOCATION_NOT_FOUND_ERROR_MSG = "Geo target code could not be found for location";
    public static final String CAMPAIGN_OPERATIONS_FAILED_ERROR_MSG = "Failed to perform operations on campaigns";
    public static final String BUDGET_OPERATIONS_FAILED_ERROR_MSG = "Failed to perform operations on campaign budgets";
    public static final String CRITERION_OPERATIONS_FAILED_ERROR_MSG = "Failed to perform operations on campaign criterion";
    public static final String MISSING_PARAMS = "Parameters are missing from the request query";

    public static final String LANGUAGE_CODE_PREFIX = "languageConstants/";

    public static final int ADVERTISING_CHANNEL_TYPE_SEARCH
            = AdvertisingChannelTypeEnum.AdvertisingChannelType.SEARCH_VALUE;
    public static final int ADVERTISING_CHANNEL_TYPE_DISPLAY
            = AdvertisingChannelTypeEnum.AdvertisingChannelType.DISPLAY_VALUE;
    public static final int ADVERTISING_CHANNEL_TYPE_MULTI_CHANNEL
            = AdvertisingChannelTypeEnum.AdvertisingChannelType.MULTI_CHANNEL_VALUE;
    public static final int POSITIVE_GEO_TARGET_TYPE_PRESENCE_OR_INTEREST
            = PositiveGeoTargetTypeEnum.PositiveGeoTargetType.PRESENCE_OR_INTEREST_VALUE;
    public static final int POSITIVE_GEO_TARGET_TYPE_SEARCH_INTEREST
            = PositiveGeoTargetTypeEnum.PositiveGeoTargetType.SEARCH_INTEREST_VALUE;
    public static final int POSITIVE_GEO_TARGET_TYPE_PRESENCE
            = PositiveGeoTargetTypeEnum.PositiveGeoTargetType.PRESENCE_VALUE;

    public static final int NEGATIVE_GEO_TARGET_TYPE_PRESENCE_OR_INTEREST
            = NegativeGeoTargetTypeEnum.NegativeGeoTargetType.PRESENCE_OR_INTEREST_VALUE;
    public static final int NEGATIVE_GEO_TARGET_TYPE_PRESENCE
            = NegativeGeoTargetTypeEnum.NegativeGeoTargetType.PRESENCE_VALUE;

    public static final int CRITERION_TYPE_KEYWORD
            = CriterionTypeEnum.CriterionType.KEYWORD_VALUE;
    public static final int CRITERION_TYPE_AD_SCHEDULE
            = CriterionTypeEnum.CriterionType.AD_SCHEDULE_VALUE;
    public static final int CRITERION_TYPE_DEVICE
            = CriterionTypeEnum.CriterionType.DEVICE_VALUE;
    public static final int CRITERION_TYPE_LOCATION
            = CriterionTypeEnum.CriterionType.LOCATION_VALUE;
    public static final int CRITERION_TYPE_LANGUAGE
            = CriterionTypeEnum.CriterionType.LANGUAGE_VALUE;
    public static final int CRITERION_TYPE_PROXIMITY
            = CriterionTypeEnum.CriterionType.PROXIMITY_VALUE;

    public static final int CAMPAIGN_STATUS_ENABLED
            = CampaignStatusEnum.CampaignStatus.ENABLED_VALUE;
    public static final int CAMPAIGN_STATUS_PAUSED
            = CampaignStatusEnum.CampaignStatus.PAUSED_VALUE;
    public static final int CAMPAIGN_STATUS_REMOVED
            = CampaignStatusEnum.CampaignStatus.REMOVED_VALUE;

    public static final int BUDGET_STATUS_ENABLED
            = BudgetStatusEnum.BudgetStatus.ENABLED_VALUE;
    public static final int BUDGET_STATUS_REMOVED
            = BudgetStatusEnum.BudgetStatus.REMOVED_VALUE;

    public static final int CRITERION_STATUS_ENABLED
            = CampaignCriterionStatusEnum.CampaignCriterionStatus.ENABLED_VALUE;
    public static final int CRITERION_STATUS_PAUSED
            = CampaignCriterionStatusEnum.CampaignCriterionStatus.PAUSED_VALUE;
    public static final int CRITERION_STATUS_REMOVED
            = CampaignCriterionStatusEnum.CampaignCriterionStatus.REMOVED_VALUE;

    public static final int KEYWORD_MATCH_TYPE_BROAD
            = KeywordMatchTypeEnum.KeywordMatchType.BROAD_VALUE;
    public static final int KEYWORD_MATCH_TYPE_EXACT
            = KeywordMatchTypeEnum.KeywordMatchType.EXACT_VALUE;
    public static final int KEYWORD_MATCH_TYPE_PHRASE
            = KeywordMatchTypeEnum.KeywordMatchType.PHRASE_VALUE;

    public static final int BUDGET_DELIVERY_METHOD_STANDARD
            = BudgetDeliveryMethodEnum.BudgetDeliveryMethod.STANDARD_VALUE;
    public static final int BUDGET_DELIVERY_METHOD_ACCELERATED
            = BudgetDeliveryMethodEnum.BudgetDeliveryMethod.ACCELERATED_VALUE;

    public static final int DEVICE_TYPE_DESKTOP = DeviceEnum.Device.DESKTOP_VALUE;
    public static final int DEVICE_TYPE_MOBILE = DeviceEnum.Device.MOBILE_VALUE;
    public static final int DEVICE_TYPE_TABLET = DeviceEnum.Device.TABLET_VALUE;

    public static final int DAY_OF_WEEK_SUNDAY = DayOfWeekEnum.DayOfWeek.SUNDAY_VALUE;
    public static final int DAY_OF_WEEK_MONDAY = DayOfWeekEnum.DayOfWeek.MONDAY_VALUE;
    public static final int DAY_OF_WEEK_TUESDAY = DayOfWeekEnum.DayOfWeek.TUESDAY_VALUE;
    public static final int DAY_OF_WEEK_WEDNESDAY = DayOfWeekEnum.DayOfWeek.WEDNESDAY_VALUE;
    public static final int DAY_OF_WEEK_THURSDAY = DayOfWeekEnum.DayOfWeek.THURSDAY_VALUE;
    public static final int DAY_OF_WEEK_FRIDAY = DayOfWeekEnum.DayOfWeek.FRIDAY_VALUE;
    public static final int DAY_OF_WEEK_SATURDAY = DayOfWeekEnum.DayOfWeek.SATURDAY_VALUE;

    public static final int MINUTE_OF_HOUR_ZERO = MinuteOfHourEnum.MinuteOfHour.ZERO_VALUE;
    public static final int MINUTE_OF_HOUR_FIFTEEN = MinuteOfHourEnum.MinuteOfHour.FIFTEEN_VALUE;
    public static final int MINUTE_OF_HOUR_THIRTY = MinuteOfHourEnum.MinuteOfHour.THIRTY_VALUE;
    public static final int MINUTE_OF_HOUR_FORTY_FIVE = MinuteOfHourEnum.MinuteOfHour.FORTY_FIVE_VALUE;

    public static final int RADIUS_UNITS_MILES = ProximityRadiusUnitsEnum.ProximityRadiusUnits.MILES_VALUE;
    public static final int RADIUS_UNITS_KILOMETERS = ProximityRadiusUnitsEnum.ProximityRadiusUnits.KILOMETERS_VALUE;
}
