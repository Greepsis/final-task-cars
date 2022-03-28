package com.senla.cars.rest.controller.admin;

import com.senla.cars.api.dto.ResponseDto;
import com.senla.cars.api.dto.user.UserDto;
import com.senla.cars.api.service.AdminService;
import com.senla.cars.api.specification.SearchCriteria;
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

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/admin/user")
@RequiredArgsConstructor
@Validated
@Tag(name = "Admin user controller", description = "Allows admins to create new admins, block users, and get all users")
public class AdminUserController {

    private final AdminService adminService;

    @GetMapping
    @Operation(
            summary = "View all users"
    )
    public ResponseEntity<ResponseDto<Page<UserDto>>> getUsers (@RequestBody List<SearchCriteria> searchCriteria,
                                                                @PageableDefault(sort = "id", direction = Sort.Direction.ASC,
                                                                        page = 0, size = 10) Pageable pageable){
        final Page<UserDto> users =adminService.getUser(pageable,searchCriteria);
        return ResponseEntity.ok(new ResponseDto<>(true , users));
    }

    @PutMapping("/{email}")
    @Operation(
            summary = "Blocking user"
    )
    public ResponseEntity<ResponseDto<String>> blockingUser(@Valid @PathVariable String email){
        adminService.blockingUser(email);
        return new ResponseEntity<>(new ResponseDto<>(true,email),HttpStatus.OK);
    }

    @PutMapping("/role/{id}")
    @Operation(
            summary = "Create new admin"
    )
    public ResponseEntity<ResponseDto<UserDto>> updateRole(@PathVariable Integer id){
        return new ResponseEntity<>(new ResponseDto<>(true,adminService.updateRole(id)),HttpStatus.OK);
    }

}
