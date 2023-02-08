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

package com.addyai.controllers.keyword;

import com.addyai.models.KeywordDetails;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface KeywordController {
    ResponseEntity<List<KeywordDetails>> findKeywordDetailsByAdGroup(String customerId, String adGroupId) throws Exception;

    ResponseEntity<Void> addKeywordsToAdGroup(String customerId, List<KeywordDetails> keywordDetailsList) throws Exception;

    ResponseEntity<Void> updateKeywords(String customerId, List<KeywordDetails> keywordDetailsList) throws Exception;

    ResponseEntity<Void> deleteKeywords(String customerId, List<KeywordDetails> keywordDetailsList) throws Exception;
}
