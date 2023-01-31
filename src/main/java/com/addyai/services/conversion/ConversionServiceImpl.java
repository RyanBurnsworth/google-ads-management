package com.addyai.services.conversion;

import com.addyai.models.ConversionDetails;
import com.addyai.repos.conversion.ConversionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversionServiceImpl implements ConversionService {
    private final ConversionRepository conversionRepository;

    public ConversionServiceImpl(ConversionRepository conversionRepository) {
        this.conversionRepository = conversionRepository;
    }

    @Override
    public List<ConversionDetails> fetchConversionDetails(String customerId) throws Exception {
        return conversionRepository.getConversionDetails(customerId);
    }
}
