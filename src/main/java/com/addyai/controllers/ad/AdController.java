package com.addyai.controllers.ad;

import com.addyai.models.ads.AdDetails;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdController {
    ResponseEntity<List<AdDetails>> fetchAdDetailsByAdGroup(long customerId, String adGroupResName) throws Exception;

    ResponseEntity<Void> addAdsToAdGroup(long customerId,
                                         String adGroupResName,
                                         List<AdDetails> adDetailsList) throws Exception;

    ResponseEntity<Void> updateAdsInAdGroup(long customerId,
                                            String adGroupResName,
                                            List<AdDetails> adDetailsList) throws Exception;

    ResponseEntity<Void> deleteAdsInAdGroup(long customerId,
                                            String adGroupResName,
                                            List<AdDetails> adDetailsList) throws Exception;
}
