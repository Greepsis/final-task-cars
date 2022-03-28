package com.senla.cars.serviceImpl.repository;


import com.senla.cars.serviceImpl.model.Bookmark;
import com.senla.cars.serviceImpl.model.BookmarkKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Book;

public interface BookmarkRepository extends JpaRepository<Bookmark, BookmarkKey> {
    Bookmark findBookmarkByAdIdAndUserId(Integer adId,Integer userId);
    Bookmark findBookmarkByAdId(Integer id);
    Page<Bookmark> findAllByUserId(Integer userId, Pageable pageable);

}
