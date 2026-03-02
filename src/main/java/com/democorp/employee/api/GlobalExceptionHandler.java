package com.democorp.employee.api;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.context.request.WebRequest;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 400 — Bean validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, WebRequest request) {
        List<ApiError.FieldErrorItem> fieldErrors = ex.getBindingResult()
                .getFieldErrors().stream()
                .map(fe -> new ApiError.FieldErrorItem(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());

        ApiError body = baseError(HttpStatus.BAD_REQUEST, "Validation failed",
                ex.getMessage(), request, fieldErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // Propagated ResponseStatusException from service (e.g., 404, 409)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleResponseStatus(ResponseStatusException ex, WebRequest request) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        ApiError body = baseError(status, status.getReasonPhrase(),
                ex.getReason(), request, null);
        return ResponseEntity.status(status).body(body);
    }

    // 409 — Typically DB unique constraint, FK constraint, etc.
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException ex, WebRequest request) {
        ApiError body = baseError(HttpStatus.CONFLICT, "Data integrity violation",
                rootMessage(ex), request, null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    // Fallback (avoid leaking 500 without context)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, WebRequest request) {
        ApiError body = baseError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
                rootMessage(ex), request, null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    private ApiError baseError(HttpStatus status, String error, @Nullable String message,
                               WebRequest request, @Nullable List<ApiError.FieldErrorItem> fields) {
        ApiError e = new ApiError();
        e.setTimeStamp(OffsetDateTime.now());
        e.setStatus(status.value());
        e.setError(error);
        e.setMessage(message);
        e.setPath(request.getDescription(false).replace("uri=", ""));
        e.setFieldErrors(fields);
        return e;
    }

    private String rootMessage(Throwable t) {
        Throwable r = t;
        while (r.getCause() != null) r = r.getCause();
        return r.getMessage();
    }
}