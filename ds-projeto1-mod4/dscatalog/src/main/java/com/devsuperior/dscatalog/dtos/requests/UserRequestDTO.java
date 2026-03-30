package com.devsuperior.dscatalog.dtos.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class UserRequestDTO {

    protected Long id;
    @NotBlank(message = "{validation.constraints.not-blank}")
    @Size(min = 1, max = 100, message = "{validation.constraints.size}")
    protected String firstName;
    @NotBlank(message = "{validation.constraints.not-blank}")
    @Size(min = 1, max = 50, message = "{validation.constraints.size}")
    protected String lastName;
    @Size(min = 9, max = 11, message = "{validation.constraints.size}")
    protected String phone;
    @JsonFormat(shape =  JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    protected LocalDate birthDate;
    @NotBlank(message = "{validation.constraints.not-blank}")
    @Email(message = "{validation.constraints.email}")
    protected String email;

}
