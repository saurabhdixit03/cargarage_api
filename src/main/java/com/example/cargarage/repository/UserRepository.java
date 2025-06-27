package com.example.cargarage.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cargarage.model.Customer;

public interface UserRepository extends JpaRepository<Customer, Long> {
	
    Customer findByEmail(String email); // Example of custom query to find User by email
    
    Optional<Customer> findById(Long id);
}
