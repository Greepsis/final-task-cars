package com.senla.cars.serviceImpl.service;

import com.senla.cars.api.dto.brand.BrandDto;
import com.senla.cars.api.service.BrandService;
import com.senla.cars.serviceImpl.enums.ExceptionEnums;
import com.senla.cars.serviceImpl.exception.NotEmptyException;
import com.senla.cars.serviceImpl.exception.NotFoundException;
import com.senla.cars.serviceImpl.exception.constant.ExceptionConstants;
import com.senla.cars.serviceImpl.model.Brand;
import com.senla.cars.serviceImpl.repository.BrandRepository;
import com.senla.cars.serviceImpl.utils.Convertor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final ModelMapper modelMapper;
    private final Convertor convert;

    @Override
    public void addBrand(BrandDto brandDto) {
        if (brandDto.getName() == null){
            throw new NotEmptyException(ExceptionEnums.BRAND_NAME.getText()+ ExceptionConstants.IS_EMPTY);
        }
        brandRepository.save(modelMapper.map(brandDto,Brand.class));
        log.info("Add brand: {}",brandDto.getName());
    }

    private Brand findBrandId(Integer id){
        return brandRepository.findById(id).orElseThrow(() ->
                new NotFoundException(ExceptionEnums.BRAND.getText() + ExceptionConstants.NOT_FOUND));
    }

    @Override
    public BrandDto findBrandByName(String name) {
        if (name == null){
            throw new NotEmptyException(ExceptionEnums.BRAND_NAME.getText()+ ExceptionConstants.IS_EMPTY);
        }
        return modelMapper.map(brandRepository.findBrandByName(name),BrandDto.class);
    }

    @Override
    public BrandDto updateBrand(BrandDto updateBrandDto) {
        if (updateBrandDto.getId() == null){
            throw new NotEmptyException(ExceptionEnums.BRAND_ID.getText() + ExceptionConstants.IS_EMPTY);
        }
        BrandDto brandDto = modelMapper.map(findBrandId(updateBrandDto.getId()),BrandDto.class);
        brandDto.setName(updateBrandDto.getName());
        brandRepository.save(modelMapper.map(brandDto,Brand.class));
        log.info("The brand name has been changed to:{}",brandDto.getName());
        return brandDto;
    }

    @Override
    public void deleteBrand(Integer id) {
        brandRepository.deleteById(id);
    }

    @Override
    public Page<BrandDto> getAllBrands(Pageable pageable) {
        Page<Brand> brands = brandRepository.findAll(pageable);
        return convert.mapEntityPageIntoDtoPage(brands, BrandDto.class);
    }


}
