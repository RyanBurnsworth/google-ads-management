package com.addyai.controllers.account;

import com.addyai.models.AccountDetails;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccountController {
    ResponseEntity<AccountDetails> getAccountDetails(String customerId) throws Exception;

    ResponseEntity<List<AccountDetails>> getAllAccountDetails(String customerId) throws Exception;
}
