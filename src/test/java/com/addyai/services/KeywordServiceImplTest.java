package com.addyai.services;

import com.addyai.builder.OperationBuilder;
import com.addyai.enums.OperationType;
import com.addyai.models.KeywordDetails;
import com.addyai.repos.keyword.KeywordRepository;
import com.addyai.services.keyword.KeywordService;
import com.addyai.services.keyword.impl.KeywordServiceImpl;
import com.addyai.utils.TestUtils;
import com.google.ads.googleads.v12.services.AdGroupCriterionOperation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = KeywordServiceImpl.class)
public class KeywordServiceImplTest {
    private static final long CUSTOMER_ID = 1L;

    @Autowired
    private KeywordService keywordService;

    @MockBean
    private KeywordRepository keywordRepository;

    @MockBean
    private OperationBuilder operationBuilder;

    private final TestUtils testUtils = new TestUtils();

    @Test
    void testShouldCreateKeywordsSuccessfully() throws Exception {
        List<String> resourceNameList = new ArrayList<>();
        List<KeywordDetails> keywordDetails = testUtils.getMockKeywordDetails();
        List<AdGroupCriterionOperation> adGroupCriterionOperations
                = testUtils.getMockAdGroupCriterionOperationsList();

        when(operationBuilder.buildAdGroupCriterionOperationList(keywordDetails, OperationType.CREATE))
                .thenReturn(adGroupCriterionOperations);

        when(keywordRepository.performKeywordOperations(CUSTOMER_ID, adGroupCriterionOperations))
                .thenReturn(resourceNameList);

        keywordService.upsertKeywords(CUSTOMER_ID, keywordDetails, true);

        verify(keywordRepository, times(1))
                .performKeywordOperations(CUSTOMER_ID, adGroupCriterionOperations);
    }
}
