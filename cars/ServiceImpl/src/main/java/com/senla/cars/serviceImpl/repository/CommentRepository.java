package com.senla.cars.serviceImpl.repository;

import com.senla.cars.serviceImpl.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Integer>, JpaSpecificationExecutor<Comment> {
    Page<Comment> findAllCommentByUserEmail(String userEmail, Pageable pageable);
    Page<Comment> findAllCommentByAdId(Integer adId, Pageable pageable);
    Comment findCommentByIdAndUserId(Integer id,Integer userId);
}
