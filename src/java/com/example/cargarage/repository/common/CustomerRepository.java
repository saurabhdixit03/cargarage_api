package com.example.cargarage.repository.common;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cargarage.model.common.Customer;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	
	@Query("SELECT c FROM Customer c WHERE (SELECT COUNT(a) FROM Appointment a WHERE a.customer.id = c.id) > 1")
    List<Customer> findRepeatCustomers();

}
