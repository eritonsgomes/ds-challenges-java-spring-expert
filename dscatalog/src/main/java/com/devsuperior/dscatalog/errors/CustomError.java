package com.devsuperior.dscatalog.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@AllArgsConstructor
@Getter
public class CustomError {

    private final Instant timestamp;
    private final Integer status;
    private final String error;
    private final String path;

}
