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

import java.io.FileNotFoundException;
import java.io.IOException;

public enum GoogleAdsClientBuilder {
    INSTANCE();
    private GoogleAdsClient googleAdsClient;

    GoogleAdsClientBuilder() {
        try {
            // build the google ads client from the properties file
            googleAdsClient = GoogleAdsClient.newBuilder().fromPropertiesFile().build();
        } catch (FileNotFoundException fnfe) {
            System.err.printf(
                    "Failed to load GoogleAdsClient configuration from file. Exception: %s%n", fnfe);
            System.exit(1);
        } catch (IOException ioe) {
            System.err.printf("Failed to create GoogleAdsClient. Exception: %s%n", ioe);
            System.exit(1);
        }
    }

    public GoogleAdsClientBuilder getInstance() {
        return INSTANCE;
    }

    public GoogleAdsClient getGoogleAdsClient() {
        return this.googleAdsClient;
    }
}
