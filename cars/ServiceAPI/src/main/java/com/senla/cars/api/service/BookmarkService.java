package com.senla.cars.api.service;

import com.senla.cars.api.dto.bookmark.BookmarkDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookmarkService {
    BookmarkDto addBookmark(Integer id);
    void deleteBookmark(Integer adId);
    boolean isExist(Integer adId,Integer userId);
    Page<BookmarkDto> getAllUserBookmark(Pageable pageable);
}
