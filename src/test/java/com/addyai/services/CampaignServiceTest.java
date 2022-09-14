package com.addyai.services;

import com.addyai.error_handling.exceptions.InvalidRequestException;
import com.addyai.error_handling.exceptions.ServiceFailureException;
import com.addyai.models.BudgetDetails;
import com.addyai.models.CampaignDetails;
import com.addyai.repos.campaigns.CampaignRepository;
import com.addyai.services.campaign.CampaignService;
import com.addyai.services.campaign.impl.CampaignServiceImpl;
import com.google.ads.googleads.v11.common.ManualCpc;
import com.google.ads.googleads.v11.enums.AdvertisingChannelTypeEnum;
import com.google.ads.googleads.v11.enums.CampaignStatusEnum;
import com.google.ads.googleads.v11.enums.NegativeGeoTargetTypeEnum;
import com.google.ads.googleads.v11.enums.PositiveGeoTargetTypeEnum;
import com.google.ads.googleads.v11.resources.Campaign;
import com.google.ads.googleads.v11.services.CampaignOperation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = CampaignServiceImpl.class)
public class CampaignServiceTest {
    private static final long CUSTOMER_ID = 1L;

    @Autowired
    private CampaignService campaignService;
    @MockBean
    private CampaignRepository campaignRepository;

    @Test
    void testAddingCampaignsToClientAccount() throws Exception {
        List<CampaignOperation> operations = getMockCampaignOperations(getMockCampaignDetailsList().get(0));
        doNothing().when(campaignRepository).addCampaigns(CUSTOMER_ID, operations);

        campaignService.addCampaignsToAccount(CUSTOMER_ID, getMockCampaignDetailsList());
        verify(campaignRepository, times(1)).addCampaigns(CUSTOMER_ID, operations);
    }

    @Test
    void testAddingCampaignsThrowsInvalidRequestException() throws Exception {
        List<CampaignOperation> operations = getMockCampaignOperations(getMockCampaignDetailsList().get(0));
        doThrow(InvalidRequestException.class).when(campaignRepository).addCampaigns(CUSTOMER_ID, operations);

        assertThrows(InvalidRequestException.class,
                () -> campaignService.addCampaignsToAccount(CUSTOMER_ID, getMockCampaignDetailsList()));
    }

    @Test
    void testAddingCampaignsThrowsServiceFailureException() throws Exception {
        List<CampaignOperation> operations = getMockCampaignOperations(getMockCampaignDetailsList().get(0));
        doThrow(ServiceFailureException.class).when(campaignRepository).addCampaigns(CUSTOMER_ID, operations);

        assertThrows(ServiceFailureException.class,
                () -> campaignService.addCampaignsToAccount(CUSTOMER_ID, getMockCampaignDetailsList()));
    }

    @Test
    void testFindAllCampaignsFromClientAccount() throws Exception {
        // setup mock data
        List<CampaignDetails> mockCampaignDetailsList = getMockCampaignDetailsList();
        List<BudgetDetails> mockBudgetDetailsList = getMockBudgetDetailsList();
        CampaignDetails mockCampaignDetails = mockCampaignDetailsList.get(0);
        BudgetDetails mockBudgetDetails = mockBudgetDetailsList.get(0);

        // set mock budget details object inside mock campaign details object
        mockCampaignDetails.setBudgetDetails(mockBudgetDetails);

        when(campaignRepository.getCampaignDetails(CUSTOMER_ID)).thenReturn(mockCampaignDetailsList);
        when(campaignRepository.getCampaignBudgetDetails(CUSTOMER_ID)).thenReturn(getMockBudgetDetailsList());

        List<CampaignDetails> campaignDetailsList = campaignService.findAllCampaignDetails(CUSTOMER_ID);
        CampaignDetails campaignDetails = campaignDetailsList.get(0);

        verify(campaignRepository, times(1)).getCampaignDetails(CUSTOMER_ID);
        verify(campaignRepository, times(1)).getCampaignBudgetDetails(CUSTOMER_ID);

        assertEquals("Test campaign", campaignDetails.getCampaignName());
        assertEquals("Test Budget", campaignDetails.getBudgetDetails().getResourceName());
    }

