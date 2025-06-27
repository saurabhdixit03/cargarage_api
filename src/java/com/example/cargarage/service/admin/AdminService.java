package com.example.cargarage.service.admin;

import com.example.cargarage.model.common.Admin;
import com.example.cargarage.model.common.Appointment;
import com.example.cargarage.model.common.Car;
import com.example.cargarage.model.common.Customer;
import com.example.cargarage.model.common.Discount;
import com.example.cargarage.model.common.FaceliftKit;
import com.example.cargarage.model.common.Services;
import com.example.cargarage.repository.common.AdminRepository;
import com.example.cargarage.repository.common.AppointmentRepository;
import com.example.cargarage.repository.common.CarRepository;
import com.example.cargarage.repository.common.CustomerRepository;
import com.example.cargarage.repository.common.DiscountRepository;
import com.example.cargarage.repository.common.FaceliftkitRepositoy;
import com.example.cargarage.repository.common.ServiceRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//DTO file import
import com.example.cargarage.dto.CustomerWithDetailsDTO;

//import com.example.cargarage.dto.RepeatCustomerDiscountDTO;
import com.example.cargarage.dto.AdminAppointmentResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
	
	 	@Autowired
	    private AdminRepository adminRepository; // Inject the Admin repository

	    @Autowired
	    private ServiceRepository serviceRepository;  // Inject the Service repository
	 
	    @Autowired
	    private CustomerRepository customerRepository;	// Inject the CustomerRepository
	    
	    @Autowired
	    private CarRepository carRepository;  // Inject the CarRepository
	    
	    @Autowired
	    private AppointmentRepository appointmentRepository;	// Inject the AppointmentRepository
	     
	    @Autowired
	    private DiscountRepository discountRepository;			// Inject the DiscountRepository
	    
	    @Autowired 
	    private FaceliftkitRepositoy faceliftrepository;		// Inject the FaceliftkitRepositoy

	    
	    public Admin registerAdmin(Admin admin) {
	        // Additional validation logic if needed
	        return adminRepository.save(admin);
	    }

	    // Find an admin by email
	    public Admin findAdminByEmail(String email) {
	        return adminRepository.findByEmail(email);  // Find Admin by email
	    }
	    
	    // Method to verify Admin login credentials
	    public boolean verifyAdmin(String email, String password) {
	        Admin admin = adminRepository.findByEmail(email);
	        if (admin != null && admin.getPassword().equals(password)) {
	            return true;
	        }
	        return false;
	    }    
	    // Service Management Service logic
	    
	    // Fetch all available services
	    public List<Services> getAllServices() {
	        return serviceRepository.findAll();  // Get all services
	    }

	    // Add a new service with its budget
	    public Services addService(Services service) {
	        return serviceRepository.save(service);  // Save the new service
	    }

	    // Update a specific service by ID
	    public Services updateService(Long id, Services updatedService) {
	        Optional<Services> existingService = serviceRepository.findById(id);
	        if (existingService.isPresent()) {
	            Services service = existingService.get();
	            service.setName(updatedService.getName());
	            service.setDescription(updatedService.getDescription());
	            service.setBudget(updatedService.getBudget());
	            service.setDiscountPercentage(updatedService.getDiscountPercentage());
	            return serviceRepository.save(service);  // Save the updated service
	        }
	        return null;  // Handle properly in real code
	    }

	    // Remove a service by ID
	    public String deleteService(Long id) {
	        if (serviceRepository.existsById(id)) {
	            serviceRepository.deleteById(id);
	            return "Service removed successfully";
	        } else {
	            return "Service not found";
	        }
	    }
	    
	    // Customers Management Service logic
	    
	    // Fetch all customers
	    public List<Customer> getAllCustomers() {
	        return customerRepository.findAll();
	    }

	    // Fetch a specific customer by ID
	    public Customer getCustomerById(Long id) {
	        return customerRepository.findById(id).orElse(null);
	    }

	    // Remove a customer by ID
	    public String deleteCustomerById(Long id) {
	        if (customerRepository.existsById(id)) {
	            customerRepository.deleteById(id);
	            return "Customer removed successfully";
	        }
	        return "Customer not found";
	    }
	    
	    // Car Management
	    
	    // Fetch all cars in the system
	    public List<Car> getAllCars() {
	        return carRepository.findAll();
	    }

	    // Fetch all cars owned by a specific customer
	    public List<Car> getCarsByCustomerId(Long customerId) {
	        return carRepository.findByCustomerId(customerId);
	    }
	    
	    // Appointment Management Service logic
	    
	    // Fetch all appointments with full data
	    public List<Appointment> getAllAppointments() {
	        return appointmentRepository.findAllWithDetails();
	    }

	    // Fetch an appointment by ID
	    public Optional<Appointment> getAppointmentById(Long id) {
	        return appointmentRepository.findById(id);
	    }

	    public Appointment updateAppointment(Long id, String newStatus) {
	        Appointment appointment = appointmentRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Appointment not found"));
	        
	        appointment.setStatus(newStatus);
	        return appointmentRepository.save(appointment);
	    }


	    // Delete an appointment by ID
	    public String deleteAppointment(Long id) {
	        if (appointmentRepository.existsById(id)) {
	            appointmentRepository.deleteById(id);
	            return "Appointment cancelled successfully";
	        } else {
	            return "Appointment not found";
	        }
	    }
	    
	    
	    // Discount Management Service logic
	    
	    public AdminService(DiscountRepository discountRepository) {
	        this.discountRepository = discountRepository;
	    }

	    public Discount addDiscount(Discount discount) {
	        return discountRepository.save(discount);
	    }

	    public List<Discount> getAllDiscounts() {
	        return discountRepository.findAll();
	    }

	    public List<Discount> getDiscountsByCustomerId(Long customerId) {
	        return discountRepository.findByCustomerId(customerId);
	    }

	    public List<Discount> getCommonDiscounts() {
	        return discountRepository.findByCustomerIdIsNull();
	    }  

	    public Discount updateDiscount(Long id, Discount updatedDiscount) {
	        Discount existingDiscount = discountRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Discount not found"));
	        existingDiscount.setName(updatedDiscount.getName());
	        existingDiscount.setDescription(updatedDiscount.getDescription());
	        existingDiscount.setDiscountPercentage(updatedDiscount.getDiscountPercentage());
	        existingDiscount.setPackageDetails(updatedDiscount.getPackageDetails());
	        existingDiscount.setValidUntil(updatedDiscount.getValidUntil());
	      //  existingDiscount.setCustomer(updatedDiscount.getCustomer());
	        return discountRepository.save(existingDiscount);
	    }

	    public void deleteDiscount(Long id) {
	        discountRepository.deleteById(id);
	    }

	    
	    // Added Later While frontend design 
	    public void CustomerService(CustomerRepository customerRepository) {
	        this.customerRepository = customerRepository;
	    }

	    public List<Customer> getRepeatCustomers() {
	        return customerRepository.findRepeatCustomers();
	    }  
	    
	    
	    public List<FaceliftKit> getAllKits() {
	        return faceliftrepository.findAll();
	    }
	    
	    public FaceliftKit getKitById(Long id) {
	        return faceliftrepository.findById(id).orElseThrow(() -> new RuntimeException("Kit not found"));
	    }

	    public List<FaceliftKit> getKitsByCarModel(String carModel) {
	        return faceliftrepository.findByCarModel(carModel);
	    }

	    public FaceliftKit addFaceliftKit(FaceliftKit kit) {
	        return faceliftrepository.save(kit);
	    }

	    public FaceliftKit updateFaceliftKit(Long id, FaceliftKit updatedKit) {
	        FaceliftKit existingKit = faceliftrepository.findById(id).orElseThrow(() -> new RuntimeException("Kit not found"));
	        existingKit.setKitName(updatedKit.getKitName());
	        existingKit.setDescription(updatedKit.getDescription());
	        existingKit.setPrice(updatedKit.getPrice());
	        existingKit.setImageUrl(updatedKit.getImageUrl());
	        existingKit.setActive(updatedKit.isActive());
	        return faceliftrepository.save(existingKit);
	    }

	    public void deleteFaceliftKit(Long id) {
	    	faceliftrepository.deleteById(id);
	    }
	    
	    // DTO implementation
	    
	    // Fetch all customers with car, appointment, and service details
	    public List<CustomerWithDetailsDTO> getAllCustomersWithDetails() {
	        return customerRepository.findAll().stream().map(customer -> {
	            Appointment appointment = appointmentRepository.findByCustomerId(customer.getId()).stream().findFirst().orElse(null);
	            if (appointment == null) return null;

	            Car car = appointment.getCar();
	            Services service = appointment.getService();

	            CustomerWithDetailsDTO dto = new CustomerWithDetailsDTO();
	            dto.setCustomerId(customer.getId());
	            dto.setName(customer.getName());
	            dto.setMobile(customer.getMobile());
	            dto.setEmail(customer.getEmail());
	            dto.setAddress(customer.getAddress());
	            dto.setCarModel(car.getModel());
	            dto.setCarColor(car.getColor());
	            dto.setLicensePlate(car.getLicensePlate());
	            dto.setAppointmentDate(appointment.getAppointmentDate());
	            dto.setServiceType(service.getName());
	            dto.setStatus(appointment.getStatus());

	            return dto;
	        }).filter(dto -> dto != null).collect(Collectors.toList());
	    }

	    // Fetch all appointments with required fields
	    public List<AppointmentResponseDTO> getAllAppointmentsWithDetails() {
	        return appointmentRepository.findAll().stream().map(appointment -> {
	            Customer customer = appointment.getCustomer();
	            Car car = appointment.getCar();
	            Services service = appointment.getService();

	            AppointmentResponseDTO dto = new AppointmentResponseDTO();
	            dto.setAppointmentId(appointment.getId());
	            dto.setAppointmentDate(appointment.getAppointmentDate());
	            dto.setStatus(appointment.getStatus());
	            dto.setCustomerName(customer.getName());
	            dto.setMobile(customer.getMobile());
	            dto.setCarModel(car.getModel());
	            dto.setServiceType(service.getName());

	            return dto;
	        }).collect(Collectors.toList());
	    }

	    // JSON Formatting 
	    
	    @Autowired
	    private ObjectMapper objectMapper;

	    public String updateAppointmentStatus(Long id, String status) {
	        Appointment appointment = appointmentRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Appointment not found"));

	        try {
	            JsonNode node = objectMapper.readTree(status);
	            appointment.setStatus(node.get("status").asText()); // Extract only the status value
	        } catch (Exception e) {
	            appointment.setStatus(status); // Store as is if it's already a plain string
	        }

	        appointmentRepository.save(appointment);
	        return "Appointment status updated successfully";
	    }

	
	   //////
	    
	/*   
	    public String assignDiscountToRepeatCustomers(RepeatCustomerDiscountDTO dto) {
    Optional<Discount> optionalDiscount = discountRepository.findById(dto.getDiscountId());
    if (!optionalDiscount.isPresent()) {
        return "Discount not found";
    }

    Discount discount = optionalDiscount.get();
    List<Customer> customers = customerRepository.findAllById(dto.getRepeatCustomerIds());

    if (customers.isEmpty()) {
        return "No valid repeat customers found";
    }

    // Fix: Assigning multiple customers correctly
    discount.getCustomers().addAll(customers);
    discountRepository.save(discount);

    return "Discount assigned to repeat customers successfully";
}

	    public List<Long> getRepeatCustomerss() {
	        return customerRepository.findRepeatCustomerIdss();
	    }
	    */
	    
}
