package com.nlp.summarization;

import com.nlp.summarization.dto.SummaryResponse;

import org.junit.jupiter.api.Test;

import tools.jackson.databind.json.JsonMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SummaryControllerIT {

    @Test
    void shouldSummarizeArabicText() throws Exception {

        HttpClient client = HttpClient.newHttpClient();

        String json = """
                {
                    "text": "أعلنت الحكومة المغربية عن مشروع جديد لتطوير التعليم."
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()

                .uri(URI.create(
                        "http://localhost:8080/api/summarize"
                ))

                .timeout(Duration.ofSeconds(70))

                .header(
                        "Content-Type",
                        "application/json; charset=utf-8"
                )

                .POST(
                        HttpRequest.BodyPublishers.ofString(
                                json,
                                StandardCharsets.UTF_8
                        )
                )

                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString(
                        StandardCharsets.UTF_8
                )
        );

        assertEquals(200, response.statusCode());

        JsonMapper mapper = JsonMapper.builder().build();

        SummaryResponse result = mapper.readValue(
                response.body(),
                SummaryResponse.class
        );

        assertEquals("success", result.status());

        assertEquals("هذا ملخص تجريبي", result.summary());

        assertEquals(
                List.of("هذا", "ملخص", "تجريبي", "<EOS>"),
                result.generatedTokens()
        );

        assertNotNull(result.metadata());

        assertTrue(
                result.metadata().processingTimeSeconds() >= 0
        );

        System.out.println("HTTP: " + response.statusCode());

        System.out.println("JSON: " + response.body());

        System.out.println("End-to-end test successful");
    }
}