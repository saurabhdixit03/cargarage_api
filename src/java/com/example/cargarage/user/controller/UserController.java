package com.example.cargarage.user.controller;

import com.example.cargarage.model.common.*;
import com.example.cargarage.service.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin("http://localhost:3000/")
public class UserController {

    @Autowired
    private UserService userService;

    // Register a new customer
    @PostMapping("/register")
    public Customer registerCustomer(@RequestBody Customer customer) {
        return userService.registerCustomer(customer);
    }

    // Customer login
    @PostMapping("/login")
    public String loginCustomer(@RequestBody Customer customer) {
        Customer foundCustomer = userService.findCustomerByEmail(customer.getEmail());
        if (foundCustomer != null && foundCustomer.getPassword().equals(customer.getPassword())) {
            return "Customer logged in successfully!";
        } else {
            return "Invalid email or password!";
        }
    }

    // Fetch all available services
    @GetMapping("/services")
    public List<Services> getAllServices() {
        return userService.getAllServices();
    }

    // Search services by name
    @GetMapping("/services/search")
    public List<Services> searchServices(@RequestParam String name) {
        return userService.searchServicesByName(name);
    }

    // Book an appointment for a service 
    @PostMapping("/{customerId}/appointments")
    public Appointment bookAppointment(@PathVariable Long customerId, @RequestBody Appointment appointment) {
        return userService.bookAppointment(customerId, appointment);
    }

    // Fetch all appointments by customer
    @GetMapping("/appointments/{customerId}")
    public List<Appointment> getAppointmentsByCustomer(@PathVariable Long customerId) {
        return userService.getAppointmentsByCustomerId(customerId);
    }

    // Fetch a specific appointment by ID
    @GetMapping("/appointments/details/{id}")
    public Optional<Appointment> getAppointmentById(@PathVariable Long id) {
        return userService.getAppointmentById(id);
    }
    
    // Update appointment
    @PutMapping("/{customerId}/appointments/{appointmentId}")
    public Appointment updateAppointment(@PathVariable Long customerId,
                                          @PathVariable Long appointmentId,
                                          @RequestBody Appointment updatedAppointment) {
        return userService.updateAppointment(customerId, appointmentId, updatedAppointment);
    }

    // Update customer details
    @PutMapping("/{id}")
    public Customer updateCustomer(@PathVariable Long id, @RequestBody Customer updatedCustomer) {
        return userService.updateCustomer(id, updatedCustomer);
    }

    // Fetch all cars owned by a customer
    @GetMapping("/{customerId}/cars")
    public List<Car> getCarsByCustomer(@PathVariable Long customerId) {
        return userService.getCarsByCustomerId(customerId);
    }

    // Add a car for the customer
    @PostMapping("/{customerId}/cars")
    public Car addCar(@PathVariable Long customerId, @RequestBody Car car) {
        return userService.addCarToCustomer(customerId, car);
    }

    // Delete a car owned by the user
    @DeleteMapping("/cars/{carId}")
    public String deleteCar(@PathVariable Long carId) {
        return userService.deleteCar(carId);
    }

    // Fetch all available discounts
    @GetMapping("/discounts")
    public List<Discount> getAllDiscounts() {
        return userService.getAllDiscounts();
    }

    // Fetch available facelift kits
    @GetMapping("/facelift")
    public List<FaceliftKit> getAllKits() {
        return userService.getAllKits();
    }
}
