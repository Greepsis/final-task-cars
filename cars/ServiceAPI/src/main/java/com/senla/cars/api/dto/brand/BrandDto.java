package com.senla.cars.api.dto.brand;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrandDto {
    private Integer id;
    @NotBlank
    private String name;
}
