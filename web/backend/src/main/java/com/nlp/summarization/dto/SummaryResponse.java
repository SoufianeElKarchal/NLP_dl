package com.nlp.summarization.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SummaryResponse(

    String status,

    String summary,

    @JsonProperty("generated_tokens")
    List<String> generatedTokens,

    Metadata metadata

) {

    public record Metadata(

        @JsonProperty("processing_time_seconds")
        Double processingTimeSeconds

    ) {}

}