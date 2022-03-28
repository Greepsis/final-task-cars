package com.senla.cars.api.dto.ad;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationAdDto {
    @NotBlank
    private String sellerName;
    @NotBlank
    private String mobilePhone;
    @NotBlank
    private String modelName;
    @NotNull
    private Integer yearIssue;
    @NotNull
    private Integer mileage;
    @NotNull
    private Double engineVolume;
    @NotBlank
    private String engineType;
    @NotBlank
    private String transmission;
    @NotBlank
    private String region;
    @NotNull
    private boolean customsClearance;
    @NotNull
    private boolean exchange;
    @NotNull
    private Integer price;
}
