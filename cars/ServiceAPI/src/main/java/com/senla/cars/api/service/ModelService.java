package com.senla.cars.api.service;

import com.senla.cars.api.dto.model.AddModelDto;
import com.senla.cars.api.dto.model.ModelDto;
import com.senla.cars.api.dto.model.UpdateModelDto;
import com.senla.cars.api.specification.SearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ModelService {
    void addModel(AddModelDto addModelDto);
    ModelDto findModelByName(String modelName);
    Page<ModelDto> getModel(Pageable pageable, List<SearchCriteria> searchCriteria);
    ModelDto updateModel(UpdateModelDto updateDto);
    void deleteModel(Integer id);
}
