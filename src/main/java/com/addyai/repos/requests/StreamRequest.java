package com.addyai.repos.requests;

import com.google.ads.googleads.v11.services.SearchGoogleAdsStreamRequest;
import com.google.ads.googleads.v11.services.SearchGoogleAdsStreamResponse;
import com.google.api.gax.rpc.ServerStream;

public interface StreamRequest {

    SearchGoogleAdsStreamRequest buildStreamRequest(long customerId, String query);

    ServerStream<SearchGoogleAdsStreamResponse> callStreamRequest(SearchGoogleAdsStreamRequest streamRequest);
}
