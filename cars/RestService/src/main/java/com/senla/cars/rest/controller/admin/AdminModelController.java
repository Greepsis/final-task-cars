package com.senla.cars.rest.controller.admin;

import com.senla.cars.api.dto.ResponseDto;
import com.senla.cars.api.dto.model.AddModelDto;
import com.senla.cars.api.dto.model.ModelDto;
import com.senla.cars.api.dto.model.UpdateModelDto;
import com.senla.cars.api.service.ModelService;
import com.senla.cars.api.specification.SearchCriteria;
import com.senla.cars.rest.enums.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("api/admin/model")
@RequiredArgsConstructor
@Validated
@Tag(name = "Admin model controller", description = "Allows admins to add car models")
public class AdminModelController {

    private final ModelService modelService;

    @PostMapping
    @Operation(
            summary = "Add new car model"
    )
    public ResponseEntity<ResponseDto<AddModelDto>> addModel(@RequestBody AddModelDto addModelDto){
        modelService.addModel(addModelDto);
        return new ResponseEntity<>(new ResponseDto<>(true, addModelDto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
            summary = "View,search,filtering models"
    )
    public ResponseEntity<ResponseDto<Page<ModelDto>>> getModel(@RequestBody List<SearchCriteria> searchCriteria,
                                                                @PageableDefault(sort = "id", direction = Sort.Direction.ASC,
                                                                  page = 0, size = 10) Pageable pageable){
        final Page<ModelDto> page = modelService.getModel(pageable,searchCriteria);
        return ResponseEntity.ok(new ResponseDto<>(true , page));
    }

    @PutMapping
    @Operation(
            summary = "Update model"
    )
    public ResponseEntity<ResponseDto<ModelDto>> update(@RequestBody UpdateModelDto updateDto){
        return new ResponseEntity<>(new ResponseDto<>(true,modelService.updateModel(updateDto)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete model"
    )
    public ResponseEntity<ResponseDto<String>> delete(@PathVariable @NotNull Integer id){
        modelService.deleteModel(id);
        return new ResponseEntity<>(new ResponseDto<>(true,"Model:" + id + " can be deleted",
                Response.NO_ERROR.getText()),HttpStatus.OK);
    }
}
