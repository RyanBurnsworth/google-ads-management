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

package com.addyai.controllers.keyword.impl;

import com.addyai.controllers.keyword.KeywordController;
import com.addyai.models.KeywordDetails;
import com.addyai.services.keyword.KeywordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/{customerId}/keyword/")
public class KeywordControllerImpl implements KeywordController {
    private final KeywordService keywordService;

    public KeywordControllerImpl(KeywordService keywordService) {
        this.keywordService = keywordService;
    }

    @Override
    @GetMapping("details")
    public ResponseEntity<List<KeywordDetails>> findKeywordDetailsByAdGroup(@PathVariable String customerId,
                                                                            @RequestParam String adGroupResName) throws Exception {
        List<KeywordDetails> keywordDetailsList =
                keywordService.findAllKeywordsByAdGroup(Long.parseLong(customerId), adGroupResName);

        return new ResponseEntity<>(keywordDetailsList, HttpStatus.OK);
    }

    @Override
    @PostMapping("create")
    public ResponseEntity<Void> addKeywordsToAdGroup(@PathVariable String customerId,
                                                     @RequestBody List<KeywordDetails> keywordDetailsList) throws Exception {

        keywordService.upsertKeywords(Long.parseLong(customerId), keywordDetailsList, true);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @PostMapping("update")
    public ResponseEntity<Void> updateKeywords(@PathVariable String customerId,
                                               @RequestBody List<KeywordDetails> keywordDetailsList) throws Exception {
        keywordService.upsertKeywords(Long.parseLong(customerId), keywordDetailsList, false);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @PostMapping("remove")
    public ResponseEntity<Void> deleteKeywords(@PathVariable String customerId,
                                               @RequestBody List<KeywordDetails> keywordDetailsList) throws Exception {
        keywordService.deleteKeywords(Long.parseLong(customerId), keywordDetailsList);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
