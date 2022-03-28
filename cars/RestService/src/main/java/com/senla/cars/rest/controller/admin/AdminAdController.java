package com.senla.cars.rest.controller.admin;

import com.senla.cars.api.dto.ResponseDto;
import com.senla.cars.api.dto.ad.AdDto;
import com.senla.cars.api.dto.ad.RegistrationAdDto;
import com.senla.cars.api.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin/ad")
@RequiredArgsConstructor
@Validated
@Tag(name = "Admin ad controller", description = "Allows admins to edit any ads")
public class AdminAdController {

    private final AdminService adminService;

    @PutMapping("/{id}")
    @Operation(
            summary = "Update ad by admin"
    )
    public ResponseEntity<ResponseDto<AdDto>> updateAdByAdmin(@RequestBody RegistrationAdDto registrationAdDto,
                                                       @PathVariable Integer id) {
        return new ResponseEntity<>(new ResponseDto<>(true, adminService.updateAd(registrationAdDto, id)), HttpStatus.OK);
    }

}
