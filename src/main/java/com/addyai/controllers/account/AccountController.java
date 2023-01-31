package com.addyai.controllers.account;

import com.addyai.models.AccountDetails;
import org.springframework.http.ResponseEntity;

public interface AccountController {
    ResponseEntity<AccountDetails> getAccountDetails(String customerId) throws Exception;
}