    @Test
    void testFindAllCampaignsThrowsInvalidRequestExceptionWhileFetchingCampaignDetails() throws Exception {
        // setup mock data
        List<CampaignDetails> mockCampaignDetailsList = getMockCampaignDetailsList();
        List<BudgetDetails> mockBudgetDetailsList = getMockBudgetDetailsList();
        CampaignDetails mockCampaignDetails = mockCampaignDetailsList.get(0);
        BudgetDetails mockBudgetDetails = mockBudgetDetailsList.get(0);

        // set mock budget details object inside mock campaign details object
        mockCampaignDetails.setBudgetDetails(mockBudgetDetails);

        when(campaignRepository.getCampaignDetails(CUSTOMER_ID)).thenThrow(InvalidRequestException.class);

        assertThrows(InvalidRequestException.class,
                () -> campaignService.findAllCampaignDetails(CUSTOMER_ID));
    }

    @Test
    void testFindAllCampaignsThrowsServiceFailureExceptionWhileFetchingCampaignBudgetDetails() throws Exception {
        // setup mock data
        List<CampaignDetails> mockCampaignDetailsList = getMockCampaignDetailsList();
        List<BudgetDetails> mockBudgetDetailsList = getMockBudgetDetailsList();
        CampaignDetails mockCampaignDetails = mockCampaignDetailsList.get(0);
        BudgetDetails mockBudgetDetails = mockBudgetDetailsList.get(0);

        // set mock budget details object inside mock campaign details object
        mockCampaignDetails.setBudgetDetails(mockBudgetDetails);

        when(campaignRepository.getCampaignDetails(CUSTOMER_ID)).thenReturn(mockCampaignDetailsList);
        when(campaignRepository.getCampaignBudgetDetails(CUSTOMER_ID)).thenThrow(ServiceFailureException.class);

        assertThrows(ServiceFailureException.class,
                () -> campaignService.findAllCampaignDetails(CUSTOMER_ID));
    }

    @Test
    void testCreatingCampaignWithEmptyCampaignNameThrowsInvalidRequestException() throws InvalidRequestException {
        String expectedErrorMessage = "Error Code: Missing Campaign Name Error Type: Invalid Campaign Details Error Message: Campaign name cannot be blank";

        List<CampaignDetails> campaignDetailsList = new ArrayList<>();
        CampaignDetails campaignDetails = new CampaignDetails();

        campaignDetailsList.add(campaignDetails);

        Throwable throwable = assertThrows(InvalidRequestException.class,
                () -> campaignService.addCampaignsToAccount(CUSTOMER_ID, campaignDetailsList));

        assertEquals(expectedErrorMessage, throwable.getMessage());
    }

    private List<CampaignDetails> getMockCampaignDetailsList() {
        List<CampaignDetails> campaignDetailsList = new ArrayList<>();

        CampaignDetails campaignDetails = new CampaignDetails();
        campaignDetails.setBudgetDetails(null);
        campaignDetails.setCampaignId(1);
        campaignDetails.setCampaignName("Test campaign");
        campaignDetails.setBudgetResourceName("Test Budget");
        campaignDetails.setStatus("ENABLED");

        campaignDetailsList.add(campaignDetails);

        return campaignDetailsList;
    }

    private List<CampaignOperation> getMockCampaignOperations(CampaignDetails campaignDetails) {
        List<CampaignOperation> campaignOperations = new ArrayList<>();

        // create a Google Ads campaign object from campaign details
        Campaign campaign = buildCampaignFromDetails(campaignDetails);

        // create a CREATE campaign operation
        CampaignOperation op = CampaignOperation.newBuilder()
                .setCreate(campaign)
                .build();

        // add to campaign operations list
        campaignOperations.add(op);

        return campaignOperations;
    }

