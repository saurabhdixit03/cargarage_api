package com.example.cargarage.repository.common; 

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cargarage.model.common.Admin;
//import com.example.cargarage.model.common.Customer;

public interface AdminRepository extends JpaRepository<Admin, Long> {
	// You can add custom queries here if needed
    Admin findByEmail(String email); // Example of custom query to find Admin by email

    Optional<Admin> findById(Long id);
}