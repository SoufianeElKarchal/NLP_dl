package com.nlp.summarization;

import com.nlp.summarization.dto.SummaryRequest;
import com.nlp.summarization.dto.SummaryResponse;

import tools.jackson.databind.json.JsonMapper;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SummaryDtoTest {

    private final JsonMapper mapper =
            JsonMapper.builder().build();

    @Test
    void shouldSerializeRequest() {

        SummaryRequest request =
                new SummaryRequest("هذا نص عربي");

        String json = mapper.writeValueAsString(request);

        assertEquals(
                "{\"text\":\"هذا نص عربي\"}",
                json
        );
    }

    @Test
    void shouldDeserializeResponse() {

        String json = """
                {
                    "status": "success",
                    "summary": "هذا ملخص تجريبي",
                    "generated_tokens": [
                        "هذا", "ملخص", "تجريبي", "<EOS>"
                    ],
                    "metadata": {
                        "processing_time_seconds": 1.001
                    }
                }
                """;

        SummaryResponse response =
                mapper.readValue(json, SummaryResponse.class);

        assertEquals("success", response.status());

        assertEquals(
                "هذا ملخص تجريبي",
                response.summary()
        );

        assertEquals(4, response.generatedTokens().size());

        assertEquals(
                "<EOS>",
                response.generatedTokens().get(3)
        );

        assertEquals(
                1.001,
                response.metadata().processingTimeSeconds(),
                0.000001
        );
    }

    @Test
    void shouldRejectBlankText() {

        try (ValidatorFactory factory =
                     Validation.buildDefaultValidatorFactory()) {

            var validator = factory.getValidator();

            assertFalse(
                    validator.validate(
                            new SummaryRequest("")
                    ).isEmpty()
            );

            assertTrue(
                    validator.validate(
                            new SummaryRequest("هذا نص عربي")
                    ).isEmpty()
            );
        }
    }
}