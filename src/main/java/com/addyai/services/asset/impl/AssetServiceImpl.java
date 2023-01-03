package com.addyai.services.asset.impl;

import com.addyai.builder.OperationBuilder;
import com.addyai.builder.impl.OperationBuilderImpl;
import com.addyai.enums.AssetLevel;
import com.addyai.enums.OperationType;
import com.addyai.error_handling.ValidationErrorResponse;
import com.addyai.error_handling.exceptions.InvalidRequestException;
import com.addyai.models.AssetLinkerDetails;
import com.addyai.models.assets.AssetDetails;
import com.addyai.repos.asset.AssetRepository;
import com.addyai.repos.linker.AssetLinkerRepository;
import com.addyai.services.asset.AssetService;
import com.addyai.utils.validators.EntityValidator;
import com.google.ads.googleads.v12.enums.AssetFieldTypeEnum;
import com.google.ads.googleads.v12.services.AssetOperation;
import com.google.ads.googleads.v12.services.CampaignAssetOperation;
import com.google.ads.googleads.v12.services.CustomerAssetOperation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AssetServiceImpl implements AssetService {
    private final AssetRepository assetRepository;

    private final AssetLinkerRepository assetLinkerRepository;

    private final OperationBuilder operationBuilder;

    public AssetServiceImpl(AssetRepository assetRepository, AssetLinkerRepository assetLinkerRepository) {
        this.assetRepository = assetRepository;
        this.assetLinkerRepository = assetLinkerRepository;
        this.operationBuilder = new OperationBuilderImpl();
    }

    /**
     * Fetch the asset details from the customer account
     *
     * @param customerId the customer id of the account
     * @return a list of {@link AssetDetails}
     * @throws Exception
     */
    @Override
    public List<AssetDetails> getAssetDetails(long customerId) throws Exception {
        return assetRepository.fetchAssets(customerId);
    }

    /**
     * Create or update {@link com.google.ads.googleads.v12.resources.Asset} on a client account
     *
     * @param customerId       the id of the customer account
     * @param assetDetailsList a list of the details of the assets
     * @param assetLevelValue  ACCOUNT_LEVEL or CAMPAIGN_LEVEL
     * @param shouldCreate     create if true, update if false
     * @throws Exception
     */
    @Override
    public void upsertAssets(long customerId,
                             List<AssetDetails> assetDetailsList,
                             String assetLevelValue,
                             String campaignResourceName,
                             boolean shouldCreate) throws Exception {
        OperationType operationType = shouldCreate ? OperationType.CREATE : OperationType.UPDATE;
        AssetLevel assetLevel = AssetLevel.valueOf(assetLevelValue);

        ValidationErrorResponse validationErrorResponse = EntityValidator.isAssetDetailsValid(assetDetailsList);

        if (validationErrorResponse != null) {
            throw new InvalidRequestException(
                    validationErrorResponse.getErrorCode(),
                    validationErrorResponse.getErrorMessage());
        }

        List<AssetOperation> assetOperationList =
                operationBuilder.buildAssetOperationList(assetDetailsList, operationType);

        List<String> assetResourceNameList =
                assetRepository.performAssetOperations(customerId, assetOperationList);

        if (operationType.equals(OperationType.CREATE) && assetResourceNameList.size() > 0) {

            AssetFieldTypeEnum.AssetFieldType assetFieldType =
                    AssetFieldTypeEnum.AssetFieldType.forNumber(assetDetailsList.get(0).getAssetType());
            if (assetLevel.equals(AssetLevel.ACCOUNT_LEVEL)) {
                performCustomerAssetOperation(customerId,
                        assetResourceNameList,
                        AssetFieldTypeEnum.AssetFieldType.forNumber(assetDetailsList.get(0).getAssetType()),
                        true);
            } else if (assetLevel.equals(AssetLevel.CAMPAIGN_LEVEL)) {
                performCampaignAssetOperation(customerId,
                        assetResourceNameList,
                        AssetFieldTypeEnum.AssetFieldType.forNumber(assetDetailsList.get(0).getAssetType()),
                        campaignResourceName,
                        true);
            }
        }
    }


    /**
     * Link or unlink a list of {@link com.google.ads.googleads.v12.resources.CampaignAsset} to a campaign
     *
     * @param customerId           the customer id to be used
     * @param assetResourceNames   a list of resources names for each of the assets
     * @param assetFieldType       the type of asset
     * @param campaignResourceName the resource name of the campaign to be used
     * @param shouldLink           links to campaign if true, unlinks if falses
     * @throws Exception
     */
    @Override
    public void performCampaignAssetOperation(long customerId,
                                              List<String> assetResourceNames,
                                              AssetFieldTypeEnum.AssetFieldType assetFieldType,
                                              String campaignResourceName,
                                              boolean shouldLink) throws Exception {
        List<AssetLinkerDetails> assetLinkerDetailsList =
                buildAssetLinkerDetailsList(assetResourceNames, assetFieldType, campaignResourceName);

        List<CampaignAssetOperation> campaignAssetOperationList;
        if (shouldLink) {
            campaignAssetOperationList =
                    operationBuilder.buildCampaignAssetOperationList(assetLinkerDetailsList, OperationType.CREATE);
        } else {
            campaignAssetOperationList =
                    operationBuilder.buildCampaignAssetOperationList(assetLinkerDetailsList, OperationType.REMOVE);
        }

        assetLinkerRepository.performCampaignAssetOperation(customerId, campaignAssetOperationList);
    }

    /**
     * Link or unlink a list of {@link com.google.ads.googleads.v12.resources.CustomerAsset} to an account
     *
     * @param customerId         the customer id to be used
     * @param assetResourceNames a list of resources names for each of the assets
     * @param assetFieldType     the type of asset
     * @param shouldLink         links to account if true, unlinks if falses
     * @throws Exception
     */
    @Override
    public void performCustomerAssetOperation(long customerId, List<String> assetResourceNames,
                                              AssetFieldTypeEnum.AssetFieldType assetFieldType,
                                              boolean shouldLink) throws Exception {

        List<CustomerAssetOperation> customerAssetOperationList;
        List<AssetLinkerDetails> assetLinkerDetailsList =
                buildAssetLinkerDetailsList(assetResourceNames, assetFieldType, "");

        if (shouldLink) {
            customerAssetOperationList =
                    operationBuilder.buildCustomerAssetOperationList(assetLinkerDetailsList, OperationType.CREATE);
        } else {
            customerAssetOperationList =
                    operationBuilder.buildCustomerAssetOperationList(assetLinkerDetailsList, OperationType.REMOVE);
        }

        assetLinkerRepository.performCustomerAssetOperation(customerId, customerAssetOperationList);
    }

    private List<AssetLinkerDetails> buildAssetLinkerDetailsList(List<String> assetResourceNames,
                                                                 AssetFieldTypeEnum.AssetFieldType assetFieldType,
                                                                 String campaignResName) {
        List<AssetLinkerDetails> assetLinkerDetailsList = new ArrayList<>();

        for (String assetResourceName : assetResourceNames) {
            AssetLinkerDetails assetLinkerDetails = new AssetLinkerDetails();
            assetLinkerDetails.setAssetResourceName(assetResourceName);
            assetLinkerDetails.setAssetType(assetFieldType);
            assetLinkerDetails.setCampaignResourceName(campaignResName);
            assetLinkerDetailsList.add(assetLinkerDetails);
        }

        return assetLinkerDetailsList;
    }
}
