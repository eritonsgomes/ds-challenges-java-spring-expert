package com.devsuperior.dscatalog.dtos.responses;

import lombok.Builder;

import java.util.Set;

@Builder
public record UserResponseDTO(
    Long id,
    String firstName,
    String lastName,
    String email,
    Set<RoleResponseDTO> roles
) {

}
