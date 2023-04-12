package com.addyai.repos.ads;

import com.addyai.models.ads.AdDetails;
import com.google.ads.googleads.v12.services.AdGroupAdOperation;

import java.util.List;

public interface SearchAdsRepository {
    List<AdDetails> fetchAdDetails(long customerId, String adGroupResName, String adType) throws Exception;

    List<String> performSearchAdOperations(long customerId,
                                           List<AdGroupAdOperation> adGroupAdOperations) throws Exception;
    List<String> validateSearchAd(long customerId, List<AdGroupAdOperation> adGroupAdOperations);
}
