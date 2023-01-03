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

package com.addyai.services;

import com.addyai.builder.OperationBuilder;
import com.addyai.builder.impl.OperationBuilderImpl;
import com.addyai.enums.OperationType;
import com.addyai.mocks.CampaignMocks;
import com.addyai.models.CampaignDetails;
import com.addyai.repos.campaign.CampaignRepository;
import com.addyai.repos.campaign.budget.BudgetRepository;
import com.addyai.repos.campaign.criterion.CriterionRepository;
import com.addyai.services.campaign.CampaignService;
import com.addyai.services.campaign.impl.CampaignServiceImpl;
import com.google.ads.googleads.v12.services.CampaignBudgetOperation;
import com.google.ads.googleads.v12.services.CampaignCriterionOperation;
import com.google.ads.googleads.v12.services.CampaignOperation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.addyai.mocks.CampaignMocks.*;
import static com.addyai.utils.misc.Constants.DEFAULT_COUNTRY_CODE;
import static com.addyai.utils.misc.Constants.DEFAULT_LOCALE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = CampaignServiceImpl.class)
public class CampaignServiceImplTest {
    private static final long CUSTOMER_ID = 1L;

    @Autowired
    private CampaignService campaignService;

    @MockBean
    private CampaignRepository campaignRepository;

    @MockBean
    private BudgetRepository budgetRepository;

    @MockBean
    private CriterionRepository criterionRepository;

    private final CampaignMocks campaignMocks = new CampaignMocks();

    private final OperationBuilder operationBuilder = new OperationBuilderImpl();

    @Test
    void testCreateCampaignWithCriterionIsSuccessful() throws Exception {
        // set the mock campaign details
        List<CampaignDetails> campaignDetailsList = campaignMocks.getMockCampaignDetailsList();

        // initialize test for creating a campaign
        List<CampaignCriterionOperation> campaignCriterionOperations =
                performCreateCampaignInitialization(campaignDetailsList);

        // perform the method under test
        campaignService.upsertCampaigns(CUSTOMER_ID, campaignDetailsList, true);

        verify(criterionRepository, times(1))
                .performCriterionOperations(CUSTOMER_ID, campaignCriterionOperations);

        assertEquals(MOCK_CAMPAIGN_BUDGET_RESOURCE_NAME, campaignDetailsList.get(0).getBudgetResourceName());
    }

    @Test
    void testCreateCampaignWithoutCriterionIsSuccessful() throws Exception {
        List<CampaignDetails> campaignDetailsList = campaignMocks.getMockCampaignDetailsList2();

        List<CampaignCriterionOperation> campaignCriterionOperations =
                performCreateCampaignInitialization(campaignDetailsList);

        campaignService.upsertCampaigns(CUSTOMER_ID, campaignDetailsList, true);

        verify(criterionRepository, times(0))
                .getGeoTargetConstant(DEFAULT_LOCALE, DEFAULT_COUNTRY_CODE, MOCK_CITY_NAME);

        verify(criterionRepository, times(0))
                .performCriterionOperations(CUSTOMER_ID, campaignCriterionOperations);

        assertEquals(MOCK_CAMPAIGN_BUDGET_RESOURCE_NAME, campaignDetailsList.get(0).getBudgetResourceName());
    }

    @Test
    void testCreateCampaignWithAndWithoutCriterionIsSuccessful() throws Exception {
        // set the mock campaign details
        List<CampaignDetails> campaignDetailsList = campaignMocks.getMockCampaignDetailsList();

        // add campaign details without criterion
        campaignDetailsList.addAll(campaignMocks.getMockCampaignDetailsList2());

        // initialize test for creating a campaign
        List<CampaignCriterionOperation> campaignCriterionOperations =
                performCreateCampaignInitialization(campaignDetailsList);

        // perform the method under test
        campaignService.upsertCampaigns(CUSTOMER_ID, campaignDetailsList, true);

        verify(criterionRepository, times(1))
                .performCriterionOperations(CUSTOMER_ID, campaignCriterionOperations);

        assertEquals(MOCK_CAMPAIGN_BUDGET_RESOURCE_NAME, campaignDetailsList.get(0).getBudgetResourceName());
    }
    @Test
    void testUpdateCampaignWithCriterionIsSuccessful() throws Exception {
        List<CampaignDetails> campaignDetailsList = campaignMocks.getMockCampaignDetailsList();

        List<CampaignCriterionOperation> campaignCriterionOperations =
                performUpdateCampaignInitialization(campaignDetailsList);

        List<CampaignCriterionOperation> removeOps = campaignMocks.getRemoveCampaignCriterionOpList();

        when(criterionRepository.performCriterionOperations(CUSTOMER_ID, removeOps))
                .thenReturn(Collections.singletonList(MOCK_CRITERION_RESOURCE_NAME));

        campaignService.upsertCampaigns(CUSTOMER_ID, campaignDetailsList, false);

        verify(criterionRepository, times(1))
                .performCriterionOperations(CUSTOMER_ID, campaignCriterionOperations);

        verify(criterionRepository, times(1))
                .fetchCampaignCriterionDetails(CUSTOMER_ID, MOCK_CAMPAIGN_RESOURCE_NAME);

        assertEquals(MOCK_CAMPAIGN_BUDGET_RESOURCE_NAME, campaignDetailsList.get(0).getBudgetResourceName());
    }

