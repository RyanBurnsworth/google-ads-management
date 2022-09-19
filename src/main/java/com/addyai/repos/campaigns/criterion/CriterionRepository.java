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

package com.addyai.repos.campaigns.criterion;

import com.addyai.models.campaign_criterion.CriterionDetails;
import com.google.ads.googleads.v11.services.CampaignCriterionOperation;

import java.util.List;

public interface CriterionRepository {

    List<CriterionDetails> fetchCampaignCriterionDetails(long customerId, String campaignResourceName);

    void addCampaignCriterion(long customerId, List<CampaignCriterionOperation> campaignCriterionOperationList) throws Exception;

    String getGeoTargetConstant(String locale, String countryCode, String location) throws Exception;
}
