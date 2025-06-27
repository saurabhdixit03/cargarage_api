package com.example.cargarage.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.cargarage.model.Appointment;
import com.example.cargarage.model.Customer;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    List<Appointment> findByCustomerId(Long customerId);

    // ✅ Count the number of appointments for a customer (to identify repeat customers)
    long countByCustomerId(Long customerId);

    // Custom query to fetch all appointments with full data
    @Query("SELECT a FROM Appointment a JOIN FETCH a.customer c JOIN FETCH a.car cr JOIN FETCH a.service s")
    List<Appointment> findAllWithDetails();

    // Custom query to fetch appointments by customer with full details
    @Query("SELECT a FROM Appointment a JOIN FETCH a.customer c JOIN FETCH a.car cr JOIN FETCH a.service s WHERE c.id = :customerId")
    List<Appointment> findAppointmentsByCustomerWithDetails(@Param("customerId") Long customerId);

	int countByCustomerIdAndStatus(Customer customer2, String string);
	
	// ✅ Needed for rescheduling logic — fetch group appointments
    List<Appointment> findByGroupId(Long groupId);

	List<Appointment> findByCarIdAndStatus(Long carId, String string);
    
}
