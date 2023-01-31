package com.addyai.repos.conversion;

import com.addyai.models.ConversionDetails;

import java.util.List;

public interface ConversionRepository {
    List<ConversionDetails> getConversionDetails(String customerId) throws Exception;
}
