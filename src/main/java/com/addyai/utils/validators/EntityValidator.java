package com.addyai.utils.validators;

import com.addyai.error_handling.ValidationErrorResponse;
import com.addyai.models.BudgetDetails;
import com.addyai.models.CampaignDetails;
import com.addyai.models.campaign_criterion.AdScheduleDetails;
import com.addyai.models.campaign_criterion.CriterionDetails;
import com.addyai.models.campaign_criterion.DeviceDetails;
import com.addyai.models.campaign_criterion.NegativeKeywordDetails;

import java.util.List;

import static com.addyai.utils.misc.Constants.*;

public class EntityValidator {
    private static final String INVALID_CAMPAIGN_DETAILS = "Invalid Campaign Details";
    private static final String MISSING_CAMPAIGN_NAME = "Missing Campaign Name";
    private static final String CAMPAIGN_NAME_EMPTY_ERR = "Campaign name cannot be blank";
    private static final String CAMPAIGN_BUDGET_ZERO_ERR = "Campaign budget cannot be 0";

    private static final String INVALID_BUDGET_DETAILS = "Invalid Budget Details";
    private static final String INVALID_BUDGET_AMOUNT = "Invalid Budget Amount";

    private static final String INVALID_CRITERION_DETAILS = "Invalid Criterion Details";
    private static final String INVALID_NEGATIVE_KEYWORD_DETAILS = "Invalid Negative Keyword Details";
    private static final String INVALID_DAY_OF_WEEK_VALUE = "Invalid Day Of Week Value";
    private static final String INVALID_HOUR_VALUE = "Invalid Hour Value";
    private static final String INVALID_MINUTE_VALUE = "Invalid Minute Value";

    private static final String NEGATIVE_KEYWORD_TEXT_EMPTY_ERR = "Negative keyword text cannot be empty";
    private static final String INVALID_DAY_OF_WEEK_ERR = "Invalid Day Of Week Value Provided";
    private static final String INVALID_HOUR_VALUE_ERR = "Ad schedule start and end hour must be between 0 and 23";
    private static final String INVALID_MINUTE_VALUE_ERR = "Ad schedule start and/or end minute are invalid";

    /**
     * Check the campaign details object's campaign name is not empty,
     * the budget details are not null, the criterion list is not null, the status is either ENABLED or PAUSED,
     * and the targeting is set to at least one option: Search, Search Partners or Content Network
     *
     * @param campaignDetails CampaignDetails object to be validated
     * @return if invalid return a ValidationResponseError, else return null
     */
    public static ValidationErrorResponse isCampaignDetailsValid(CampaignDetails campaignDetails) {
        if (campaignDetails.getCampaignName().isEmpty())
            return new ValidationErrorResponse(INVALID_CAMPAIGN_DETAILS,
                    MISSING_CAMPAIGN_NAME,
                    CAMPAIGN_NAME_EMPTY_ERR);
        return null;
    }

    /**
     * Check the budget details object's daily budget amount is above 0
     *
     * @param budgetDetails BudgetDetails to be validated
     * @return if invalid return a ValidationResponseError, else return null
     */
    public static ValidationErrorResponse isBudgetDetailsValid(BudgetDetails budgetDetails) {
        if (budgetDetails.getDailyBudgetAmount() <= 0)
            return new ValidationErrorResponse(INVALID_BUDGET_DETAILS,
                    INVALID_BUDGET_AMOUNT,
                    CAMPAIGN_BUDGET_ZERO_ERR);
        return null;
    }

