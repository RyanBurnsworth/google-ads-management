package com.addyai;

import com.addyai.builder.GoogleAdsClientBuilder;
import com.google.ads.googleads.lib.GoogleAdsClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GoogleAdsManagementApplication {
    public final static long MANAGER_ACCOUNT_ID = 2898332235L;
    public final static long CLIENT_ACCOUNT_ID = 9059845250L;

    public static GoogleAdsClient googleAdsClient = null;

    public static void main(String[] args) {
        SpringApplication.run(GoogleAdsManagementApplication.class, args);
    }

    public static GoogleAdsClient getGoogleAdsClient() {
        if (googleAdsClient == null) {
            GoogleAdsClientBuilder builder = new GoogleAdsClientBuilder();
            googleAdsClient = builder.build();
        }
        return googleAdsClient;
    }
}
