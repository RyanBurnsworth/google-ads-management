package com.addyai.services.account;

import com.addyai.models.AccountDetails;

public interface AccountService {
    AccountDetails fetchAccountDetails(String customerId) throws Exception;
}
