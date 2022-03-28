package com.senla.cars.api.service;

import com.senla.cars.api.dto.ad.AdDto;
import com.senla.cars.api.dto.ad.RegistrationAdDto;
import com.senla.cars.api.specification.SearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdService {
    void addAd(RegistrationAdDto registrationAdDto);
    AdDto findAdById(Integer id);
    AdDto deactivationAd(Integer id);
    Page<AdDto> getAd(Pageable pageable, List<SearchCriteria> searchCriteria);
    Page<AdDto> getUserAd(Pageable pageable);
    AdDto getAdByModelName(String name);
    AdDto updateAd(RegistrationAdDto registrationAdDto, Integer id);
    public AdDto updatePublicationDate(Integer id);
}
