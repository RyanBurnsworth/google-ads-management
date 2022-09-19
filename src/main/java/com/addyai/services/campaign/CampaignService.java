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

package com.addyai.services.campaign;

import com.addyai.models.CampaignDetails;

import java.util.List;

public interface CampaignService {

    void addCampaignsToAccount(long customerId, List<CampaignDetails> campaignDetailsList) throws Exception;

    List<CampaignDetails> findAllCampaignDetails(long customerId) throws Exception;

    CampaignDetails findCampaignDetailsByName(long customerId, String campaignName) throws Exception;

    void updateCampaigns(long customerId, List<CampaignDetails> campaignDetails) throws Exception;

    void deleteCampaigns(long customerId, List<Long> campaignIds) throws Exception;
}
