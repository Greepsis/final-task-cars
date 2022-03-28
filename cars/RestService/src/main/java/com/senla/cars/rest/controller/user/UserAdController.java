package com.senla.cars.rest.controller.user;

import com.senla.cars.api.dto.ResponseDto;
import com.senla.cars.api.dto.ad.AdDto;
import com.senla.cars.api.dto.ad.RegistrationAdDto;
import com.senla.cars.api.service.AdService;
import com.senla.cars.api.specification.SearchCriteria;
import com.senla.cars.serviceImpl.repository.AdRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/ad")
@RequiredArgsConstructor
@Validated
@Tag(name = "User ad controller",description = "Allows users to manipulate ads")
public class UserAdController {
    private final AdService adService;
    @Autowired
    private AdRepository adRepository;

    @PostMapping
    @Operation(
            summary = "Registration new ad"
    )
    public ResponseEntity<ResponseDto<RegistrationAdDto>> addAd(@RequestBody RegistrationAdDto registrationAdDto){
        adService.addAd(registrationAdDto);
        return new ResponseEntity<>(new ResponseDto<>(true,registrationAdDto), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    @Operation(
            summary = "Update ad"
    )
    public ResponseEntity<ResponseDto<AdDto>> updateAd(@RequestBody RegistrationAdDto registrationAdDto,
                                                       @PathVariable Integer id){
        return new ResponseEntity<>(new ResponseDto<>(true,adService.updateAd(registrationAdDto, id)), HttpStatus.OK);
    }

    @PutMapping("/{id}/deactivation")
    @Operation(
            summary = "Deactivation ad"
    )
    public ResponseEntity<ResponseDto<AdDto>> deactivationAd(@PathVariable Integer id){
        return new ResponseEntity<>(new ResponseDto<>(true,adService.deactivationAd(id)),HttpStatus.OK);
    }

    @GetMapping
    @Operation(
            summary = "View,filtration and search ad"
    )
    public ResponseEntity<ResponseDto<Page<AdDto>>> getAd(@RequestBody List<SearchCriteria> searchCriteria,
                                                          @PageableDefault(sort = "id", direction = Sort.Direction.ASC,
            page = 0, size = 10) Pageable pageable){
        final Page<AdDto> page = adService.getAd(pageable, searchCriteria);
        return ResponseEntity.ok(new ResponseDto<>(true , page));
    }

    @GetMapping("/myAd")
    @Operation(
            summary = "View my ad"
    )
    public ResponseEntity<ResponseDto<Page<AdDto>>> getMyAd(@PageableDefault(sort = "id", direction = Sort.Direction.ASC,
            page = 0, size = 10) Pageable pageable){
        final Page<AdDto> page = adService.getUserAd(pageable);
        return ResponseEntity.ok(new ResponseDto<>(true , page));
    }

    @PutMapping("/{id}/updateDate")
    @Operation(
            summary = "Raise ad"
    )
    public ResponseEntity<ResponseDto<AdDto>> updatePublicationDate(@PathVariable Integer id){
        return new ResponseEntity<>(new ResponseDto<>(true,adService.updatePublicationDate(id)), HttpStatus.OK);
    }
}
