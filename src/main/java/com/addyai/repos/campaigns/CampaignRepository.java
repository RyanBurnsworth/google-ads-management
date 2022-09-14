package com.addyai.repos.campaigns;

import com.addyai.models.BudgetDetails;
import com.addyai.models.CampaignDetails;
import com.google.ads.googleads.v11.services.CampaignBudgetOperation;
import com.google.ads.googleads.v11.services.CampaignOperation;

import java.util.List;

public interface CampaignRepository {
    List<CampaignDetails> getCampaignDetails(long customerId) throws Exception;

    void updateCampaigns(long customerId, List<CampaignOperation> campaignOperations) throws Exception;

    void deleteCampaigns(long customerId, List<Long> campaignOperations) throws Exception;

    void addCampaigns(long customerId, List<CampaignOperation> campaignOperations) throws Exception;

    String createSingleCampaignBudget(long customerId, BudgetDetails budgetDetails) throws Exception;

    List<BudgetDetails> getCampaignBudgetDetails(long customerId) throws Exception;

    void updateCampaignBudgets(long customerId, List<CampaignBudgetOperation> campaignBudgetOperations) throws Exception;

    void deleteCampaignBudgets(long customerId, List<Long> campaignBudgetIds);
}
