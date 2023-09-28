package com.addyai.repos.account.impl;

import com.addyai.builder.GoogleAdsClientBuilder;
import com.addyai.error_handling.ApiExceptionResolver;
import com.addyai.models.AccountDetails;
import com.addyai.repos.account.AccountRepository;
import com.addyai.repos.request.StreamRequest;
import com.addyai.repos.request.impl.StreamRequestImpl;
import com.addyai.utils.helpers.GAQLHelper;
import com.google.ads.googleads.v14.services.*;
import com.google.api.gax.rpc.ServerStream;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AccountRepositoryImpl implements AccountRepository {
    private final CustomerServiceClient customerServiceClient;
    private final StreamRequest requestBuilder;

    public AccountRepositoryImpl() {
        GoogleAdsClientBuilder googleAdsClientBuilder = GoogleAdsClientBuilder.INSTANCE;

        GoogleAdsServiceClient googleAdsServiceClient = googleAdsClientBuilder
                .getGoogleAdsClient()
                .getLatestVersion()
                .createGoogleAdsServiceClient();

        this.requestBuilder = new StreamRequestImpl(googleAdsServiceClient);

        customerServiceClient = googleAdsClientBuilder
                .getGoogleAdsClient()
                .getLatestVersion()
                .createCustomerServiceClient();
    }

    @Override
    public AccountDetails fetchAccountDetails(String customerId) throws Exception {
        try {
            String query = GAQLHelper.getAccountDetailsQuery(customerId);

            SearchGoogleAdsStreamRequest request = requestBuilder.buildStreamRequest(Long.parseLong(customerId), query);
            ServerStream<SearchGoogleAdsStreamResponse> response = requestBuilder.callStreamRequest(request);

            return GAQLHelper.convertStreamResponseToAccountDetails(response);
        } catch (Exception e) {
            throw ApiExceptionResolver.doResolveException(e);
        }
    }

    @Override
    public AccountDetails fetchAccountDetailsByResourceName(String customerId, String resourceName) {
        AccountDetails accountDetails = new AccountDetails();
        try {
            String name = "customers/{" + resourceName + "}";
            System.out.println("NAME: " + name);
            String query = GAQLHelper.getAccountDetailsQueryByResourceName(name);

            SearchGoogleAdsStreamRequest request = requestBuilder.buildStreamRequest(Long.parseLong(customerId), query);
            ServerStream<SearchGoogleAdsStreamResponse> response = requestBuilder.callStreamRequest(request);

            accountDetails = GAQLHelper.convertStreamResponseToAccountDetails(response);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return accountDetails;
    }

    @Override
    public List<String> fetchAccountResourceNames() throws Exception {
        List<String> resourceNameList = new ArrayList<>();

        try {
            ListAccessibleCustomersResponse response =
                    customerServiceClient.listAccessibleCustomers(
                            ListAccessibleCustomersRequest.newBuilder().build());

            for (String customerResourceName : response.getResourceNamesList()) {

                String customerId = stripToAccountId(customerResourceName);
                resourceNameList.add(customerId);
            }
        } catch (Exception e) {
            throw ApiExceptionResolver.doResolveException(e);
        }
        return resourceNameList;
    }

    /**
     * Strips 'customers' from the from of the account id
     *
     * @param accountId the account id to strip
     * @return a numerical account id as a string
     */
    private String stripToAccountId(String accountId) {
        return accountId.replace("customers/", "");
    }
}
