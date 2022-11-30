package com.addyai.services.ads;

import com.addyai.models.ads.AdDetails;

import java.util.List;

public interface SearchAdService {

    List<AdDetails> findAllAdsByAdGroup(long customerId, String adGroupResName) throws Exception;

    void upsertAds(long customerId, String adGroupResName, List<AdDetails> adDetailsList, boolean shouldCreate) throws Exception;

    void deleteAds(long customerId, List<AdDetails> adDetailsList, String adGroupResName) throws Exception;
}
