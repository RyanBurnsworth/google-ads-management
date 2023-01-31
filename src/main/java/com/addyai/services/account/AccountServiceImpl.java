package com.addyai.services.account;

import com.addyai.models.AccountDetails;
import com.addyai.repos.account.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    private AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountDetails fetchAccountDetails(String customerId) throws Exception {
        return accountRepository.fetchAccountDetails(customerId);
    }
}
