package com.example.cargarage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cargarage.model.FaceliftKit;

@Repository
public interface FaceliftkitRepositoy extends JpaRepository<FaceliftKit, Long> {

    // Fetch facelift kits based on car model and if the kit is active
    List<FaceliftKit> findByCarMakeAndCarModelAndIsActiveTrue(String carMake, String carModel);
    
    // Fetch facelift kits by car model (without checking if active)
    List<FaceliftKit> findByCarModel(String carModel);
}
