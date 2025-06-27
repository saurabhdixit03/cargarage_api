package com.example.cargarage.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cargarage.model.Customer;
import com.example.cargarage.model.CustomerType;
import com.example.cargarage.model.Discount;

import java.util.List;
import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    
    
    @EntityGraph(attributePaths = {"services"})
    List<Discount> findByCustomerId(Long customerId);

    @EntityGraph(attributePaths = {"services"})
    List<Discount> findByCustomerType(CustomerType customerType);
    
    Optional<Discount> findById(Long id);

	List<Discount> findByCustomerIdIsNull();
}
