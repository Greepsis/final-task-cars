package com.senla.cars.serviceImpl.repository;

import com.senla.cars.serviceImpl.model.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand,Integer> {
    Brand findBrandByName(String name);
}
