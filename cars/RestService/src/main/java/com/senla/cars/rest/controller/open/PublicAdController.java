package com.senla.cars.rest.controller.open;

import com.senla.cars.api.dto.ResponseDto;
import com.senla.cars.api.dto.ad.AdDto;
import com.senla.cars.api.service.AdService;
import com.senla.cars.api.specification.SearchCriteria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/public")
@RequiredArgsConstructor
@Validated
@Tag(name = "Public ad controller", description = "Allows unregistered users to view, search, filter ads")
public class PublicAdController {
    private final AdService adService;

    @GetMapping
    @Operation(
            summary = "View, search, filter ads"
    )
    public ResponseEntity<ResponseDto<Page<AdDto>>> getAd(@RequestBody List<SearchCriteria> searchCriteria,
                                                          @PageableDefault(sort = "id", direction = Sort.Direction.ASC,
                                                                  page = 0, size = 10) Pageable pageable){
        final Page<AdDto> page = adService.getAd(pageable, searchCriteria);
        return ResponseEntity.ok(new ResponseDto<>(true , page));
    }

}
