package com.senla.cars.rest.controller.open;

import com.senla.cars.api.dto.auth.AuthenticationRequestDto;
import com.senla.cars.api.dto.ResponseDto;
import com.senla.cars.api.dto.auth.AuthenticationResponseDto;
import com.senla.cars.api.dto.user.RegistrationDto;
import com.senla.cars.api.service.UserService;
import com.senla.cars.rest.security.jwt.JwtToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@Validated
@Tag(name = "Authentication Controller", description = "Allows you to log in or register a user")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtToken jwtToken;
    private final UserService userService;

    @PostMapping
    @Operation(
            summary = "Log in user"
    )
    public ResponseEntity<ResponseDto<AuthenticationResponseDto>> login(@RequestBody AuthenticationRequestDto requestDto){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDto.getEmail(),
                    requestDto.getPassword()));
            AuthenticationResponseDto authenticationResponseDto = userService.authenticationUser(requestDto.getEmail());
            authenticationResponseDto.setToken(jwtToken.createToken(requestDto.getEmail()));
            return new ResponseEntity<>(new ResponseDto<>(true,authenticationResponseDto), HttpStatus.OK);
        }catch (AuthenticationException exception){
            return new ResponseEntity<>(new ResponseDto<>(false,"Invalid email or password"),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/registration")
    @Operation(
            summary = "Registration user",
            description = "Allows to register a new user"
    )
    public ResponseEntity<ResponseDto<RegistrationDto>> registration(@RequestBody @Valid RegistrationDto userDto){
        try {
            userService.addUser(userDto);
            return new ResponseEntity<>(new ResponseDto<>(true,userDto),HttpStatus.CREATED);
        }catch (AuthenticationException exception){
            return new ResponseEntity<>(new ResponseDto<>(false,"Invalid email or password"),HttpStatus.FORBIDDEN);
        }

    }

}
