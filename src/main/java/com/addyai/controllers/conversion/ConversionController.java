package com.addyai.controllers.conversion;

import com.addyai.models.ConversionDetails;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ConversionController {
    ResponseEntity<List<ConversionDetails>> getConversionDetails(String customerId) throws Exception;
}
