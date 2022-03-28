package com.senla.cars.api.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.senla.cars.api.utils.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Integer id;
    @Email
    @Size(min = 5, max = 50)
    private String email;
    @NotBlank
    private String roles;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    private String password;
    @JsonFormat(pattern = DateUtil.DATE_PATTERN)
    private LocalDate blocking;
    @JsonFormat(pattern = DateUtil.DATE_PATTERN)
    private LocalDate deletion;
}


