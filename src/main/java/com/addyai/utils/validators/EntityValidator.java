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

package com.addyai.utils.validators;

import com.addyai.enums.OperationType;
import com.addyai.error_handling.ValidationErrorResponse;
import com.addyai.models.BudgetDetails;
import com.addyai.models.CampaignDetails;
import com.addyai.models.campaign_criterion.*;
import com.addyai.utils.misc.Constants;
import com.google.ads.googleads.v11.enums.CampaignStatusEnum;

import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Locale;

public class EntityValidator {
    public static final String INVALID_CAMPAIGN_DETAILS_ERR_CODE = "INVALID_CAMPAIGN_DETAILS";
    public static final String INVALID_BUDGET_DETAILS_ERR_CODE = "INVALID_BUDGET_DETAILS";
    public static final String INVALID_NEGATIVE_KEYWORD_DETAILS_ERR_CODE = "INVALID_NEGATIVE_KEYWORD_DETAILS";
    public static final String INVALID_AD_SCHEDULE_DETAILS_ERR_CODE = "INVALID_AD_SCHEDULE_DETAILS";
    public static final String INVALID_DEVICE_DETAILS_ERR_CODE = "INVALID_DEVICE_DETAILS";
    public static final String INVALID_LANGUAGE_DETAILS_ERR_CODE = "INVALID_LANGUAGE_DETAILS";
    public static final String INVALID_LOCATION_DETAILS_ERR_CODE = "INVALID_LOCATION_DETAILS";
    public static final String INVALID_PROXIMITY_DETAILS_ERR_CODE = "INVALID_PROXIMITY_DETAILS";

    public static final String GENERAL_NAME_EMPTY_ERR_MSG = "Name field cannot be empty";
    public static final String GENERAL_RES_NAME_EMPTY_ERR_MSG = "Resource name cannot be empty for update operations";
    public static final String GENERAL_INVALID_STATUS_ERR_MSG = "Status field is invalid";
    public static final String GENERAL_BID_MODIFIER_OUT_OF_RANGE = "Bid modifier out of range. Must be between 0.0 and 10.0";
    public static final String GENERAL_BID_MODIFIER_PLUS_NEGATIVE = "Cannot set a bid-modifier on a negative target";

    public static final String INVALID_ADVERTISING_CHANNEL_ERR_MSG = "Advertising channel value is invalid";
    public static final String INVALID_POSITIVE_GEO_TARGET_TYPE_ERR_MSG = "Positive geo target type is invalid";
    public static final String INVALID_NEGATIVE_GEO_TARGET_TYPE_ERR_MSG = "Negative geo target type is invalid";
    public static final String INVALID_START_DATE_FORMAT_ERR_MSG = "Invalid start date format. Must be yyyy-MM-dd";
    public static final String INVALID_END_DATE_FORMAT_ERR_MSG = "Invalid end date format. Must be yyyy-MM-dd";
    public static final String NETWORK_TARGETING_NOT_SET_ERR_MSG = "Must target at least one network: Google Search Network, Content Network or Search Network";
    public static final String INVALID_NETWORK_TARGETING_ERR_MSG = "Campaigns targeting Search Network must also target Google Search Network";

    public static final String BUDGET_TOO_LOW_ERR_MSG = "Campaign budget must be greater than 0";
    public static final String INVALID_BUDGET_DELIVERY_STATUS_ERR_MSG = "Delivery status must be set to standard or accelerated";

    public static final String NEGATIVE_KEYWORD_TEXT_EMPTY_ERR_MSG = "Negative keyword text cannot be empty";
    public static final String NEGATIVE_KEYWORD_INVALID_MATCH_TYPE_ERR_MSG = "Invalid match type. Must be BROAD, PHRASE OR EXACT";

    public static final String INVALID_DEVICE_TYPE_DETAILS_ERR_MSG = "Missing device type";

    public static final String INVALID_DAY_OF_WEEK_ERR_MSG = "Invalid Day Of Week Value Provided";
    public static final String INVALID_HOUR_VALUE_ERR_MSG = "Ad schedule start and end hour must be between 0 and 23";
    public static final String INVALID_MINUTE_VALUE_ERR_MSG = "Ad schedule start and/or end minute are invalid";

