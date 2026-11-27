package com.nlp.summarization;

import com.nlp.summarization.dto.SummaryRequest;
import com.nlp.summarization.dto.SummaryResponse;
import com.nlp.summarization.service.SummaryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class SummaryControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private SummaryService summaryService;

    private MockMvc mockMvc;

    private final JsonMapper mapper =
            JsonMapper.builder().build();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    void shouldReturnSummary() throws Exception {

        String text = "هذا نص باللغة العربية";

        SummaryRequest request =
                new SummaryRequest(text);

        SummaryResponse response = new SummaryResponse(
                "success",
                "هذا ملخص تجريبي",
                List.of("هذا", "ملخص", "تجريبي", "<EOS>"),
                new SummaryResponse.Metadata(1.0)
        );

        when(summaryService.summarize(request))
                .thenReturn(response);

        var result = mockMvc.perform(
                post("/api/summarize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)
                                .getBytes(StandardCharsets.UTF_8))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andReturn();

        String json = new String(
                result.getResponse().getContentAsByteArray(),
                StandardCharsets.UTF_8
        );

        SummaryResponse actual =
                mapper.readValue(json, SummaryResponse.class);

        assertEquals(response, actual);

        verify(summaryService).summarize(request);
    }

    @Test
    void shouldRejectInvalidTexts() throws Exception {

        List<String> invalidRequests = List.of(
                "{\"text\":\"\"}",
                "{\"text\":\"   \"}",
                "{\"text\":null}",
                "{}"
        );

        for (String json : invalidRequests) {

            mockMvc.perform(
                    post("/api/summarize")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
            )
                    .andExpect(status().isBadRequest());
        }

        verifyNoInteractions(summaryService);
    }

    @Test
    void shouldRejectMalformedJson() throws Exception {

        mockMvc.perform(
                post("/api/summarize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid json")
        )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(summaryService);
    }

    @Test
    void shouldRejectGetMethod() throws Exception {

        mockMvc.perform(get("/api/summarize"))
                .andExpect(status().isMethodNotAllowed());

        verifyNoInteractions(summaryService);
    }
    @Test
void shouldReturn503WhenPythonIsUnavailable() throws Exception {

    when(summaryService.summarize(any(SummaryRequest.class)))
            .thenThrow(new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Le service NLP est indisponible."
            ));

    mockMvc.perform(
            post("/api/summarize")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"text\":\"Test\"}")
    )
            .andExpect(status().isServiceUnavailable())
            .andExpect(jsonPath("$.status").value("error"))
            .andExpect(jsonPath("$.code").value(503))
            .andExpect(jsonPath("$.message")
                    .value("Le service NLP est indisponible."));
}
@Test
void shouldReturn504WhenPythonIsTooSlow() throws Exception {

    when(summaryService.summarize(any(SummaryRequest.class)))
            .thenThrow(new ResponseStatusException(
                    HttpStatus.GATEWAY_TIMEOUT,
                    "Le service NLP met trop de temps à répondre."
            ));

    mockMvc.perform(
            post("/api/summarize")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"text\":\"Test\"}")
    )
            .andExpect(status().isGatewayTimeout())
            .andExpect(jsonPath("$.status").value("error"))
            .andExpect(jsonPath("$.code").value(504));
}
}