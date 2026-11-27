package com.nlp.summarization;

import com.nlp.summarization.dto.SummaryRequest;
import com.nlp.summarization.dto.SummaryResponse;
import com.nlp.summarization.service.SummaryService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
class SummaryServiceIT {

    @Autowired
    private SummaryService summaryService;

    @Test
    void shouldCommunicateWithFastApi() {

        SummaryRequest request = new SummaryRequest(
                "أعلنت الحكومة المغربية عن مشروع جديد لتطوير التعليم."
        );

        SummaryResponse response =
                summaryService.summarize(request);

        assertNotNull(response);

        assertEquals("success", response.status());

        assertNotNull(response.summary());

        assertFalse(response.summary().isBlank());
        assertEquals(
            "هذا ملخص تجريبي",
            response.summary()
        );

        assertEquals(
            "هذا",
            response.generatedTokens().get(0)
        );
        assertTrue(
            response.summary()
                .codePoints()
                .anyMatch(c -> c >= 0x0600 && c <= 0x06FF),
            "Le resume doit contenir des caracteres arabes"
        );
        assertNotNull(response.generatedTokens());

        assertFalse(response.generatedTokens().isEmpty());

        assertNotNull(response.metadata());

        assertTrue(
                response.metadata().processingTimeSeconds() >= 0
        );

        System.out.println("Status: " + response.status());

        System.out.println("Summary: " + response.summary());

        System.out.println("Tokens: " + response.generatedTokens());

        System.out.println(
                "Processing time: "
                + response.metadata().processingTimeSeconds()
        );
    }
}