package com.addyai.utils;

public class Constants {
    public static final String ADVERTISING_TYPE_SEARCH = "SEARCH";

    public static final String INTERNAL_ERROR = "INTERNAL_ERROR";

    public static final String MISSING_REQUIRED_PARAMS = "MISSING_REQUIRED_PARAMS";
    public static final String UNKNOWN_SERVICE_ERROR = "UNKNOWN_SERVICE_ERROR";

    public static final String MISSING_CUSTOMER_ID = "MISSING_CUSTOMER_ID";


    public static final String GEO_TARGET_TYPE_UNKNOWN = "UNKNOWN";

    public static final String UPDATE_RES_EXCEPTION_MSG = "Failed to update resource(s) for account: ";
    public static final String DELETE_RES_EXCEPTION_MSG = "Failed to delete resource(s) for account: ";
    public static final String ADD_RES_EXCEPTION_MSG = "Failed to add resource(s) to account: ";
    public static final String GET_RES_EXCEPTION_MSG = "Failed to fetch resource(s) from account: ";


    // campaign budget error types and messages
    // https://developers.google.com/google-ads/api/reference/rpc/v11/CampaignBudgetErrorEnum.CampaignBudgetError

    public static final String CAMPAIGN_BUDGET_REMOVED = "CAMPAIGN_BUDGET_REMOVED";
    public static final String CAMPAIGN_BUDGET_IN_USE = "CAMPAIGN_BUDGET_IN_USE";
    public static final String DUPLICATE_NAME = "DUPLICATE_NAME";
    public static final String MONEY_AMOUNT_TOO_LARGE = "MONEY_AMOUNT_TOO_LARGE";
    public static final String NEGATIVE_MONEY_AMOUNT = "NEGATIVE_MONEY_AMOUNT";


    // field error types and messages
    // https://developers.google.com/google-ads/api/reference/rpc/v11/FieldErrorEnum.FieldError

    public static final String INVALID_ARGUMENT = "INVALID_ARGUMENT";
}
