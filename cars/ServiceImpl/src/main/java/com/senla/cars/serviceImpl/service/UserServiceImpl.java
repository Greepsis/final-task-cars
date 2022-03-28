package com.senla.cars.serviceImpl.service;

import com.senla.cars.api.dto.auth.AuthenticationResponseDto;
import com.senla.cars.api.dto.user.RegistrationDto;
import com.senla.cars.api.dto.user.UserDto;
import com.senla.cars.api.service.UserService;
import com.senla.cars.serviceImpl.enums.ExceptionEnums;
import com.senla.cars.serviceImpl.exception.NotEmptyException;
import com.senla.cars.serviceImpl.exception.NotFoundException;
import com.senla.cars.serviceImpl.exception.auth.BlockedEmailException;
import com.senla.cars.serviceImpl.exception.auth.DeletedUserException;
import com.senla.cars.serviceImpl.exception.constant.ExceptionConstants;
import com.senla.cars.serviceImpl.utils.Convertor;
import com.senla.cars.serviceImpl.utils.Validator;
import com.senla.cars.serviceImpl.exception.user.EmailAlreadyExistsException;
import com.senla.cars.serviceImpl.exception.user.InvalidPasswordException;
import com.senla.cars.serviceImpl.model.User;
import com.senla.cars.serviceImpl.repository.UserRepository;
import com.senla.cars.serviceImpl.roles.RolesConstants;
import com.senla.cars.serviceImpl.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final Validator validator;
    private final Convertor convert;

    @Override
    public boolean isExist(String email) {
        Optional<User> user = Optional.ofNullable(userRepository.getUserByEmail(email));
        return user.isPresent();
    }

    @Override
    public void addUser(RegistrationDto userDto) {
        if (userDto.getEmail() == null){
            throw new NotEmptyException(ExceptionEnums.USER_EMAIL.getText() + ExceptionConstants.IS_EMPTY);
        }else {
            if (isExist(userDto.getEmail())){
                throw new EmailAlreadyExistsException(ExceptionEnums.EMAIL.getText() +
                        String.format(ExceptionConstants.ALREADY_EXISTS,userDto.getEmail()));
            }
        }
        if (userDto.getPassword() != null){
            if (!validator.validatePassword(userDto.getPassword())){
                throw new InvalidPasswordException(ExceptionEnums.PASSWORD.getText() + ExceptionConstants.NOT_VALID);
            }
        }else {
            throw new NotEmptyException(ExceptionEnums.PASSWORD.getText() + ExceptionConstants.IS_EMPTY);
        }
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setRoles(RolesConstants.USER);
        userRepository.save(modelMapper.map(userDto,User.class));
        log.info("Registration user: {}",userDto.getEmail());
    }

    @Override
    public AuthenticationResponseDto authenticationUser(String email) {
            UserDto user = findUserByEmail(email);
            if (user.getBlocking() != null){
                throw new BlockedEmailException(ExceptionEnums.USER.getText() + ExceptionConstants.HAS_BEEN_BLOCKED);
            }
            if (user.getDeletion() != null){
                throw new DeletedUserException(ExceptionEnums.USER.getText() + ExceptionConstants.HAS_BEEN_DELETED);
            }
            AuthenticationResponseDto authenticationResponseDto = new AuthenticationResponseDto();
            authenticationResponseDto.setEmail(user.getEmail());
            return authenticationResponseDto;
    }

    @Override
    public UserDto findUserByEmail(String email){
        User user = userRepository.getUserByEmail(email);
        if (Objects.isNull(user)){
            throw new NotFoundException(ExceptionEnums.USER.getText()+ ExceptionConstants.NOT_FOUND);
        }else {
            return modelMapper.map(user,UserDto.class);
        }
    }

    @Override
    public void deleteUser (){
        UserDto userDto = modelMapper.map(findUserByEmail(SecurityUtil.getCurrentUserLogin()),UserDto.class);
        userDto.setDeletion(LocalDate.now());
        userRepository.save(modelMapper.map(userDto,User.class));
        log.info("User:{},can be delete",userDto.getEmail());
    }

    @Override
    public Page<UserDto> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAllByBlockingNullAndDeletionNull(pageable);
        return convert.mapEntityPageIntoDtoPage(users,UserDto.class);
    }

}
