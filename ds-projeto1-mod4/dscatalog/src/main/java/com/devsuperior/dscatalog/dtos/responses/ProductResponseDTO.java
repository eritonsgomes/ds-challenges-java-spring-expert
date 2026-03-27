package com.devsuperior.dscatalog.dtos.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.NonNull;

import java.time.Instant;
import java.util.Set;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductResponseDTO (
    Long id, String name, String description, Double price, String imgURL,
    Set<CategoryResponseDTO> categories,
    Instant date
) {
}
