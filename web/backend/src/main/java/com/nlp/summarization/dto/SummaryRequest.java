package com.nlp.summarization.dto;

import jakarta.validation.constraints.NotBlank;

public record SummaryRequest(

    @NotBlank(message = "Le texte ne doit pas être vide.")
    String text

) {}