package com.example.cargarage.admin.controller;

import com.example.cargarage.model.common.Admin;
import com.example.cargarage.model.common.Appointment;
import com.example.cargarage.model.common.Car;
import com.example.cargarage.model.common.Customer;
import com.example.cargarage.model.common.Discount;
import com.example.cargarage.model.common.FaceliftKit;
import com.example.cargarage.model.common.Services;
import com.example.cargarage.service.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//DTO files import

import com.example.cargarage.dto.CustomerWithDetailsDTO;
import com.example.cargarage.dto.AdminAppointmentResponseDTO;
//import com.example.cargarage.dto.RepeatCustomerDiscountDTO;

import com.example.cargarage.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@CrossOrigin("http://localhost:3000/")
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
  
    // Register a new admin
    @PostMapping("/register")
    public Admin registerAdmin(@RequestBody Admin admin) {
        return adminService.registerAdmin(admin);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginAdmin(@RequestBody Admin admin) {
        Admin foundAdmin = adminService.findAdminByEmail(admin.getEmail());
        Map<String, String> response = new HashMap<>();

        if (foundAdmin != null && foundAdmin.getPassword().equals(admin.getPassword())) {
           // String token = jwtUtil.generateToken(foundAdmin.getEmail());
            response.put("status", "success");
            response.put("message", "Login successful");
         //   response.put("token", token); // Token added here
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            response.put("message", "Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    // Service Management 
    
    // Fetch all available services
    @GetMapping("/services")
    public List<Services> getAllServices() {  // Return List<Services>
        return adminService.getAllServices();
    }

    // Add a new service with its budget
    @PostMapping("/services")
    public Services addService(@RequestBody Services service) {  // Change to Services, not Admin
        return adminService.addService(service);
    }

    // Update a specific service by ID
    @PutMapping("/services/{id}")
    public Services updateService(@PathVariable Long id, @RequestBody Services service) {  // Services model
        return adminService.updateService(id, service);
    }

    // Remove a service by ID
    @DeleteMapping("/services/{id}")
    public String deleteService(@PathVariable Long id) {
        return adminService.deleteService(id);
    }
    
	// Customer Management 
    
    // Fetch all customers
    @GetMapping("/customers")
    public List<Customer> getAllCustomers() {
        return adminService.getAllCustomers();
    }

    // Fetch details of a specific customer
    @GetMapping("/customers/{id}")
    public Customer getCustomerById(@PathVariable Long id) {
        return adminService.getCustomerById(id);
    }

    // Remove a customer by ID
    @DeleteMapping("/customers/{id}")
    public String deleteCustomerById(@PathVariable Long id) {
        return adminService.deleteCustomerById(id);
    }
    
    // Fetch all cars owned by a specific customer
    @GetMapping("/customers/{customerId}/cars")
    public List<Car> getCarsByCustomer(@PathVariable Long customerId) {
        return adminService.getCarsByCustomerId(customerId);
    }

	// Car Management 
    
    // Fetch all cars in the system
    @GetMapping("/cars")
    public List<Car> getAllCars() {
        return adminService.getAllCars();
    }
    
	// Appointment Management 
    
    // Fetch all appointments with full data
    @GetMapping("/appointments")
    public List<Appointment> getAllAppointments() {
        return adminService.getAllAppointments();
    }

    // Fetch a specific appointment by ID
    @GetMapping("/appointments/{id}")
    public Optional<Appointment> getAppointmentById(@PathVariable Long id) {
        return adminService.getAppointmentById(id);
    }

    @PutMapping("/appointments/{id}")
    public String updateAppointment(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {
        String newStatus = requestBody.get("status");
        return adminService.updateAppointmentStatus(id, newStatus);
    }

    // Delete an appointment by ID
    @DeleteMapping("/appointments/{id}")
    public String deleteAppointment(@PathVariable Long id) {
        return adminService.deleteAppointment(id);
    }
    
	// Discount Management 
    
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/discounts")
    public Discount addDiscount(@RequestBody Discount discount) {
        return adminService.addDiscount(discount);
    }

    @GetMapping("/discounts")
    public List<Discount> getAllDiscounts() {
        return adminService.getAllDiscounts();
    }

    @GetMapping("/customers/{customerId}/discounts")
    public List<Discount> getDiscountsByCustomerId(@PathVariable Long customerId) {
        return adminService.getDiscountsByCustomerId(customerId);
    }

    @GetMapping("/discounts/common")
    public List<Discount> getCommonDiscounts() {
        return adminService.getCommonDiscounts();
    }

    @PutMapping("/discounts/{id}")
    public Discount updateDiscount(@PathVariable Long id, @RequestBody Discount discount) {
        return adminService.updateDiscount(id, discount);
    }

    @DeleteMapping("/discounts/{id}")
    public void deleteDiscount(@PathVariable Long id) {
        adminService.deleteDiscount(id);
    }

    
    // FaceliftKit Controllers
    
    @GetMapping("/facelift")
    public List<FaceliftKit> getAllKits() {
        return adminService.getAllKits();
    }

    @GetMapping("/facelift/{id}")
    public FaceliftKit getKitById(@PathVariable Long id) {
        return adminService.getKitById(id);
    }

    @GetMapping("/facelift/car/{carModel}")
    public List<FaceliftKit> getKitsByCarModel(@PathVariable String carModel) {
        return adminService.getKitsByCarModel(carModel);
    }

    @PostMapping("/facelift")
    public ResponseEntity<FaceliftKit> addFaceliftKit(@RequestBody FaceliftKit kit) {
        return new ResponseEntity<>(adminService.addFaceliftKit(kit), HttpStatus.CREATED);
    }

    @PutMapping("/facelift/{id}")
    public ResponseEntity<FaceliftKit> updateFaceliftKit(@PathVariable Long id, @RequestBody FaceliftKit kit) {
        return new ResponseEntity<>(adminService.updateFaceliftKit(id, kit), HttpStatus.OK);
    }

    @DeleteMapping("/facelift/{id}")
    public ResponseEntity<String> deleteFaceliftKit(@PathVariable Long id) {
    	adminService.deleteFaceliftKit(id);
        return new ResponseEntity<>("Facelift kit deleted", HttpStatus.OK);
    }
    
    
    // DTO Controllers
    
    //Get all customers with details
    @GetMapping("/customers-with-details")
    public List<CustomerWithDetailsDTO> getAllCustomersWithDetails() {
        return adminService.getAllCustomersWithDetails();
    }

    // Get all appointments with details
    @GetMapping("/appointments-with-details")
    public List<AppointmentResponseDTO> getAllAppointmentsWithDetails() {
        return adminService.getAllAppointmentsWithDetails();
    }

    // Update appointment status
    @PutMapping("/appointments/{id}/status")
    public String updateAppointmentStatus(@PathVariable Long id, @RequestBody String status) {
        return adminService.updateAppointmentStatus(id, status);
    }
    
    ///*
    
  /*  @GetMapping("/repeat-customers")
    public List<Long> getRepeatCustomers() {
        return adminService.getRepeatCustomerss();
    }



    @PostMapping("/assign-discount")
    public String assignDiscountToRepeatCustomers(@RequestBody RepeatCustomerDiscountDTO dto) {
        return adminService.assignDiscountToRepeatCustomers(dto);
    }
    
    */
    
}
