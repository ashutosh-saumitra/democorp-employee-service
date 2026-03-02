package com.democorp.employee.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.FieldError;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
public class ApiError {

    private OffsetDateTime timeStamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<FieldErrorItem> fieldErrors;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldErrorItem{
        private String field;
        private String message;
    }
}
