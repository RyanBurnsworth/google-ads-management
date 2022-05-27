package com.addyai;

import com.addyai.e2e.E2ETester;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GoogleAdsManagementApplication {
    public final static long MANAGER_ACCOUNT_ID = 2898332235L;
    public final static long CLIENT_ACCOUNT_ID = 9059845250L;

    public static void main(String[] args) {
        SpringApplication.run(GoogleAdsManagementApplication.class, args);

        E2ETester e2ETester = new E2ETester();
    }
}
