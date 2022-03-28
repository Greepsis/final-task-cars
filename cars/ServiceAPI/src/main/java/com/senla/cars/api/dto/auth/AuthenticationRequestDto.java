package com.senla.cars.api.dto.auth;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class AuthenticationRequestDto {
    @Email
    private String email;
    @NotBlank
    @Size(min = 8, max = 16)
    private String password;
}
