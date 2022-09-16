package com.addyai.services;

import com.addyai.error_handling.exceptions.InvalidRequestException;
import com.addyai.models.BudgetDetails;
import com.addyai.models.CampaignDetails;
import com.addyai.repos.campaigns.CampaignRepository;
import com.addyai.repos.campaigns.budget.CampaignBudgetRepository;
import com.addyai.services.campaign.CampaignService;
import com.addyai.services.campaign.impl.CampaignServiceImpl;
import com.addyai.utils.CampaignUtils;
import com.google.ads.googleads.v11.resources.Campaign;
import com.google.ads.googleads.v11.services.CampaignBudgetOperation;
import com.google.ads.googleads.v11.services.CampaignOperation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = CampaignServiceImpl.class)
public class CampaignServiceTest {
    private static final long CUSTOMER_ID = 1L;

    @Autowired
    private CampaignService campaignService;
    @MockBean
    private CampaignRepository campaignRepository;
    @MockBean
    private CampaignBudgetRepository campaignBudgetRepository;

    /*
        Test successfully adding campaigns that have budget details that match an existing budget in the client's account.
        This requires that the BudgetDetails object within the CampaignDetails object have the same delivery_method,
        budgetValue and isShared values as an existing budgetDetails object.
     */
    @Test
    void testSuccessfullyAddingSingleCampaignThatMatchesWithExistingBudget() throws Exception {
        CampaignUtils campaignUtils = new CampaignUtils();

        when(campaignBudgetRepository.fetchAllCampaignBudgetDetails(CUSTOMER_ID))
                .thenReturn(getListOfMultipleBudgetDetails());

        Campaign campaign = campaignUtils
                .buildCampaignFromDetails(getCompletedCampaignDetails(), true);

        List<CampaignOperation> campaignOperations = new ArrayList<>();
        CampaignOperation operation = CampaignOperation.newBuilder()
                .setCreate(campaign)
                .build();

        campaignOperations.add(operation);

        campaignService.addCampaignsToAccount(CUSTOMER_ID,
                Collections.singletonList(getCampaignDetailsWithExistingBudget()));

        verify(campaignRepository, times(1)).addCampaigns(CUSTOMER_ID, campaignOperations);
    }

    /*
        Test successfully adding a campaign to the client account that DOES NOT have an existing budget set.
        This requires that the budgetResourceName is empty but the campaign details object is complete with
        values not found in the getListOfMultipleBudgetDetails list object.
     */
    @Test
    void testSuccessfullyAddingSingleCampaignWithoutExistingBudget() throws Exception {
        CampaignUtils campaignUtils = new CampaignUtils();
        when(campaignBudgetRepository.fetchAllCampaignBudgetDetails(CUSTOMER_ID))
                .thenReturn(getListOfMultipleBudgetDetails());

        // create campaignBudgetOperations list for the new budget to be created
        List<CampaignBudgetOperation> campaignBudgetOperations =
                campaignUtils.buildCampaignBudgetOperationList(Collections.singletonList(
                        getCampaignDetailsWithoutExistingBudget().getBudgetDetails()), true);

        // when the budget is created return the same budget details object
        when(campaignBudgetRepository.createOrUpdateBudgets(CUSTOMER_ID, campaignBudgetOperations))
                .thenReturn(Collections.singletonList(getNonExistentBudgetDetails()));

        // set the budget resource name in the campaign details
        CampaignDetails updatedCampaignDetails = getCampaignDetailsWithoutExistingBudget();
        updatedCampaignDetails.setBudgetResourceName(getNonExistentBudgetDetails().getResourceName());

        // create a campaign using the details
        Campaign campaign = campaignUtils.buildCampaignFromDetails(updatedCampaignDetails, true);

        // create the CREATE operations list
        List<CampaignOperation> campaignOperations = new ArrayList<>();
        CampaignOperation operation = CampaignOperation.newBuilder()
                .setCreate(campaign)
                .build();

        campaignOperations.add(operation);

        // add campaigns to the account
        campaignService.addCampaignsToAccount(CUSTOMER_ID,
                Collections.singletonList(getCampaignDetailsWithoutExistingBudget()));

        verify(campaignRepository, times(1)).addCampaigns(CUSTOMER_ID, campaignOperations);
    }

    @Test
    void testEmptyCampaignNameThrowsInvalidRequestException() {
        CampaignDetails campaignDetails = getCampaignDetailsWithExistingBudget();
        campaignDetails.setCampaignName("");

        assertThatThrownBy(() -> campaignService.addCampaignsToAccount(CUSTOMER_ID, Collections.singletonList(campaignDetails)))
                .isInstanceOf(InvalidRequestException.class);
    }

