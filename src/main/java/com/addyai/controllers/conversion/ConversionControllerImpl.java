package com.addyai.controllers.conversion;

import com.addyai.models.ConversionDetails;
import com.addyai.repos.conversion.ConversionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/{customerId}/conversions")
public class ConversionControllerImpl implements ConversionController {
    private final ConversionRepository conversionRepository;

    public ConversionControllerImpl(ConversionRepository conversionRepository) {
        this.conversionRepository = conversionRepository;
    }

    @Override
    @GetMapping("/details")
    public ResponseEntity<List<ConversionDetails>> getConversionDetails(@PathVariable String customerId) throws Exception {
        List<ConversionDetails> conversionDetailsList = conversionRepository.getConversionDetails(customerId);
        return new ResponseEntity<>(conversionDetailsList, HttpStatus.OK);
    }
}
