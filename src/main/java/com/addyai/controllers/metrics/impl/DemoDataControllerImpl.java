package com.addyai.controllers.metrics.impl;

import com.addyai.controllers.metrics.DemoDataController;
import com.addyai.utils.DemoDataGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/demo/generator")
public class DemoDataControllerImpl implements DemoDataController {
    @Override
    @GetMapping("/campaign")
    public ResponseEntity<List<DemoDataGenerator>> getDemoCampaignData() {
        List<DemoDataGenerator> demoDataGenerators = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            demoDataGenerators.add(DemoDataGenerator.getRandomCampaignObject("19615829686", "Test Campaign Refactor 2022-1004"));
        }

        return new ResponseEntity<>(demoDataGenerators, HttpStatus.OK);
    }
}
