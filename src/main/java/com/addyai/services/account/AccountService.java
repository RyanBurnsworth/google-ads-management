package com.addyai.services.account;

import com.addyai.models.AccountDetails;

import java.util.List;

public interface AccountService {
    AccountDetails fetchAccountDetails(String customerId) throws Exception;

    List<AccountDetails> fetchAllAccounts(String customerId) throws Exception;
}