    /**
     * Create a campaign object using a CampaignDetails object
     *
     * @param campaignDetails the details of the campaign to be created
     * @return campaign object based on campaign details provided
     */
    private Campaign buildCampaignFromDetails(CampaignDetails campaignDetails) {
        // create a Manual cpc object with or without enhanced CPC
        ManualCpc manualCpc = ManualCpc.newBuilder()
                .setEnhancedCpcEnabled(campaignDetails.isEnhancedCpcEnabled())
                .build();

        // create a GeoTargetTypeSetting using the negative and positive targets
        Campaign.GeoTargetTypeSetting geoTargetTypeSetting = Campaign.GeoTargetTypeSetting.newBuilder()
                .setNegativeGeoTargetType(NegativeGeoTargetTypeEnum.NegativeGeoTargetType
                        .forNumber(campaignDetails.getNegativeGeoTargetType()))
                .setPositiveGeoTargetType(PositiveGeoTargetTypeEnum.PositiveGeoTargetType
                        .forNumber(campaignDetails.getPositiveGeoTargetType()))
                .build();

        // extract network settings into its own object
        Campaign.NetworkSettings networkSettings = Campaign.NetworkSettings.newBuilder()
                .setTargetContentNetwork(campaignDetails.isTargetingContentNetwork())
                .setTargetSearchNetwork(campaignDetails.isTargetingSearchNetwork())
                .setTargetPartnerSearchNetwork(campaignDetails.isTargetingPartnerSearchNetwork())
                .build();

        // create the campaign status object
        CampaignStatusEnum.CampaignStatus status =
                CampaignStatusEnum.CampaignStatus.valueOf(campaignDetails.getStatus());

        //create the advertising channel type object
        AdvertisingChannelTypeEnum.AdvertisingChannelType advertisingChannelType =
                AdvertisingChannelTypeEnum.AdvertisingChannelType.valueOf(campaignDetails.getAdvertisingChannelType());

        // create and return a campaign object with the above settings
        return Campaign.newBuilder()
                .setStatus(status)
                .setId(campaignDetails.getCampaignId())
                .setStartDate(campaignDetails.getStartDate())
                .setEndDate(campaignDetails.getEndDate())
                .setName(campaignDetails.getCampaignName())
                .setGeoTargetTypeSetting(geoTargetTypeSetting)
                .setCampaignBudget(campaignDetails.getBudgetResourceName())
                .setManualCpc(manualCpc)
                .setNetworkSettings(networkSettings)
                .setAdvertisingChannelType(advertisingChannelType)
                .build();
    }

    /**
     * Create a list of complete campaign details models.
     *
     * @param customerId          the customer id of the client account
     * @param baseCampaignDetails a list of campaign details models sans BudgetDetails
     * @return a list of complete CampaignDetails
     */
    private List<CampaignDetails> buildCompleteCampaignDetailsList(long customerId, List<CampaignDetails> baseCampaignDetails) {
        List<CampaignDetails> completeCampaignDetailsList = new ArrayList<>();
        List<BudgetDetails> budgetDetailsList = getMockBudgetDetailsList();

        // associate each campaign with a budget by resource name
        for (CampaignDetails campaignDetails : baseCampaignDetails) {
            for (BudgetDetails budgetDetails : budgetDetailsList) {
                // set budgetDetails in campaignDetails model if matched
                if (campaignDetails.getBudgetResourceName().equals(budgetDetails.getResourceName())) {
                    campaignDetails.setBudgetDetails(budgetDetails);

                    // add updated campaign details model to completed list
                    completeCampaignDetailsList.add(campaignDetails);
                    break;
                }
            }
        }

        return completeCampaignDetailsList;
    }

    private List<BudgetDetails> getMockBudgetDetailsList() {
        List<BudgetDetails> budgetDetailsList = new ArrayList<>();

        BudgetDetails budgetDetails = new BudgetDetails();
        budgetDetails.setResourceName("Test Budget");

        budgetDetailsList.add(budgetDetails);
        return budgetDetailsList;
    }
}
