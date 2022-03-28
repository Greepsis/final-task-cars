package com.senla.cars.api.dto.auth;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDto {
    @Email
    private String email;
    @NotBlank
    private String token;
}
