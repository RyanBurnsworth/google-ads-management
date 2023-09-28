package com.addyai.repos.account;

import com.addyai.models.AccountDetails;

import java.util.List;

public interface AccountRepository {
    AccountDetails fetchAccountDetails(String customerId) throws Exception;

    AccountDetails fetchAccountDetailsByResourceName(String customerId, String resourceName);

    List<String> fetchAccountResourceNames() throws Exception;
}
