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
import com.addyai.enums.OperationType;
import com.addyai.models.CampaignDetails;
import com.addyai.repos.campaign.CampaignRepository;
import com.addyai.repos.campaign.budget.BudgetRepository;
import com.addyai.repos.campaign.criterion.CriterionRepository;
import com.addyai.services.campaign.CampaignService;
import com.addyai.services.campaign.impl.CampaignServiceImpl;
import com.addyai.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.addyai.utils.TestUtils.MOCK_CAMPAIGN_BUDGET_RESOURCE_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CampaignServiceImpl.class)
public class CampaignServiceImplTest {
    private static long CUSTOMER_ID = 1L;

    @Autowired
    private CampaignService campaignService;

    @MockBean
    private CampaignRepository campaignRepository;

    @MockBean
    private BudgetRepository budgetRepository;

    @MockBean
    private CriterionRepository criterionRepository;

    private TestUtils testUtils = new TestUtils();

    @MockBean
    private OperationBuilder operationBuilder;

    @Test
    void testCreateCampaignIsSuccessful() throws Exception {
        List<CampaignDetails> campaignDetailsList = testUtils.getMockCampaignDetailsList();
        campaignDetailsList.get(0).setBudgetResourceName(""); // clear before starting creation process

        when(budgetRepository.performCampaignBudgetOperations(CUSTOMER_ID, new ArrayList<>())).thenReturn(
                Collections.singletonList(MOCK_CAMPAIGN_BUDGET_RESOURCE_NAME));

        when(operationBuilder.buildCampaignBudgetOperationList(Collections
                .singletonList(campaignDetailsList.get(0).getBudgetDetails()), OperationType.CREATE)).thenReturn(
                new ArrayList<>());

        campaignService.upsertCampaigns(CUSTOMER_ID, campaignDetailsList, true);
        assertEquals(MOCK_CAMPAIGN_BUDGET_RESOURCE_NAME, campaignDetailsList.get(0).getBudgetResourceName());
    }
}