    public static final String INVALID_LANGUAGE_CODE_ERR_MSG = "Invalid language code";

    public static final String INVALID_EMPTY_LOCATION_ERR_MSG = "Location cannot be empty";
    public static final String INVALID_LOCATION_VALUE_ERR_MSG = "Invalid location value";
    public static final String INVALID_PROXIMITY_LONGITUDE_LATITUDE_ERR_MSG = "Invalid values for microLongitude and/or microLatitude";
    public static final String INVALID_PROXIMITY_CITY_NAME_VALUE_ERR_MSG = "Invalid city name value";
    public static final String INVALID_PROXIMITY_POSTAL_CODE_VALUE_ERR_MSG = "Invalid postal code value";
    public static final String INVALID_PROXIMITY_GEO_COORDS_ERR_MSG = "Invalid longitude or latitude coordinates";
    public static final String INVALID_PROXIMITY_RADIUS_VALUE_ERR_MSG = "Invalid radius value";
    public static final String INVALID_PROXIMITY_RADIUS_UNIT_VALUE_ERR_MSG = "Invalid radius units value";
    public static final String INVALID_PROXIMITY_MISSING_ADDRESS_FIELDS_ERR_MSG = "Missing address fields";

    /**
     * Validate the fields of the CampaignDetails object
     *
     * @param campaignDetails CampaignDetails object to be validated
     * @param operationType   the type of operation being validated against
     * @return if invalid return a ValidationResponseError, else return null
     */
    public static ValidationErrorResponse isCampaignDetailsValid(CampaignDetails campaignDetails,
                                                                 OperationType operationType) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd", Locale.US)
                .withResolverStyle(ResolverStyle.STRICT);

