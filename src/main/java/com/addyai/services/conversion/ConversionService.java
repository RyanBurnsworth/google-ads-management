package com.addyai.services.conversion;

import com.addyai.models.ConversionDetails;

import java.util.List;

public interface ConversionService {
    List<ConversionDetails> fetchConversionDetails(String customerId) throws Exception;
}
