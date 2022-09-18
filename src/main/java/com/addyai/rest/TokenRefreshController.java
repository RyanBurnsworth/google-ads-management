package com.addyai.rest;

import com.addyai.services.account.ManagerAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/token")
public class TokenRefreshController {
    // TODO move this information to a config file
    private static final String CLIENT_ID = "453865601493-hjr9cbb0uslsdb5k4uuq9vaso03tpua6.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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";
    private static final String EMAIL_ADDR = "mccppcblast@gmail.com";

    @GetMapping("/refresh")
    String getTokenRefresh() {
        ManagerAccountService managerAccountService = new ManagerAccountService();
        try {
            managerAccountService.initiateGoogleAdsAccountLinking(CLIENT_ID, CLIENT_SECRET, EMAIL_ADDR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "";
    }
}
