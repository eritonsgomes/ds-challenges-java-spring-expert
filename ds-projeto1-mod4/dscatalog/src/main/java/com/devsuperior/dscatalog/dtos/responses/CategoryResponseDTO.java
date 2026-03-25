package com.devsuperior.dscatalog.dtos.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record CategoryResponseDTO(Long id, String name) {
}
