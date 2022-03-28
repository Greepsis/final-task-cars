package com.senla.cars.serviceImpl.repository;

import com.senla.cars.serviceImpl.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer>, JpaSpecificationExecutor<User> {
    Page<User> findAllByBlockingNullAndDeletionNull(Pageable pageable);
    User getUserByEmail(String email);
    Optional<User> findUserByEmail(String email);
}
