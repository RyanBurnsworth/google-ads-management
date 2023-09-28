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

package com.addyai.services.adgroup.impl;

import com.addyai.builder.OperationBuilder;
import com.addyai.builder.impl.OperationBuilderImpl;
import com.addyai.enums.OperationType;
import com.addyai.error_handling.ValidationErrorResponse;
import com.addyai.error_handling.exceptions.InvalidRequestException;
import com.addyai.models.AdGroupDetails;
import com.addyai.repos.adgroup.AdGroupRepository;
import com.addyai.services.adgroup.AdGroupService;
import com.addyai.utils.validators.EntityValidator;
import com.google.ads.googleads.v14.services.AdGroupOperation;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.addyai.utils.misc.Constants.INVALID_REQUEST_ERROR;
import static com.addyai.utils.misc.Constants.MISSING_PARAMS;

@Service
public class AdGroupServiceImpl implements AdGroupService {
    private final AdGroupRepository adGroupRepository;

    private final OperationBuilder operationBuilder;

    public AdGroupServiceImpl(AdGroupRepository adGroupRepository) {
        this.adGroupRepository = adGroupRepository;
        this.operationBuilder = new OperationBuilderImpl();
    }

    @Override
    public void upsertAdGroups(long customerId, List<AdGroupDetails> adGroupDetailsList, boolean shouldCreate) throws Exception {
        OperationType operationType = shouldCreate ? OperationType.CREATE : OperationType.UPDATE;

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isAdGroupDetailsValid(adGroupDetailsList, operationType);

        if (validationErrorResponse != null) {
            throw new InvalidRequestException(
                    validationErrorResponse.getErrorCode(),
                    validationErrorResponse.getErrorMessage());
        }

        List<AdGroupOperation> adGroupOperationList = operationBuilder
                .buildAdGroupOperationList(adGroupDetailsList, operationType);

        adGroupRepository.performAdGroupOperations(customerId, adGroupOperationList);
    }

    @Override
    public void deleteAdGroups(long customerId, List<AdGroupDetails> adGroupDetailsList) throws Exception {
        OperationType operationType = OperationType.REMOVE;

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isAdGroupDetailsValid(adGroupDetailsList, operationType);

        if (validationErrorResponse != null) {
            throw new InvalidRequestException(
                    validationErrorResponse.getErrorCode(),
                    validationErrorResponse.getErrorMessage());
        }

        List<AdGroupOperation> adGroupOperationList = operationBuilder
                .buildAdGroupOperationList(adGroupDetailsList, operationType);

        adGroupRepository.performAdGroupOperations(customerId, adGroupOperationList);
    }

    @Override
    public List<AdGroupDetails> findAllAdGroupsByCampaign(long customerId, String campaignResName) throws Exception {
        // throw an InvalidRequestException if the campaign resource nName is missing
        if (campaignResName.isEmpty())
            throw new InvalidRequestException(INVALID_REQUEST_ERROR, MISSING_PARAMS);

        return adGroupRepository.fetchAllAdGroupDetails(customerId, campaignResName);
    }
}