    @Test
    void testZeroBudgetValueThrowsInvalidRequestException() {
        CampaignDetails campaignDetails = getCampaignDetailsWithExistingBudget();
        campaignDetails.getBudgetDetails().setDailyBudgetAmount(0);

        assertThatThrownBy(() -> campaignService.addCampaignsToAccount(CUSTOMER_ID, Collections.singletonList(campaignDetails)))
                .isInstanceOf(InvalidRequestException.class);
    }

    /*
        Get mock object methods
     */

    /*
        Get a mock campaign details object that contains a BudgetDetails object that exists already
        Returns a single CampaignDetails object
     */
    private CampaignDetails getCampaignDetailsWithExistingBudget() {
        CampaignDetails campaignDetails = new CampaignDetails();
        campaignDetails.setCampaignId(0L);
        campaignDetails.setCampaignName("Test Campaign 1");
        campaignDetails.setBudgetResourceName("");
        campaignDetails.setStatus("ENABLED");
        campaignDetails.setStartDate("2022-09-31");
        campaignDetails.setEndDate("2022-10-01");
        campaignDetails.setEnhancedCpcEnabled(false);
        campaignDetails.setTargetingPartnerSearchNetwork(false);
        campaignDetails.setTargetingSearchNetwork(true);
        campaignDetails.setTargetingContentNetwork(false);
        campaignDetails.setAdvertisingChannelType("SEARCH");
        campaignDetails.setPositiveGeoTargetType(7);
        campaignDetails.setNegativeGeoTargetType(5);
        campaignDetails.setCampaignResourceName("customers/9059845250/campaigns/18343627878");
        campaignDetails.setBudgetDetails(getExistingBudgetDetails());
        return campaignDetails;
    }

    /*
        Get a mock campaign details object that does not have an existing budget resource set
        Returns a list with a single CampaignDetails object
     */
    private CampaignDetails getCampaignDetailsWithoutExistingBudget() {
        CampaignDetails campaignDetails = new CampaignDetails();
        campaignDetails.setCampaignId(0L);
        campaignDetails.setCampaignName("Test Campaign 2");
        campaignDetails.setBudgetResourceName(""); // empty budget resource name
        campaignDetails.setStatus("ENABLED");
        campaignDetails.setStartDate("2022-11-31");
        campaignDetails.setEndDate("2022-12-01");
        campaignDetails.setEnhancedCpcEnabled(false);
        campaignDetails.setTargetingPartnerSearchNetwork(false);
        campaignDetails.setTargetingSearchNetwork(true);
        campaignDetails.setTargetingContentNetwork(false);
        campaignDetails.setAdvertisingChannelType("DISPLAY");
        campaignDetails.setPositiveGeoTargetType(7);
        campaignDetails.setNegativeGeoTargetType(5);
        campaignDetails.setCampaignResourceName("customers/9059845250/campaigns/18343627878");
        campaignDetails.setBudgetDetails(getNonExistentBudgetDetails());

        return campaignDetails;
    }

    /*
        Get a list of campaign details objects. One has an existing budget and one does not
        Return a list with a 2 CampaignDetails objects
     */
    private List<CampaignDetails> getMultipleCampaignDetailsList() {
        List<CampaignDetails> campaignDetailsList = new ArrayList<>();

        CampaignDetails campaignDetails = new CampaignDetails();
        campaignDetails.setCampaignId(0L);
        campaignDetails.setCampaignName("Test Campaign 1");
        campaignDetails.setBudgetResourceName("");
        campaignDetails.setStatus("ENABLED");
        campaignDetails.setStartDate("2022-09-31");
        campaignDetails.setEndDate("2022-10-01");
        campaignDetails.setEnhancedCpcEnabled(false);
        campaignDetails.setTargetingPartnerSearchNetwork(false);
        campaignDetails.setTargetingSearchNetwork(true);
        campaignDetails.setTargetingContentNetwork(false);
        campaignDetails.setAdvertisingChannelType("SEARCH");
        campaignDetails.setPositiveGeoTargetType(7);
        campaignDetails.setNegativeGeoTargetType(5);
        campaignDetails.setCampaignResourceName("");
        campaignDetails.setBudgetDetails(getExistingBudgetDetails());

        CampaignDetails campaignDetails2 = new CampaignDetails();
        campaignDetails2.setCampaignId(0L);
        campaignDetails2.setCampaignName("Test Campaign 2");
        campaignDetails2.setBudgetResourceName(""); // empty budget resource name
        campaignDetails2.setStatus("ENABLED");
        campaignDetails2.setStartDate("2022-11-31");
        campaignDetails2.setEndDate("2022-12-01");
        campaignDetails2.setEnhancedCpcEnabled(false);
        campaignDetails2.setTargetingPartnerSearchNetwork(false);
        campaignDetails2.setTargetingSearchNetwork(true);
        campaignDetails2.setTargetingContentNetwork(false);
        campaignDetails2.setAdvertisingChannelType("DISPLAY");
        campaignDetails2.setPositiveGeoTargetType(7);
        campaignDetails2.setNegativeGeoTargetType(5);
        campaignDetails2.setCampaignResourceName("");
        campaignDetails2.setBudgetDetails(getNonExistentBudgetDetails());

        campaignDetailsList.add(campaignDetails);
        campaignDetailsList.add(campaignDetails2);

        return campaignDetailsList;
    }

