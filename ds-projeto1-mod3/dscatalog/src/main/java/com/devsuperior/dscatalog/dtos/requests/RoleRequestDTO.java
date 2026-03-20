package com.devsuperior.dscatalog.dtos.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record RoleRequestDTO(
        Long id,
        String authority
) {
}
