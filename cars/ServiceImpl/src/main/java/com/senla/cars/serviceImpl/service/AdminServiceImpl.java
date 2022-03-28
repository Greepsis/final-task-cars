package com.senla.cars.serviceImpl.service;

import com.senla.cars.api.dto.ad.AdDto;
import com.senla.cars.api.dto.ad.RegistrationAdDto;
import com.senla.cars.api.dto.model.ModelDto;
import com.senla.cars.api.dto.user.UserDto;
import com.senla.cars.api.service.AdService;
import com.senla.cars.api.service.AdminService;
import com.senla.cars.api.service.ModelService;
import com.senla.cars.api.service.UserService;
import com.senla.cars.api.specification.GenericSpecification;
import com.senla.cars.api.specification.SearchCriteria;
import com.senla.cars.serviceImpl.enums.ExceptionEnums;
import com.senla.cars.serviceImpl.exception.NotEmptyException;
import com.senla.cars.serviceImpl.exception.NotFoundException;
import com.senla.cars.serviceImpl.exception.constant.ExceptionConstants;
import com.senla.cars.serviceImpl.model.Ad;
import com.senla.cars.serviceImpl.model.User;
import com.senla.cars.serviceImpl.repository.AdRepository;
import com.senla.cars.serviceImpl.repository.UserRepository;
import com.senla.cars.serviceImpl.roles.RolesConstants;
import com.senla.cars.serviceImpl.utils.Convertor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final AdRepository adRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final Convertor convert;
    private final AdService adService;
    private final ModelService modelService;

    @Override
    public Page<UserDto> getUser(Pageable pageable, List<SearchCriteria> searchCriteria){
        if (searchCriteria.isEmpty()){
            throw new NotEmptyException(ExceptionEnums.SEARCH_CRITERIA.getText() + ExceptionConstants.IS_EMPTY);
        }
        GenericSpecification<User> genericSpecification = new GenericSpecification<>();
        searchCriteria.stream().map(searchCriterion -> new SearchCriteria(searchCriterion.getKey(),
                searchCriterion.getValue(), searchCriterion.getOperation())).forEach(genericSpecification::add);
        Page<User> users = userRepository.findAll(genericSpecification,pageable);
        return convert.mapEntityPageIntoDtoPage(users,UserDto.class);
    }

    @Override
    public void blockingUser(String email) {
        if (email.isEmpty()){
            throw new NotEmptyException(ExceptionEnums.USER_NAME.getText() + ExceptionConstants.IS_EMPTY);
        }else {
            UserDto userDto = modelMapper.map(userService.findUserByEmail(email),UserDto.class);
            userDto.setBlocking(LocalDate.now());
            userRepository.save(modelMapper.map(userDto,User.class));
            log.info("User:{},was blocked",email);
        }
    }

    @Override
    public UserDto updateRole(Integer id) {
        UserDto userDto = modelMapper.map(findUserById(id),UserDto.class);
        if (userDto.getRoles().equals(RolesConstants.USER)){
            userDto.setRoles(RolesConstants.ADMIN);
        }else {
            userDto.setRoles(RolesConstants.USER);
        }
        userRepository.save(modelMapper.map(userDto,User.class));
        log.info("User:{} became admin",userDto.getEmail());
        return userDto;
    }

    private User findUserById(Integer id){
        return userRepository.findById(id).orElseThrow(() ->
                new NotFoundException(ExceptionEnums.USER.getText() + ExceptionConstants.NOT_FOUND) );
    }

    @Override
    public AdDto updateAd(RegistrationAdDto registrationAdDto, Integer id){
        AdDto adDto = modelMapper.map(registrationAdDto,AdDto.class);
        ModelDto modelDto = modelService.findModelByName(registrationAdDto.getModelName());
        adDto.setModel(modelDto);
        adDto.setUser(adService.findAdById(id).getUser());
        adDto.setId(id);
        adDto.setPublicationDateTime(adService.findAdById(id).getPublicationDateTime());
        adRepository.save(modelMapper.map(adDto, Ad.class));
        log.info("Ad:{} was changed by admin",adDto.getId());
        return adDto;
    }

}
