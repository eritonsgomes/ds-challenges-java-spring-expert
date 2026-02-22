package com.devsuperior.dscatalog.dtos.requests;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CategoryRequestDTO(
        Long id,
        String name
) {
}
