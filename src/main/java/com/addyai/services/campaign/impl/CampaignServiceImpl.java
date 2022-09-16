package com.addyai.services.campaign.impl;

import com.addyai.models.BudgetDetails;
import com.addyai.models.CampaignDetails;
import com.addyai.repos.campaigns.CampaignRepository;
import com.addyai.repos.campaigns.budget.CampaignBudgetRepository;
import com.addyai.services.campaign.CampaignService;
import com.addyai.utils.CampaignUtils;
import com.google.ads.googleads.lib.utils.FieldMasks;
import com.google.ads.googleads.v11.resources.Campaign;
import com.google.ads.googleads.v11.services.CampaignBudgetOperation;
import com.google.ads.googleads.v11.services.CampaignOperation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CampaignServiceImpl implements CampaignService {
    private final CampaignRepository campaignRepository;

    private final CampaignBudgetRepository campaignBudgetRepository;

    private final CampaignUtils campaignUtils;


    public CampaignServiceImpl(CampaignRepository campaignRepository,
                               CampaignBudgetRepository campaignBudgetRepository) {
        this.campaignUtils = new CampaignUtils();
        this.campaignRepository = campaignRepository;
        this.campaignBudgetRepository = campaignBudgetRepository;
    }

    /**
     * Add campaigns to a client's account
     *
     * @param customerId          the customer id of the client account
     * @param campaignDetailsList [CampaignDetails] to be used in campaign creation
     */
    @Override
    public void addCampaignsToAccount(long customerId, List<CampaignDetails> campaignDetailsList) throws Exception {
        List<CampaignOperation> campaignOperations = new ArrayList<>();

        // fetch all campaign budget details from the client account
        List<BudgetDetails> existingBudgets = campaignBudgetRepository.fetchAllCampaignBudgetDetails(customerId);

        for (CampaignDetails campaignDetails : campaignDetailsList) {
            // validate campaign details before proceeding
            campaignUtils.validateCampaignDetails(campaignDetails);

            // validate the campaign budget details and assign budget resource to campaign
            campaignUtils.validateCampaignBudgetDetails(campaignDetails.getBudgetDetails());

            // get the resource name of an existing budget in the client's account
            String resourceName = campaignUtils
                    .getResourceNameForExistingBudget(campaignDetails.getBudgetDetails(), existingBudgets);

            // if the resource name is empty, create a campaign budget resource on the client account
            if (resourceName.isEmpty()) {
                // create a campaign budget operation only for this single budget
                List<BudgetDetails> singleBudgetDetailsList = Collections.singletonList(campaignDetails.getBudgetDetails());
                List<CampaignBudgetOperation> campaignBudgetOperations =
                        campaignUtils.buildCampaignBudgetOperationList(singleBudgetDetailsList, true);

                // create this budget on the client's account
                List<BudgetDetails> createdBudgets =
                        campaignBudgetRepository.createOrUpdateBudgets(customerId, campaignBudgetOperations);

                // associated the newly created budget with campaign
                BudgetDetails createdBudget = createdBudgets.get(0);
                campaignDetails.setBudgetResourceName(createdBudget.getResourceName());
            } else {
                // if resourceName is not empty, set this as the budgetResourceName in the campaignDetails
                campaignDetails.setBudgetResourceName(resourceName);
            }

            // create a Google Ads campaign object from campaign details
            Campaign campaign = campaignUtils.buildCampaignFromDetails(campaignDetails, true);

            // create a CREATE campaign operation
            CampaignOperation op = CampaignOperation.newBuilder()
                    .setCreate(campaign)
                    .build();

            // add to campaign operations list
            campaignOperations.add(op);
        }

        // add campaigns to the client account
        campaignRepository.addCampaigns(customerId, campaignOperations);
    }

    /**
     * Fetch campaigns from a client account
     *
     * @param customerId the customer id of the client account
     * @return a list of complete campaign details containing all campaigns in a client's account
     */
    @Override
    public List<CampaignDetails> findAllCampaignDetails(long customerId) throws Exception {
        // fetch all campaigns from the client account
        List<CampaignDetails> campaignDetailsList = campaignRepository.fetchAllCampaignDetails(customerId);

        // fetch all campaign budget details from the client account
        List<BudgetDetails> existingBudgets = campaignBudgetRepository.fetchAllCampaignBudgetDetails(customerId);

        // associate the budget details to its campaign details
        for (CampaignDetails campaignDetails : campaignDetailsList) {
            BudgetDetails budgetDetails = campaignUtils.findBudgetDetailsByResourceName(campaignDetails.getBudgetResourceName(),
                    existingBudgets);

            campaignDetails.setBudgetDetails(budgetDetails);
        }
        return campaignDetailsList;
    }

    /**
     * Update a campaign in a Google Ads account
     *
     * @param campaignDetailsList a list of updated [CampaignDetails]
     */
    @Override
    public void updateCampaign(long customerId, List<CampaignDetails> campaignDetailsList) throws Exception {
        List<CampaignOperation> campaignOperations = new ArrayList<>();

        // create an UPDATE campaign operation for each campaign
        for (CampaignDetails campaignDetails : campaignDetailsList) {
            Campaign campaign = campaignUtils.buildCampaignFromDetails(campaignDetails, false);
            CampaignOperation operation = CampaignOperation.newBuilder()
                    .setUpdate(campaign)
                    .setUpdateMask(FieldMasks.allSetFieldsOf(campaign))
                    .build();

            // add newly created operation to list
            campaignOperations.add(operation);
        }

        // perform update on all campaigns
        campaignRepository.updateCampaigns(customerId, campaignOperations);
    }

    /**
     * Delete campaigns from a client's account
     *
     * @param customerId  the customer id of the client account
     * @param campaignIds the ids of the campaigns to be deleted
     */
    @Override
    public void deleteCampaigns(long customerId, List<Long> campaignIds) throws Exception {
        campaignRepository.deleteCampaigns(customerId, campaignIds);
    }

    /**
     * Update campaign budgets within a client's account
     *
     * @param customerId        the customer id of the client account
     * @param budgetDetailsList a list of [BudgetDetails] to be updated
     */
    @Override
    public void updateCampaignBudgets(long customerId, List<BudgetDetails> budgetDetailsList) throws Exception {
        // validate budget details before proceeding
        for (BudgetDetails budgetDetails : budgetDetailsList) {
            campaignUtils.validateCampaignBudgetDetails(budgetDetails);
        }

        // create a list of CREATE campaign budget operations using the budgetDetails within each campaignDetails
        // in the campaign details list
        List<CampaignBudgetOperation> campaignBudgetOperations =
                campaignUtils.buildCampaignBudgetOperationList(budgetDetailsList, false);

        // perform update on all campaigns
        campaignBudgetRepository.createOrUpdateBudgets(customerId, campaignBudgetOperations);
    }
}
