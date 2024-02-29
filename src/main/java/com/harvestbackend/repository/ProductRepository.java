package com.harvestbackend.repository;

import com.harvestbackend.model.EType;
import com.harvestbackend.model.Farmer;
import com.harvestbackend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product , Long> {


    List<Product> findByType(EType type);

    List<Product> findByNameContainingIgnoreCase(String name);

    @Query("SELECT p FROM Product p WHERE p.type = :type AND LOWER(p.name) LIKE %:name%")
    List<Product> findByTypeAndName(@Param("type") EType type, @Param("name") String name);


    List<Product> findByNameContainingAndType(String name, EType type);
    List<Product> findByFarmer(Farmer farmer);

    @Query("SELECT p FROM Product p WHERE p.type = :type AND LOWER(p.name) LIKE %:name%")
    Page<Product> findByTypeAndName(@Param("type") EType type, @Param("name") String name, Pageable pageable);



}
