package com.devsuperior.bds04.errors;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class ValidationError extends CustomError {

    private final List<FieldMessageError> errors = new ArrayList<>();

    public ValidationError(Instant timestamp, Integer status, String error, String path) {
        super(timestamp, status, error, path);
    }

    public void addError(String fieldName, String message) {
        errors.add(new FieldMessageError(fieldName, message));
    }

    public List<FieldMessageError> getErrors() {
        return errors;
    }

}