    public static ValidationErrorResponse isCriterionDetailsValid(List<CriterionDetails> criterionDetailsList) {
        for (CriterionDetails criterionDetails : criterionDetailsList) {
            if (criterionDetails instanceof NegativeKeywordDetails) {
                ValidationErrorResponse keywordValidationError =
                        isKeywordDetailsValid(((NegativeKeywordDetails) criterionDetails));

                if (keywordValidationError != null) {
                    return new ValidationErrorResponse(
                            keywordValidationError.getErrorType(),
                            keywordValidationError.getErrorCode(),
                            keywordValidationError.getErrorMessage());
                }
            } else if (criterionDetails instanceof AdScheduleDetails) {
                ValidationErrorResponse adScheduleValidationError =
                        isAdScheduledDetailsValid(((AdScheduleDetails) criterionDetails));

                if (adScheduleValidationError != null) {
                    return new ValidationErrorResponse(
                            adScheduleValidationError.getErrorType(),
                            adScheduleValidationError.getErrorCode(),
                            adScheduleValidationError.getErrorMessage());
                }
            }
        }
        return null;
    }

    private static ValidationErrorResponse isKeywordDetailsValid(NegativeKeywordDetails negativeKeywordDetails) {
        if (negativeKeywordDetails.getKeywordText().isEmpty())
            return new ValidationErrorResponse(
                    INVALID_CRITERION_DETAILS,
                    INVALID_NEGATIVE_KEYWORD_DETAILS,
                    NEGATIVE_KEYWORD_TEXT_EMPTY_ERR);
        return null;
    }

    private static ValidationErrorResponse isAdScheduledDetailsValid(AdScheduleDetails adScheduleDetails) {
        if (adScheduleDetails.getDayOfWeek() != DAY_OF_WEEK_SUNDAY ||
                adScheduleDetails.getDayOfWeek() != DAY_OF_WEEK_MONDAY ||
                adScheduleDetails.getDayOfWeek() != DAY_OF_WEEK_TUESDAY ||
                adScheduleDetails.getDayOfWeek() != DAY_OF_WEEK_WEDNESDAY ||
                adScheduleDetails.getDayOfWeek() != DAY_OF_WEEK_THURSDAY ||
                adScheduleDetails.getDayOfWeek() != DAY_OF_WEEK_FRIDAY ||
                adScheduleDetails.getDayOfWeek() != DAY_OF_WEEK_SATURDAY)
            return new ValidationErrorResponse(
                    INVALID_CRITERION_DETAILS,
                    INVALID_DAY_OF_WEEK_VALUE,
                    INVALID_DAY_OF_WEEK_ERR);
        else if (adScheduleDetails.getStartHour() < 0 || adScheduleDetails.getStartHour() > 23 ||
                adScheduleDetails.getEndHour() < 0 || adScheduleDetails.getEndHour() > 23)
            return new ValidationErrorResponse(
                    INVALID_CRITERION_DETAILS,
                    INVALID_HOUR_VALUE,
                    INVALID_HOUR_VALUE_ERR);
        else if (adScheduleDetails.getStartMinute() != MINUTE_OF_HOUR_ZERO ||
                adScheduleDetails.getStartMinute() != MINUTE_OF_HOUR_FIFTEEN ||
                adScheduleDetails.getStartMinute() != MINUTE_OF_HOUR_THIRTY ||
                adScheduleDetails.getStartMinute() != MINUTE_OF_HOUR_FORTY_FIVE ||
                adScheduleDetails.getEndMinute() != MINUTE_OF_HOUR_ZERO ||
                adScheduleDetails.getEndMinute() != MINUTE_OF_HOUR_FIFTEEN ||
                adScheduleDetails.getEndMinute() != MINUTE_OF_HOUR_THIRTY ||
                adScheduleDetails.getEndMinute() != MINUTE_OF_HOUR_FORTY_FIVE)
            return new ValidationErrorResponse(
                    INVALID_CRITERION_DETAILS,
                    INVALID_MINUTE_VALUE,
                    INVALID_MINUTE_VALUE_ERR);
        return null;
    }

    private static ValidationErrorResponse isDeviceDetailsValid(DeviceDetails deviceDetails) {
        if (deviceDetails.getDeviceType() != DEVICE_TYPE_DESKTOP ||
                deviceDetails.getDeviceType() != DEVICE_TYPE_MOBILE ||
                deviceDetails.getDeviceType() != DEVICE_TYPE_TABLET)
            return new ValidationErrorResponse("", "", "");

        return null;
    }
}
