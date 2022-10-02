/*
 * Copyright (c) 2022.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. This program
 * is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 *
 */

package com.addyai.repos.request.impl;

import com.addyai.repos.request.StreamRequest;
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