    private List<CampaignCriterionOperation> performCreateCampaignInitialization(
            List<CampaignDetails> campaignDetailsList) throws Exception {

        // set the mock campaign budget operations for CREATE ops
        List<CampaignBudgetOperation> campaignBudgetOperations
                = operationBuilder.buildCampaignBudgetOperationList(Collections
                .singletonList(campaignDetailsList.get(0).getBudgetDetails()), OperationType.CREATE);

        // set the budget resource name before creating campaign ops
        campaignDetailsList.get(0).setBudgetResourceName(MOCK_BUDGET_RESOURCE_NAME); // clear before starting creation process
        campaignDetailsList.get(0).getBudgetDetails().setResourceName(MOCK_BUDGET_RESOURCE_NAME);

        // create the campaign operations list
        List<CampaignOperation> campaignOperations = operationBuilder
                .buildCampaignOperationList(campaignDetailsList, OperationType.CREATE);

        // create the campaign criterion operations list
        List<CampaignCriterionOperation> campaignCriterionOperations = operationBuilder
                .buildCampaignCriterionOperationList(campaignMocks.getMockCriterionMapping(), OperationType.CREATE);

        // clear the budget name
        campaignDetailsList.get(0).setBudgetResourceName(""); // clear before starting creation process

        // mock the performing of campaign budget operations
        when(budgetRepository.performCampaignBudgetOperations(CUSTOMER_ID, campaignBudgetOperations)).thenReturn(
                Collections.singletonList(MOCK_CAMPAIGN_BUDGET_RESOURCE_NAME));

        // mock the performing of campaign operations
        when(campaignRepository.performCampaignOperations(CUSTOMER_ID, campaignOperations))
                .thenReturn(Collections.singletonList(MOCK_CAMPAIGN_RESOURCE_NAME));

        // mock the fetching of the geo target constant
        when(criterionRepository.getGeoTargetConstant(DEFAULT_LOCALE, DEFAULT_COUNTRY_CODE, MOCK_CITY_NAME))
                .thenReturn(MOCK_LOCATION_GEO_TARGET_CONSTANT);

        // mock the performing of the criterion operations
        when(criterionRepository.performCriterionOperations(CUSTOMER_ID, campaignCriterionOperations))
                .thenReturn(new ArrayList<>());

        return campaignCriterionOperations;
    }

    private List<CampaignCriterionOperation> performUpdateCampaignInitialization(
            List<CampaignDetails> campaignDetailsList) throws Exception {

        // set the budget resource name before updating campaign ops
        campaignDetailsList.get(0).setBudgetResourceName(MOCK_BUDGET_RESOURCE_NAME);
        campaignDetailsList.get(0).getBudgetDetails().setResourceName(MOCK_BUDGET_RESOURCE_NAME);

        // set the mock campaign budget operations for UPDATE ops
        List<CampaignBudgetOperation> campaignBudgetOperations
                = operationBuilder.buildCampaignBudgetOperationList(Collections
                .singletonList(campaignDetailsList.get(0).getBudgetDetails()), OperationType.UPDATE);

        // set the mock campaign budget operations for UPDATE ops
        List<CampaignOperation> campaignOperations = operationBuilder
                .buildCampaignOperationList(campaignDetailsList, OperationType.UPDATE);

        // set the mock for campaign criterion operations for UPDATE ops
        List<CampaignCriterionOperation> campaignCriterionOperations = operationBuilder
                .buildCampaignCriterionOperationList(campaignMocks.getMockCriterionMapping(), OperationType.CREATE);

        // mock the performing of campaign budget operations
        when(budgetRepository.performCampaignBudgetOperations(CUSTOMER_ID, campaignBudgetOperations)).thenReturn(
                Collections.singletonList(MOCK_CAMPAIGN_BUDGET_RESOURCE_NAME));

        // mock the performing of campaign operations
        when(campaignRepository.performCampaignOperations(CUSTOMER_ID, campaignOperations))
                .thenReturn(Collections.singletonList(MOCK_CAMPAIGN_RESOURCE_NAME));

        // mock the fetching of the geo target constant
        when(criterionRepository.getGeoTargetConstant(DEFAULT_LOCALE, DEFAULT_COUNTRY_CODE, MOCK_CITY_NAME))
                .thenReturn(MOCK_LOCATION_GEO_TARGET_CONSTANT);

        // mock the performing of campaign criterion operations
        when(criterionRepository.performCriterionOperations(CUSTOMER_ID, campaignCriterionOperations))
                .thenReturn(new ArrayList<>());

        // mock the fetching of existing campaign criterion for the given resource name
        when(criterionRepository.fetchCampaignCriterionDetails(CUSTOMER_ID, MOCK_CAMPAIGN_RESOURCE_NAME))
                .thenReturn(campaignMocks.getMockCampaignDetails().getCampaignCriteriaList());

        return campaignCriterionOperations;
    }
}
