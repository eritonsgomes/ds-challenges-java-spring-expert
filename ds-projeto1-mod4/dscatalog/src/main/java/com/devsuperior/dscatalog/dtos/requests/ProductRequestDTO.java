package com.devsuperior.dscatalog.dtos.requests;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.time.Instant;
import java.util.Set;

@Builder
public record ProductRequestDTO(
        String name,
        String description,
        Double price,
        String imgURL,
        @NotEmpty
        Set<CategoryRequestDTO> categories,
        Instant date
) {

}
