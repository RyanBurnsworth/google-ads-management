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
//
//package com.addyai.services;
//
//import com.addyai.error_handling.exceptions.InvalidRequestException;
//import com.addyai.models.BudgetDetails;
//import com.addyai.models.CampaignDetails;
//import com.addyai.repos.campaigns.CampaignRepository;
//import com.addyai.repos.campaigns.budget.CampaignBudgetRepository;
//import com.addyai.repos.campaigns.criterion.CriterionRepository;
//import com.addyai.services.campaign.CampaignService;
//import com.addyai.services.campaign.impl.CampaignServiceImpl;
//import com.addyai.utils.helpers.CampaignHelper;
//import com.google.ads.googleads.lib.utils.FieldMasks;
//import com.google.ads.googleads.v11.resources.Campaign;
//import com.google.ads.googleads.v11.services.CampaignBudgetOperation;
//import com.google.ads.googleads.v11.services.CampaignOperation;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest(classes = CampaignServiceImpl.class)
//public class CampaignServiceTest {
//    private static final long CUSTOMER_ID = 1L;
//
//    @Autowired
//    private CampaignService campaignService;
//    @MockBean
//    private CampaignRepository campaignRepository;
//    @MockBean
//    private CampaignBudgetRepository campaignBudgetRepository;
//
//    @MockBean
//    private CriterionRepository criterionRepository;
//
//    /*
//        Test successfully adding a campaign to the client account that DOES NOT have an existing budget set.
//        This requires that the budgetResourceName is empty but the campaign details object is complete with
//        values not found in the getListOfMultipleBudgetDetails list object.
//     */
////    @Test
////    void testSuccessfullyAddingSingleCampaign() throws Exception {
////        CampaignHelper campaignHelper = new CampaignHelper();
////        CampaignDetails campaignDetails = getCampaignDetails();
////
////        // create campaignBudgetOperations list for the new budget to be created
////        List<CampaignBudgetOperation> campaignBudgetOperations =
////                campaignHelper.buildCampaignBudgetOperationList(Collections.singletonList(
////                        campaignDetails.getBudgetDetails()), true);
////
////        // when the budget is created return the budget details object containing a resource name
////        when(campaignBudgetRepository.createOrUpdateBudgets(CUSTOMER_ID, campaignBudgetOperations))
////                .thenReturn(Collections.singletonList("customers/9059845250/campaignBudgets/18343627878"));
////
////        // set the budget resource name in the campaign details
////        campaignDetails.setBudgetResourceName(getBudgetDetails().getResourceName());
////
////        // create a campaign using the details
////        Campaign campaign = campaignHelper.buildCampaignFromDetails(campaignDetails, true);
////
////        // create the CREATE operations list
////        List<CampaignOperation> campaignOperations = new ArrayList<>();
////        CampaignOperation operation = CampaignOperation.newBuilder()
////                .setCreate(campaign)
////                .build();
////
////        campaignOperations.add(operation);
////
////        // add campaigns to the account
////        campaignService.addCampaignsToAccount(CUSTOMER_ID,
////                Collections.singletonList(getCampaignDetails()));
////
////        verify(campaignRepository, times(1)).addCampaigns(CUSTOMER_ID, campaignOperations);
////    }
////
////    /*
////        Test that an InvalidRequestException is thrown if the campaign name is empty
////     */
////    @Test
////    void testEmptyCampaignNameThrowsInvalidRequestException() {
////        CampaignDetails campaignDetails = getCampaignDetails();
////        campaignDetails.setCampaignName("");
////
////        assertThatThrownBy(() -> campaignService.addCampaignsToAccount(CUSTOMER_ID, Collections.singletonList(campaignDetails)))
////                .isInstanceOf(InvalidRequestException.class);
////    }
////
////    /*
////        Test that an InvalidRequestException is thrown if the campaign budget value is 0
////     */
////    @Test
////    void testZeroBudgetValueThrowsInvalidRequestException() {
////        CampaignDetails campaignDetails = getCampaignDetails();
////        campaignDetails.getBudgetDetails().setDailyBudgetAmount(0);
////
////        assertThatThrownBy(() -> campaignService.addCampaignsToAccount(CUSTOMER_ID, Collections.singletonList(campaignDetails)))
////                .isInstanceOf(InvalidRequestException.class);
////    }
////
////    /*
////        Test updating existing campaigns
////     */
////    @Test
////    void testUpdateExistingCampaign() throws Exception {
////        CampaignHelper campaignHelper = new CampaignHelper();
////        List<CampaignOperation> campaignOperations = new ArrayList<>();
////
////        List<CampaignBudgetOperation> budgetOperations =
////                campaignHelper.buildCampaignBudgetOperationList(getListOfBudgetDetails(), false);
////
////        campaignService.updateCampaigns(CUSTOMER_ID, getListOfCampaignDetails());
////
////        for (CampaignDetails campaignDetails : getListOfCampaignDetails()) {
////            Campaign campaign = campaignHelper.buildCampaignFromDetails(campaignDetails, false);
////            CampaignOperation campaignOperation = CampaignOperation.newBuilder()
////                    .setUpdate(campaign)
////                    .setUpdateMask(FieldMasks.allSetFieldsOf(campaign))
////                    .build();
////            campaignOperations.add(campaignOperation);
////        }
////
////        verify(campaignBudgetRepository, times(1)).createOrUpdateBudgets(CUSTOMER_ID, budgetOperations);
////        verify(campaignRepository, times(1)).updateCampaigns(CUSTOMER_ID, campaignOperations);
////    }
////
////    /*
////         Test delete campaigns functionality
////     */
////    @Test
////    void testDeleteCampaign() throws Exception {
////        List<Long> campaignIds = new ArrayList<>();
////        campaignIds.add(1L);
////        campaignIds.add(2L);
////
////        campaignService.deleteCampaigns(CUSTOMER_ID, campaignIds);
////
////        verify(campaignRepository, times(1)).deleteCampaigns(CUSTOMER_ID, campaignIds);
////    }
////
////    /*
////        Test finding an individual campaign details object by name
////     */
////    @Test
////    void testFindCampaignDetailsByCampaignName() throws Exception {
////        CampaignDetails campaignDetails = getCampaignDetails();
////        when(campaignRepository.fetchCampaignDetailsByName(CUSTOMER_ID, campaignDetails.getCampaignName()))
////                .thenReturn(campaignDetails);
////
////        when(campaignBudgetRepository.fetchAllCampaignBudgetDetails(CUSTOMER_ID)).thenReturn(getListOfBudgetDetails());
////
////        CampaignDetails actualCampaignDetails = campaignService
////                .findCampaignDetailsByName(CUSTOMER_ID, campaignDetails.getCampaignName());
////
////        assertEquals(getCampaignDetails().getCampaignId(), actualCampaignDetails.getCampaignId());
////        assertEquals(getCampaignDetails().getCampaignResourceName(), actualCampaignDetails.getCampaignResourceName());
////        assertEquals(getCampaignDetails().getBudgetResourceName(), actualCampaignDetails.getBudgetResourceName());
////        assertEquals(getCampaignDetails().getEndDate(), actualCampaignDetails.getEndDate());
////        assertEquals(getCampaignDetails().getStartDate(), actualCampaignDetails.getStartDate());
////        assertEquals(getCampaignDetails().getBudgetDetails().getName(), actualCampaignDetails.getBudgetDetails().getName());
////        assertEquals(getCampaignDetails().getBudgetDetails().getDailyBudgetAmount(),
////                actualCampaignDetails.getBudgetDetails().getDailyBudgetAmount());
////    }
////
////    /*
////        Test campaignService.findCampaignDetailsByName throws InvalidRequestException if the campaign
////        name is missing.
////     */
////    @Test
////    void testThrowsExceptionMissingCampaignNameWhenFindingCampaignDetailsByName() {
////        assertThatThrownBy(() -> campaignService.findCampaignDetailsByName(CUSTOMER_ID, ""))
////                .isInstanceOf(InvalidRequestException.class);
////    }
////
////    /*
////        Get a mock campaign details object that contains a BudgetDetails object that exists already
////        Returns a single CampaignDetails object
////     */
////    private CampaignDetails getCampaignDetails() {
////        CampaignDetails campaignDetails = new CampaignDetails();
////        campaignDetails.setCampaignId(0L);
////        campaignDetails.setCampaignName("Test Campaign 1");
////        campaignDetails.setBudgetResourceName("");
////        campaignDetails.setStatus("ENABLED");
////        campaignDetails.setStartDate("2022-09-31");
////        campaignDetails.setEndDate("2022-10-01");
////        campaignDetails.setEnhancedCpcEnabled(false);
////        campaignDetails.setTargetingGoogleSearchNetwork(false);
////        campaignDetails.setTargetingSearchNetwork(true);
////        campaignDetails.setTargetingContentNetwork(false);
////        campaignDetails.setAdvertisingChannelType("SEARCH");
////        campaignDetails.setPositiveGeoTargetType(7);
////        campaignDetails.setNegativeGeoTargetType(5);
////        campaignDetails.setCampaignResourceName("");
////        campaignDetails.setBudgetDetails(getBudgetDetails());
////        campaignDetails.getBudgetDetails().setResourceName(""); // update to remove resource name
////        return campaignDetails;
////    }
////
////    /*
////        Get a list of campaign details objects. One has an existing budget and one does not
////        Return a list with a 2 CampaignDetails objects
////     */
////    private List<CampaignDetails> getListOfCampaignDetails() {
////        List<CampaignDetails> campaignDetailsList = new ArrayList<>();
////
////        CampaignDetails campaignDetails = new CampaignDetails();
////        campaignDetails.setCampaignId(0L);
////        campaignDetails.setCampaignName("Test Campaign 1");
////        campaignDetails.setBudgetResourceName("");
////        campaignDetails.setStatus("ENABLED");
////        campaignDetails.setStartDate("2022-09-31");
////        campaignDetails.setEndDate("2022-10-01");
////        campaignDetails.setEnhancedCpcEnabled(false);
////        campaignDetails.setTargetingGoogleSearchNetwork(false);
////        campaignDetails.setTargetingSearchNetwork(true);
////        campaignDetails.setTargetingContentNetwork(false);
////        campaignDetails.setAdvertisingChannelType("SEARCH");
////        campaignDetails.setPositiveGeoTargetType(7);
////        campaignDetails.setNegativeGeoTargetType(5);
////        campaignDetails.setCampaignResourceName("");
////        campaignDetails.setBudgetDetails(getListOfBudgetDetails().get(0));
////
////        CampaignDetails campaignDetails2 = new CampaignDetails();
////        campaignDetails2.setCampaignId(0L);
////        campaignDetails2.setCampaignName("Test Campaign 2");
////        campaignDetails2.setBudgetResourceName(""); // empty budget resource name
////        campaignDetails2.setStatus("ENABLED");
////        campaignDetails2.setStartDate("2022-11-31");
////        campaignDetails2.setEndDate("2022-12-01");
////        campaignDetails2.setEnhancedCpcEnabled(false);
////        campaignDetails2.setTargetingGoogleSearchNetwork(false);
////        campaignDetails2.setTargetingSearchNetwork(true);
////        campaignDetails2.setTargetingContentNetwork(false);
////        campaignDetails2.setAdvertisingChannelType("DISPLAY");
////        campaignDetails2.setPositiveGeoTargetType(7);
////        campaignDetails2.setNegativeGeoTargetType(5);
////        campaignDetails2.setCampaignResourceName("");
////        campaignDetails2.setBudgetDetails(getListOfBudgetDetails().get(1));
////
////        campaignDetailsList.add(campaignDetails);
////        campaignDetailsList.add(campaignDetails2);
////
////        return campaignDetailsList;
////    }
////
////    /*
////        Get a budget details object that exists in the list of multiple budget details
////        Returns a single BudgetDetails object
////     */
////    private BudgetDetails getBudgetDetails() {
////        BudgetDetails budgetDetails = new BudgetDetails();
////        budgetDetails.setName("Test Campaign 1");
////        budgetDetails.setResourceName("customers/9059845250/campaignBudgets/18343627878");
////        budgetDetails.setBudgetId(0L);
////        budgetDetails.setShared(true);
////        budgetDetails.setDailyBudgetAmount(100);
////        budgetDetails.setDeliveryMethod(2);
////        budgetDetails.setStatus(2);
////
////        return budgetDetails;
////    }
////
////    /*
////        Get a mock budget details object
////        Returns a list with multiple BudgetDetails object
////     */
////    private List<BudgetDetails> getListOfBudgetDetails() {
////        List<BudgetDetails> budgetDetailsList = new ArrayList<>();
////
////        BudgetDetails budgetDetails = new BudgetDetails();
////        budgetDetails.setName("Test Campaign 1");
////        budgetDetails.setResourceName("customers/9059845250/campaignBudgets/18343627878");
////        budgetDetails.setBudgetId(0L);
////        budgetDetails.setShared(true);
////        budgetDetails.setDailyBudgetAmount(100);
////        budgetDetails.setDeliveryMethod(2);
////        budgetDetails.setStatus(2);
////
////        BudgetDetails budgetDetails2 = new BudgetDetails();
////        budgetDetails2.setName("Test Campaign 2");
////        budgetDetails2.setResourceName("customers/9059845250/campaignBudgets/18343654871");
////        budgetDetails2.setBudgetId(1L);
////        budgetDetails2.setShared(false);
////        budgetDetails2.setDailyBudgetAmount(250);
////        budgetDetails2.setDeliveryMethod(1);
////        budgetDetails2.setStatus(2);
////
////        budgetDetailsList.add(budgetDetails);
////        budgetDetailsList.add(budgetDetails2);
////
////        return budgetDetailsList;
////    }
//}
//
