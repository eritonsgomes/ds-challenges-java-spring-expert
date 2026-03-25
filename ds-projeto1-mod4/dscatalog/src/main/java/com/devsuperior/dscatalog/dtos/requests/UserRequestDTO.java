package com.devsuperior.dscatalog.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.Set;

@Builder
public record UserRequestDTO(
    Long id,
    @NotBlank(message = "{validation.constraints.not-blank}")
    @Size(min = 1, max = 100, message = "{validation.constraints.size}")
    String firstName,
    @NotBlank(message = "{validation.constraints.not-blank}")
    @Size(min = 1, max = 50, message = "{validation.constraints.size}")
    String lastName,
    @NotBlank(message = "{validation.constraints.not-blank}")
    @Email(message = "{validation.constraints.email}")
    String email,
    @Size(min = 1, max = 100)
    String password,
    @NotEmpty(message = "{validation.constraints.not-empty}")
    Set<RoleRequestDTO> roles
) {

}
