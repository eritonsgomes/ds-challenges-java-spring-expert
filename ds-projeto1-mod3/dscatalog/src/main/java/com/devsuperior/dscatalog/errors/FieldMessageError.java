package com.devsuperior.dscatalog.errors;


import lombok.Builder;

@Builder
public record FieldMessageError(String fieldName, String message) {

}
