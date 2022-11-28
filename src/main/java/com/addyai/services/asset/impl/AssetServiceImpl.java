package com.addyai.services.asset.impl;

import com.addyai.builder.OperationBuilder;
import com.addyai.builder.impl.OperationBuilderImpl;
import com.addyai.enums.OperationType;
import com.addyai.error_handling.ValidationErrorResponse;
import com.addyai.error_handling.exceptions.InvalidRequestException;
import com.addyai.models.assets.AssetDetails;
import com.addyai.repos.asset.AssetRepository;
import com.addyai.services.asset.AssetService;
import com.addyai.utils.validators.EntityValidator;
import com.google.ads.googleads.v12.services.AssetOperation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetServiceImpl implements AssetService {
    private final AssetRepository assetRepository;

    private final OperationBuilder operationBuilder;

    public AssetServiceImpl(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
        this.operationBuilder = new OperationBuilderImpl();
    }

    @Override
    public List<AssetDetails> getAssetDetails(long customerId) throws Exception {
        return assetRepository.fetchAssets(customerId);
    }

    @Override
    public void upsertAssets(long customerId, List<AssetDetails> assetDetailsList, boolean shouldCreate) throws Exception {
        OperationType operationType = shouldCreate ? OperationType.CREATE : OperationType.UPDATE;

        ValidationErrorResponse validationErrorResponse = EntityValidator.isAssetDetailsValid(assetDetailsList);

        if (validationErrorResponse != null) {
            throw new InvalidRequestException(
                    validationErrorResponse.getErrorCode(),
                    validationErrorResponse.getErrorMessage());
        }

        List<AssetOperation> assetOperationList =
                operationBuilder.buildAssetOperationList(assetDetailsList, operationType);

        assetRepository.performAssetOperations(customerId, assetOperationList);
    }
}
