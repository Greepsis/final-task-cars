package com.senla.cars.serviceImpl.repository;

import com.senla.cars.serviceImpl.model.Ad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AdRepository extends JpaRepository<Ad,Integer>, JpaSpecificationExecutor<Ad> {
    Page<Ad> findAllAdByUserEmail(String userEmail,Pageable pageable);
    Ad findByModelName(String name);

}
