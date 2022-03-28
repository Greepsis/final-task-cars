package com.senla.cars.api.dto.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddModelDto {
    @NotBlank
    private String nameModel;
    @NotBlank
    private String nameBrand;
}
