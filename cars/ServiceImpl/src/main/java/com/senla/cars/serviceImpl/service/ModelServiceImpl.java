package com.senla.cars.serviceImpl.service;

import com.senla.cars.api.dto.brand.BrandDto;
import com.senla.cars.api.dto.model.AddModelDto;
import com.senla.cars.api.dto.model.ModelDto;
import com.senla.cars.api.dto.model.UpdateModelDto;
import com.senla.cars.api.service.BrandService;
import com.senla.cars.api.service.ModelService;
import com.senla.cars.api.specification.GenericSpecification;
import com.senla.cars.api.specification.SearchCriteria;
import com.senla.cars.serviceImpl.enums.ExceptionEnums;
import com.senla.cars.serviceImpl.exception.NotEmptyException;
import com.senla.cars.serviceImpl.exception.NotFoundException;
import com.senla.cars.serviceImpl.exception.constant.ExceptionConstants;
import com.senla.cars.serviceImpl.model.Model;
import com.senla.cars.serviceImpl.repository.ModelRepository;
import com.senla.cars.serviceImpl.utils.Convertor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ModelServiceImpl implements ModelService {
    private final ModelRepository modelRepository;
    private final ModelMapper modelMapper;
    private final BrandService brandService;
    private final Convertor convertor;

    @Override
    public void addModel(AddModelDto addModelDto) {
        BrandDto brandDto = brandService.findBrandByName(addModelDto.getNameBrand());
        if (brandDto == null){
            throw new NotFoundException(ExceptionEnums.BRAND.getText() + ExceptionConstants.NOT_FOUND);
        }
        ModelDto modelDto = new ModelDto();
        modelDto.setName(addModelDto.getNameModel());
        modelDto.setBrand(brandDto);
        modelRepository.save(modelMapper.map(modelDto,Model.class));
        log.info("The model:{} has been added and linked to the:{} brand",
                modelDto.getName(), modelDto.getBrand().getName());
    }

    @Override
    public ModelDto findModelByName(String name) {
        if (name == null){
            throw new NotEmptyException(ExceptionEnums.MODEL_NAME.getText() + ExceptionConstants.IS_EMPTY);
        }
        Model model = modelRepository.findModelByName(name);
        if (Objects.isNull(model)){
            throw new NotFoundException(ExceptionEnums.MODEL.getText() + ExceptionConstants.NOT_FOUND);
        }else {
            return modelMapper.map(model,ModelDto.class);
        }
    }

    @Override
    public ModelDto updateModel(UpdateModelDto updateDto){
        if (updateDto.getId() == null){
            throw new NotEmptyException(ExceptionEnums.MODEL_ID.getText() + ExceptionConstants.IS_EMPTY);
        }
        ModelDto modelDto = modelMapper.map(findModelById(updateDto.getId()),ModelDto.class);
        modelDto.setName(updateDto.getName());
        modelRepository.save(modelMapper.map(modelDto,Model.class));
        log.info("The model name has been changed to:{}", updateDto.getName());
        return modelDto;
    }

    @Override
    public void deleteModel(Integer id){
        modelRepository.deleteById(id);
        log.info("The model with ID:{} has been deleted",id);
    }

    private Model findModelById(Integer id){
        return modelRepository.findById(id).orElseThrow(() ->
                new NotFoundException(ExceptionEnums.MODEL.getText() + ExceptionConstants.NOT_FOUND));
    }

    @Override
    public Page<ModelDto> getModel(Pageable pageable, List<SearchCriteria> searchCriteria) {
        if (searchCriteria.isEmpty()){
            throw new NotEmptyException(ExceptionEnums.SEARCH_CRITERIA.getText() + ExceptionConstants.IS_EMPTY);
        }
        GenericSpecification<Model> genericSpecification = new GenericSpecification<>();
        searchCriteria.stream().map(searchCriterion -> new SearchCriteria(searchCriterion.getKey(),
                searchCriterion.getValue(), searchCriterion.getOperation())).forEach(genericSpecification::add);
        Page<Model> models = modelRepository.findAll(genericSpecification,pageable);
        return convertor.mapEntityPageIntoDtoPage(models, ModelDto.class);
    }
}

