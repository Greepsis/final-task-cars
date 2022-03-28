package com.senla.cars.api.dto.bookmark;

import com.senla.cars.api.dto.ad.AdDto;
import com.senla.cars.api.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkDto {
    @NotNull
    private UserDto user;
    @NotNull
    private AdDto ad;
}
