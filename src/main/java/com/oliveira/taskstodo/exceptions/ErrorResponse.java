package com.oliveira.taskstodo.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor // constructor for all - final variables
@JsonInclude(JsonInclude.Include.NON_NULL) // for this vars don't show trace null in navegator
public class ErrorResponse {
    private final int status;
    private final String message;
    //in production is not goot to show this trace for security datas
    private String stackTrace;
    private List<ValidationError> erros;

    @Getter
    @Setter
    @RequiredArgsConstructor
    private static class ValidationError{
        private final String field;
        private final String message;

    }

    public void addValidationError(String field, String message){
        if (Objects.isNull(erros)) {
            this.erros = new ArrayList<>();

        }
        this.erros.add(new ValidationError(field, message));
    }
}
