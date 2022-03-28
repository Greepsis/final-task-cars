package com.senla.cars.api.service;

import com.senla.cars.api.dto.brand.BrandDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BrandService {
    void addBrand(BrandDto brandDto);
    Page<BrandDto> getAllBrands(Pageable pageable);
    BrandDto updateBrand(BrandDto updateBrandDto);
    void deleteBrand(Integer id);
    BrandDto findBrandByName(String name);
}
