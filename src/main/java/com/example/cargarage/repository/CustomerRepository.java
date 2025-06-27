package com.example.cargarage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.cargarage.model.Customer;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	
	@Query("SELECT c FROM Customer c WHERE (SELECT COUNT(a) FROM Appointment a WHERE a.customer.id = c.id) > 1")
    List<Customer> findRepeatCustomers();
	
	// Optionally, add a query to fetch only regular customers (if you track them differently)
    @Query("SELECT c FROM Customer c WHERE (SELECT COUNT(a) FROM Appointment a WHERE a.customer.id = c.id) = 1")
    List<Customer> findRegularCustomers();
    
    // Fetches the customers with carModel for sending email  
    @Query("SELECT DISTINCT c FROM Customer c JOIN c.cars car WHERE car.model = :model")
    List<Customer> findCustomersByCarModel(String model);


}
