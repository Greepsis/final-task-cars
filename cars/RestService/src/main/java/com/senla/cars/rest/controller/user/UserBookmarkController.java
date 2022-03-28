package com.senla.cars.rest.controller.user;

import com.senla.cars.api.dto.ResponseDto;
import com.senla.cars.api.dto.bookmark.BookmarkDto;
import com.senla.cars.api.service.BookmarkService;
import com.senla.cars.rest.enums.Response;
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

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/user/bookmark")
@RequiredArgsConstructor
@Validated
@Tag(name = "User bookmark controller", description = "Allows you to add, edit, delete bookmarks")
public class UserBookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/{id}")
    @Operation(
            summary = "Add new bookmark"
    )
    public ResponseEntity<ResponseDto<BookmarkDto>> addBookmark(@PathVariable Integer id){
        return new ResponseEntity<>(new ResponseDto<>(true,bookmarkService.addBookmark(id)), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete user bookmark"
    )
    public ResponseEntity<ResponseDto<String>> delete(@PathVariable @NotNull Integer id){
        bookmarkService.deleteBookmark(id);
        return new ResponseEntity<>(new ResponseDto<>(true,"Bookmark with adId:" + id + " can be deleted",
                Response.NO_ERROR.getText()),HttpStatus.OK);
    }

    @GetMapping
    @Operation(
            summary = "View all user bookmarks"
    )
    public ResponseEntity<ResponseDto<Page<BookmarkDto>>> getAllUserBookmarks(@PageableDefault(sort = "ad_id", direction = Sort.Direction.ASC,
            page = 0, size = 10) Pageable pageable){
        final Page<BookmarkDto> page = bookmarkService.getAllUserBookmark(pageable);
        return ResponseEntity.ok(new ResponseDto<>(true , page));
    }

}
