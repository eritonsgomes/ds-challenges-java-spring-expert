package com.devsuperior.dscatalog.dtos.requests;

import lombok.Builder;
import lombok.NonNull;

import java.time.Instant;
import java.util.Set;

@Builder
public record ProductRequestDTO(
        String name,
        String description,
        Double price,
        String imgURL,
        @NonNull Set<CategoryRequestDTO> categories,
        Instant date
) {

}
