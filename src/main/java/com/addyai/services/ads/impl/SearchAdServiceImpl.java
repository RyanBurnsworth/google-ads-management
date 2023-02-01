package com.addyai.services.ads.impl;

import com.addyai.builder.OperationBuilder;
import com.addyai.builder.impl.OperationBuilderImpl;
import com.addyai.enums.OperationType;
import com.addyai.error_handling.ValidationErrorResponse;
import com.addyai.error_handling.exceptions.InvalidRequestException;
import com.addyai.models.ads.AdDetails;
import com.addyai.repos.ads.SearchAdsRepository;
import com.addyai.services.ads.SearchAdService;
import com.addyai.utils.helpers.ResourceNameHelper;
import com.addyai.utils.validators.EntityValidator;
import com.google.ads.googleads.v12.services.AdGroupAdOperation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.addyai.utils.misc.Constants.*;

@Service
public class SearchAdServiceImpl implements SearchAdService {

    private final SearchAdsRepository searchAdsRepository;
    private final OperationBuilder operationBuilder;

    public SearchAdServiceImpl(SearchAdsRepository searchAdsRepository) {
        this.searchAdsRepository = searchAdsRepository;
        this.operationBuilder = new OperationBuilderImpl();
    }

    @Override
    public List<AdDetails> findAllAdsByAdGroup(long customerId, String adGroupResName) throws Exception {
        List<AdDetails> adDetailsList = new ArrayList<>();

        if (adGroupResName.isEmpty())
            throw new InvalidRequestException(INVALID_REQUEST_ERROR, MISSING_PARAMS);

        for (int i = 0; i < NUM_SEARCH_AD_TYPES; i++) {
            // TODO when adding  new ad types fix this hardcoding
            adDetailsList.addAll(
                    searchAdsRepository.fetchAdDetails(customerId, adGroupResName, RESPONSIVE_AD_TYPE));
        }

        return adDetailsList;
    }

    @Override
    public void upsertAds(long customerId,
                          String adGroupResName,
                          List<AdDetails> adDetailsList,
                          boolean shouldCreate) throws Exception {
        OperationType operationType = shouldCreate ? OperationType.CREATE : OperationType.UPDATE;

        ValidationErrorResponse validationErrorResponse =
                EntityValidator.isAdDetailsValid(adDetailsList);

        if (validationErrorResponse != null) {
            throw new InvalidRequestException(
                    validationErrorResponse.getErrorCode(),
                    validationErrorResponse.getErrorMessage());
        }

        List<AdGroupAdOperation> adGroupAdOperationList =
                operationBuilder.buildAdGroupAdOperationList(adDetailsList, operationType, adGroupResName);

        searchAdsRepository.performSearchAdOperations(customerId, adGroupAdOperationList);
    }

    @Override
    public void deleteAds(long customerId, List<AdDetails> adDetailsList, String adGroupResName) throws Exception {
        OperationType operationType = OperationType.REMOVE;

        // update the adName to match
        // customers/{customer_id}/adGroupAds/{ad_group_id}~{ad_id}
        for(AdDetails adDetails : adDetailsList) {
            adDetails.setAdName(ResourceNameHelper
                    .getAdResourceName(customerId, adGroupResName, adDetails.getAdName()));
        }

        List<AdGroupAdOperation> adGroupAdOperationList =
                operationBuilder.buildAdGroupAdOperationList(adDetailsList, operationType, adGroupResName);

        searchAdsRepository.performSearchAdOperations(customerId, adGroupAdOperationList);
    }
}