    private CampaignDetails getCompletedCampaignDetails() {
        CampaignDetails campaignDetails = new CampaignDetails();
        campaignDetails.setCampaignId(0L);
        campaignDetails.setCampaignName("Test Campaign 1");
        campaignDetails.setBudgetResourceName("customers/9059845250/campaignBudgets/18343627878");
        campaignDetails.setStatus("ENABLED");
        campaignDetails.setStartDate("2022-09-31");
        campaignDetails.setEndDate("2022-10-01");
        campaignDetails.setEnhancedCpcEnabled(false);
        campaignDetails.setTargetingPartnerSearchNetwork(false);
        campaignDetails.setTargetingSearchNetwork(true);
        campaignDetails.setTargetingContentNetwork(false);
        campaignDetails.setAdvertisingChannelType("SEARCH");
        campaignDetails.setPositiveGeoTargetType(7);
        campaignDetails.setNegativeGeoTargetType(5);
        campaignDetails.setCampaignResourceName("customers/9059845250/campaigns/18343627811");
        campaignDetails.setBudgetDetails(getExistingBudgetDetails());
        return campaignDetails;
    }

    /*
        Get a budget details object that exists in the list of multiple budget details
        Returns a single BudgetDetails object
     */
    private BudgetDetails getExistingBudgetDetails() {
        BudgetDetails budgetDetails = new BudgetDetails();
        budgetDetails.setResourceName("customers/9059845250/campaignBudgets/18343627878");
        budgetDetails.setBudgetId(0L);
        budgetDetails.setShared(true);
        budgetDetails.setDailyBudgetAmount(100);
        budgetDetails.setDeliveryMethod(2);
        budgetDetails.setStatus(2);

        return budgetDetails;
    }

    /*
        Get a budget details object that exists in the list of multiple budget details
        Returns a single BudgetDetails object
     */
    private BudgetDetails getNonExistentBudgetDetails() {
        BudgetDetails budgetDetails = new BudgetDetails();
        budgetDetails.setResourceName("customers/9059845250/campaignBudgets/18343627811");
        budgetDetails.setBudgetId(0L);
        budgetDetails.setShared(false);
        budgetDetails.setDailyBudgetAmount(3100);
        budgetDetails.setDeliveryMethod(2);
        budgetDetails.setStatus(2);

        return budgetDetails;
    }

    /*
        Get a mock budget details object
        Returns a list with multiple BudgetDetails object
     */
    private List<BudgetDetails> getListOfMultipleBudgetDetails() {
        List<BudgetDetails> budgetDetailsList = new ArrayList<>();

        BudgetDetails budgetDetails = new BudgetDetails();
        budgetDetails.setResourceName("customers/9059845250/campaignBudgets/18343627878");
        budgetDetails.setBudgetId(0L);
        budgetDetails.setShared(true);
        budgetDetails.setDailyBudgetAmount(100);
        budgetDetails.setDeliveryMethod(2);
        budgetDetails.setStatus(2);

        BudgetDetails budgetDetails2 = new BudgetDetails();
        budgetDetails2.setResourceName("customers/9059845250/campaignBudgets/18343654871");
        budgetDetails2.setBudgetId(1L);
        budgetDetails2.setShared(false);
        budgetDetails2.setDailyBudgetAmount(250);
        budgetDetails.setDeliveryMethod(1);
        budgetDetails2.setStatus(2);

        BudgetDetails budgetDetails3 = new BudgetDetails();
        budgetDetails3.setResourceName("customers/9059845250/campaigns/18793627832");
        budgetDetails3.setBudgetId(2L);
        budgetDetails3.setShared(true);
        budgetDetails3.setDailyBudgetAmount(500);
        budgetDetails.setDeliveryMethod(2);
        budgetDetails3.setStatus(1);

        budgetDetailsList.add(budgetDetails);
        budgetDetailsList.add(budgetDetails2);
        budgetDetailsList.add(budgetDetails3);

        return budgetDetailsList;
    }
}
