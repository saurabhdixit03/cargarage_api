package com.example.cargarage.user.controller;

import com.example.cargarage.dto.AppointmentUpdateRequestDTO;


import com.example.cargarage.dto.AppointmentUpdateResponseDTO;
import com.example.cargarage.dto.FaceliftKitBookingRequest;
import com.example.cargarage.dto.FaceliftKitBookingResponse;
import com.example.cargarage.dto.UserAppointmentRequestDTO;
import com.example.cargarage.dto.UserAppointmentResponseDTO;
import com.example.cargarage.dto.UserDiscountResponseDTO;

import com.example.cargarage.dto.UserFaceliftKitDTO;
import com.example.cargarage.model.*;
import com.example.cargarage.user.service.UserService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // ---------------------------------------------------- USER AUTHENTICATION -------------------------------------------------------
    
    // Register Controller
    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@RequestBody Customer customer) {
        if (userService.findCustomerByEmail(customer.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Email already registered"));
        }
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        Customer savedCustomer = userService.registerCustomer(customer);
        return ResponseEntity.ok(Map.of("message", "Customer registered successfully", "customer", savedCustomer));
    }

    // Login Controller
    @PostMapping("/login")
    public ResponseEntity<?> loginCustomer(@RequestBody Customer customer, HttpSession session) {
        Customer storedCustomer = userService.findCustomerByEmail(customer.getEmail());
        if (storedCustomer != null && passwordEncoder.matches(customer.getPassword(), storedCustomer.getPassword())) {
            session.setAttribute("userEmail", storedCustomer.getEmail());
            session.setAttribute("userName", storedCustomer.getName());
            session.setAttribute("userAuthenticated", true);
            session.setAttribute("userId", storedCustomer.getId());
            return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "token", session.getId(),
                "email", storedCustomer.getEmail(),
                "name", storedCustomer.getName(),
                "userId", storedCustomer.getId()
                ));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid email or password"));
    }

    // Session api
    @GetMapping("/session")
    public ResponseEntity<?> checkSession(HttpSession session) {
        Boolean isAuthenticated = (Boolean) session.getAttribute("userAuthenticated");
        String userEmail = (String) session.getAttribute("userEmail");
        String userName = (String) session.getAttribute("userName");

        Object userIdObj = session.getAttribute("userId");
        Long userId = (userIdObj instanceof Number) ? ((Number) userIdObj).longValue() : -1L;

        if (Boolean.TRUE.equals(isAuthenticated) && userEmail != null) {
            return ResponseEntity.ok(Map.of(
                "authenticated", true,
                "userId", userId,  
                "email", userEmail,
                "name", userName != null ? userName : "User"
            ));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("authenticated", false));
    }

    // Logout Controller
    @PostMapping("/logout")
    public ResponseEntity<?> logoutCustomer(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
    
    // ---------------------------------------------- CUSTOMER PROFILE MANAGEMENT ---------------------------------------------
    
    // Get customer details
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomer(@PathVariable Long id, HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("userAuthenticated"))) {
            return ResponseEntity.ok(userService.findCustomerById(id));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }
    
    // Update customer details
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody Customer updatedCustomer, HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("userAuthenticated"))) {
            return ResponseEntity.ok(userService.updateCustomer(id, updatedCustomer));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }

    // ------------------------------------------------- CARS PRFILE MANAGEMENT ------------------------------------------------
    
    // Fetch all cars owned by a customer
    @GetMapping("/{customerId}/cars")
    public ResponseEntity<?> getCarsByCustomer(@PathVariable Long customerId, HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("userAuthenticated"))) {
            return ResponseEntity.ok(userService.getCarsByCustomerId(customerId));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }

    // Add a car for the customer
    @PostMapping("/{customerId}/cars")
    public ResponseEntity<?> addCar(@PathVariable Long customerId, @RequestBody Car car, HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("userAuthenticated"))) {
            return ResponseEntity.ok(userService.addCarToCustomer(customerId, car));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }
    
    // update a car owned by the user
    @PutMapping("/cars/{id}")
    public ResponseEntity<?> updateCar(@PathVariable Long id, @RequestBody Car updatedCar, HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("userAuthenticated"))) {
            Car updated = userService.updateCar(id, updatedCar);
            if (updated != null) {
                return ResponseEntity.ok(updated);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Car not found"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }

    // Delete a car owned by the user
    @DeleteMapping("/cars/{carId}")
    public ResponseEntity<?> deleteCar(@PathVariable Long carId, HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("userAuthenticated"))) {
            return ResponseEntity.ok(Map.of("message", userService.deleteCar(carId)));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }

    // -------------------------------------------------------- SERVICE MANAGEMENT --------------------------------------------------     

    // Fetch all available services
    @GetMapping("/services")
    public ResponseEntity<?> getAllServices(HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("userAuthenticated"))) {
            return ResponseEntity.ok(userService.getAllServices());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }

    // Search services by name
    @GetMapping("/services/search")
    public ResponseEntity<?> searchServices(@RequestParam String name, HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("userAuthenticated"))) {
            return ResponseEntity.ok(userService.searchServicesByName(name));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }

    // ----------------------------------------------- APPOINTMENT MANAGEMENT ----------------------------------------------
    
    /* Book an appointment for a service 
    @PostMapping("/{customerId}/appointments")
    public ResponseEntity<?> bookAppointment(@PathVariable Long customerId, @RequestBody Appointment appointment, HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("userAuthenticated"))) {
            return ResponseEntity.ok(userService.bookAppointment(customerId, appointment));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }

    // Fetch all appointments by customer
    @GetMapping("/appointments/{customerId}")
    public ResponseEntity<?> getAppointmentsByCustomer(@PathVariable Long customerId, HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("userAuthenticated"))) {
            return ResponseEntity.ok(userService.getAppointmentsByCustomerId(customerId));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }

    // Fetch a specific appointment by ID
    @GetMapping("/appointments/details/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable Long id, HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("userAuthenticated"))) {
            return ResponseEntity.ok(userService.getAppointmentById(id));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }  */
    
    // ------------------------------------------------------ DTO Controller ----------------------------------------------------------- 
    
    // Book an appointment (Authenticated)
    @PostMapping("/{customerId}/appointments")
    public ResponseEntity<?> bookAppointment(
            @PathVariable Long customerId,
            @RequestBody UserAppointmentRequestDTO requestDTO,
            HttpSession session) {

        if (Boolean.TRUE.equals(session.getAttribute("userAuthenticated"))) {
            List<UserAppointmentResponseDTO> response = userService.bookAppointment(customerId, requestDTO);
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Unauthorized"));
    }

    // Fetch all appointments by customer (Authenticated)
    @GetMapping("/appointments/{customerId}")
    public ResponseEntity<?> getAppointmentsByCustomer(
            @PathVariable Long customerId,
            HttpSession session) {

        if (Boolean.TRUE.equals(session.getAttribute("userAuthenticated"))) {
            List<UserAppointmentResponseDTO> appointments = userService.getAppointmentsByCustomerId(customerId);
            return ResponseEntity.ok(appointments);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Unauthorized"));
    }

    // Endpoint for rescheduling appointment
    @PutMapping("/appointments/{carId}")
    public ResponseEntity<?> rescheduleByCar(
            @PathVariable Long carId,
            @RequestBody AppointmentUpdateRequestDTO requestDTO,
            HttpSession session) {

        if (Boolean.TRUE.equals(session.getAttribute("userAuthenticated"))) {
            try {
                List<AppointmentUpdateResponseDTO> updated = userService.rescheduleByCar(carId, requestDTO);
                return ResponseEntity.ok(updated);
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", e.getMessage()));
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Unauthorized"));
    }
    
    
    // Delete Appointment controller 
    @DeleteMapping("/appointments/{customerId}/{groupId}")
    public ResponseEntity<?> deleteAppointmentGroup(
            @PathVariable Long customerId,
            @PathVariable Long groupId,
            HttpSession session) {

        if (Boolean.TRUE.equals(session.getAttribute("userAuthenticated"))) {
            try {
                userService.deleteAppointmentGroup(groupId, customerId);
                return ResponseEntity.ok(Map.of("message", "Appointment group deleted successfully"));
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", e.getMessage()));
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Unauthorized"));
    }


    /* ✅ Fetch a specific appointment by ID (Authenticated)
    @GetMapping("/appointments/details/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable Long id, HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("userAuthenticated"))) {
            UserAppointmentResponseDTO appointment = userService.getAppointmentById(id);
            return ResponseEntity.ok(appointment);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }  */
    	
    
    // ----------------------------------------------------- DISCOUNT MANAGEMENT -------------------------------------------------

    // Fetch applicable discounts for the logged-in user
    @GetMapping("/discounts")
    public ResponseEntity<?> getUserDiscounts(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId != null && Boolean.TRUE.equals(session.getAttribute("userAuthenticated"))) {
            List<UserDiscountResponseDTO> discounts = userService.getDiscountsByUserId(userId);
            return ResponseEntity.ok(discounts);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }   
    
    // ----------------------------------------------------- FACELIFT KIT MANAGEMENT ----------------------------------------------- 

    // Fetch available facelift kits for the user's car
    @GetMapping("/facelift/{customerId}")
    public ResponseEntity<?> getFaceliftKitsForUserCar(HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("userAuthenticated"))) {
            Long userId = (Long) session.getAttribute("userId"); // Assuming you store userId in session
            
            // Get facelift kit information for the user's cars
            List<UserFaceliftKitDTO> faceliftInfo = userService.getFaceliftKitsForUserCar(userId);
            
            // Check if any facelift kits are found
            if (faceliftInfo != null && !faceliftInfo.isEmpty()) {
                return ResponseEntity.ok(faceliftInfo); // Return the facelift kits info
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "No facelift kit available for your car"));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Unauthorized")); // Unauthorized response if not authenticated
    }    
    
    // *************************************** Facelift kit bookings ****************************************
    
    // Book Facelift Kits
    @PostMapping("/facelift/book/{customerId}")
    public ResponseEntity<?> bookFaceliftKit(
            @PathVariable Long customerId,
            @RequestBody FaceliftKitBookingRequest request,
            HttpSession session) {

        if (Boolean.TRUE.equals(session.getAttribute("userAuthenticated"))) {
            FaceliftKitBookingResponse response = userService.bookFaceliftKit(customerId, request);
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Unauthorized"));
    }

    // Fetch the Bookings of kits
    @GetMapping("/facelift/bookings/{customerId}")
    public ResponseEntity<?> getAllBookings(
            @PathVariable Long customerId,
            HttpSession session) {

        if (Boolean.TRUE.equals(session.getAttribute("userAuthenticated"))) {
            List<FaceliftKitBookingResponse> response = userService.getBookingsForCustomer(customerId);
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Unauthorized"));
    }

}

    
    
