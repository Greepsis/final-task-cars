package com.senla.cars.api.dto.ad;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.senla.cars.api.dto.model.ModelDto;
import com.senla.cars.api.dto.user.UserDto;
import com.senla.cars.api.utils.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdDto {
    private Integer id;
    @NotNull
    private UserDto user;
    @NotBlank
    private String sellerName;
    @NotBlank
    private String mobilePhone;
    @NotNull
    private ModelDto model;
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
    @JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
    private LocalDateTime publicationDateTime;

    private boolean deactivated;
}
