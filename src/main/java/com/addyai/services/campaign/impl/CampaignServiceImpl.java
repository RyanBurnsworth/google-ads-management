package com.addyai.services.campaign.impl;

import com.addyai.error_handling.exceptions.InvalidRequestException;
import com.addyai.models.BudgetDetails;
import com.addyai.models.CampaignDetails;
import com.addyai.models.campaign_criterion.CriterionDetails;
import com.addyai.models.campaign_criterion.LocationDetails;
import com.addyai.repos.campaigns.CampaignRepository;
import com.addyai.repos.campaigns.budget.CampaignBudgetRepository;
import com.addyai.repos.campaigns.criterion.CriterionRepository;
import com.addyai.services.campaign.CampaignService;
import com.addyai.utils.helpers.CampaignHelper;
import com.google.ads.googleads.lib.utils.FieldMasks;
import com.google.ads.googleads.v11.resources.Campaign;
import com.google.ads.googleads.v11.services.CampaignBudgetOperation;
import com.google.ads.googleads.v11.services.CampaignCriterionOperation;
import com.google.ads.googleads.v11.services.CampaignOperation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.addyai.utils.misc.Constants.INVALID_REQUEST_ERROR;
import static com.addyai.utils.misc.Constants.MISSING_PARAMS;

@Service
public class CampaignServiceImpl implements CampaignService {
    private final CampaignRepository campaignRepository;

    private final CampaignBudgetRepository campaignBudgetRepository;

    private final CriterionRepository criterionRepository;

    private final CampaignHelper campaignHelper;

    public CampaignServiceImpl(CampaignRepository campaignRepository,
                               CampaignBudgetRepository campaignBudgetRepository,
                               CriterionRepository criterionRepository) {
        this.campaignHelper = new CampaignHelper();
        this.campaignRepository = campaignRepository;
        this.campaignBudgetRepository = campaignBudgetRepository;
        this.criterionRepository = criterionRepository;
    }

    /**
     * Add campaigns including campaign budget and campaign criterion to a client's account
     *
     * @param customerId          the customer id of the client account
     * @param campaignDetailsList list of [CampaignDetails] to be used in campaign creation
     */
    @Override
    public void addCampaignsToAccount(long customerId, List<CampaignDetails> campaignDetailsList) throws Exception {
        List<CampaignOperation> campaignOperations = new ArrayList<>();

        for (CampaignDetails campaignDetails : campaignDetailsList) {
            // validate campaign details before proceeding
            campaignHelper.validateCampaignDetails(campaignDetails);

            // validate the campaign budget details
            campaignHelper.validateCampaignBudgetDetails(campaignDetails.getBudgetDetails());

            //TODO: validate campaign criterion details list

            // create a campaign budget operation for this individual budget
            List<BudgetDetails> singleBudgetDetailsList = Collections.singletonList(campaignDetails.getBudgetDetails());
            List<CampaignBudgetOperation> campaignBudgetOperations =
                    campaignHelper.buildCampaignBudgetOperationList(singleBudgetDetailsList, true);

            // create the budget and extract the budget resource name
            String budgetResourceName =
                    campaignBudgetRepository.createOrUpdateBudgets(customerId, campaignBudgetOperations).get(0);

            // associated the newly created budget with campaign
            campaignDetails.setBudgetResourceName(budgetResourceName);

            // create a Google Ads campaign object from campaign details
            Campaign campaign = campaignHelper.buildCampaignFromDetails(campaignDetails, true);

            // create a CREATE campaign operation
            CampaignOperation op = CampaignOperation.newBuilder()
                    .setCreate(campaign)
                    .build();

            // add to campaign operations list
            campaignOperations.add(op);
        }

        // add campaigns to the client account and store the campaign resource names
        List<String> campaignResourceNames = campaignRepository.addCampaigns(customerId, campaignOperations);

        // Instantiate an empty CampaignCriterionOperation list
        List<CampaignCriterionOperation> campaignCriterionOperationList = new ArrayList<>();

        // set the campaignResource name and, if needed,
        // set the geo target location constant for locationDetails
        for (int i = 0; i < campaignResourceNames.size(); i++) {
            // extract the campaign resource name from the list
            String campaignResourceName = campaignResourceNames.get(i);

            // extract the campaign criterion list
            List<CriterionDetails> criterionDetailsList
                    = campaignDetailsList.get(i).getCampaignCriteriaList();

            // set the campaignResourceName to it's campaignCriterion
            for (CriterionDetails criterionDetails : criterionDetailsList) {
                criterionDetails.setCampaignResourceName(campaignResourceName);
            }

            // if location details object exists within criterionDetails, retrieve and set the geo targeting constant
            for (CriterionDetails criterionDetails : criterionDetailsList) {
                if (criterionDetails instanceof LocationDetails) {
                    LocationDetails locationDetails = ((LocationDetails) criterionDetails);

                    // fetch the geo target constant from Google Ads
                    String geoTargetConstant = getGeoTargetConstant(locationDetails.getLocale(),
                            locationDetails.getCountryCode(),
                            locationDetails.getLocation());

                    // set the geo target constant for this campaign criterion
                    locationDetails.setGeoTargetingConstant(geoTargetConstant);
                }
            }

            // build a list of campaign criterion operations
            List<CampaignCriterionOperation> campaignCriterionOperations =
                    campaignHelper.buildCampaignCriterionOperationList(campaignDetailsList.get(i).getCampaignCriteriaList(),
                            true);

            // add all the new campaignCriterionOperations to the existing list of operations
            campaignCriterionOperationList.addAll(campaignCriterionOperations);
        }

        // create campaign criterion for each campaign on the client account
        criterionRepository.addCampaignCriterion(customerId, campaignCriterionOperationList);
    }

