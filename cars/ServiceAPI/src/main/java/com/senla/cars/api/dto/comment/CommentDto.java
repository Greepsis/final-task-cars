package com.senla.cars.api.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.senla.cars.api.dto.ad.AdDto;
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
public class CommentDto {
    private Integer id;
    @NotNull
    private AdDto ad;
    @NotNull
    private UserDto user;
    @NotBlank
    private String comment;
    @JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
    private LocalDateTime date;
}
