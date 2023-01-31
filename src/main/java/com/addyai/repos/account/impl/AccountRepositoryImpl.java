package com.addyai.repos.account.impl;

import com.addyai.builder.GoogleAdsClientBuilder;
import com.addyai.error_handling.ApiExceptionResolver;
import com.addyai.models.AccountDetails;
import com.addyai.repos.account.AccountRepository;
import com.addyai.repos.request.StreamRequest;
import com.addyai.repos.request.impl.StreamRequestImpl;
import com.addyai.utils.helpers.GAQLHelper;
import com.google.ads.googleads.v12.services.CustomerServiceClient;
import com.google.ads.googleads.v12.services.GoogleAdsServiceClient;
import com.google.ads.googleads.v12.services.SearchGoogleAdsStreamRequest;
import com.google.ads.googleads.v12.services.SearchGoogleAdsStreamResponse;
import com.google.api.gax.rpc.ServerStream;
import org.springframework.stereotype.Repository;

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
}
