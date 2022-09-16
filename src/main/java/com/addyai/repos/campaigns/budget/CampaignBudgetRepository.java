package com.addyai.repos.campaigns.budget;

import com.addyai.models.BudgetDetails;
import com.google.ads.googleads.v11.services.CampaignBudgetOperation;

import java.util.List;

public interface CampaignBudgetRepository {
    List<BudgetDetails> fetchAllCampaignBudgetDetails(long customerId) throws Exception;

    List<BudgetDetails> createOrUpdateBudgets(long customerId, List<CampaignBudgetOperation> campaignBudgetOperationList) throws Exception;

    void deleteCampaignBudgets(long customerId, List<Long> budgetIds);

}
