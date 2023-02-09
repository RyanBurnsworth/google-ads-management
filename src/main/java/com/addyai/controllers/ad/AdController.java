package com.addyai.controllers.ad;

import com.addyai.models.ads.AdDetails;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdController {
    ResponseEntity<List<AdDetails>> fetchAdDetailsByAdGroup(long customerId, String adGroupId) throws Exception;

    ResponseEntity<Void> addAdsToAdGroup(long customerId,
                                         String adGroupId,
                                         List<AdDetails> adDetails) throws Exception;

    ResponseEntity<Void> updateAdsInAdGroup(long customerId,
                                            String adGroupId,
                                            List<AdDetails> adDetails) throws Exception;

    ResponseEntity<Void> deleteAdsInAdGroup(long customerId,
                                            String adGroupId,
                                            List<AdDetails> adDetails) throws Exception;
}
