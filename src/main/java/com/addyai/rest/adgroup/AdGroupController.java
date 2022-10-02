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

package com.addyai.rest.adgroup;

import com.addyai.models.AdGroupDetails;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdGroupController {
    ResponseEntity<List<AdGroupDetails>> fetchAllAdGroupDetails(long customerId, String campaignResName) throws Exception;

    ResponseEntity<Void> createAdGroups(String customerId, List<AdGroupDetails> adGroupDetailsList) throws Exception;

    ResponseEntity<Void> updateAdGroups(String customerId, List<AdGroupDetails> adGroupDetailsList) throws Exception;

    ResponseEntity<Void> deleteAdGroups(String customerId, List<AdGroupDetails> adGroupDetailsList) throws Exception;
}
