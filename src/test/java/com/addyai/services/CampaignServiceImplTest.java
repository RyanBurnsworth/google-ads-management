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

import com.addyai.repos.campaigns.CampaignRepository;
import com.addyai.repos.campaigns.budget.BudgetRepository;
import com.addyai.repos.campaigns.criterion.CriterionRepository;
import com.addyai.services.campaign.CampaignService;
import com.addyai.services.campaign.impl.CampaignServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = CampaignServiceImpl.class)
public class CampaignServiceImplTest {
    @Autowired
    private CampaignService campaignService;

    @MockBean
    private CampaignRepository campaignRepository;

    @MockBean
    private BudgetRepository budgetRepository;

    @MockBean
    private CriterionRepository criterionRepository;
}
