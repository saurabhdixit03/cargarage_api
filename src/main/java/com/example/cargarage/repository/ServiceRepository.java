package com.example.cargarage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cargarage.model.Services;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Services, Long> {
List<Services> findByName(String name);
    
    List<Services> findByBudgetGreaterThan(Double budget);
    
    List<Services> findByNameContainingIgnoreCase(String name);
    
}
