package com.nlp.summarization.service;

import com.nlp.summarization.dto.SummaryRequest;
import com.nlp.summarization.dto.SummaryResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.server.ResponseStatusException;

import java.net.SocketTimeoutException;

@Service
public class SummaryService {

    private final RestClient restClient;

    public SummaryService(
            @Value("${model.api.url}") String modelApiUrl,
            @Value("${model.api.read-timeout-ms:60000}") int readTimeout
    ) {

        SimpleClientHttpRequestFactory factory =
                new SimpleClientHttpRequestFactory();

        factory.setConnectTimeout(3000);
        factory.setReadTimeout(readTimeout);

        this.restClient = RestClient.builder()
                .baseUrl(modelApiUrl)
                .requestFactory(factory)
                .build();
    }

    public SummaryResponse summarize(SummaryRequest request) {

        SummaryResponse response;

        try {

            response = restClient.post()
                    .uri("/api/v1/summarize")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(SummaryResponse.class);

        } catch (RestClientException e) {

            if (e.contains(SocketTimeoutException.class)) {

                throw new ResponseStatusException(
                        HttpStatus.GATEWAY_TIMEOUT,
                        "Le service NLP met trop de temps à répondre.",
                        e
                );
            }

            if (e instanceof ResourceAccessException) {

                throw new ResponseStatusException(
                        HttpStatus.SERVICE_UNAVAILABLE,
                        "Le service NLP est indisponible.",
                        e
                );
            }

            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Erreur de communication avec le service NLP.",
                    e
            );
        }

        if (
                response == null ||
                !"success".equals(response.status()) ||
                response.summary() == null ||
                response.summary().isBlank() ||
                response.generatedTokens() == null ||
                response.generatedTokens().isEmpty() ||
                response.metadata() == null ||
                response.metadata().processingTimeSeconds() == null ||
                !Double.isFinite(
                    response.metadata().processingTimeSeconds()
                ) ||
                response.metadata().processingTimeSeconds() < 0
        ) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Réponse invalide du service NLP."
            );
        }

        return response;
    }
}