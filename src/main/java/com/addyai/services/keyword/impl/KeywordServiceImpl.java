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

package com.addyai.services.keyword.impl;

import com.addyai.builder.OperationBuilder;
import com.addyai.builder.impl.OperationBuilderImpl;
import com.addyai.enums.OperationType;
import com.addyai.error_handling.ValidationErrorResponse;
import com.addyai.error_handling.exceptions.InvalidRequestException;
import com.addyai.models.KeywordDetails;
import com.addyai.repos.keyword.KeywordRepository;
import com.addyai.services.keyword.KeywordService;
import com.addyai.utils.validators.EntityValidator;
import com.google.ads.googleads.v14.services.AdGroupCriterionOperation;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.addyai.utils.misc.Constants.INVALID_REQUEST_ERROR;
import static com.addyai.utils.misc.Constants.MISSING_PARAMS;

@Service
public class KeywordServiceImpl implements KeywordService {

    private final KeywordRepository keywordRepository;

    private final OperationBuilder operationBuilder;

    public KeywordServiceImpl(KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
        this.operationBuilder = new OperationBuilderImpl();
    }

    @Override
    public void upsertKeywords(long customerId, List<KeywordDetails> keywordDetailsList, boolean shouldCreate) throws Exception {
        OperationType operationType = shouldCreate ? OperationType.CREATE : OperationType.UPDATE;

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isKeywordDetailsValid(keywordDetailsList, operationType);

        if (validationErrorResponse != null) {
            throw new InvalidRequestException(
                    validationErrorResponse.getErrorCode(),
                    validationErrorResponse.getErrorMessage());
        }

        List<AdGroupCriterionOperation> keywordOperations = operationBuilder
                .buildAdGroupCriterionOperationList(keywordDetailsList, operationType);

        keywordRepository.performKeywordOperations(customerId, keywordOperations);
    }

    @Override
    public void deleteKeywords(long customerId, List<KeywordDetails> keywordDetailsList) throws Exception {
        OperationType operationType = OperationType.REMOVE;

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isKeywordDetailsValid(keywordDetailsList, operationType);

        if (validationErrorResponse != null) {
            throw new InvalidRequestException(
                    validationErrorResponse.getErrorCode(),
                    validationErrorResponse.getErrorMessage());
        }

        List<AdGroupCriterionOperation> keywordOperations = operationBuilder
                .buildAdGroupCriterionOperationList(keywordDetailsList, operationType);

        keywordRepository.performKeywordOperations(customerId, keywordOperations);
    }

    @Override
    public List<KeywordDetails> findAllKeywordsByAdGroup(long customerId, String adGroupResName) throws Exception {
        // throw an InvalidRequestException if the campaign resource nName is missing
        if (adGroupResName.isEmpty())
            throw new InvalidRequestException(INVALID_REQUEST_ERROR, MISSING_PARAMS);

        return keywordRepository.fetchKeywordDetailsByAdGroup(customerId, adGroupResName);
    }
}
