package com.addyai.utils;

import com.google.ads.googleads.lib.GoogleAdsClient;

import java.io.FileNotFoundException;
import java.io.IOException;

public enum GoogleAdsManagementClient {
    INSTANCE();
    private GoogleAdsClient googleAdsClient;

    private GoogleAdsManagementClient() {
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

    public GoogleAdsManagementClient getInstance() {
        return INSTANCE;
    }

    public GoogleAdsClient getGoogleAdsClient() {
        return googleAdsClient;
    }
}
