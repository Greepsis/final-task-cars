package com.senla.cars.serviceImpl.service;

import com.senla.cars.api.dto.ad.AdDto;
import com.senla.cars.api.dto.ad.RegistrationAdDto;
import com.senla.cars.api.dto.model.ModelDto;
import com.senla.cars.api.dto.user.UserDto;
import com.senla.cars.api.service.AdService;
import com.senla.cars.api.service.ModelService;
import com.senla.cars.api.service.UserService;
import com.senla.cars.serviceImpl.enums.ExceptionEnums;
import com.senla.cars.serviceImpl.exception.NotEmptyException;
import com.senla.cars.serviceImpl.exception.NotFoundException;
import com.senla.cars.serviceImpl.exception.ad.EmailMismatchException;
import com.senla.cars.serviceImpl.exception.ad.InvalidPhoneNumberException;
import com.senla.cars.serviceImpl.exception.ad.PromotionException;
import com.senla.cars.serviceImpl.exception.constant.ExceptionConstants;
import com.senla.cars.serviceImpl.model.Ad;
import com.senla.cars.serviceImpl.repository.AdRepository;
import com.senla.cars.api.specification.GenericSpecification;
import com.senla.cars.api.specification.SearchCriteria;
import com.senla.cars.serviceImpl.utils.Convertor;
import com.senla.cars.serviceImpl.utils.Validator;
import com.senla.cars.serviceImpl.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class AdServiceImpl implements AdService {
    private final AdRepository adRepository;
    private final ModelMapper modelMapper;
    private final Convertor convertor;
    private final UserService userService;
    private final ModelService modelService;
    private final Validator validator;

    @Value("${spring.ad.promotion.promotion-in-hour}")
    private long promotionDateInHours;


    @Override
    public void addAd(RegistrationAdDto registrationAdDto) {
        UserDto userDto = userService.findUserByEmail(SecurityUtil.getCurrentUserLogin());
        ModelDto modelDto = modelService.findModelByName(registrationAdDto.getModelName());
        if (registrationAdDto.getMobilePhone() != null){
            if (!validator.validateTelephoneNumber(registrationAdDto.getMobilePhone())){
                throw new InvalidPhoneNumberException(ExceptionEnums.PHONE_NUMBER.getText() + ExceptionConstants.NOT_VALID);
            }
        }
        AdDto adDto = modelMapper.map(registrationAdDto,AdDto.class);
        adDto.setUser(userDto);
        adDto.setModel(modelDto);
        adDto.setPublicationDateTime(LocalDateTime.now());
        adDto.setDeactivated(false);
        adRepository.save(modelMapper.map(adDto, Ad.class));
        log.info("User:{} added ad",adDto.getUser().getEmail());
    }



    @Override
    public AdDto deactivationAd(Integer id) {
        AdDto adDto = findAdById(id);
        if (Objects.equals(SecurityUtil.getCurrentUserLogin(), adDto.getUser().getEmail())){
            adDto.setDeactivated(true);
            adRepository.save(modelMapper.map(adDto,Ad.class));
            return adDto;
        }else {
            throw new EmailMismatchException(ExceptionEnums.EMAILS.getText() + ExceptionConstants.DO_NOT_MATCH);
        }
    }

    @Override
    public AdDto findAdById(Integer id) {
        Ad ad = adRepository.findById(id).orElseThrow(() ->
                new NotFoundException(ExceptionEnums.AD.getText() + ExceptionConstants.NOT_FOUND));

        return modelMapper.map(ad,AdDto.class);
    }

    @Override
    public AdDto updateAd(RegistrationAdDto registrationAdDto, Integer id){
        if (id == null){
            throw new NotEmptyException(ExceptionEnums.ID.getText() + ExceptionConstants.IS_EMPTY);
        }
        ModelDto modelDto = modelService.findModelByName(registrationAdDto.getModelName());
        AdDto adDto = modelMapper.map(registrationAdDto,AdDto.class);
        adDto.setModel(modelDto);
        adDto.setUser(findAdById(id).getUser());
        if (!Objects.equals(adDto.getUser().getEmail(),SecurityUtil.getCurrentUserLogin())){
            throw new EmailMismatchException(ExceptionEnums.EMAILS.getText() + ExceptionConstants.DO_NOT_MATCH);
        }
        adDto.setId(id);
        adDto.setPublicationDateTime(findAdById(id).getPublicationDateTime());
        adRepository.save(modelMapper.map(adDto,Ad.class));
        log.info("Ad:{} was change",adDto.getId());
        return adDto;
    }

    @Override
    public Page<AdDto> getAd(Pageable pageable, List<SearchCriteria> searchCriteria){
        if (searchCriteria.isEmpty()){
            throw new NotEmptyException(ExceptionEnums.SEARCH_CRITERIA.getText() + ExceptionConstants.IS_EMPTY);
        }
        GenericSpecification<Ad> genericSpecification = new GenericSpecification<>();
        searchCriteria.stream().map(searchCriterion -> new SearchCriteria(searchCriterion.getKey(),
                searchCriterion.getValue(), searchCriterion.getOperation())).forEach(genericSpecification::add);
        Page<Ad> ads = adRepository.findAll(genericSpecification,pageable);
        return convertor.mapEntityPageIntoDtoPage(ads,AdDto.class);
    }

    @Override
    public Page<AdDto> getUserAd(Pageable pageable){
        Page<Ad> ads = adRepository.findAllAdByUserEmail(SecurityUtil.getCurrentUserLogin(),pageable);
        return convertor.mapEntityPageIntoDtoPage(ads,AdDto.class);
    }

    @Override
    public AdDto getAdByModelName(String name){
        if (name.isEmpty()){
            throw new NotEmptyException(ExceptionEnums.MODEL_NAME.getText() + ExceptionConstants.IS_EMPTY);
        }
        Ad ad = adRepository.findByModelName(name);
        if (Objects.isNull(ad)){
            throw new NotFoundException(ExceptionEnums.AD.getText() + ExceptionConstants.NOT_FOUND);
        }
        return modelMapper.map(ad,AdDto.class);
    }

    @Override
    public AdDto updatePublicationDate(Integer id){
        AdDto adDto = findAdById(id);
        if (Objects.equals(adDto.getUser().getEmail(),SecurityUtil.getCurrentUserLogin())){
            if (adDto.getPublicationDateTime().plusHours(promotionDateInHours).isBefore(LocalDateTime.now())){
                adDto.setPublicationDateTime(LocalDateTime.now());
                adRepository.save(modelMapper.map(adDto,Ad.class));
                return adDto;
            }else {
                throw new PromotionException(ExceptionEnums.PROMOTION.getText() + ExceptionConstants.IS_UNAVAILABLE);
            }
        }else {
            throw new EmailMismatchException(ExceptionEnums.EMAILS.getText() + ExceptionConstants.DO_NOT_MATCH);
        }
    }
}
