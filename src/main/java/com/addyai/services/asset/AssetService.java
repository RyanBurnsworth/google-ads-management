package com.addyai.services.asset;

import com.addyai.models.assets.AssetDetails;
import com.google.ads.googleads.v14.enums.AssetFieldTypeEnum;

import java.util.List;

public interface AssetService {
    List<AssetDetails> getAssetDetails(long customerId) throws Exception;

    void upsertAssets(long customerId,
                      List<AssetDetails> assetDetailsList,
                      String assetLevelValue,
                      String campaignResourceName,
                      boolean shouldCreate) throws Exception;

    void performCampaignAssetOperation(long customerId,
                                       List<String> assetResourceNames,
                                       AssetFieldTypeEnum.AssetFieldType assetFieldType,
                                       String campaignResourceName,
                                       boolean shouldLink) throws Exception;

    void performCustomerAssetOperation(long customerId,
                                       List<String> assetResourceNames,
                                       AssetFieldTypeEnum.AssetFieldType assetFieldType,
                                       boolean shouldLink) throws Exception;
}
