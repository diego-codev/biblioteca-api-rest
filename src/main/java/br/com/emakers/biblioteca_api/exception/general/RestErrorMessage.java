package br.com.emakers.biblioteca_api.exception.general;

import java.time.Instant;
import java.util.List;
import org.springframework.http.HttpStatus;

public record RestErrorMessage(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        List<FieldErrorDetail> fieldErrors
) {
    public static RestErrorMessage of(HttpStatus status, String message, String path) {
        return new RestErrorMessage(Instant.now(), status.value(), status.getReasonPhrase(), message, path, null);
    }
    public static RestErrorMessage ofValidation(HttpStatus status, String message, String path, List<FieldErrorDetail> fieldErrors) {
        return new RestErrorMessage(Instant.now(), status.value(), status.getReasonPhrase(), message, path, fieldErrors);
    }
    public record FieldErrorDetail(String field, String message) {}
}