        DateValidator dateValidator = new DateValidator(dateTimeFormatter);
        if (operationType.equals(OperationType.UPDATE) && campaignDetails.getCampaignResourceName().isEmpty()) {
            return new ValidationErrorResponse(
                    INVALID_CAMPAIGN_DETAILS_ERR_CODE,
                    GENERAL_RES_NAME_EMPTY_ERR_MSG);
        } else if (campaignDetails.getCampaignName().isEmpty())
            return new ValidationErrorResponse(
                    INVALID_CAMPAIGN_DETAILS_ERR_CODE,
                    GENERAL_NAME_EMPTY_ERR_MSG);
        else if (campaignDetails.getStatus() != CampaignStatusEnum.CampaignStatus.ENABLED_VALUE &&
                campaignDetails.getStatus() != CampaignStatusEnum.CampaignStatus.PAUSED_VALUE) {
            return new ValidationErrorResponse(
                    INVALID_CAMPAIGN_DETAILS_ERR_CODE,
                    GENERAL_INVALID_STATUS_ERR_MSG);
        } else if (campaignDetails.getAdvertisingChannelType() != Constants.ADVERTISING_CHANNEL_TYPE_SEARCH &&
                campaignDetails.getAdvertisingChannelType() != Constants.ADVERTISING_CHANNEL_TYPE_DISPLAY &&
                campaignDetails.getAdvertisingChannelType() != Constants.ADVERTISING_CHANNEL_TYPE_MULTI_CHANNEL) {
            return new ValidationErrorResponse(
                    INVALID_CAMPAIGN_DETAILS_ERR_CODE,
                    INVALID_ADVERTISING_CHANNEL_ERR_MSG);
        } else if (campaignDetails.getPositiveGeoTargetType() != Constants.POSITIVE_GEO_TARGET_TYPE_PRESENCE_OR_INTEREST &&
                campaignDetails.getPositiveGeoTargetType() != Constants.POSITIVE_GEO_TARGET_TYPE_SEARCH_INTEREST &&
                campaignDetails.getPositiveGeoTargetType() != Constants.POSITIVE_GEO_TARGET_TYPE_PRESENCE) {
            return new ValidationErrorResponse(
                    INVALID_CAMPAIGN_DETAILS_ERR_CODE,
                    INVALID_POSITIVE_GEO_TARGET_TYPE_ERR_MSG);
        } else if (campaignDetails.getNegativeGeoTargetType() != Constants.NEGATIVE_GEO_TARGET_TYPE_PRESENCE_OR_INTEREST &&
                campaignDetails.getNegativeGeoTargetType() != Constants.NEGATIVE_GEO_TARGET_TYPE_PRESENCE) {
            return new ValidationErrorResponse(
                    INVALID_CAMPAIGN_DETAILS_ERR_CODE,
                    INVALID_NEGATIVE_GEO_TARGET_TYPE_ERR_MSG);
        } else if (!dateValidator.isDateValid(campaignDetails.getStartDate())) {
            return new ValidationErrorResponse(
                    INVALID_CAMPAIGN_DETAILS_ERR_CODE,
                    INVALID_START_DATE_FORMAT_ERR_MSG);
        } else if (!dateValidator.isDateValid(campaignDetails.getEndDate())) {
            return new ValidationErrorResponse(
                    INVALID_CAMPAIGN_DETAILS_ERR_CODE,
                    INVALID_END_DATE_FORMAT_ERR_MSG);
        } else if (!campaignDetails.isTargetingSearchNetwork() && !campaignDetails.isTargetingGoogleSearchNetwork() &&
                !campaignDetails.isTargetingContentNetwork()) {
            return new ValidationErrorResponse(
                    INVALID_CAMPAIGN_DETAILS_ERR_CODE,
                    NETWORK_TARGETING_NOT_SET_ERR_MSG);
        } else if (campaignDetails.isTargetingSearchNetwork() && !campaignDetails.isTargetingGoogleSearchNetwork()) {
            return new ValidationErrorResponse(
                    INVALID_CAMPAIGN_DETAILS_ERR_CODE,
                    INVALID_NETWORK_TARGETING_ERR_MSG);
        }
        return null;
    }

    /**
     * Validate the fields of the BudgetDetails object
     *
     * @param budgetDetails BudgetDetails to be validated
     * @param operationType the type of operation being validated against
     * @return if invalid return a ValidationResponseError, else return null
     */
    public static ValidationErrorResponse isBudgetDetailsValid(BudgetDetails budgetDetails, OperationType operationType) {
        if (operationType.equals(OperationType.UPDATE) && budgetDetails.getResourceName().isEmpty()) {
            return new ValidationErrorResponse(
                    INVALID_BUDGET_DETAILS_ERR_CODE,
                    GENERAL_RES_NAME_EMPTY_ERR_MSG);
        } else if (budgetDetails.getDailyBudgetAmount() <= 0) {
            return new ValidationErrorResponse(
                    INVALID_BUDGET_DETAILS_ERR_CODE,
                    BUDGET_TOO_LOW_ERR_MSG);
        } else if (budgetDetails.getName().isEmpty()) {
            return new ValidationErrorResponse(
                    INVALID_BUDGET_DETAILS_ERR_CODE,
                    GENERAL_NAME_EMPTY_ERR_MSG);
        } else if (budgetDetails.getStatus() != Constants.BUDGET_STATUS_ENABLED &&
                budgetDetails.getStatus() != Constants.BUDGET_STATUS_REMOVED) {
            return new ValidationErrorResponse(
                    INVALID_BUDGET_DETAILS_ERR_CODE,
                    GENERAL_INVALID_STATUS_ERR_MSG);
        } else if (budgetDetails.getDeliveryMethod() != Constants.BUDGET_DELIVERY_METHOD_STANDARD &&
                budgetDetails.getDeliveryMethod() != Constants.BUDGET_DELIVERY_METHOD_ACCELERATED) {
            return new ValidationErrorResponse(
                    INVALID_BUDGET_DETAILS_ERR_CODE,
                    INVALID_BUDGET_DELIVERY_STATUS_ERR_MSG);
        }
        return null;
    }

    /**
     * Validate the fields of a [CriterionDetails]
     *
     * @param criterionDetailsList [CriterionDetails] to be validated
     * @return if invalid return a ValidationResponseError, else return null
     */
    public static ValidationErrorResponse isCriterionDetailsValid(List<CriterionDetails> criterionDetailsList) {
        for (CriterionDetails criterionDetails : criterionDetailsList) {
            if (criterionDetails instanceof NegativeKeywordDetails) {
                ValidationErrorResponse keywordValidationError =
                        isKeywordDetailsValid(((NegativeKeywordDetails) criterionDetails));

                if (keywordValidationError != null)
                    return keywordValidationError;

            } else if (criterionDetails instanceof AdScheduleDetails) {
                ValidationErrorResponse adScheduleValidationError =
                        isAdScheduledDetailsValid(((AdScheduleDetails) criterionDetails));

                if (adScheduleValidationError != null)
                    return adScheduleValidationError;
            } else if (criterionDetails instanceof DeviceDetails) {
                ValidationErrorResponse deviceValidationError =
                        isDeviceDetailsValid(((DeviceDetails) criterionDetails));

                if (deviceValidationError != null)
                    return deviceValidationError;
            } else if (criterionDetails instanceof LanguageDetails) {
                ValidationErrorResponse languageValidationError =
                        isLanguageDetailsValid(((LanguageDetails) criterionDetails));

                if (languageValidationError != null)
                    return languageValidationError;
            } else if (criterionDetails instanceof LocationDetails) {
                ValidationErrorResponse locationValidationError =
                        isLocationDetailsValid(((LocationDetails) criterionDetails));

                if (locationValidationError != null)
                    return locationValidationError;
            } else if (criterionDetails instanceof ProximityDetails) {
                ValidationErrorResponse proximityValidationError =
                        isProximityDetailsValid(((ProximityDetails) criterionDetails));

                if (proximityValidationError != null)
                    return proximityValidationError;
            }
        }
        return null;
    }

    private static ValidationErrorResponse isKeywordDetailsValid(NegativeKeywordDetails negativeKeywordDetails) {
        if (negativeKeywordDetails.getStatus() != Constants.CRITERION_STATUS_ENABLED &&
                negativeKeywordDetails.getStatus() != Constants.CRITERION_STATUS_PAUSED &&
                negativeKeywordDetails.getStatus() != Constants.CRITERION_STATUS_REMOVED) {
            return new ValidationErrorResponse(
                    INVALID_NEGATIVE_KEYWORD_DETAILS_ERR_CODE,
                    GENERAL_INVALID_STATUS_ERR_MSG);
        } else if (negativeKeywordDetails.getKeywordText().isEmpty()) {
            return new ValidationErrorResponse(
                    INVALID_NEGATIVE_KEYWORD_DETAILS_ERR_CODE,
                    NEGATIVE_KEYWORD_TEXT_EMPTY_ERR_MSG);
        } else if (negativeKeywordDetails.getKeywordMatchType() != Constants.KEYWORD_MATCH_TYPE_BROAD &&
                negativeKeywordDetails.getKeywordMatchType() != Constants.KEYWORD_MATCH_TYPE_PHRASE &&
                negativeKeywordDetails.getKeywordMatchType() != Constants.KEYWORD_MATCH_TYPE_EXACT) {
            return new ValidationErrorResponse(
                    INVALID_NEGATIVE_KEYWORD_DETAILS_ERR_CODE,
                    NEGATIVE_KEYWORD_INVALID_MATCH_TYPE_ERR_MSG);
        }
        return null;
    }

    private static ValidationErrorResponse isAdScheduledDetailsValid(AdScheduleDetails adScheduleDetails) {
        if (adScheduleDetails.getStatus() != Constants.CRITERION_STATUS_ENABLED &&
                adScheduleDetails.getStatus() != Constants.CRITERION_STATUS_PAUSED &&
                adScheduleDetails.getStatus() != Constants.CRITERION_STATUS_REMOVED) {
            return new ValidationErrorResponse(
                    INVALID_AD_SCHEDULE_DETAILS_ERR_CODE,
                    GENERAL_INVALID_STATUS_ERR_MSG);
        } else if (adScheduleDetails.getDayOfWeek() != Constants.DAY_OF_WEEK_SUNDAY &&
                adScheduleDetails.getDayOfWeek() != Constants.DAY_OF_WEEK_MONDAY &&
                adScheduleDetails.getDayOfWeek() != Constants.DAY_OF_WEEK_TUESDAY &&
                adScheduleDetails.getDayOfWeek() != Constants.DAY_OF_WEEK_WEDNESDAY &&
                adScheduleDetails.getDayOfWeek() != Constants.DAY_OF_WEEK_THURSDAY &&
                adScheduleDetails.getDayOfWeek() != Constants.DAY_OF_WEEK_FRIDAY &&
                adScheduleDetails.getDayOfWeek() != Constants.DAY_OF_WEEK_SATURDAY) {
            return new ValidationErrorResponse(
                    INVALID_AD_SCHEDULE_DETAILS_ERR_CODE,
                    INVALID_DAY_OF_WEEK_ERR_MSG);
        } else if (adScheduleDetails.getBidModifier() < 0 || adScheduleDetails.getBidModifier() > 10) {
            return new ValidationErrorResponse(
                    INVALID_AD_SCHEDULE_DETAILS_ERR_CODE,
                    GENERAL_BID_MODIFIER_OUT_OF_RANGE);
        } else if (adScheduleDetails.getStartHour() < 0 || adScheduleDetails.getStartHour() > 23 ||
                adScheduleDetails.getEndHour() < 0 || adScheduleDetails.getEndHour() > 23) {
            return new ValidationErrorResponse(
                    INVALID_AD_SCHEDULE_DETAILS_ERR_CODE,
                    INVALID_HOUR_VALUE_ERR_MSG);
        } else if (adScheduleDetails.getStartMinute() != Constants.MINUTE_OF_HOUR_ZERO &&
                adScheduleDetails.getStartMinute() != Constants.MINUTE_OF_HOUR_FIFTEEN &&
                adScheduleDetails.getStartMinute() != Constants.MINUTE_OF_HOUR_THIRTY &&
                adScheduleDetails.getStartMinute() != Constants.MINUTE_OF_HOUR_FORTY_FIVE ||
                adScheduleDetails.getEndMinute() != Constants.MINUTE_OF_HOUR_ZERO &&
                        adScheduleDetails.getEndMinute() != Constants.MINUTE_OF_HOUR_FIFTEEN &&
                        adScheduleDetails.getEndMinute() != Constants.MINUTE_OF_HOUR_THIRTY &&
                        adScheduleDetails.getEndMinute() != Constants.MINUTE_OF_HOUR_FORTY_FIVE) {
            return new ValidationErrorResponse(
                    INVALID_AD_SCHEDULE_DETAILS_ERR_CODE,
                    INVALID_MINUTE_VALUE_ERR_MSG);
        }
        return null;
    }

    private static ValidationErrorResponse isDeviceDetailsValid(DeviceDetails deviceDetails) {
        if (deviceDetails.getStatus() != Constants.CRITERION_STATUS_ENABLED &&
                deviceDetails.getStatus() != Constants.CRITERION_STATUS_PAUSED &&
                deviceDetails.getStatus() != Constants.CRITERION_STATUS_REMOVED) {
            return new ValidationErrorResponse(
                    INVALID_DEVICE_DETAILS_ERR_CODE,
                    GENERAL_INVALID_STATUS_ERR_MSG);
        } else if (deviceDetails.getBidModifier() < 0 || deviceDetails.getBidModifier() > 10) {
            return new ValidationErrorResponse(
                    INVALID_DEVICE_DETAILS_ERR_CODE,
                    GENERAL_BID_MODIFIER_OUT_OF_RANGE);
        } else if (deviceDetails.getDeviceType() != Constants.DEVICE_TYPE_DESKTOP &&
                deviceDetails.getDeviceType() != Constants.DEVICE_TYPE_MOBILE &&
                deviceDetails.getDeviceType() != Constants.DEVICE_TYPE_TABLET) {
            return new ValidationErrorResponse(
                    INVALID_DEVICE_DETAILS_ERR_CODE,
                    INVALID_DEVICE_TYPE_DETAILS_ERR_MSG);
        }
        return null;
    }

    private static ValidationErrorResponse isLanguageDetailsValid(LanguageDetails languageDetails) {
        String languageCodeValue = languageDetails.getLanguageCode().replace(Constants.LANGUAGE_CODE_PREFIX, "");

        if (languageDetails.getStatus() != Constants.CRITERION_STATUS_ENABLED &&
                languageDetails.getStatus() != Constants.CRITERION_STATUS_PAUSED &&
                languageDetails.getStatus() != Constants.CRITERION_STATUS_REMOVED) {
            return new ValidationErrorResponse(
                    INVALID_LANGUAGE_DETAILS_ERR_CODE,
                    GENERAL_INVALID_STATUS_ERR_MSG);
        } else if (languageDetails.getLanguageCode().isEmpty() ||
                languageDetails.getLanguageCode().contains(Constants.LANGUAGE_CODE_PREFIX) &&
                        !NumberValidator.isNumeric(languageCodeValue)) {
            return new ValidationErrorResponse(
                    INVALID_LANGUAGE_DETAILS_ERR_CODE,
                    INVALID_LANGUAGE_CODE_ERR_MSG);
        }

        return null;
    }

    private static ValidationErrorResponse isLocationDetailsValid(LocationDetails locationDetails) {
        if (locationDetails.getStatus() != Constants.CRITERION_STATUS_ENABLED &&
                locationDetails.getStatus() != Constants.CRITERION_STATUS_PAUSED &&
                locationDetails.getStatus() != Constants.CRITERION_STATUS_REMOVED) {
            return new ValidationErrorResponse(
                    INVALID_LOCATION_DETAILS_ERR_CODE,
                    GENERAL_INVALID_STATUS_ERR_MSG);
        } else if (locationDetails.getBidModifier() < 0 || locationDetails.getBidModifier() > 10) {
            return new ValidationErrorResponse(
                    INVALID_LOCATION_DETAILS_ERR_CODE,
                    GENERAL_BID_MODIFIER_OUT_OF_RANGE);
        } else if (locationDetails.isNegative() && locationDetails.getBidModifier() > 0) {
            return new ValidationErrorResponse(
                    INVALID_LOCATION_DETAILS_ERR_CODE,
                    GENERAL_BID_MODIFIER_PLUS_NEGATIVE);
        } else if (locationDetails.getLocation().isEmpty()) {
            return new ValidationErrorResponse(
                    INVALID_LOCATION_DETAILS_ERR_CODE,
                    INVALID_EMPTY_LOCATION_ERR_MSG);
        } else if (NumberValidator.containsDigits(locationDetails.getLocation())) {
            return new ValidationErrorResponse(
                    INVALID_LOCATION_DETAILS_ERR_CODE,
                    INVALID_LOCATION_VALUE_ERR_MSG);
        }
        return null;
    }

    private static ValidationErrorResponse isProximityDetailsValid(ProximityDetails proximityDetails) {
        if (proximityDetails.getStatus() != Constants.CRITERION_STATUS_ENABLED &&
                proximityDetails.getStatus() != Constants.CRITERION_STATUS_PAUSED &&
                proximityDetails.getStatus() != Constants.CRITERION_STATUS_REMOVED) {
            return new ValidationErrorResponse(
                    INVALID_PROXIMITY_DETAILS_ERR_CODE,
                    GENERAL_INVALID_STATUS_ERR_MSG);
        } else if (proximityDetails.getBidModifier() < 0 || proximityDetails.getBidModifier() > 10) {
            return new ValidationErrorResponse(
                    INVALID_PROXIMITY_DETAILS_ERR_CODE,
                    GENERAL_BID_MODIFIER_OUT_OF_RANGE);
        } else if (proximityDetails.isNegative() && proximityDetails.getBidModifier() > 0) {
            return new ValidationErrorResponse(
                    INVALID_PROXIMITY_DETAILS_ERR_CODE,
                    GENERAL_BID_MODIFIER_PLUS_NEGATIVE);
        } else if ((proximityDetails.getLatitude() > 0 && proximityDetails.getLongitude() == 0) ||
                (proximityDetails.getLatitude() == 0 && proximityDetails.getLongitude() > 0)) {
            return new ValidationErrorResponse(
                    INVALID_PROXIMITY_DETAILS_ERR_CODE,
                    INVALID_PROXIMITY_LONGITUDE_LATITUDE_ERR_MSG);
        } else if (!proximityDetails.getCityName().isEmpty() &&
                NumberValidator.containsDigits(proximityDetails.getCityName())) {
            return new ValidationErrorResponse(
                    INVALID_PROXIMITY_DETAILS_ERR_CODE,
                    INVALID_PROXIMITY_CITY_NAME_VALUE_ERR_MSG);
        } else if (!proximityDetails.getPostalCode().isEmpty() &&
                !NumberValidator.isNumeric(proximityDetails.getPostalCode())) {
            return new ValidationErrorResponse(
                    INVALID_PROXIMITY_DETAILS_ERR_CODE,
                    INVALID_PROXIMITY_POSTAL_CODE_VALUE_ERR_MSG);
        } else if (proximityDetails.getLatitude() < -90 || proximityDetails.getLatitude() > 90 ||
                proximityDetails.getLongitude() < -180 || proximityDetails.getLongitude() > 180) {
            return new ValidationErrorResponse(
                    INVALID_PROXIMITY_DETAILS_ERR_CODE,
                    INVALID_PROXIMITY_GEO_COORDS_ERR_MSG);
        } else if (!proximityDetails.getStreetAddress().isEmpty() && (proximityDetails.getPostalCode().isEmpty() ||
                proximityDetails.getCityName().isEmpty())) {
            return new ValidationErrorResponse(
                    INVALID_PROXIMITY_DETAILS_ERR_CODE,
                    INVALID_PROXIMITY_MISSING_ADDRESS_FIELDS_ERR_MSG);
        } else if (!proximityDetails.getPostalCode().isEmpty() && (proximityDetails.getCityName().isEmpty() ||
                proximityDetails.getStreetAddress().isEmpty())) {
            return new ValidationErrorResponse(
                    INVALID_PROXIMITY_DETAILS_ERR_CODE,
                    INVALID_PROXIMITY_MISSING_ADDRESS_FIELDS_ERR_MSG);
        } else if (!proximityDetails.getCityName().isEmpty() && (proximityDetails.getPostalCode().isEmpty() ||
                proximityDetails.getStreetAddress().isEmpty())) {
            return new ValidationErrorResponse(
                    INVALID_PROXIMITY_DETAILS_ERR_CODE,
                    INVALID_PROXIMITY_MISSING_ADDRESS_FIELDS_ERR_MSG);
        } else if (proximityDetails.getRadius() <= 0) {
            return new ValidationErrorResponse(
                    INVALID_PROXIMITY_DETAILS_ERR_CODE,
                    INVALID_PROXIMITY_RADIUS_VALUE_ERR_MSG);
        } else if (proximityDetails.getRadiusUnits() != Constants.RADIUS_UNITS_MILES &&
                proximityDetails.getRadiusUnits() != Constants.RADIUS_UNITS_KILOMETERS) {
            return new ValidationErrorResponse(
                    INVALID_PROXIMITY_DETAILS_ERR_CODE,
                    INVALID_PROXIMITY_RADIUS_UNIT_VALUE_ERR_MSG);
        }
        return null;
    }

    public static ValidationErrorResponse validateNonEmptyCampaignResourceNames(List<CampaignDetails> campaignDetailsList) {
        for (CampaignDetails campaignDetails : campaignDetailsList) {
            if (campaignDetails.getCampaignResourceName().isEmpty()) {
                return new ValidationErrorResponse(
                        Constants.INVALID_REQUEST_ERROR,
                        Constants.MISSING_PARAMS);
            }
        }
        return null;
    }
}
