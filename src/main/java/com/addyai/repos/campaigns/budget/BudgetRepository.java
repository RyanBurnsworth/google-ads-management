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

package com.addyai.repos.campaigns.budget;

import com.addyai.models.BudgetDetails;
import com.google.ads.googleads.v11.services.CampaignBudgetOperation;

import java.util.List;

public interface BudgetRepository {
    List<BudgetDetails> fetchAllBudgetDetails(long customerId) throws Exception;

    List<String> performCampaignBudgetOperations(long customerId, List<CampaignBudgetOperation> campaignBudgetOperationList) throws Exception;
}
