package com.addyai.repos.ads.impl;

import com.addyai.builder.GoogleAdsClientBuilder;
import com.addyai.error_handling.ApiExceptionResolver;
import com.addyai.models.ads.AdDetails;
import com.addyai.repos.ads.SearchAdsRepository;
import com.addyai.repos.request.StreamRequest;
import com.addyai.repos.request.impl.StreamRequestImpl;
import com.addyai.utils.helpers.GAQLHelper;
import com.google.ads.googleads.v12.errors.GoogleAdsException;
import com.google.ads.googleads.v12.services.*;
import com.google.api.gax.rpc.ServerStream;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SearchAdsRepositoryImpl implements SearchAdsRepository {
    private final AdGroupAdServiceClient adGroupAdServiceClient;

    private final StreamRequest requestBuilder;

    public SearchAdsRepositoryImpl() {
        GoogleAdsClientBuilder googleAdsClientBuilder = GoogleAdsClientBuilder.INSTANCE;

        GoogleAdsServiceClient googleAdsServiceClient = googleAdsClientBuilder
                .getGoogleAdsClient()
                .getLatestVersion()
                .createGoogleAdsServiceClient();

        this.requestBuilder = new StreamRequestImpl(googleAdsServiceClient);

        adGroupAdServiceClient = googleAdsClientBuilder
                .getGoogleAdsClient()
                .getLatestVersion()
                .createAdGroupAdServiceClient();
    }

    @Override
    public List<AdDetails> fetchAdDetails(long customerId, String adGroupResName, String adType) throws Exception {
        List<AdDetails> adDetailsList;

        try {
            String query = GAQLHelper.getResponsiveSearchAdQuery(adGroupResName);

            SearchGoogleAdsStreamRequest request = requestBuilder.buildStreamRequest(customerId, query);
            ServerStream<SearchGoogleAdsStreamResponse> response = requestBuilder.callStreamRequest(request);

            adDetailsList = GAQLHelper.convertStreamResponseToAdDetails(response, adType);

            return adDetailsList;
        } catch (Exception e) {
            throw ApiExceptionResolver.doResolveException(e);
        }
    }

    @Override
    public List<String> performSearchAdOperations(long customerId, List<AdGroupAdOperation> adGroupAdOperations) throws Exception {
        List<String> adResourceNameList = new ArrayList<>();
        try {
            MutateAdGroupAdsResponse response =
                    adGroupAdServiceClient.mutateAdGroupAds(Long.toString(customerId), adGroupAdOperations);

            for (MutateAdGroupAdResult result : response.getResultsList()) {
                adResourceNameList.add(result.getResourceName());
            }
            return adResourceNameList;
        } catch (Exception e) {
            throw ApiExceptionResolver.doResolveException(e);
        }
    }

    @Override
    public List<String> validateSearchAd(long customerId, List<AdGroupAdOperation> adGroupAdOperations) {
        try {
            MutateAdGroupAdsRequest request = MutateAdGroupAdsRequest.newBuilder()
                    .setCustomerId(Long.toString(customerId))
                    .addOperations(adGroupAdOperations.get(0))
                    .setValidateOnly(true)
                    .build();

            adGroupAdServiceClient.mutateAdGroupAds(request);

            return new ArrayList<>();
        } catch (GoogleAdsException e) {
            List<String> errorList = new ArrayList<>();

            // extract the error reasons from Google Ads and put into a string
            // send the list of error strings back
            e.getGoogleAdsFailure().getErrorsList().forEach(err -> {
                if (err != null && err.getDetails().hasPolicyFindingDetails() && err.getDetails().getPolicyFindingDetails().getPolicyTopicEntriesList().size() > 0 && err.getDetails().getPolicyFindingDetails().getPolicyTopicEntries(0).getEvidencesList().size() > 0) {
                    if (err.getDetails().getPolicyFindingDetails().getPolicyTopicEntriesList().get(0).getEvidencesList().get(0).hasDestinationNotWorking()) {
                        String error_type = err.getDetails().getPolicyFindingDetails().getPolicyTopicEntriesList().get(0).getEvidencesList().get(0).getDestinationNotWorking().getDnsErrorType().toString();
                        String expanded_url = err.getDetails().getPolicyFindingDetails().getPolicyTopicEntriesList().get(0).getEvidencesList().get(0).getDestinationNotWorking().getExpandedUrl();

                        String finalStr = error_type + ": " + expanded_url;
                        errorList.add(finalStr);
                    } else {
                        String topic = err.getDetails().getPolicyFindingDetails().getPolicyTopicEntries(0).getTopic();
                        String type = err.getDetails().getPolicyFindingDetails().getPolicyTopicEntries(0).getType().toString();
                        String culprit = err.getDetails().getPolicyFindingDetails().getPolicyTopicEntries(0).getEvidences(0).getTextList().getTexts(0);

                        String finalStr = topic + " " + type + ": " + culprit;
                        errorList.add(finalStr);
                    }
                } else if (err != null && err.getTrigger() != null) {
                    String finalStr = err.getMessage() + ": " + err.getTrigger().getStringValue();
                    errorList.add(finalStr);
                }
            });

            return errorList;
        }
    }
}
