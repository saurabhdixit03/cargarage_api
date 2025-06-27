package com.example.cargarage.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cargarage.model.Employee;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);
}