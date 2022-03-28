package com.senla.cars.rest.controller.user;


import com.senla.cars.api.dto.ResponseDto;
import com.senla.cars.api.service.UserService;
import com.senla.cars.rest.enums.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
@Tag(name = "User controller",description = "Allows you to add, edit, delete, view your comments")
public class UserController {

    private final UserService userService;

    @DeleteMapping
    @Operation(
            summary = "Delete user"
    )
    public ResponseEntity<ResponseDto<String>> deleteUser(){
        userService.deleteUser();
        return new ResponseEntity<>(new ResponseDto<>(true,"User can be deleted",
                Response.NO_ERROR.getText()),HttpStatus.OK);
    }



}
