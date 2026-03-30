package com.devsuperior.dscatalog.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record PasswordRecoveryRequestDTO(
    @NotBlank(message = "{validation.constraints.not-blank}")
    @Email(message = "{validation.constraints.email}")
    String email
) {

}