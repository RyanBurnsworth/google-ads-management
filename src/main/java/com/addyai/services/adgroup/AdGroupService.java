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

package com.addyai.services.adgroup;

import com.addyai.models.AdGroupDetails;

import java.util.List;

public interface AdGroupService {

    void upsertAdGroups(long customerId, List<AdGroupDetails> adGroupDetailsList, boolean shouldCreate) throws Exception;

    void deleteAdGroups(long customerId, List<AdGroupDetails> adGroupDetailsList) throws Exception;

    List<AdGroupDetails> findAllAdGroupsByCampaign(long customerId, String campaignResName) throws Exception;
}
