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

import com.addyai.enums.OperationType;
import com.addyai.error_handling.ValidationErrorResponse;
import com.addyai.models.BudgetDetails;
import com.addyai.models.CampaignDetails;
import com.addyai.models.campaign_criterion.*;
import com.addyai.utils.TestUtils;
import com.addyai.utils.validators.EntityValidator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static com.addyai.utils.TestUtils.MOCK_CAMPAIGN_RESOURCE_NAME;
import static com.addyai.utils.misc.Constants.*;
import static com.addyai.utils.validators.EntityValidator.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class EntityValidatorTest {
    private final TestUtils testUtils = new TestUtils();

    @Test
    void testValidCampaignReturnsNullValidationErrorResponse() {
        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCampaignDetailsValid(testUtils.getMockCampaignDetails(), OperationType.CREATE);
        assertNull(validationErrorResponse);
    }

    @Test
    void testCampaignDetailsWithMissingNameReturnValidationError() {
        CampaignDetails campaignDetails = testUtils.getMockCampaignDetails();
        campaignDetails.setCampaignName("");

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCampaignDetailsValid(campaignDetails, OperationType.CREATE);

        assert validationErrorResponse != null;
        assertEquals(INVALID_CAMPAIGN_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(GENERAL_NAME_EMPTY_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testCampaignDetailsWithMissingStatusReturnsValidationError() {
        CampaignDetails campaignDetails = testUtils.getMockCampaignDetails();
        campaignDetails.setStatus(-1);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCampaignDetailsValid(campaignDetails, OperationType.CREATE);

        assert validationErrorResponse != null;
        assertEquals(INVALID_CAMPAIGN_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(GENERAL_INVALID_STATUS_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testCampaignDetailsWithMissingAdChannelTypeReturnsValidationError() {
        CampaignDetails campaignDetails = testUtils.getMockCampaignDetails();
        campaignDetails.setAdvertisingChannelType(-1);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCampaignDetailsValid(campaignDetails, OperationType.CREATE);

        assert validationErrorResponse != null;
        assertEquals(INVALID_CAMPAIGN_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(INVALID_ADVERTISING_CHANNEL_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testCampaignDetailsWithInvalidPosGeoTargetReturnsValidationError() {
        CampaignDetails campaignDetails = testUtils.getMockCampaignDetails();
        campaignDetails.setPositiveGeoTargetType(100);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCampaignDetailsValid(campaignDetails, OperationType.CREATE);

        assert validationErrorResponse != null;
        assertEquals(INVALID_CAMPAIGN_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(INVALID_POSITIVE_GEO_TARGET_TYPE_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testCampaignDetailsWithInvalidNegGeoTargetReturnsValidationError() {
        CampaignDetails campaignDetails = testUtils.getMockCampaignDetails();
        campaignDetails.setNegativeGeoTargetType(100);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCampaignDetailsValid(campaignDetails, OperationType.CREATE);

        assert validationErrorResponse != null;
        assertEquals(INVALID_CAMPAIGN_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(INVALID_NEGATIVE_GEO_TARGET_TYPE_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testCampaignDetailsWithInvalidStartDateReturnsValidationError() {
        CampaignDetails campaignDetails = testUtils.getMockCampaignDetails();
        campaignDetails.setStartDate("12-20-2022");

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCampaignDetailsValid(campaignDetails, OperationType.CREATE);

        assert validationErrorResponse != null;
        assertEquals(INVALID_CAMPAIGN_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(INVALID_START_DATE_FORMAT_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testCampaignDetailsWithInvalidEndDateReturnsValidationError() {
        CampaignDetails campaignDetails = testUtils.getMockCampaignDetails();
        campaignDetails.setEndDate("12-20-2022");

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCampaignDetailsValid(campaignDetails, OperationType.CREATE);

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
                EntityValidator.isCampaignDetailsValid(campaignDetails, OperationType.CREATE);

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
                EntityValidator.isCampaignDetailsValid(campaignDetails, OperationType.CREATE);

        assert validationErrorResponse != null;
        assertEquals(INVALID_CAMPAIGN_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(INVALID_NETWORK_TARGETING_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testValidBudgetDetailsReturnsNullValidationError() {
        BudgetDetails budgetDetails = testUtils.getMockBudgetDetails();
        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isBudgetDetailsValid(budgetDetails, OperationType.CREATE);

        assertNull(validationErrorResponse);
    }

    @Test
    void testBudgetDetailsWithZeroBudgetReturnsValidationError() {
        BudgetDetails budgetDetails = testUtils.getMockBudgetDetails();
        budgetDetails.setDailyBudgetAmount(0);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isBudgetDetailsValid(budgetDetails, OperationType.CREATE);

        assert validationErrorResponse != null;
        assertEquals(INVALID_BUDGET_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(BUDGET_TOO_LOW_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testBudgetDetailsWithNoNameReturnsValidationError() {
        BudgetDetails budgetDetails = testUtils.getMockBudgetDetails();
        budgetDetails.setName("");

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isBudgetDetailsValid(budgetDetails, OperationType.CREATE);

        assert validationErrorResponse != null;
        assertEquals(INVALID_BUDGET_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(GENERAL_NAME_EMPTY_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testBudgetDetailsWithInvalidStatusReturnsValidationError() {
        BudgetDetails budgetDetails = testUtils.getMockBudgetDetails();
        budgetDetails.setStatus(100);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isBudgetDetailsValid(budgetDetails, OperationType.CREATE);

        assert validationErrorResponse != null;
        assertEquals(INVALID_BUDGET_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(GENERAL_INVALID_STATUS_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testBudgetDetailsWithInvalidDeliveryMethodReturnsValidationError() {
        BudgetDetails budgetDetails = testUtils.getMockBudgetDetails();
        budgetDetails.setDeliveryMethod(100);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isBudgetDetailsValid(budgetDetails, OperationType.CREATE);

        assert validationErrorResponse != null;
        assertEquals(INVALID_BUDGET_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(INVALID_BUDGET_DELIVERY_STATUS_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testValidCriterionDetailsReturnsNullValidationErrorResponse() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(criterionDetailsList);
        assertNull(validationErrorResponse);
    }

    @Test
    void testAdScheduleDetailsInvalidStatusReturnValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        AdScheduleDetails adScheduleDetails = ((AdScheduleDetails) criterionDetailsList.get(0));
        adScheduleDetails.setStatus(-1);
        singleCriterionDetailsList.add(adScheduleDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_AD_SCHEDULE_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(GENERAL_INVALID_STATUS_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testAdScheduleDetailsInvalidDayOfWeekReturnValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        AdScheduleDetails adScheduleDetails = ((AdScheduleDetails) criterionDetailsList.get(0));
        adScheduleDetails.setDayOfWeek(-1);
        singleCriterionDetailsList.add(adScheduleDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_AD_SCHEDULE_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(INVALID_DAY_OF_WEEK_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testAdScheduleDetailsBidModifierTooLowReturnValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        AdScheduleDetails adScheduleDetails = ((AdScheduleDetails) criterionDetailsList.get(0));
        adScheduleDetails.setBidModifier(-1f);
        singleCriterionDetailsList.add(adScheduleDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_AD_SCHEDULE_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(GENERAL_BID_MODIFIER_OUT_OF_RANGE, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testAdScheduleDetailsBidModifierTooHighReturnValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        AdScheduleDetails adScheduleDetails = ((AdScheduleDetails) criterionDetailsList.get(0));
        adScheduleDetails.setBidModifier(11f);
        singleCriterionDetailsList.add(adScheduleDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_AD_SCHEDULE_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(GENERAL_BID_MODIFIER_OUT_OF_RANGE, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testAdScheduleDetailsBidModifierInvalidStartHour1ReturnValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        AdScheduleDetails adScheduleDetails = ((AdScheduleDetails) criterionDetailsList.get(0));
        adScheduleDetails.setStartHour(-1);
        singleCriterionDetailsList.add(adScheduleDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_AD_SCHEDULE_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(INVALID_HOUR_VALUE_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testAdScheduleDetailsBidModifierInvalidStartHour2ReturnValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        AdScheduleDetails adScheduleDetails = ((AdScheduleDetails) criterionDetailsList.get(0));
        adScheduleDetails.setStartHour(24);
        singleCriterionDetailsList.add(adScheduleDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_AD_SCHEDULE_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(INVALID_HOUR_VALUE_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testAdScheduleDetailsBidModifierInvalidEndHour1ReturnValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        AdScheduleDetails adScheduleDetails = ((AdScheduleDetails) criterionDetailsList.get(0));
        adScheduleDetails.setEndHour(-1);
        singleCriterionDetailsList.add(adScheduleDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_AD_SCHEDULE_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(INVALID_HOUR_VALUE_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testAdScheduleDetailsBidModifierInvalidEndHour2ReturnValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        AdScheduleDetails adScheduleDetails = ((AdScheduleDetails) criterionDetailsList.get(0));
        adScheduleDetails.setEndHour(24);
        singleCriterionDetailsList.add(adScheduleDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_AD_SCHEDULE_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(INVALID_HOUR_VALUE_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testAdScheduleDetailsBidModifierInvalidStartMinReturnValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        AdScheduleDetails adScheduleDetails = ((AdScheduleDetails) criterionDetailsList.get(0));
        adScheduleDetails.setStartMinute(-1);
        singleCriterionDetailsList.add(adScheduleDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_AD_SCHEDULE_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(INVALID_MINUTE_VALUE_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testAdScheduleDetailsBidModifierInvalidEndMinReturnValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        AdScheduleDetails adScheduleDetails = ((AdScheduleDetails) criterionDetailsList.get(0));
        adScheduleDetails.setEndMinute(-1);
        singleCriterionDetailsList.add(adScheduleDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_AD_SCHEDULE_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(INVALID_MINUTE_VALUE_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testProximityDetailsInvalidStatusReturnValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        ProximityDetails proximityDetails = ((ProximityDetails) criterionDetailsList.get(3));
        proximityDetails.setStatus(-1);
        singleCriterionDetailsList.add(proximityDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_PROXIMITY_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(GENERAL_INVALID_STATUS_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testProximityDetailsInvalidBidModifierReturnValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        ProximityDetails proximityDetails = ((ProximityDetails) criterionDetailsList.get(3));
        proximityDetails.setBidModifier(-1);
        singleCriterionDetailsList.add(proximityDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_PROXIMITY_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(GENERAL_BID_MODIFIER_OUT_OF_RANGE, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testProximityDetailsInvalidCityNameReturnValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        ProximityDetails proximityDetails = ((ProximityDetails) criterionDetailsList.get(3));
        proximityDetails.setCityName("cityname1");
        singleCriterionDetailsList.add(proximityDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_PROXIMITY_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(INVALID_PROXIMITY_CITY_NAME_VALUE_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testProximityDetailsInvalidPostalCodeReturnValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        ProximityDetails proximityDetails = ((ProximityDetails) criterionDetailsList.get(3));
        proximityDetails.setPostalCode("54321s");
        singleCriterionDetailsList.add(proximityDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_PROXIMITY_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(INVALID_PROXIMITY_POSTAL_CODE_VALUE_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testProximityDetailsMissingLocationDetailsReturnValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        ProximityDetails proximityDetails = ((ProximityDetails) criterionDetailsList.get(3));
        proximityDetails.setPostalCode("");
        singleCriterionDetailsList.add(proximityDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_PROXIMITY_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(INVALID_PROXIMITY_MISSING_ADDRESS_FIELDS_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testProximityDetailsMissingLocationDetails2ReturnValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        ProximityDetails proximityDetails = ((ProximityDetails) criterionDetailsList.get(3));
        proximityDetails.setPostalCode("54321");
        proximityDetails.setStreetAddress("");
        singleCriterionDetailsList.add(proximityDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_PROXIMITY_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(INVALID_PROXIMITY_MISSING_ADDRESS_FIELDS_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testProximityDetailsMissingLocationDetails3ReturnValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        ProximityDetails proximityDetails = ((ProximityDetails) criterionDetailsList.get(3));
        proximityDetails.setCityName("testcity");
        proximityDetails.setPostalCode("");
        singleCriterionDetailsList.add(proximityDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_PROXIMITY_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(INVALID_PROXIMITY_MISSING_ADDRESS_FIELDS_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testProximityDetailsInvalidRadiusReturnValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        ProximityDetails proximityDetails = ((ProximityDetails) criterionDetailsList.get(3));
        proximityDetails.setRadius(-5.0);
        singleCriterionDetailsList.add(proximityDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_PROXIMITY_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(INVALID_PROXIMITY_RADIUS_VALUE_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testProximityDetailsInvalidRadiusUnitsReturnValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        ProximityDetails proximityDetails = ((ProximityDetails) criterionDetailsList.get(3));
        proximityDetails.setRadiusUnits(-1);
        singleCriterionDetailsList.add(proximityDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_PROXIMITY_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(INVALID_PROXIMITY_RADIUS_UNIT_VALUE_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testProximityDetailsInvalidCoordinatesReturnValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        ProximityDetails proximityDetails = ((ProximityDetails) criterionDetailsList.get(3));
        proximityDetails.setStreetAddress("");
        proximityDetails.setPostalCode("");
        proximityDetails.setCityName("");
        proximityDetails.setMicroLatitude(0);
        proximityDetails.setMicroLongitude(10);
        singleCriterionDetailsList.add(proximityDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_PROXIMITY_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(INVALID_PROXIMITY_LONGITUDE_LATITUDE_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testProximityDetailsInvalidCoordinates2ReturnValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        ProximityDetails proximityDetails = ((ProximityDetails) criterionDetailsList.get(3));
        proximityDetails.setStreetAddress("");
        proximityDetails.setPostalCode("");
        proximityDetails.setCityName("");
        proximityDetails.setMicroLatitude(10);
        proximityDetails.setMicroLongitude(0);
        singleCriterionDetailsList.add(proximityDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_PROXIMITY_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(INVALID_PROXIMITY_LONGITUDE_LATITUDE_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testNegativeKeywordDetailsInvalidStatusReturnValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        NegativeKeywordDetails negativeKeywordDetails = ((NegativeKeywordDetails) criterionDetailsList.get(2));
        negativeKeywordDetails.setStatus(-1);
        singleCriterionDetailsList.add(negativeKeywordDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_NEGATIVE_KEYWORD_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(GENERAL_INVALID_STATUS_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testNegativeKeywordDetailsEmptyKeywordTextValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        NegativeKeywordDetails negativeKeywordDetails = ((NegativeKeywordDetails) criterionDetailsList.get(2));
        negativeKeywordDetails.setKeywordText("");
        singleCriterionDetailsList.add(negativeKeywordDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_NEGATIVE_KEYWORD_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(NEGATIVE_KEYWORD_TEXT_EMPTY_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testNegativeKeywordDetailsInvalidKeywordMatchTypeValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        NegativeKeywordDetails negativeKeywordDetails = ((NegativeKeywordDetails) criterionDetailsList.get(2));
        negativeKeywordDetails.setKeywordMatchType(-1);
        singleCriterionDetailsList.add(negativeKeywordDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_NEGATIVE_KEYWORD_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(NEGATIVE_KEYWORD_INVALID_MATCH_TYPE_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testDeviceDetailsInvalidStatusValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        DeviceDetails deviceDetails = ((DeviceDetails) criterionDetailsList.get(5));
        deviceDetails.setStatus(-1);
        singleCriterionDetailsList.add(deviceDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_DEVICE_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(GENERAL_INVALID_STATUS_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testDeviceDetailsInvalidDeviceTypeValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        DeviceDetails deviceDetails = ((DeviceDetails) criterionDetailsList.get(5));
        deviceDetails.setDeviceType(-1);
        singleCriterionDetailsList.add(deviceDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_DEVICE_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(INVALID_DEVICE_TYPE_DETAILS_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testDeviceDetailsBidModifierTooLowValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        DeviceDetails deviceDetails = ((DeviceDetails) criterionDetailsList.get(5));
        deviceDetails.setBidModifier(-1);
        singleCriterionDetailsList.add(deviceDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_DEVICE_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(GENERAL_BID_MODIFIER_OUT_OF_RANGE, validationErrorResponse.getErrorMessage());
    }
    @Test
    void testDeviceDetailsBidModifierTooHighValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        DeviceDetails deviceDetails = ((DeviceDetails) criterionDetailsList.get(5));
        deviceDetails.setBidModifier(11);
        singleCriterionDetailsList.add(deviceDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_DEVICE_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(GENERAL_BID_MODIFIER_OUT_OF_RANGE, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testLanguageDetailsInvalidStatusValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        LanguageDetails languageDetails = ((LanguageDetails) criterionDetailsList.get(6));
        languageDetails.setStatus(-1);
        singleCriterionDetailsList.add(languageDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_LANGUAGE_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(GENERAL_INVALID_STATUS_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testLanguageDetailsEmptyLanguageCodeValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        LanguageDetails languageDetails = ((LanguageDetails) criterionDetailsList.get(6));
        languageDetails.setLanguageCode("");
        singleCriterionDetailsList.add(languageDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_LANGUAGE_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(INVALID_LANGUAGE_CODE_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testLanguageDetailsInvalidLanguageCodeValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        LanguageDetails languageDetails = ((LanguageDetails) criterionDetailsList.get(6));
        languageDetails.setLanguageCode(LANGUAGE_CODE_PREFIX + "test");
        singleCriterionDetailsList.add(languageDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_LANGUAGE_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(INVALID_LANGUAGE_CODE_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testLocationDetailsInvalidLanguageCodeValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        LocationDetails locationDetails = ((LocationDetails) criterionDetailsList.get(7));
        locationDetails.setStatus(-1);
        singleCriterionDetailsList.add(locationDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_LOCATION_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(GENERAL_INVALID_STATUS_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testLocationDetailsBidModifierTooLowValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        LocationDetails locationDetails = ((LocationDetails) criterionDetailsList.get(7));
        locationDetails.setBidModifier(-1);
        singleCriterionDetailsList.add(locationDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_LOCATION_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(GENERAL_BID_MODIFIER_OUT_OF_RANGE, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testLocationDetailsBidModifierTooHighValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        LocationDetails locationDetails = ((LocationDetails) criterionDetailsList.get(7));
        locationDetails.setBidModifier(11);
        singleCriterionDetailsList.add(locationDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_LOCATION_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(GENERAL_BID_MODIFIER_OUT_OF_RANGE, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testLocationDetailsBidModifierPlusNegativeValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        LocationDetails locationDetails = ((LocationDetails) criterionDetailsList.get(7));
        locationDetails.setBidModifier(1);
        locationDetails.setNegative(true);
        singleCriterionDetailsList.add(locationDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_LOCATION_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(GENERAL_BID_MODIFIER_PLUS_NEGATIVE, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testLocationDetailsLocationEmptyValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        LocationDetails locationDetails = ((LocationDetails) criterionDetailsList.get(7));
        locationDetails.setLocation("");
        singleCriterionDetailsList.add(locationDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_LOCATION_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(INVALID_EMPTY_LOCATION_ERR_MSG, validationErrorResponse.getErrorMessage());
    }
    @Test
    void testLocationDetailsLocationContainsDigitsValidationError() {
        List<CriterionDetails> criterionDetailsList =
                testUtils.getMockCriterionMapping().get(MOCK_CAMPAIGN_RESOURCE_NAME);

        List<CriterionDetails> singleCriterionDetailsList = new ArrayList<>();

        LocationDetails locationDetails = ((LocationDetails) criterionDetailsList.get(7));
        locationDetails.setLocation("city2");
        singleCriterionDetailsList.add(locationDetails);

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCriterionDetailsValid(singleCriterionDetailsList);

        assert validationErrorResponse != null;
        assertEquals(INVALID_LOCATION_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(INVALID_LOCATION_VALUE_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testValidUpdateCampaignReturnsNullValidationErrorResponse() {
        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCampaignDetailsValid(testUtils.getMockCampaignDetails(), OperationType.UPDATE);
        assertNull(validationErrorResponse);
    }

    @Test
    void testUpdateCampaignDetailsWithMissingResNameReturnValidationError() {
        CampaignDetails campaignDetails = testUtils.getMockCampaignDetails();
        campaignDetails.setCampaignResourceName("");

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isCampaignDetailsValid(campaignDetails, OperationType.UPDATE);

        assert validationErrorResponse != null;
        assertEquals(INVALID_CAMPAIGN_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(GENERAL_RES_NAME_EMPTY_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testValidUpdateBudgetDetailsReturnsNullValidationError() {
        BudgetDetails budgetDetails = testUtils.getMockBudgetDetails();
        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isBudgetDetailsValid(budgetDetails, OperationType.UPDATE);

        assertNull(validationErrorResponse);
    }

    @Test
    void testUpdateBudgetDetailsMissingResNameReturnsValidationError() {
        BudgetDetails budgetDetails = testUtils.getMockBudgetDetails();
        budgetDetails.setResourceName("");

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isBudgetDetailsValid(budgetDetails, OperationType.UPDATE);

        assert validationErrorResponse != null;
        assertEquals(INVALID_BUDGET_DETAILS_ERR_CODE, validationErrorResponse.getErrorCode());
        assertEquals(GENERAL_RES_NAME_EMPTY_ERR_MSG, validationErrorResponse.getErrorMessage());
    }

    @Test
    void testValidateNonEmptyCampaignResourceNamesReturnsNull() {
        List<CampaignDetails> campaignDetailsList = testUtils.getMockCampaignDetailsList();

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.validateNonEmptyCampaignResourceNames(campaignDetailsList);

        assertNull(validationErrorResponse);
    }

    @Test
    void testValidateEmptyCampaignResourceNamesReturnsValidationError() {
        List<CampaignDetails> campaignDetailsList = testUtils.getMockCampaignDetailsList();
        campaignDetailsList.get(0).setCampaignResourceName("");

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.validateNonEmptyCampaignResourceNames(campaignDetailsList);

        assertEquals(INVALID_REQUEST_ERROR, validationErrorResponse.getErrorCode());
        assertEquals(MISSING_PARAMS, validationErrorResponse.getErrorMessage());
    }
}
