package com.senla.cars.serviceImpl.repository;

import com.senla.cars.serviceImpl.model.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelRepository extends JpaRepository<Model,Integer>, JpaSpecificationExecutor<Model> {
    Model findModelByName(String name);
}
