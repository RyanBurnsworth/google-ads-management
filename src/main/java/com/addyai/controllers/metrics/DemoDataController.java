package com.addyai.controllers.metrics;

import com.addyai.utils.DemoDataGenerator;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DemoDataController {
    ResponseEntity<List<DemoDataGenerator>> getDemoCampaignData();
}
