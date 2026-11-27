package com.nlp.summarization.controller;

import com.nlp.summarization.dto.SummaryRequest;
import com.nlp.summarization.dto.SummaryResponse;
import com.nlp.summarization.service.SummaryService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
public class SummaryController {

    private final SummaryService summaryService;

    public SummaryController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @PostMapping("/summarize")
    public SummaryResponse summarize(
            @Valid @RequestBody SummaryRequest request
    ) {

        return summaryService.summarize(request);

    }
}