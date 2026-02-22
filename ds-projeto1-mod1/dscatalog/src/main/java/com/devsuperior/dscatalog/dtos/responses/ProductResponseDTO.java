package com.devsuperior.dscatalog.dtos.responses;

import lombok.Builder;
import lombok.NonNull;

import java.time.Instant;
import java.util.Set;

@Builder
public record ProductResponseDTO (
    Long id, String name, String description, Double price, String imgURL,
    @NonNull Set<CategoryResponseDTO> categories,
    Instant date
) {
}
