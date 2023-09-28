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

package com.addyai.builder;

import com.google.ads.googleads.lib.GoogleAdsClient;
import com.google.auth.Credentials;
import com.google.auth.oauth2.UserCredentials;

public enum GoogleAdsClientBuilder {
    INSTANCE();
    private GoogleAdsClient googleAdsClient;

    GoogleAdsClientBuilder() {
        Credentials credentials = UserCredentials.newBuilder()
                .setClientId("453865601493-hjr9cbb0uslsdb5k4uuq9vaso03tpua6.apps.googleusercontent.com")
                .setClientSecret("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")
                .setRefreshToken("1//04CUCpznkxFGpCgYIARAAGAQSNwF-L9Ir0OgWEXlwbSCEOmB0zJh4hjlPc14Rl6LKIHOAQ-k65yXjEXR2UAsiz8oG3KM4QN5qHiM")
                .build();

        googleAdsClient = GoogleAdsClient.newBuilder()
                .setCredentials(credentials)
                .setDeveloperToken("9X_MSxxuM8YsgvoX5z1gJQ")
                .setLoginCustomerId(2898332235L)
                .build();
    }

    public GoogleAdsClientBuilder getInstance() {
        return INSTANCE;
    }

    public GoogleAdsClient getGoogleAdsClient() {
        return this.googleAdsClient;
    }
}
