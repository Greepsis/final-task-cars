package com.senla.cars.rest.controller.admin;

import com.senla.cars.api.dto.brand.BrandDto;
import com.senla.cars.api.dto.ResponseDto;
import com.senla.cars.api.service.BrandService;
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


@RestController
@RequestMapping("api/admin/brand")
@RequiredArgsConstructor
@Validated
@Tag(name = "Admin brand controller", description = "Allows admins to add car brands")
public class AdminBrandController {

    private final BrandService brandService;

    @PostMapping
    @Operation(
            summary = "Add new car brand"
    )
    public ResponseEntity<ResponseDto<BrandDto>> addBrand(@RequestBody BrandDto brandDto){
        brandService.addBrand(brandDto);
        return new ResponseEntity<>(new ResponseDto<>(true,brandDto), HttpStatus.CREATED);
    }

    @PutMapping
    @Operation(
            summary = "Update brand"
    )
    public ResponseEntity<ResponseDto<BrandDto>> update(@RequestBody BrandDto updateBrandDto){
        return new ResponseEntity<>(new ResponseDto<>(true,brandService.updateBrand(updateBrandDto)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete brand"
    )
    public ResponseEntity<ResponseDto<String>> delete(@PathVariable @NotNull Integer id){
        brandService.deleteBrand(id);
        return new ResponseEntity<>(new ResponseDto<>(true,"Brand:" + id + " can be deleted",
                Response.NO_ERROR.getText()),HttpStatus.OK);
    }

    @GetMapping
    @Operation(
            summary = "View all car brands"
    )
    public ResponseEntity<ResponseDto<Page<BrandDto>>> getAll(@PageableDefault(sort = "id", direction = Sort.Direction.ASC, page = 0, size = 10) Pageable pageable){
        final Page<BrandDto> page = brandService.getAllBrands(pageable);
        return ResponseEntity.ok(new ResponseDto<>(true , page));
    }

}
