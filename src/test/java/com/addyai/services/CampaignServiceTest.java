package com.addyai.services;

import com.addyai.services.campaign.impl.CampaignServiceImpl;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = CampaignServiceImpl.class)
public class CampaignServiceTest {
/*    private static final long MOCK_CUSTOMER_ID = 929910010L;
    private static final long MOCK_CAMPAIGN_ID = 1L;
    private static final String MOCK_CAMPAIGN_NAME = "Mock Campaign Details";

    @Autowired
    private CampaignService campaignService;

    @MockBean
    private CampaignRepository campaignRepository;

    *//*
        Test ability to fetch campaign details from a given account
     *//*
    @Test
    void testGetCampaignDetailsForTestAccount() throws GetResourceException {
        when(campaignRepository.getCampaignDetails(MOCK_CUSTOMER_ID)).thenReturn(getMockCampaignDetailsList());
        List<CampaignDetails> campaignDetails = campaignService.getCampaignDetailsForAccount(MOCK_CUSTOMER_ID);

        assertEquals(campaignDetails.get(0).getCampaignId(), 1L);
    }

    *//*
        Test that when a customer account has 0 campaigns we receive an empty list of campaign details
     *//*
    @Test
    void testGetCampaignDetailsForTestAccountWithZeroCampaigns() throws GetResourceException {
        when(campaignRepository.getCampaignDetails(MOCK_CUSTOMER_ID)).thenReturn(new ArrayList<>());
        List<CampaignDetails> campaignDetails = campaignService.getCampaignDetailsForAccount(MOCK_CUSTOMER_ID);

        assertEquals(0, campaignDetails.size());
    }

    *//*
        Test when an exception occurs during fetching of campaign details, the exception bubbles up to the service
     *//*
    @Test
    void testGetCampaignDetailsThrowsGetResourceException() throws GetResourceException {
        when(campaignRepository.getCampaignDetails(MOCK_CUSTOMER_ID))
                .thenThrow(new GetResourceException(GET_RES_EXCEPTION_MSG + MOCK_CUSTOMER_ID));

        Throwable throwable = assertThrows(GetResourceException.class,
                () -> campaignService.getCampaignDetailsForAccount(MOCK_CUSTOMER_ID));

        assertEquals(GET_RES_EXCEPTION_MSG + MOCK_CUSTOMER_ID,
                throwable.getMessage());
    }

    *//*
        Test updating campaigns in a customer account. If no exception is thrown, we assume success
     *//*
    @Test
    void testUpdateCampaign() throws UpdateResourceException {
        List<CampaignDetails> campaignDetailsList = getMockCampaignDetailsList();
        doNothing().when(campaignRepository)
                .updateCampaigns(MOCK_CUSTOMER_ID, getMockCampaignUpdateOperationList(campaignDetailsList));

        campaignService.updateCampaign(MOCK_CUSTOMER_ID, campaignDetailsList);
    }

    *//*
        Test deleting campaigns in a customer account. If no exception is thrown, we assume success
     *//*
    @Test
    void testDeleteCampaigns() throws DeleteResourceException {
        List<Long> campaignIds = new ArrayList<>();
        campaignIds.add(1L);
        campaignIds.add(2L);

        doNothing().when(campaignRepository).deleteCampaigns(MOCK_CUSTOMER_ID, campaignIds);

        campaignService.deleteCampaigns(MOCK_CUSTOMER_ID, campaignIds);
    }

    *//*
        Test when an exception occurs during updating of campaign details, the exception bubbles up to the service
     *//*
    @Test
    void testUpdateCampaignThrowsUpdateResourceException() throws UpdateResourceException {
        List<CampaignDetails> campaignDetailsList = getMockCampaignDetailsList();
        doThrow(UpdateResourceException.class).when(campaignRepository)
                .updateCampaigns(MOCK_CUSTOMER_ID, getMockCampaignUpdateOperationList(campaignDetailsList));

        assertThrows(UpdateResourceException.class,
                () -> campaignService.updateCampaign(MOCK_CUSTOMER_ID, campaignDetailsList));
    }

    *//*
        Test when an exception occurs during deleting of campaigns, the exception bubbles up to the service
     *//*
    @Test
    void testDeleteCampaignThrowsDeleteResourceException() throws DeleteResourceException {
        List<Long> campaignIds = new ArrayList<>();
        campaignIds.add(1L);
        campaignIds.add(2L);

        doThrow(DeleteResourceException.class).when(campaignRepository).deleteCampaigns(MOCK_CUSTOMER_ID, campaignIds);

        assertThrows(DeleteResourceException.class,
                () -> campaignService.deleteCampaigns(MOCK_CUSTOMER_ID, campaignIds));
    }

    *//*
        Test adding campaigns to a client account
     *//*
    @Test
    void testAddingCampaignsToClientAccount() throws CreateResourceException {
        List<CampaignOperation> campaignOperations = getMockCampaignCreateOperationList(getMockCampaignDetailsList());

        doNothing().when(campaignRepository).addCampaigns(MOCK_CUSTOMER_ID, campaignOperations);
        campaignService.addCampaignsToAccount(MOCK_CUSTOMER_ID, getMockCampaignDetailsList());
    }

    *//*
        Test when an exception occurs during adding campaigns, the exception bubbles up to the service
     *//*
    @Test
    void testAddingCampaignsWhenExceptionThrown() throws CreateResourceException {
        List<CampaignOperation> campaignOperations = getMockCampaignCreateOperationList(getMockCampaignDetailsList());

        doThrow(CreateResourceException.class).when(campaignRepository).addCampaigns(MOCK_CUSTOMER_ID, campaignOperations);

        assertThrows(CreateResourceException.class,
                () -> campaignService.addCampaignsToAccount(MOCK_CUSTOMER_ID, getMockCampaignDetailsList()));
    }

    private List<CampaignDetails> getMockCampaignDetailsList() {
        List<CampaignDetails> campaignDetails = new ArrayList<>();

        CampaignDetails mockCampaignDetails = new CampaignDetails();
        mockCampaignDetails.setCampaignId(MOCK_CAMPAIGN_ID);
        mockCampaignDetails.setCampaignName(MOCK_CAMPAIGN_NAME);
        mockCampaignDetails.setBudget("10000");
        mockCampaignDetails.setEndDate("10/10/25");
        mockCampaignDetails.setStartDate("10/10/22");
        mockCampaignDetails.setEnhancedCpcEnabled(true);
        mockCampaignDetails.setTargetingGoogleSearch(true);
        mockCampaignDetails.setTargetingContentNetwork(true);
        mockCampaignDetails.setTargetingPartnerSearchNetwork(true);
        mockCampaignDetails.setTargetingSearchNetwork(true);
        mockCampaignDetails.setAdvertisingChannelType(AdvertisingChannelTypeEnum.AdvertisingChannelType.SEARCH);
        mockCampaignDetails.setBiddingStrategy(BiddingStrategy.newBuilder().build().toString());
        mockCampaignDetails.setPositiveGeoTargetType(PositiveGeoTargetTypeEnum.PositiveGeoTargetType.PRESENCE);
        mockCampaignDetails.setNegativeGeoTargetType(NegativeGeoTargetTypeEnum.NegativeGeoTargetType.UNKNOWN);
        mockCampaignDetails.setStatus(CampaignStatusEnum.CampaignStatus.ENABLED);

        campaignDetails.add(mockCampaignDetails);
        return campaignDetails;
    }

    private List<CampaignOperation> getMockCampaignUpdateOperationList(List<CampaignDetails> campaignDetailsList) {
        List<CampaignOperation> campaignOperations = new ArrayList<>();

        for (CampaignDetails campaignDetails : campaignDetailsList) {
            Campaign campaign = extractCampaignFromDetails(campaignDetails);

            CampaignOperation operation = CampaignOperation.newBuilder()
                    .setUpdate(campaign)
                    .setUpdateMask(FieldMasks.allSetFieldsOf(campaign))
                    .build();

            campaignOperations.add(operation);
        }
        return campaignOperations;
    }

    private List<CampaignOperation> getMockCampaignCreateOperationList(List<CampaignDetails> campaignDetailsList) {
        List<CampaignOperation> campaignOperations = new ArrayList<>();

        for (CampaignDetails campaignDetails : campaignDetailsList) {
            Campaign campaign = extractCampaignFromDetails(campaignDetails);

            CampaignOperation operation = CampaignOperation.newBuilder()
                    .setCreate(campaign)
                    .build();

            campaignOperations.add(operation);
        }
        return campaignOperations;
    }

    private Campaign extractCampaignFromDetails(CampaignDetails campaignDetails) {
        ManualCpc manualCpc = ManualCpc.newBuilder()
                .setEnhancedCpcEnabled(campaignDetails.isEnhancedCpcEnabled())
                .build();

        Campaign.GeoTargetTypeSetting geoTargetTypeSetting = Campaign.GeoTargetTypeSetting.newBuilder()
                .setNegativeGeoTargetType(campaignDetails.getNegativeGeoTargetType())
                .setPositiveGeoTargetType(campaignDetails.getPositiveGeoTargetType())
                .build();

        // extract network settings into its own object
        Campaign.NetworkSettings networkSettings = Campaign.NetworkSettings.newBuilder()
                .setTargetContentNetwork(campaignDetails.isTargetingContentNetwork())
                .setTargetSearchNetwork(campaignDetails.isTargetingSearchNetwork())
                .setTargetGoogleSearch(campaignDetails.isTargetingGoogleSearch())
                .setTargetPartnerSearchNetwork(campaignDetails.isTargetingPartnerSearchNetwork())
                .build();

        return Campaign.newBuilder()
                .setManualCpc(manualCpc)
                .setStatus(campaignDetails.getStatus())
                .setId(campaignDetails.getCampaignId())
                .setStartDate(campaignDetails.getStartDate())
                .setEndDate(campaignDetails.getEndDate())
                .setName(campaignDetails.getCampaignName())
                .setGeoTargetTypeSetting(geoTargetTypeSetting)
                .setCampaignBudget(campaignDetails.getBudget())
                .setBiddingStrategy(campaignDetails.getBiddingStrategy())
                .setNetworkSettings(networkSettings)
                .setAdvertisingChannelType(campaignDetails.getAdvertisingChannelType())
                .build();
    }*/
}
