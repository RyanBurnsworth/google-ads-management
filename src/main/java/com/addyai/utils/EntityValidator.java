package com.addyai.utils;

import com.addyai.error_handling.ValidationErrorResponse;
import com.addyai.models.BudgetDetails;
import com.addyai.models.CampaignDetails;

public class EntityValidator {
    private static final String INVALID_CAMPAIGN_DETAILS = "Invalid Campaign Details";
    private static final String MISSING_CAMPAIGN_NAME = "Missing Campaign Name";
    private static final String CAMPAIGN_NAME_EMPTY_ERR = "Campaign name cannot be blank";
    private static final String INVALID_BUDGET_DETAILS = "Invalid Budget Details";
    private static final String INVALID_BUDGET_AMOUNT = "Invalid Budget Amount";
    private static final String CAMPAIGN_BUDGET_ZERO_ERR = "Campaign budget cannot be 0";

    public static ValidationErrorResponse isCampaignDetailsValid(CampaignDetails campaignDetails) {
        if (campaignDetails.getCampaignName().isEmpty())
            return new ValidationErrorResponse(INVALID_CAMPAIGN_DETAILS,
                    MISSING_CAMPAIGN_NAME,
                    CAMPAIGN_NAME_EMPTY_ERR);
        return null;
    }

    public static ValidationErrorResponse isBudgetDetailsValid(BudgetDetails budgetDetails) {
        if (budgetDetails.getDailyBudgetAmount() <= 0)
            return new ValidationErrorResponse(INVALID_BUDGET_DETAILS, INVALID_BUDGET_AMOUNT, CAMPAIGN_BUDGET_ZERO_ERR);
        return null;
    }
}
