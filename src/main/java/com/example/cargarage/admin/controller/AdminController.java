package com.example.cargarage.admin.controller;

import jakarta.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

//DTO files import

import com.example.cargarage.dto.CustomerWithDetailsDTO;
import com.example.cargarage.dto.DiscountDTO;
import com.example.cargarage.dto.FaceliftKitBookingResponse;
import com.example.cargarage.employee.service.EmployeeService;
import com.example.cargarage.model.Admin;
import com.example.cargarage.model.Appointment;
import com.example.cargarage.model.BookingStatus;
import com.example.cargarage.model.Car;
import com.example.cargarage.model.Customer;
import com.example.cargarage.model.Discount;
import com.example.cargarage.model.Employee;
import com.example.cargarage.model.FaceliftKit;
import com.example.cargarage.model.Services;
import com.example.cargarage.admin.service.AdminService;
import com.example.cargarage.dto.AdminAppointmentResponseDTO;
//import com.example.cargarage.dto.RepeatCustomerDiscountDTO;
import com.example.cargarage.dto.CarWithCustomerDTO;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    
  @Autowired
  private EmployeeService employeeService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    // ------------------------------------------------ADMIN AUTHENTICATION-----------------------------------------------------
    
    // Register controller
    @PostMapping("/register")
    public ResponseEntity<?> registerAdmin(@RequestBody Admin admin) {
        if (adminService.findAdminByEmail(admin.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Email already registered"));
        }

        // Encrypt the password before saving
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        Admin savedAdmin = adminService.registerAdmin(admin);
        return ResponseEntity.ok(Map.of("message", "Admin registered successfully", "admin", savedAdmin));
    }

    // Login controller
    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(@RequestBody Admin admin, HttpSession session) {
        Admin storedAdmin = adminService.findAdminByEmail(admin.getEmail());

        if (storedAdmin != null && passwordEncoder.matches(admin.getPassword(), storedAdmin.getPassword())) {
        	session.setAttribute("adminId", storedAdmin.getId());
            session.setAttribute("adminEmail", storedAdmin.getEmail());
            session.setAttribute("adminName", storedAdmin.getName());
            session.setAttribute("adminAuthenticated", true);

            return ResponseEntity.ok(Map.of(
            		"adminId", storedAdmin.getId(),
                "message", "Login successful",
                "token", session.getId(),
                "email", storedAdmin.getEmail(),
                "name", storedAdmin.getName()
            ));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid email or password"));
    }
    
    // Get Admin details
    @GetMapping("/{id}")
    public ResponseEntity<?> getAdmin(@PathVariable Long id, HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("adminAuthenticated"))) {
            return ResponseEntity.ok(adminService.findAdminById(id));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }
    
    // Update Admin details
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable Long id, @RequestBody Admin updatedAdmin, HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("adminAuthenticated"))) {
            return ResponseEntity.ok(adminService.updateAdmin(id, updatedAdmin));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }
    
    // session api 
    @GetMapping("/session")
    public ResponseEntity<?> checkSession(HttpSession session) {
        Boolean isAuthenticated = (Boolean) session.getAttribute("adminAuthenticated");
        String adminEmail = (String) session.getAttribute("adminEmail");
        String adminName = (String) session.getAttribute("adminName"); // Fetch admin name from session
        
        if (Boolean.TRUE.equals(isAuthenticated) && adminEmail != null) {
            return ResponseEntity.ok(Map.of("authenticated", true, 
            		"email", adminEmail,
            		"name", adminName != null ? adminName : "Admin" )); // Ensure name is always returned
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("authenticated", false));
    }

    // Logout Controller
    @PostMapping("/logout")
    public ResponseEntity<?> logoutAdmin(HttpSession session) {
        session.invalidate(); // Clear session
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
    
    // ------------------------------------------------------- SERVICE MANAGEMENT ----------------------------------------------------------

    // Fetch all available services (Authenticated)
    @GetMapping("/services")
    public ResponseEntity<?> getAllServices(HttpSession session) {
        Boolean isAuthenticated = (Boolean) session.getAttribute("adminAuthenticated");

        if (Boolean.TRUE.equals(isAuthenticated)) {
            return ResponseEntity.ok(adminService.getAllServices());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }

    // Add a new service (Authenticated)
    @PostMapping("/services")
    public ResponseEntity<?> addService(@RequestBody Services service, HttpSession session) {
        Boolean isAuthenticated = (Boolean) session.getAttribute("adminAuthenticated");

        if (Boolean.TRUE.equals(isAuthenticated)) {
            return ResponseEntity.ok(adminService.addService(service));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }

    // Update a specific service by ID (Authenticated)
    @PutMapping("/services/{id}")
    public ResponseEntity<?> updateService(@PathVariable Long id, @RequestBody Services service, HttpSession session) {
        Boolean isAuthenticated = (Boolean) session.getAttribute("adminAuthenticated");

        if (Boolean.TRUE.equals(isAuthenticated)) {
            return ResponseEntity.ok(adminService.updateService(id, service));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }

    // Remove a service by ID (Authenticated)
    @DeleteMapping("/services/{id}")
    public ResponseEntity<?> deleteService(@PathVariable Long id, HttpSession session) {
        Boolean isAuthenticated = (Boolean) session.getAttribute("adminAuthenticated");

        if (Boolean.TRUE.equals(isAuthenticated)) {
            return ResponseEntity.ok(adminService.deleteService(id));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }
    
    
	// ---------------------------------------------------- CUSTOMER MANAGEMENT ---------------------------------------------------------
    
    // Fetch all customers (Authenticated)
    @GetMapping("/customers")
    public ResponseEntity<?> getAllCustomers(HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("adminAuthenticated"))) {
            return ResponseEntity.ok(adminService.getAllCustomers());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }

    // Fetch details of a specific customer (Authenticated)
    @GetMapping("/customers/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long id, HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("adminAuthenticated"))) {
            return ResponseEntity.ok(adminService.getCustomerById(id));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }

    // Remove a customer by ID (Authenticated)
    @DeleteMapping("/customers/{id}")
    public ResponseEntity<?> deleteCustomerById(@PathVariable Long id, HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("adminAuthenticated"))) {
            return ResponseEntity.ok(adminService.deleteCustomerById(id));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }

    // Fetch all cars owned by a specific customer (Authenticated)
    @GetMapping("/customers/{customerId}/cars")
    public ResponseEntity<?> getCarsByCustomer(@PathVariable Long customerId, HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("adminAuthenticated"))) {
            return ResponseEntity.ok(adminService.getCarsByCustomerId(customerId));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }

	// ---------------------------------------------------------- CAR MANAGEMENT -------------------------------------- ---------------------
    
    // Fetch all cars in the system (Authenticated)
    @GetMapping("/cars")
    public ResponseEntity<?> getAllCars(HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("adminAuthenticated"))) {
            return ResponseEntity.ok(adminService.getAllCars());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }

    // Fetch all cars with customer details in the system (DTO Controller)
    @GetMapping("/cars-with-customers")
    public ResponseEntity<List<CarWithCustomerDTO>> getCarsWithCustomers() {
        List<CarWithCustomerDTO> response = adminService.getCarsGroupedByModelWithCustomers();
        return ResponseEntity.ok(response);
    }
        
	// ------------------------------------------------ APPOINTMENT MANAGEMENT -------------------------------------------------------- 
    
    // Fetch all appointments for admin (Authenticated)
    @GetMapping("/appointments")
    public ResponseEntity<?> getAllAppointments(HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("adminAuthenticated"))) {
            return ResponseEntity.ok(adminService.getAllAppointments());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }

    // Fetch a specific appointment by ID (Authenticated)
    @GetMapping("/appointments/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable Long id, HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("adminAuthenticated"))) {
            return ResponseEntity.ok(adminService.getAppointmentById(id));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }

    // Update appointment status (Authenticated)
    @PutMapping("/appointments/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable Long id, @RequestBody Map<String, String> requestBody, HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("adminAuthenticated"))) {
            String newStatus = requestBody.get("status");
            return ResponseEntity.ok(adminService.updateAppointmentStatus(id, newStatus));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }

    // Delete an appointment by ID (Authenticated)
    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable Long id, HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("adminAuthenticated"))) {
            return ResponseEntity.ok(adminService.deleteAppointment(id));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }
    
    // --------------------------------------------------- DISCOUNT MANAGEMENT --------------------------------------------------
    
    // Add a new discount (Authenticated)
    @PostMapping("/discounts")
    public ResponseEntity<?> addDiscount(@RequestBody DiscountDTO dto, HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("adminAuthenticated"))) {
            return ResponseEntity.ok(adminService.addDiscount(dto));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }

    // Fetch all discounts (Authenticated)
    @GetMapping("/discounts")
    public ResponseEntity<?> getAllDiscounts(HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("adminAuthenticated"))) {
            List<DiscountDTO> discountDTOs = adminService.getAllDiscounts();
            return ResponseEntity.ok(discountDTOs);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }

    // Update a discount (Authenticated)
    @PutMapping("/discounts/{id}")
    public ResponseEntity<?> updateDiscount(@PathVariable Long id, @RequestBody DiscountDTO dto, HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("adminAuthenticated"))) {
            return ResponseEntity.ok(adminService.updateDiscount(id, dto));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }

    // Delete a discount (Authenticated)
    @DeleteMapping("/discounts/{id}")
    public ResponseEntity<?> deleteDiscount(@PathVariable Long id, HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("adminAuthenticated"))) {
            adminService.deleteDiscount(id);
            return ResponseEntity.ok(Map.of("message", "Discount deleted successfully"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }

    // Fetch repeat customers (Authenticated)
    @GetMapping("/customers/repeat")
    public ResponseEntity<?> getRepeatCustomers(HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("adminAuthenticated"))) {
            return ResponseEntity.ok(adminService.getRepeatCustomers());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }
    
    // ------------------------------------------------ FACELIFT KITS MANAGEMENT ----------------------------------------------

    // Fetch all facelift kits (Authenticated)
    @GetMapping("/facelift")
    public ResponseEntity<?> getAllKits(HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("adminAuthenticated"))) {
            return ResponseEntity.ok(adminService.getAllKits());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }

    // Fetch a specific facelift kit by ID (Authenticated)
    @GetMapping("/facelift/{id}")
    public ResponseEntity<?> getKitById(@PathVariable Long id, HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("adminAuthenticated"))) {
            return ResponseEntity.ok(adminService.getFaceliftKitById(id));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }

    // Fetch facelift kits by car model (Authenticated)
    @GetMapping("/facelift/car/{carModel}")
    public ResponseEntity<?> getKitsByCarModel(@PathVariable String carModel, HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("adminAuthenticated"))) {
            return ResponseEntity.ok(adminService.getKitsByCarModel(carModel));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }

    // Add a new facelift kit (Authenticated)
    @PostMapping("/facelift/with-images")
    public ResponseEntity<?> addFaceliftKitWithImages(
            @RequestParam("carMake") String carMake,
            @RequestParam("carModel") String carModel,
            @RequestParam("kitName") String kitName,
            @RequestParam("availableCount") Long availableCount,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            HttpSession session) {

        if (!Boolean.TRUE.equals(session.getAttribute("adminAuthenticated"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
        }

        try {
            List<String> imageUrls = new ArrayList<>();
            if (images != null && !images.isEmpty()) {
                String uploadDir = "C:/Users/Komal/DriveA/cargarage/uploads/faceliftkits";
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                for (MultipartFile file : images) {
                    String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                    File dest = new File(dir, fileName);
                    file.transferTo(dest);
                    imageUrls.add("/uploads/faceliftkits/" + fileName);
                }
            }

            // Create FaceliftKit object and set fields
            FaceliftKit kit = new FaceliftKit();
            kit.setCarMake(carMake);
            kit.setCarModel(carModel);
            kit.setKitName(kitName);
            kit.setAvailableCount(availableCount);
            kit.setDescription(description);
            kit.setPrice(price);
            kit.setImages(imageUrls);
            kit.setActive(true);  // default true

            FaceliftKit savedKit = adminService.addFaceliftKit(kit);
            return new ResponseEntity<>(savedKit, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Upload failed"));
        }
    }

    // Update a new facelift kit (Authenticated)
    @PutMapping("/facelift/with-images/{id}")
    public ResponseEntity<?> updateFaceliftKitWithImages(
            @PathVariable Long id,
            @RequestParam("carMake") String carMake,
            @RequestParam("carModel") String carModel,
            @RequestParam("kitName") String kitName,
            @RequestParam("availableCount") Long availableCount,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            HttpSession session) {

        if (!Boolean.TRUE.equals(session.getAttribute("adminAuthenticated"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
        }

        try {
            // First get existing kit from DB (using repository or service)
            Optional<FaceliftKit> optionalKit = Optional.ofNullable(adminService.getFaceliftKitById(id));
            if (optionalKit.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Kit not found"));
            }
            FaceliftKit existingKit = optionalKit.get();

            // Handle image uploads and append to existing images list
            List<String> imageUrls = new ArrayList<>(); // this clears all previous images
            if (images != null && !images.isEmpty()) {
                String uploadDir = "C:/Users/Komal/DriveA/cargarage/uploads/faceliftkits";
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                for (MultipartFile file : images) {
                    String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                    File dest = new File(dir, fileName);
                    file.transferTo(dest);
                    imageUrls.add("/uploads/faceliftkits/" + fileName);
                }
            }

            // Update the existing kit fields from @RequestParam
            existingKit.setCarMake(carMake);
            existingKit.setCarModel(carModel);
            existingKit.setKitName(kitName);
            existingKit.setAvailableCount(availableCount);
            existingKit.setDescription(description);
            existingKit.setPrice(price);
            existingKit.setImages(imageUrls);
            existingKit.setActive(true);  // or keep as is if you prefer

            // Now call your existing update method (which expects id and full FaceliftKit)
            FaceliftKit updatedKit = adminService.updateFaceliftKit(id, existingKit);

            return ResponseEntity.ok(updatedKit);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Update failed"));
        }
    }

    // Delete a facelift kit (Authenticated)
    @DeleteMapping("/facelift/{id}")
    public ResponseEntity<?> deleteFaceliftKit(@PathVariable Long id, HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("adminAuthenticated"))) {
            adminService.deleteFaceliftKit(id);
            return ResponseEntity.ok(Map.of("message", "Facelift kit deleted successfully"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }
    
    // *********************************** FIlE UPLOAD CONTROLLER ************************************
    
    @PostMapping("/facelift/image/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            String uploadDir = "C:/Users/Komal/DriveA/cargarage/uploads/faceliftkits";
            
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File dest = new File(dir, fileName);
            file.transferTo(dest);

            String imageUrl = "/uploads/faceliftkits/" + fileName;

            return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Upload failed"));
        }
    }
       
    // ******************************** Facelift kit booking Controllers ***********************************
    
    @GetMapping("/facelift/bookings")
    public ResponseEntity<List<FaceliftKitBookingResponse>> getAllFaceliftBookings() {
        return ResponseEntity.ok(adminService.getAllFaceliftBookings());
    }

    // Update facelift booking status (Authenticated)
    @PutMapping("/bookings/{bookingId}/status")
    public ResponseEntity<?> updateBookingStatus(
            @PathVariable Long bookingId,
            @RequestBody Map<String, String> requestBody,
            HttpSession session) {

        if (Boolean.TRUE.equals(session.getAttribute("adminAuthenticated"))) {
            String newStatus = requestBody.get("status");
            return ResponseEntity.ok(adminService.updateBookingStatus(bookingId, BookingStatus.valueOf(newStatus)));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Unauthorized"));
    }
    
    // -------------------------------------------------- DTO CONTROLLERS ------------------------------------------------------
    
    // Get all customers with details (Authenticated)
    @GetMapping("/customers-with-details")
    public ResponseEntity<?> getAllCustomersWithDetails(HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("adminAuthenticated"))) {
            return ResponseEntity.ok(adminService.getAllCustomersWithDetails());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }

    
    // Get all appointments with details (Authenticated)
    @GetMapping("/appointments-with-details")
    public ResponseEntity<?> getAllAppointmentsWithDetails(HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("adminAuthenticated"))) {
            return ResponseEntity.ok(adminService.getAllAppointmentsWithDetails());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }

    // Update appointment status (Authenticated)
    @PutMapping("/appointments/{id}/status")
    public ResponseEntity<?> updateAppointmentStatus(@PathVariable Long id, @RequestBody Map<String, String> request, HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("adminAuthenticated"))) {
            String status = request.get("status");
            return ResponseEntity.ok(adminService.updateAppointmentStatus(id, status));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }
    
    // ================= EMPLOYEE MANAGEMENT =================

    // Employee Registration 
    @PostMapping("/register-employee")
    public ResponseEntity<?> registerEmployee(@RequestBody Employee employeeRequest) {
        try {
            // Check if email already exists
            Optional<Employee> existingEmployee = employeeService.getEmployeeByEmail(employeeRequest.getEmail());
            if (existingEmployee.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
            }

            // Hash the password
            String hashedPassword = passwordEncoder.encode(employeeRequest.getPassword());
            employeeRequest.setPassword(hashedPassword);
            employeeRequest.setRole("EMPLOYEE"); // Just in case
            employeeRequest.setActive(true);     // Enable employee by default

            // Save employee
            employeeService.registerEmployee(employeeRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("Employee registered successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed: " + e.getMessage());
        }
    }

    // Edit Employee Details
    @PutMapping("/edit-employee/{id}")
    public ResponseEntity<?> updateEmployee(
            @PathVariable("id") Long employeeId,
            @RequestBody Map<String, Object> updates) {
        try {
            String result = employeeService.updateEmployeeDetails(employeeId, updates);
            return ResponseEntity.ok(result);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed: " + e.getMessage());
        }
    }

}
