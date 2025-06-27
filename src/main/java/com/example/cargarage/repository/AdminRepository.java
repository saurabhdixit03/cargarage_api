package com.example.cargarage.repository; 

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cargarage.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Admin findByEmail(String email); // Example of custom query to find Admin by email

    Optional<Admin> findById(Long id);
}