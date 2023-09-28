package com.addyai.repos.linker;

import com.google.ads.googleads.v14.services.CampaignAssetOperation;
import com.google.ads.googleads.v14.services.CustomerAssetOperation;

import java.util.List;

public interface AssetLinkerRepository {
    void performCampaignAssetOperation(long customerId, List<CampaignAssetOperation> campaignAssetOperationList) throws Exception;

    void performCustomerAssetOperation(long customerId, List<CustomerAssetOperation> customerAssetOperationList) throws Exception;
}
