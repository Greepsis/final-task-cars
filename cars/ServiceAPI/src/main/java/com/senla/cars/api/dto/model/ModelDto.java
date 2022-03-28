package com.senla.cars.api.dto.model;

import com.senla.cars.api.dto.brand.BrandDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModelDto {
    private Integer id;
    @NotBlank
    private String name;
    @NotNull
    private BrandDto brand;
}
