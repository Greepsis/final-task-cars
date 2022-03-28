package com.senla.cars.serviceImpl.service;

import com.senla.cars.api.dto.ad.AdDto;
import com.senla.cars.api.dto.comment.AddCommentDto;
import com.senla.cars.api.dto.comment.CommentDto;
import com.senla.cars.api.dto.user.UserDto;
import com.senla.cars.api.service.AdService;
import com.senla.cars.api.service.CommentService;
import com.senla.cars.api.service.UserService;
import com.senla.cars.api.specification.GenericSpecification;
import com.senla.cars.api.specification.SearchCriteria;
import com.senla.cars.serviceImpl.enums.ExceptionEnums;
import com.senla.cars.serviceImpl.exception.NotEmptyException;
import com.senla.cars.serviceImpl.exception.NotFoundException;
import com.senla.cars.serviceImpl.exception.ad.EmailMismatchException;
import com.senla.cars.serviceImpl.exception.constant.ExceptionConstants;
import com.senla.cars.serviceImpl.model.Comment;
import com.senla.cars.serviceImpl.repository.CommentRepository;
import com.senla.cars.serviceImpl.utils.Convertor;
import com.senla.cars.serviceImpl.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final Convertor convertor;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final AdService adService;

    @Override
    public boolean isExist(Integer commentId,Integer userId) {
        Optional<Comment> comment = Optional.ofNullable(commentRepository.findCommentByIdAndUserId(commentId,userId));
        return comment.isPresent();
    }

    @Override
    public void addComment(AddCommentDto addCommentDto) {
        if (addCommentDto.getId() == null){
            throw new NotEmptyException(ExceptionEnums.ID.getText() + ExceptionConstants.IS_EMPTY);
        }
        AdDto adDto = adService.findAdById(addCommentDto.getId());
        UserDto userDto = userService.findUserByEmail(SecurityUtil.getCurrentUserLogin());
        CommentDto commentDto = modelMapper.map(addCommentDto,CommentDto.class);
        commentDto.setAd(adDto);
        commentDto.setUser(userDto);
        commentDto.setDate(LocalDateTime.now());
        commentRepository.save(modelMapper.map(commentDto, Comment.class));
    }

    @Override
    public CommentDto updateComment(AddCommentDto updateCommentDto) {
        if (updateCommentDto.getId() == null){
            throw new NotEmptyException(ExceptionEnums.ID.getText() + ExceptionConstants.IS_EMPTY);
        }
        CommentDto commentDto = modelMapper.map(findCommentById(updateCommentDto.getId()),CommentDto.class);
        if (Objects.equals(SecurityUtil.getCurrentUserLogin(), commentDto.getUser().getEmail())){
            commentDto.setComment(updateCommentDto.getComment());
            commentDto.setDate(LocalDateTime.now());
            commentRepository.save(modelMapper.map(commentDto,Comment.class));
            return commentDto;
        }else {
            throw new EmailMismatchException(ExceptionEnums.EMAILS.getText() + ExceptionConstants.DO_NOT_MATCH);
        }
    }

    private Comment findCommentById(Integer id){
        return commentRepository.findById(id).orElseThrow(() ->
                new NotFoundException(ExceptionEnums.COMMENT.getText() + ExceptionConstants.NOT_FOUND));
    }

    @Override
    public Page<CommentDto> getAllUserComments(Pageable pageable) {
        Page<Comment> comments = commentRepository.findAllCommentByUserEmail(SecurityUtil.getCurrentUserLogin(),pageable);
        return convertor.mapEntityPageIntoDtoPage(comments,CommentDto.class);
    }

    @Override
    public Page<CommentDto> getComments(Pageable pageable, List<SearchCriteria> searchCriteria){
        if (searchCriteria.isEmpty()){
            throw new NotEmptyException(ExceptionEnums.SEARCH_CRITERIA.getText() + ExceptionConstants.IS_EMPTY);
        }
        GenericSpecification<Comment> genericSpecification = new GenericSpecification<>();
        searchCriteria.stream().map(searchCriterion -> new SearchCriteria(searchCriterion.getKey(),
                searchCriterion.getValue(), searchCriterion.getOperation())).forEach(genericSpecification::add);
        Page<Comment> comments = commentRepository.findAll(genericSpecification,pageable);
        return convertor.mapEntityPageIntoDtoPage(comments,CommentDto.class);
    }

    @Override
    public void deleteComment(Integer id) {
        if (isExist(id,userService.findUserByEmail(SecurityUtil.getCurrentUserLogin()).getId())){
            commentRepository.delete(commentRepository.findCommentByIdAndUserId(id,
                    userService.findUserByEmail(SecurityUtil.getCurrentUserLogin()).getId()));
        }else {
            throw new NotFoundException(ExceptionEnums.COMMENT.getText() + ExceptionConstants.NOT_FOUND);
        }
    }
}
