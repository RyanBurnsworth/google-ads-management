package com.addyai.services.account;

import com.google.ads.googleads.lib.GoogleAdsClient;
import com.google.ads.googleads.v11.resources.Customer;
import com.google.ads.googleads.v11.services.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A service for retrieving information about customer accounts from a given manager account
 */
public class CustomerAccountService {
    private final GoogleAdsClient googleAdsClient;

    public CustomerAccountService(GoogleAdsClient googleAdsClient) {
        this.googleAdsClient = googleAdsClient;
    }

    /**
     * Retrieve all customer account ids on a manager account
     *
     * @return a list of customer ids
     */
    public List<String> getAllCustomerAccountIds() {
        List<String> accountIdList = new ArrayList<>();

        try (CustomerServiceClient customerService =
                     googleAdsClient.getLatestVersion().createCustomerServiceClient()) {
            ListAccessibleCustomersResponse response =
                    customerService.listAccessibleCustomers(
                            ListAccessibleCustomersRequest.newBuilder().build());

            for (String customerResourceName : response.getResourceNamesList()) {
                String customerId = stripToAccountId(customerResourceName);
                accountIdList.add(customerId);
            }
        }
        return accountIdList;
    }

    /**
     * Retrieve import details about the overall customer account
     *
     * @param customerId the id of the customer account
     */
    public void getCustomerDetails(long customerId) {
        try (GoogleAdsServiceClient googleAdsServiceClient =
                     googleAdsClient.getLatestVersion().createGoogleAdsServiceClient()) {
            // Constructs a query to retrieve the customer.
            String query =
                    "SELECT customer.id, "
                            + "customer.descriptive_name, "
                            + "customer.currency_code, "
                            + "customer.time_zone, "
                            + "customer.tracking_url_template, "
                            + "customer.auto_tagging_enabled "
                            + "FROM customer "
                            // Limits to 1 to clarify that selecting from the customer resource
                            // will always return only one row, which will be for the customer
                            // ID specified in the request.
                            + "LIMIT 1";

            // Executes the query and gets the Customer object from the single row of the response.
            GoogleAdsServiceClient.SearchPagedResponse response =
                    googleAdsServiceClient.search(Long.toString(customerId), query);
            GoogleAdsRow googleAdsRow = response.iterateAll().iterator().next();
            Customer customer = googleAdsRow.getCustomer();

            // Prints account information.
            System.out.printf(
                    "Customer with ID %d, descriptive name '%s', currency code '%s', timezone '%s', "
                            + "tracking URL template '%s' and auto tagging enabled '%s' was retrieved.%n",
                    customer.getId(),
                    customer.getDescriptiveName(),
                    customer.getCurrencyCode(),
                    customer.getTimeZone(),
                    customer.getTrackingUrlTemplate(),
                    customer.getAutoTaggingEnabled());
        }
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
