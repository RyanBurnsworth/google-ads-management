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

package com.addyai.builder;

import com.addyai.enums.OperationType;
import com.addyai.models.AdGroupDetails;
import com.addyai.models.BudgetDetails;
import com.addyai.models.CampaignDetails;
import com.addyai.models.campaign_criterion.CriterionDetails;
import com.google.ads.googleads.v11.services.AdGroupOperation;
import com.google.ads.googleads.v11.services.CampaignBudgetOperation;
import com.google.ads.googleads.v11.services.CampaignCriterionOperation;
import com.google.ads.googleads.v11.services.CampaignOperation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface OperationBuilder {
    List<CampaignOperation> buildCampaignOperationList(List<CampaignDetails> campaignDetailsList,
                                                       OperationType operationType);

    List<CampaignBudgetOperation> buildCampaignBudgetOperationList(List<BudgetDetails> budgetDetailsList,
                                                                   OperationType operationType);

    List<CampaignCriterionOperation> buildCampaignCriterionOperationList(Map<String, List<CriterionDetails>> criterionMapper,
                                                                         OperationType operationType);

    List<AdGroupOperation> buildAdGroupOperationList(List<AdGroupDetails> adGroupDetailsList, OperationType operationType);
}
