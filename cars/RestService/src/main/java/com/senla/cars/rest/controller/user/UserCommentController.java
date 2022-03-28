package com.senla.cars.rest.controller.user;

import com.senla.cars.api.dto.ResponseDto;
import com.senla.cars.api.dto.comment.AddCommentDto;
import com.senla.cars.api.dto.comment.CommentDto;
import com.senla.cars.api.service.CommentService;
import com.senla.cars.api.specification.SearchCriteria;
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
import java.util.List;

@RestController
@RequestMapping("/api/user/comment")
@RequiredArgsConstructor
@Validated
@Tag(name = "User comment controller", description = "Allows you to add, edit, delete, view your comments")
public class UserCommentController {

    private final CommentService commentService;

    @PostMapping
    @Operation(
            summary = "Add new comment"
    )
    public ResponseEntity<ResponseDto<AddCommentDto>> addComment(@RequestBody AddCommentDto addCommentDto){
        commentService.addComment(addCommentDto);
        return new ResponseEntity<>(new ResponseDto<>(true,addCommentDto), HttpStatus.CREATED);

    }
    @PutMapping
    @Operation(
            summary = "Update user comment"
    )
    public ResponseEntity<ResponseDto<CommentDto>> updateComment(@RequestBody AddCommentDto updateCommentDto){
        return new ResponseEntity<>(new ResponseDto<>(true,
                commentService.updateComment(updateCommentDto)),HttpStatus.OK);
    }
    @GetMapping("/user")
    @Operation(
            summary = "View all user comments"
    )
    public ResponseEntity<ResponseDto<Page<CommentDto>>> getAllUserComments(@PageableDefault(sort = "id", direction = Sort.Direction.ASC,
            page = 0, size = 10) Pageable pageable){
        final Page<CommentDto> comments = commentService.getAllUserComments(pageable);
        return ResponseEntity.ok(new ResponseDto<>(true , comments));
    }

    @GetMapping
    @Operation(
            summary = "View, filtering, search user comments"
    )
    public ResponseEntity<ResponseDto<Page<CommentDto>>> getComments(@RequestBody List<SearchCriteria> searchCriteria,
                                                          @PageableDefault(sort = "id", direction = Sort.Direction.ASC,
                                                                  page = 0, size = 10) Pageable pageable){
        final Page<CommentDto> comments = commentService.getComments(pageable, searchCriteria);
        return ResponseEntity.ok(new ResponseDto<>(true , comments));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete user comment"
    )
    public ResponseEntity<ResponseDto<String>> deleteComment(@PathVariable @NotNull Integer id){
        commentService.deleteComment(id);
        return new ResponseEntity<>(new ResponseDto<>(true,"Comment:" + id + " can be deleted",
                Response.NO_ERROR.getText()),HttpStatus.OK);
    }

}