    /**
     * Fetch all campaigns from a client account
     *
     * @param customerId the customer id of the client account
     * @return a list of all [CampaignDetails] in a client's account
     */
    @Override
    public List<CampaignDetails> findAllCampaignDetails(long customerId) throws Exception {
        // fetch all campaigns from the client account
        List<CampaignDetails> campaignDetailsList = campaignRepository.fetchAllCampaignDetails(customerId);

        // fetch all campaign budget details from the client account
        List<BudgetDetails> existingBudgets = campaignBudgetRepository.fetchAllCampaignBudgetDetails(customerId);

        // associate the budget details to its campaign details
        for (CampaignDetails campaignDetails : campaignDetailsList) {
            BudgetDetails budgetDetails = campaignHelper.findBudgetDetailsByResourceName(campaignDetails.getBudgetResourceName(),
                    existingBudgets);

            // assign the budget details object to the campaign details object
            campaignDetails.setBudgetDetails(budgetDetails);
        }
        return campaignDetailsList;
    }

    /**
     * Fetch a single CampaignDetails by name
     *
     * @param customerId   the customer id of the client account
     * @param campaignName the name of the campaign to fetch
     * @return CampaignDetails
     * @throws Exception
     */
    @Override
    public CampaignDetails findCampaignDetailsByName(long customerId, String campaignName) throws Exception {
        // throw an InvalidRequestException if the campaignName is missing
        if (campaignName.isEmpty()) {
            throw new InvalidRequestException(INVALID_REQUEST_ERROR, MISSING_PARAMS, "Missing campaign name");
        }

        // fetch the campaign details from the client account using the campaign name
        CampaignDetails campaignDetails = campaignRepository.fetchCampaignDetailsByName(customerId, campaignName);

        // TODO create an endpoint to grab a single budget details object by id or name
        // fetch all the campaign budgets from the client's account
        List<BudgetDetails> existingBudgets = campaignBudgetRepository.fetchAllCampaignBudgetDetails(customerId);

        // find the specific budget details object for this campaign
        BudgetDetails budgetDetails = campaignHelper.findBudgetDetailsByName(campaignName, existingBudgets);

        // set the budget details object
        campaignDetails.setBudgetDetails(budgetDetails);

        return campaignDetails;
    }

    /**
     * Update campaigns in a client's account
     *
     * @param customerId          the customer id of the client's account
     * @param campaignDetailsList a list of updated [CampaignDetails]
     */
    @Override
    public void updateCampaigns(long customerId, List<CampaignDetails> campaignDetailsList) throws Exception {
        List<CampaignOperation> campaignOperations = new ArrayList<>();
        List<BudgetDetails> budgetDetailsList = new ArrayList<>();

        // extract all the budget details from the campaign details into a list
        for (CampaignDetails campaignDetails : campaignDetailsList) {
            // validate the campaign budget details before adding to the list
            campaignHelper.validateCampaignBudgetDetails(campaignDetails.getBudgetDetails());
            budgetDetailsList.add(campaignDetails.getBudgetDetails());
        }

        // create a list of campaign budget operations for updating campaign budgets
        List<CampaignBudgetOperation> campaignBudgetOperations =
                campaignHelper.buildCampaignBudgetOperationList(budgetDetailsList, false);

        // update the campaign budgets
        campaignBudgetRepository.createOrUpdateBudgets(customerId, campaignBudgetOperations);

        // create an UPDATE campaign operation for each campaign and add to a list
        for (CampaignDetails campaignDetails : campaignDetailsList) {
            Campaign campaign = campaignHelper.buildCampaignFromDetails(campaignDetails, false);
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
     * Fetch the geo target constant for a given location from Google Ads
     *
     * @param locale      Locale is using ISO 639-1 format. If an invalid locale is given, 'en' is used by default.
     * @param countryCode A list of country codes can be referenced here:
     *                    <a href="https://developers.google.com/google-ads/api/reference/data/geotargets">Country Codes</a>
     * @param location    the location to target
     * @return the geo target resource name
     * @throws Exception
     */
    private String getGeoTargetConstant(String locale, String countryCode, String location) throws Exception {
        return criterionRepository.getGeoTargetConstant(locale, countryCode, location);
    }
}
