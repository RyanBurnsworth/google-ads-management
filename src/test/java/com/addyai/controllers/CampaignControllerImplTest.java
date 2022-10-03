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

package com.addyai.controllers;

import com.addyai.error_handling.exceptions.InvalidRequestException;
import com.addyai.models.CampaignDetails;
import com.addyai.services.campaign.CampaignService;
import com.addyai.utils.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.addyai.utils.TestUtils.MOCK_CAMPAIGN_RESOURCE_NAME;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CampaignControllerImplTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    private static final String BASE_URL = "/api/v1/123456789/campaign/";
    private static final long CUSTOMER_ID = 123456789L;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CampaignService campaignService;

    private final TestUtils testUtils = new TestUtils();

    private final Gson gson = new Gson();

    @Test
    void testFetchAllCampaignsMissingCustomerIdReturnStatusNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/campaign/details"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFetchAllCampaignDetailsReturnsList() throws Exception {
        List<CampaignDetails> campaignDetailsList = new ArrayList<>();
        campaignDetailsList.add(testUtils.getMockCampaignDetails());

        when(campaignService.findAllCampaignDetails(CUSTOMER_ID))
                .thenReturn(campaignDetailsList);

        mockMvc.perform(get(BASE_URL + "details"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Type typeToken = new TypeToken<ArrayList<CampaignDetails>>() {
                    }.getType();

                    List<CampaignDetails> actualCampaignDetailsList =
                            gson.fromJson(result.getResponse().getContentAsString(), typeToken);

                    CampaignDetails expectedCampaignDetails = campaignDetailsList.get(0);
                    CampaignDetails actualCampaignDetails = actualCampaignDetailsList.get(0);

                    assertEquals(expectedCampaignDetails.getCampaignName(), actualCampaignDetails.getCampaignName());
                });
    }

    @Test
    void testFetchAllCampaignDetailsThrowsInvalidRequestException() throws Exception {
        when(campaignService.findAllCampaignDetails(CUSTOMER_ID))
                .thenThrow(new InvalidRequestException("TEST_EXCEPTION", "Testing exception"));

        mockMvc.perform(get(BASE_URL + "details"))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> {
                    assertTrue(result.getResolvedException() instanceof InvalidRequestException);

                    String exception = result.getResponse().getContentAsString();
                    InvalidRequestException invalidRequestException = gson.fromJson(exception,
                            InvalidRequestException.class);

                    assertEquals("TEST_EXCEPTION", invalidRequestException.getErrorCode());
                    assertEquals("Testing exception", invalidRequestException.getErrorMessage());
                });
    }

    @Test
    void testCreateCampaignsIsSuccessful() throws Exception {
        List<CampaignDetails> campaignDetailsList = new ArrayList<>();
        campaignDetailsList.add(testUtils.getMockCampaignDetails());

        mockMvc.perform(post(BASE_URL + "create")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(gson.toJson(campaignDetailsList)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void testUpdateCampaignsIsSuccessful() throws Exception {
        List<CampaignDetails> campaignDetailsList = new ArrayList<>();
        campaignDetailsList.add(testUtils.getMockCampaignDetails());

        mockMvc.perform(put(BASE_URL + "update")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(gson.toJson(campaignDetailsList)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void testDeleteCampaignIsSuccessful() throws Exception {
        List<CampaignDetails> campaignDetailsList = new ArrayList<>();
        CampaignDetails campaignDetails = new CampaignDetails();
        campaignDetails.setCampaignResourceName(MOCK_CAMPAIGN_RESOURCE_NAME);
        campaignDetailsList.add(campaignDetails);

        mockMvc.perform(post(BASE_URL + "remove")
                .contentType(APPLICATION_JSON_UTF8)
                .content(gson.toJson(campaignDetailsList)))
                .andExpect(status().isOk());
    }
}
