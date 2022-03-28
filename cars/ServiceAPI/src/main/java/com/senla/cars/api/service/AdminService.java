package com.senla.cars.api.service;

import com.senla.cars.api.dto.ad.AdDto;
import com.senla.cars.api.dto.ad.RegistrationAdDto;
import com.senla.cars.api.dto.user.UserDto;
import com.senla.cars.api.specification.SearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminService {
    void blockingUser(String email);
    UserDto updateRole(Integer id);
    Page<UserDto> getUser(Pageable pageable, List<SearchCriteria> searchCriteria);
    AdDto updateAd(RegistrationAdDto registrationAdDto, Integer id);
}
