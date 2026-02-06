package com.devsuperior.dscatalog.dtos.responses;

import lombok.Builder;

@Builder
public record CategoryResponseDTO(Long id, String name) {
}
