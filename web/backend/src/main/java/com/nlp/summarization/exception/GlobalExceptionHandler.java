package com.nlp.summarization.exception;

import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation() {

        return ResponseEntity.badRequest().body(
                Map.of(
                        "status", "error",
                        "code", 400,
                        "message", "Le texte ne doit pas être vide."
                )
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidJson() {

        return ResponseEntity.badRequest().body(
                Map.of(
                        "status", "error",
                        "code", 400,
                        "message", "Le format JSON est invalide."
                )
        );
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleServiceError(
            ResponseStatusException e
    ) {

        return ResponseEntity.status(e.getStatusCode()).body(
                Map.of(
                        "status", "error",
                        "code", e.getStatusCode().value(),
                        "message", e.getReason() == null
                                ? "Une erreur est survenue."
                                : e.getReason()
                )
        );
    }
}