package com.devsuperior.dscatalog.dtos.requests;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UpdateUserRequestDTO extends UserRequestDTO {

    @NotEmpty(message = "{validation.constraints.not-empty}")
    Set<RoleRequestDTO> roles;

}