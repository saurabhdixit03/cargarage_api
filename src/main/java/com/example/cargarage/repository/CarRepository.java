package com.example.cargarage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cargarage.model.Car;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
	
    List<Car> findByCustomerId(Long customerId);  // Fetch cars by customer ID
    
    
}