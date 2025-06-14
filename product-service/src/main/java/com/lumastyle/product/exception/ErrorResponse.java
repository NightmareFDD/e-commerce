package com.lumastyle.product.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;

@Getter
@Builder
public class ErrorResponse {
    @Schema(description = "Timestamp when the error occurred", example = "2025-06-14T10:15:30")
    private Instant timestamp;
    @Schema(description = "HTTP status code", example = "400")
    private int status;
    private String error;
    @Schema(description = "General error message", example = "Validation failed")
    private String message;
    @Schema(description = "Optional field-level validation errors")
    private Map<String, String> errors; // Optional field errors
}
