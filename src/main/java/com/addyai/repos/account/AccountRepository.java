package com.addyai.repos.account;

import com.addyai.models.AccountDetails;

public interface AccountRepository {
    AccountDetails fetchAccountDetails(String customerId) throws Exception;
}
