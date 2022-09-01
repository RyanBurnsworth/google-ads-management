package com.addyai.repos.requests.impl;

import com.addyai.repos.requests.StreamRequest;
import com.google.ads.googleads.v11.services.GoogleAdsServiceClient;
import com.google.ads.googleads.v11.services.SearchGoogleAdsStreamRequest;
import com.google.ads.googleads.v11.services.SearchGoogleAdsStreamResponse;
import com.google.api.gax.rpc.ServerStream;

public class StreamRequestImpl implements StreamRequest {
    private final GoogleAdsServiceClient client;

    public StreamRequestImpl(final GoogleAdsServiceClient googleAdsServiceClient) {
        this.client = googleAdsServiceClient;
    }

    @Override
    public SearchGoogleAdsStreamRequest buildStreamRequest(long customerId, String query) {
        return SearchGoogleAdsStreamRequest.newBuilder()
                .setCustomerId(Long.toString(customerId))
                .setQuery(query)
                .build();
    }

    @Override
    public ServerStream<SearchGoogleAdsStreamResponse> callStreamRequest(SearchGoogleAdsStreamRequest streamRequest) {
        return this.client.searchStreamCallable().call(streamRequest);
    }
}
