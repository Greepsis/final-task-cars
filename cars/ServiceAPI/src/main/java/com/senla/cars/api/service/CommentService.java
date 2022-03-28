package com.senla.cars.api.service;

import com.senla.cars.api.dto.comment.AddCommentDto;
import com.senla.cars.api.dto.comment.CommentDto;
import com.senla.cars.api.specification.SearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {
    void addComment(AddCommentDto addCommentDto);
    CommentDto updateComment(AddCommentDto updateCommentDto);
    Page<CommentDto> getAllUserComments(Pageable pageable);
    public Page<CommentDto> getComments(Pageable pageable, List<SearchCriteria> searchCriteria);
    void deleteComment(Integer id);
    boolean isExist(Integer commentId,Integer userId);
}
