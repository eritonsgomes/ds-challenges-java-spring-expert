package com.devsuperior.dscatalog.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CreateUserRequestDTO extends UserRequestDTO {

    @NotBlank(message = "{validation.constraints.not-blank}")
    @Size(min = 8, message = "Deve ter no mínimo 8 caracteres")
    private String password;

}