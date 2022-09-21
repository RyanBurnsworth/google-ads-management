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

package com.addyai.validators;

import com.addyai.error_handling.ValidationErrorResponse;
import com.addyai.models.BudgetDetails;
import com.addyai.models.CampaignDetails;
import com.addyai.utils.TestUtils;
import com.addyai.utils.validators.EntityValidator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.addyai.utils.validators.EntityValidator.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class EntityValidatorTest {
    private final TestUtils testUtils = new TestUtils();

    @Test
    void testValidCampaignReturnsNullValidationErrorResponse() {
        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCampaignDetailsValid(testUtils.getMockCampaignDetails());
        assertNull(validationErrorResponse);
    }

    @Test
    void testCampaignDetailsWithMissingNameReturnValidationError() {
        CampaignDetails campaignDetails = testUtils.getMockCampaignDetails();
        campaignDetails.setCampaignName("");

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCampaignDetailsValid(campaignDetails);

        assert validationErrorResponse != null;
        assertEquals(INVALID_CAMPAIGN_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(GENERAL_NAME_EMPTY_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testCampaignDetailsWithMissingStatusReturnsValidationError() {
        CampaignDetails campaignDetails = testUtils.getMockCampaignDetails();
        campaignDetails.setStatus("");

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCampaignDetailsValid(campaignDetails);

        assert validationErrorResponse != null;
        assertEquals(INVALID_CAMPAIGN_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(GENERAL_INVALID_STATUS_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testCampaignDetailsWithMissingAdChannelTypeReturnsValidationError() {
        CampaignDetails campaignDetails = testUtils.getMockCampaignDetails();
        campaignDetails.setAdvertisingChannelType("");

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCampaignDetailsValid(campaignDetails);

        assert validationErrorResponse != null;
        assertEquals(INVALID_CAMPAIGN_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(INVALID_ADVERTISING_CHANNEL_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testCampaignDetailsWithInvalidPosGeoTargetReturnsValidationError() {
        CampaignDetails campaignDetails = testUtils.getMockCampaignDetails();
        campaignDetails.setPositiveGeoTargetType(100);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCampaignDetailsValid(campaignDetails);

        assert validationErrorResponse != null;
        assertEquals(INVALID_CAMPAIGN_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(INVALID_POSITIVE_GEO_TARGET_TYPE_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testCampaignDetailsWithInvalidNegGeoTargetReturnsValidationError() {
        CampaignDetails campaignDetails = testUtils.getMockCampaignDetails();
        campaignDetails.setNegativeGeoTargetType(100);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCampaignDetailsValid(campaignDetails);

        assert validationErrorResponse != null;
        assertEquals(INVALID_CAMPAIGN_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(INVALID_NEGATIVE_GEO_TARGET_TYPE_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testCampaignDetailsWithInvalidStartDateReturnsValidationError() {
        CampaignDetails campaignDetails = testUtils.getMockCampaignDetails();
        campaignDetails.setStartDate("12-20-2022");

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCampaignDetailsValid(campaignDetails);

        assert validationErrorResponse != null;
        assertEquals(INVALID_CAMPAIGN_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(INVALID_START_DATE_FORMAT_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testCampaignDetailsWithInvalidEndDateReturnsValidationError() {
        CampaignDetails campaignDetails = testUtils.getMockCampaignDetails();
        campaignDetails.setEndDate("12-20-2022");

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCampaignDetailsValid(campaignDetails);

        assert validationErrorResponse != null;
        assertEquals(INVALID_CAMPAIGN_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(INVALID_END_DATE_FORMAT_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testCampaignDetailsWithNetworkSettingsNotSetReturnsValidationError() {
        CampaignDetails campaignDetails = testUtils.getMockCampaignDetails();
        campaignDetails.setTargetingSearchNetwork(false);
        campaignDetails.setTargetingGoogleSearchNetwork(false);
        campaignDetails.setTargetingContentNetwork(false);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCampaignDetailsValid(campaignDetails);

        assert validationErrorResponse != null;
        assertEquals(INVALID_CAMPAIGN_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(NETWORK_TARGETING_NOT_SET_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testCampaignDetailsWithInvalidNetworkSettingsReturnsValidationError() {
        CampaignDetails campaignDetails = testUtils.getMockCampaignDetails();
        campaignDetails.setTargetingSearchNetwork(true);
        campaignDetails.setTargetingGoogleSearchNetwork(false);
        campaignDetails.setTargetingContentNetwork(false);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCampaignDetailsValid(campaignDetails);

        assert validationErrorResponse != null;
        assertEquals(INVALID_CAMPAIGN_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(INVALID_NETWORK_TARGETING_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testValidBudgetDetailsReturnsNullValidationError() {
        BudgetDetails budgetDetails = testUtils.getMockBudgetDetails();
        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isBudgetDetailsValid(budgetDetails);

        assertNull(validationErrorResponse);
    }

    @Test
    void testBudgetDetailsWithZeroBudgetReturnsValidationError() {
        BudgetDetails budgetDetails = testUtils.getMockBudgetDetails();
        budgetDetails.setDailyBudgetAmount(0);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isBudgetDetailsValid(budgetDetails);

        assert validationErrorResponse != null;
        assertEquals(INVALID_BUDGET_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(BUDGET_TOO_LOW_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testBudgetDetailsWithNoNameReturnsValidationError() {
        BudgetDetails budgetDetails = testUtils.getMockBudgetDetails();
        budgetDetails.setName("");

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isBudgetDetailsValid(budgetDetails);

        assert validationErrorResponse != null;
        assertEquals(INVALID_BUDGET_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(GENERAL_NAME_EMPTY_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testBudgetDetailsWithInvalidStatusReturnsValidationError() {
        BudgetDetails budgetDetails = testUtils.getMockBudgetDetails();
        budgetDetails.setStatus(100);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isBudgetDetailsValid(budgetDetails);

        assert validationErrorResponse != null;
        assertEquals(INVALID_BUDGET_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(GENERAL_INVALID_STATUS_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testBudgetDetailsWithInvalidDeliveryMethodReturnsValidationError() {
        BudgetDetails budgetDetails = testUtils.getMockBudgetDetails();
        budgetDetails.setDeliveryMethod(100);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isBudgetDetailsValid(budgetDetails);

        assert validationErrorResponse != null;
        assertEquals(INVALID_BUDGET_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(INVALID_BUDGET_DELIVERY_STATUS_ERR_MSG, validationErrorResponse.getErrorMessage());
    }
}
