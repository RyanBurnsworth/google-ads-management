package com.addyai.repository;

import com.addyai.repos.campaigns.CampaignRepository;
import com.addyai.repos.campaigns.impl.CampaignRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = CampaignRepositoryImpl.class)
public class CampaignRepositoryTest {
    @Autowired
    private CampaignRepository campaignRepository;


}
