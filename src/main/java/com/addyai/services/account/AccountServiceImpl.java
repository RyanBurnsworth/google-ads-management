package com.addyai.services.account;

import com.addyai.models.AccountDetails;
import com.addyai.repos.account.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountDetails fetchAccountDetails(String customerId) throws Exception {
        return accountRepository.fetchAccountDetails(customerId);
    }

    @Override
    public List<AccountDetails> fetchAllAccounts(String customerId) throws Exception {
        List<AccountDetails> accountDetailsList = new ArrayList<>();
        List<String> resourceNamesList = accountRepository.fetchAccountResourceNames();

        AccountDetails mainAccountDetails = accountRepository.fetchAccountDetails(customerId);
        accountDetailsList.add(mainAccountDetails);

        resourceNamesList.forEach(resourceName -> {
            AccountDetails accountDetails = null;
            try {
                accountDetails = accountRepository.fetchAccountDetailsByResourceName(customerId, resourceName);
                if (!accountDetails.getCustomerId().isEmpty()) {
                    accountDetailsList.add(accountDetails);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return accountDetailsList;
    }
}
