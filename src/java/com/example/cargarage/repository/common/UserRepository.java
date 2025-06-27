package com.example.cargarage.repository.common;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cargarage.model.common.Customer;

public interface UserRepository extends JpaRepository<Customer, Long> {
	// You can add custom queries here if needed
    Customer findByEmail(String email); // Example of custom query to find User by email
    
    Optional<Customer> findById(Long id);
}
