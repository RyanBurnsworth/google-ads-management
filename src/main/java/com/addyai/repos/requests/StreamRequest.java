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

package com.addyai.repos.requests;

import com.google.ads.googleads.v11.services.SearchGoogleAdsStreamRequest;
import com.google.ads.googleads.v11.services.SearchGoogleAdsStreamResponse;
import com.google.api.gax.rpc.ServerStream;

public interface StreamRequest {

    SearchGoogleAdsStreamRequest buildStreamRequest(long customerId, String query);

    ServerStream<SearchGoogleAdsStreamResponse> callStreamRequest(SearchGoogleAdsStreamRequest streamRequest);
}
