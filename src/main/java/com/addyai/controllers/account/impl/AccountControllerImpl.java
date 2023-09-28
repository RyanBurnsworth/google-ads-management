package com.addyai.controllers.account.impl;

import com.addyai.controllers.account.AccountController;
import com.addyai.models.AccountDetails;
import com.addyai.services.account.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/{customerId}/account")
public class AccountControllerImpl implements AccountController {
    private final AccountService accountService;

    public AccountControllerImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/details")
    public ResponseEntity<AccountDetails> getAccountDetails(@PathVariable String customerId) throws Exception {
        AccountDetails accountDetails = accountService.fetchAccountDetails(customerId);
        return new ResponseEntity<>(accountDetails, HttpStatus.OK);
    }

    @Override
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/all")
    public ResponseEntity<List<AccountDetails>> getAllAccountDetails(@PathVariable String customerId) throws Exception {
        List<AccountDetails> accountDetailsList = accountService.fetchAllAccounts(customerId);
        return new ResponseEntity<>(accountDetailsList, HttpStatus.OK);
    }


}
