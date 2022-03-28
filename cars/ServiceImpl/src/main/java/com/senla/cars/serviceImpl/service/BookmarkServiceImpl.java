package com.senla.cars.serviceImpl.service;

import com.senla.cars.api.dto.ad.AdDto;
import com.senla.cars.api.dto.bookmark.BookmarkDto;
import com.senla.cars.api.dto.user.UserDto;
import com.senla.cars.api.service.AdService;
import com.senla.cars.api.service.BookmarkService;
import com.senla.cars.api.service.UserService;
import com.senla.cars.serviceImpl.enums.ExceptionEnums;
import com.senla.cars.serviceImpl.exception.NotEmptyException;
import com.senla.cars.serviceImpl.exception.NotFoundException;
import com.senla.cars.serviceImpl.exception.constant.ExceptionConstants;
import com.senla.cars.serviceImpl.model.Bookmark;
import com.senla.cars.serviceImpl.repository.BookmarkRepository;
import com.senla.cars.serviceImpl.utils.Convertor;
import com.senla.cars.serviceImpl.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookmarkServiceImpl implements BookmarkService {
    private final ModelMapper modelMapper;
    private final Convertor convertor;
    private final BookmarkRepository bookmarkRepository;
    private final UserService userService;
    private final AdService adService;

    @Override
    public boolean isExist(Integer adId, Integer userId) {
        Optional<Bookmark> bookmark = Optional.ofNullable(bookmarkRepository.
                findBookmarkByAdIdAndUserId(adId,userId));
        return bookmark.isPresent();
    }

    @Override
    public BookmarkDto addBookmark(Integer id) {
        if(id == null){
            throw new NotEmptyException(ExceptionEnums.ID.getText() + ExceptionConstants.IS_EMPTY);
        }
        UserDto userDto = userService.findUserByEmail(SecurityUtil
                .getCurrentUserLogin());
        AdDto adDto = adService.findAdById(id);
        BookmarkDto bookmarkDto = new BookmarkDto();
        bookmarkDto.setAd(adDto);
        bookmarkDto.setUser(userDto);
        bookmarkRepository.save(modelMapper.map(bookmarkDto, Bookmark.class));
        log.info("User:{} add Ad:{} to bookmark",userDto.getEmail(),adDto.getId());
        return bookmarkDto;
    }

    @Override
    public void deleteBookmark(Integer adId) {
        if (isExist(adId,userService
                .findUserByEmail(SecurityUtil
                        .getCurrentUserLogin())
                .getId())){
            bookmarkRepository.delete(bookmarkRepository
                    .findBookmarkByAdIdAndUserId(adId, userService
                            .findUserByEmail(SecurityUtil
                                    .getCurrentUserLogin())
                            .getId()));
        }else
            throw new NotFoundException(ExceptionEnums.BOOKMARK.getText() + ExceptionConstants.NOT_FOUND);
    }

    @Override
    public Page<BookmarkDto> getAllUserBookmark(Pageable pageable) {
        Page<Bookmark> bookmarks = bookmarkRepository.findAllByUserId(userService.findUserByEmail(SecurityUtil
                        .getCurrentUserLogin())
                        .getId(),
                         pageable);
        return convertor.mapEntityPageIntoDtoPage(bookmarks,BookmarkDto.class);
    }
}
