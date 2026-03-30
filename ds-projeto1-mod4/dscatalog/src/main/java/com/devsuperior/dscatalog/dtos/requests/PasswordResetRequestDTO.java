package com.devsuperior.dscatalog.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record PasswordResetRequestDTO(
        @NotBlank(message = "{validation.constraints.not-blank}")
        String token,
        @NotBlank(message = "{validation.constraints.not-blank}")
        @Size(min = 8, message = "Deve ter no mínimo 8 caracteres")
        String password
) {

}