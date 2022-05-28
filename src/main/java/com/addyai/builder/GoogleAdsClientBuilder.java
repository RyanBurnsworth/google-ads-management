package com.addyai.builder;

import com.google.ads.googleads.lib.GoogleAdsClient;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Builds a GoogleAdsClient for single use throughout application
 */
public class GoogleAdsClientBuilder {
    private final GoogleAdsClient googleAdsClient;

    public GoogleAdsClientBuilder() {
        GoogleAdsClient googleAdsClient = null;
        try {
            googleAdsClient = GoogleAdsClient.newBuilder().fromPropertiesFile().build();
        } catch (FileNotFoundException fnfe) {
            System.err.printf(
                    "Failed to load GoogleAdsClient configuration from file. Exception: %s%n", fnfe);
            System.exit(1);
        } catch (IOException ioe) {
            System.err.printf("Failed to create GoogleAdsClient. Exception: %s%n", ioe);
            System.exit(1);
        }
        this.googleAdsClient = googleAdsClient;
    }

    public GoogleAdsClient build() {
        return this.googleAdsClient;
    }
}
