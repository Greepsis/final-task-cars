package com.senla.cars.api.service;

import com.senla.cars.api.dto.auth.AuthenticationResponseDto;
import com.senla.cars.api.dto.user.RegistrationDto;
import com.senla.cars.api.dto.user.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface UserService {
    void addUser(RegistrationDto userDto);
    boolean isExist(String email);
    void deleteUser();
    Page<UserDto> getAllUsers(Pageable pageable);
    UserDto findUserByEmail(String email);
    AuthenticationResponseDto authenticationUser(String email);

}
