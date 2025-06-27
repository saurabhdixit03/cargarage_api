package com.example.cargarage.admin.service;

import com.example.cargarage.repository.AdminRepository;
import com.example.cargarage.repository.AppointmentRepository;
import com.example.cargarage.repository.CarRepository;
import com.example.cargarage.repository.CustomerRepository;
import com.example.cargarage.repository.DiscountRepository;
import com.example.cargarage.repository.FaceliftKitBookingRepository;
import com.example.cargarage.repository.FaceliftkitRepositoy;
import com.example.cargarage.repository.ServiceRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

//DTO file import
import com.example.cargarage.dto.CustomerWithDetailsDTO;
import com.example.cargarage.admin.service.EmailService;
import com.example.cargarage.dto.AdminAppointmentRequestDTO;
//import com.example.cargarage.dto.RepeatCustomerDiscountDTO;
import com.example.cargarage.dto.AdminAppointmentResponseDTO;
import com.example.cargarage.dto.CarDTO;
import com.example.cargarage.dto.CarWithCustomerDTO;
import com.example.cargarage.dto.CustomerDTO;
import com.example.cargarage.dto.DiscountDTO;
import com.example.cargarage.dto.FaceliftKitBookingResponse;
import com.example.cargarage.dto.websocket.FaceliftBookingStatusUpdate;
import com.example.cargarage.model.Admin;
import com.example.cargarage.model.Appointment;
import com.example.cargarage.model.BookingStatus;
import com.example.cargarage.model.Car;
import com.example.cargarage.model.Customer;
import com.example.cargarage.model.CustomerType;
import com.example.cargarage.model.Discount;
import com.example.cargarage.model.FaceliftKit;
import com.example.cargarage.model.FaceliftKitBooking;
import com.example.cargarage.model.Services;

import org.springframework.security.crypto.password.PasswordEncoder;

// for real time implementation 
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Service
public class AdminService {
	
		// REPOSRITORY INJECTION
	
	 	@Autowired
	    private AdminRepository adminRepository;
	 	
	    @Autowired
	    private ServiceRepository serviceRepository;  
	 
	    @Autowired
	    private CustomerRepository customerRepository;	
	    
	    @Autowired
	    private CarRepository carRepository;  
	    
	    @Autowired
	    private AppointmentRepository appointmentRepository;	
	     
	    @Autowired
	    private DiscountRepository discountRepository;			
	    
	    @Autowired 
	    private FaceliftkitRepositoy faceliftRepository;		
	  
	    @Autowired
	    private PasswordEncoder passwordEncoder;
	    
	    @Autowired
	    private EmailService emailService; 
	    
	    @Autowired
	    private FaceliftKitBookingRepository bookingRepository;
	    
	    @Autowired
	    private SimpMessagingTemplate messagingTemplate;

	    
	    
	    //------------------------------------------------ ADMIN MANAGEMENT------------------------------------------
	    
	    public Admin registerAdmin(Admin admin) {
	        return adminRepository.save(admin);
	    }

	    public Admin findAdminByEmail(String email) {
	        return adminRepository.findByEmail(email);
	    }
	    
	    // Fetch admin profile by ID
	    public Optional<Admin> findAdminById(Long adminId) {
	        return adminRepository.findById(adminId);
	    }

	    // Update admin details
	    public Admin updateAdmin(Long id, Admin updatedAdmin) {
	        Optional<Admin> existingAdminOpt = adminRepository.findById(id);
	        if (existingAdminOpt.isPresent()) {
	            Admin existingAdmin = existingAdminOpt.get();
	            existingAdmin.setName(updatedAdmin.getName());
	            existingAdmin.setEmail(updatedAdmin.getEmail());
	            existingAdmin.setPassword(updatedAdmin.getPassword());
	           
	            return adminRepository.save(existingAdmin);
	        }
	        return null;
	    }
	    
	    // ----------------------------------- Service Management Service logic --------------------------------------
	    
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
	    
	    // -------------------------------------------- Customers Management Service logic -------------------------------------------
	    
	    // Fetch all customers
	    public List<Customer> getAllCustomers() {
	        return customerRepository.findAll();
	    }

	    // Fetch a specific customer by ID
	    public Customer getCustomerById(Long id) {
	        return customerRepository.findById(id).orElse(null);
	    }

	    @Transactional
	    public String deleteCustomerById(Long id) {
	        if (customerRepository.existsById(id)) {
	            customerRepository.deleteById(id);  // Delete customer
	            return "Customer and associated cars removed successfully";
	        }
	        return "Customer not found";
	    }
	    
	    // Added Later While frontend design 
	    
	    public void CustomerService(CustomerRepository customerRepository) {
	        this.customerRepository = customerRepository;
	    }

	    public List<Customer> getRepeatCustomers() {
	        return customerRepository.findRepeatCustomers();
	    }  
	    
	    // ************************************* Customers With Details DTO implementation *********************************
	    
	    public List<CustomerWithDetailsDTO> getAllCustomersWithDetails() {
	        return customerRepository.findAll().stream().map(customer -> {
	            // Get all cars for the customer
	            List<Car> cars = carRepository.findByCustomerId(customer.getId());

	            // Get all appointments for the customer
	            List<Appointment> appointments = appointmentRepository.findByCustomerId(customer.getId());

	            // Map the customer into the DTO
	            CustomerWithDetailsDTO dto = new CustomerWithDetailsDTO();
	            dto.setCustomerId(customer.getId());
	            dto.setName(customer.getName());
	            dto.setMobile(customer.getMobile());
	            dto.setEmail(customer.getEmail());
	            dto.setAddress(customer.getAddress());

	            // Map the cars list
	            List<CarDTO> carDTOs = cars.stream().map(car -> {
	                CarDTO carDTO = new CarDTO();
	                carDTO.setCarModel(car.getModel());
	                carDTO.setCarColor(car.getColor());
	                carDTO.setLicensePlate(car.getLicensePlate());

	                // Filter appointments for the current car
	                List<AdminAppointmentRequestDTO> carAppointments = appointments.stream()
	                    .filter(appointment -> appointment.getCar().getId().equals(car.getId())) // Filter by car ID
	                    .map(appointment -> {
	                        AdminAppointmentRequestDTO adminAppointmentRequestDTO = new AdminAppointmentRequestDTO();
	                        adminAppointmentRequestDTO.setAppointmentDate(appointment.getAppointmentDate());
	                        adminAppointmentRequestDTO.setServiceType(appointment.getService().getName());
	                        adminAppointmentRequestDTO.setStatus(appointment.getStatus());
	                        return adminAppointmentRequestDTO;
	                    }).collect(Collectors.toList());

	                // Set the appointments list specific to the car
	                carDTO.setAppointments(carAppointments);

	                return carDTO;
	            }).collect(Collectors.toList());

	            dto.setCars(carDTOs);

	            return dto;
	        }).collect(Collectors.toList());
	    }

	 
	    // ---------------------------------------------- Car Management -------------------------------------------------------
	    
	    // Fetch all cars in the system
	    public List<Car> getAllCars() {
	        return carRepository.findAll();
	    }

	    // Fetch all cars owned by a specific customer
	    public List<Car> getCarsByCustomerId(Long customerId) {
	        return carRepository.findByCustomerId(customerId);
	    }
	    
	    // ******************************* Cars With Details DTO Implementation *********************************
	    
	    public List<CarWithCustomerDTO> getCarsGroupedByModelWithCustomers() {
	        List<Customer> allCustomers = customerRepository.findAll();
	        Map<String, List<Customer>> modelToCustomersMap = new HashMap<>();
	        Map<String, List<CarDTO>> modelToCarDTOsMap = new HashMap<>();

	        for (Customer customer : allCustomers) {
	            List<Car> cars = carRepository.findByCustomerId(customer.getId());

	            // Prepare customer DTO once
	            CustomerDTO ownerDTO = new CustomerDTO();
	            ownerDTO.setId(customer.getId());
	            ownerDTO.setName(customer.getName());
	            ownerDTO.setEmail(customer.getEmail());
	            ownerDTO.setMobile(customer.getMobile());
	            ownerDTO.setAddress(customer.getAddress());

	            for (Car car : cars) {
	                String model = car.getModel();

	                // Add customer to model grouping
	                modelToCustomersMap.computeIfAbsent(model, k -> new ArrayList<>()).add(customer);

	                // Build CarDTO with owner
	                CarDTO carDto = new CarDTO();
	                carDto.setCarModel(car.getModel());
	                carDto.setCarColor(car.getColor());
	                carDto.setLicensePlate(car.getLicensePlate());
	                carDto.setOwner(ownerDTO); // ✅ attach the owner

	                // Add carDTO to model grouping
	                modelToCarDTOsMap.computeIfAbsent(model, k -> new ArrayList<>()).add(carDto);
	            }
	        }

	        // Build final result list
	        List<CarWithCustomerDTO> result = new ArrayList<>();

	        for (String model : modelToCustomersMap.keySet()) {
	            List<Customer> customers = modelToCustomersMap.get(model);

	            List<CustomerDTO> customerDTOs = customers.stream().map(customer -> {
	                CustomerDTO dto = new CustomerDTO();
	                dto.setId(customer.getId());
	                dto.setName(customer.getName());
	                dto.setEmail(customer.getEmail());
	                dto.setMobile(customer.getMobile());
	                dto.setAddress(customer.getAddress());
	                return dto;
	            }).collect(Collectors.toList());

	            List<CarDTO> carDTOs = modelToCarDTOsMap.getOrDefault(model, new ArrayList<>());

	            CarWithCustomerDTO carWithCustomerDTO = new CarWithCustomerDTO();
	            carWithCustomerDTO.setCarModel(model);
	            carWithCustomerDTO.setNumberOfCustomers(customerDTOs.size());
	            carWithCustomerDTO.setCustomers(customerDTOs);
	            carWithCustomerDTO.setCars(carDTOs);

	            result.add(carWithCustomerDTO);
	        }

	        return result;
	    }

	      
	    //------------------------------------------ Appointment Management Service logic -----------------------------------------
	    
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
	    
	    // ********************************** Admin Appointment Management ************************************
	    
	    // Fetch all appointments with required fields
	    public List<AdminAppointmentResponseDTO> getAllAppointmentsWithDetails() {
	        return appointmentRepository.findAll().stream().map(appointment -> {
	            Customer customer = appointment.getCustomer();
	            Car car = appointment.getCar();
	            Services service = appointment.getService();

	            AdminAppointmentResponseDTO dto = new AdminAppointmentResponseDTO();
	            dto.setAppointmentId(appointment.getId());
	            dto.setAppointmentDate(appointment.getAppointmentDate());
	            dto.setStatus(appointment.getStatus());
	            dto.setCustomerName(customer.getName());
	            dto.setMobile(customer.getMobile());
	            dto.setCarModel(car.getModel());
	            dto.setServiceType(service.getName());
	            dto.setBudget(service.getBudget());

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

		public Object getUserAppointments(Long userId) {
			return null;
		}
	    
	    
	    
	    // ---------------------------------------------Discount Management Service logic-----------------------------------------
	    
	    public AdminService(DiscountRepository discountRepository) {
	        this.discountRepository = discountRepository;
	    }

	   public Discount addDiscount(DiscountDTO dto) {
	        Discount discount = new Discount();
	        discount.setName(dto.getName());
	        discount.setDescription(dto.getDescription());
	        discount.setDiscountPercentage(dto.getDiscountPercentage());
	        discount.setValidUntil(dto.getValidUntil());
	        discount.setCustomerType(dto.getCustomerType());

	        // ✅ Handle per-customer discount
	        if (dto.getCustomerId() != null) {
	            customerRepository.findById(dto.getCustomerId()).ifPresent(discount::setCustomer);
	        }

	        // ✅ Link selected services
	        List<Services> services = serviceRepository.findAllById(dto.getServiceIds());
	        discount.setServices(services); // fix typo: setservices -> setServices

	        // ✅ Save the discount to the DB
	        Discount savedDiscount = discountRepository.save(discount);

	        // ✅ Email Notification Logic
	        if (dto.getCustomerType() != null) {
	            List<Customer> targetCustomers = new ArrayList<>();
	            String subject;

	            if (dto.getCustomerType() == CustomerType.REPEAT) {
	                targetCustomers = customerRepository.findRepeatCustomers(); // make sure this is implemented
	                subject = "Special Discount Available for You!";
	            } else {
	                targetCustomers = customerRepository.findRegularCustomers(); // make sure this is implemented
	                subject = "Exclusive Discount for You!";
	            }

	            
	            System.out.println("🎯 Total Customers Fetched: " + targetCustomers.size());

	            // ✅ Compose and send dynamic emails
	            for (Customer customer : targetCustomers) {
	                if (customer.getEmail() != null && !customer.getEmail().isEmpty()) {
	                    StringBuilder emailBody = new StringBuilder();
	                    emailBody.append("Dear ").append(customer.getName()).append(",\n\n");

	                    if (dto.getCustomerType() == CustomerType.REPEAT) {
	                        emailBody.append("We are excited to offer you a special discount of ")
	                                 .append(discount.getDiscountPercentage()).append("% on the following services:\n");
	                    } else {
	                        emailBody.append("Thank you for being a loyal customer! We're pleased to offer you a discount of ")
	                                 .append(discount.getDiscountPercentage()).append("% on the following services:\n");
	                    }

	                    // ✅ Add services list
	                    for (Services service : services) {
	                        emailBody.append("- ").append(service.getName()).append(": ").append(service.getDescription()).append("\n");
	                    }

	                    emailBody.append("\nThis offer is valid until ").append(discount.getValidUntil()).append(".\n\n")
	                             .append("Thank you for choosing us!\n\n")
	                             .append("Best regards,\nThe CarGarage Team");

	                    emailService.sendEmail(customer.getEmail(), subject, emailBody.toString());
	                }
	            }
	        }

	        return savedDiscount;
	    }

	    // get all discounts
	    @Transactional
	    public List<DiscountDTO> getAllDiscounts() {
	        List<Discount> discounts = discountRepository.findAll();

	        return discounts.stream().map(discount -> {
	            DiscountDTO dto = new DiscountDTO();
	            dto.setId(discount.getId());
	            dto.setName(discount.getName());
	            dto.setDescription(discount.getDescription());
	            dto.setDiscountPercentage(discount.getDiscountPercentage());
	            dto.setValidUntil(discount.getValidUntil());
	            dto.setCustomerType(discount.getCustomerType());

	            // Handle customer
	            if (discount.getCustomer() != null) {
	                dto.setCustomerId(discount.getCustomer().getId());
	            }

	            // Extract service IDs and names
	            List<Services> services = discount.getServices();

	            List<Long> serviceIds = services.stream()
	                    .map(Services::getId)
	                    .collect(Collectors.toList());
	            dto.setServiceIds(serviceIds);

	            List<String> serviceNames = services.stream()
	                    .map(Services::getName)
	                    .collect(Collectors.toList());
	            dto.setServiceNames(serviceNames);

	            return dto;
	        }).collect(Collectors.toList());
	    }

	    public List<Discount> getDiscountsByCustomerId(Long customerId) {
	        return discountRepository.findByCustomerId(customerId);
	    }

	    public List<Discount> getCommonDiscounts() {
	        return discountRepository.findByCustomerIdIsNull();
	    }

	    public Discount updateDiscount(Long id, DiscountDTO dto) {
	        Discount existingDiscount = discountRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Discount not found"));

	        existingDiscount.setName(dto.getName());
	        existingDiscount.setDescription(dto.getDescription());
	        existingDiscount.setDiscountPercentage(dto.getDiscountPercentage());
	        existingDiscount.setValidUntil(dto.getValidUntil());
	        existingDiscount.setCustomerType(dto.getCustomerType());

	        if (dto.getCustomerId() != null) {
	            customerRepository.findById(dto.getCustomerId()).ifPresent(existingDiscount::setCustomer);
	        } else {
	            existingDiscount.setCustomer(null);
	        }

	        List<Services> services = serviceRepository.findAllById(dto.getServiceIds());
	        existingDiscount.setServices(services);

	        return discountRepository.save(existingDiscount);
	    }

	    public void deleteDiscount(Long id) {
	        discountRepository.deleteById(id);
	    }
	    

	    
	    //------------------------------------------------------- Facelift Kit Management -------------------------------------------------
	    
	    // Get all kits
	    public List<FaceliftKit> getAllKits() {
	        return faceliftRepository.findAll();
	    }

	    // Get a kit by its ID
	    public FaceliftKit getFaceliftKitById(Long id) {
	        return faceliftRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Kit not found"));
	    }

	    // Get kits by car model
	    public List<FaceliftKit> getKitsByCarModel(String carModel) {
	        return faceliftRepository.findByCarModel(carModel);
	    }

	    // Add a new facelift kit (with multiple images if provided)
	    public FaceliftKit addFaceliftKit(FaceliftKit kit) {
	    	FaceliftKit savedKit = faceliftRepository.save(kit);

	    	// Find customers whose cars match the kit’s car model
	    	List<Customer> matchingCustomers = customerRepository.findCustomersByCarModel(kit.getCarModel());

	    	for (Customer customer : matchingCustomers) {
	    	    String subject = "New Facelift Kit Available for Your Car!";
	    	    String body = String.format(
	    	        "Hello %s,\n\nWe’ve added a new facelift kit for your %s model!\n\nKit Name: %s\nPrice: ₹%.2f\nDescription: %s\n\nVisit the app to explore and book it.\n\nThanks,\nCarGarage Team",
	    	        customer.getName(),
	    	        kit.getCarModel(),
	    	        kit.getKitName(),
	    	        kit.getPrice(),
	    	        kit.getDescription()
	    	    );

	    	    emailService.sendEmail(customer.getEmail(), subject, body);
	    	}
	    	return savedKit;
	    }

	    // Update an existing facelift kit
	    public FaceliftKit updateFaceliftKit(Long id, FaceliftKit updatedKit) {
	        FaceliftKit existingKit = faceliftRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Kit not found"));

	        existingKit.setCarMake(updatedKit.getCarMake());
	        existingKit.setCarModel(updatedKit.getCarModel());
	        existingKit.setKitName(updatedKit.getKitName());
	        existingKit.setAvailableCount(updatedKit.getAvailableCount());
	        existingKit.setDescription(updatedKit.getDescription());
	        existingKit.setPrice(updatedKit.getPrice());
	        existingKit.setImages(updatedKit.getImages()); // <-- Support for multiple images
	        existingKit.setActive(updatedKit.isActive());

	        return faceliftRepository.save(existingKit);
	    }

	    // Delete a kit by its ID
	    public void deleteFaceliftKit(Long id) {
	        faceliftRepository.deleteById(id);
	    }	    
	    
	    // ************************************** Facelift kit Booking *********************************************

		public List<FaceliftKitBookingResponse> getAllFaceliftBookings() {
		    List<FaceliftKitBooking> bookings = bookingRepository.findAll();
		    List<FaceliftKitBookingResponse> responses = new ArrayList<>();

		    for (FaceliftKitBooking booking : bookings) {
		        FaceliftKitBookingResponse response = new FaceliftKitBookingResponse();
		        response.setBookingId(booking.getId());
		        response.setFaceliftKitId(booking.getFaceliftKit().getId());
		        response.setKitName(booking.getFaceliftKit().getKitName());
		        response.setCarId(booking.getCar().getId());
		        response.setCarModel(booking.getCar().getModel());

		        // 🧑‍💼 Customer Info
		        Customer customer = booking.getCustomer();
		      
		        response.setCustomerName(customer.getName());
		        response.setMobile(customer.getMobile());
		        response.setDropOffDate(booking.getDropOffDate());
		        response.setPickUpDate(booking.getPickUpDate());
		        response.setBookingStatus(booking.getBookingStatus());
		        responses.add(response);
		    }

		    return responses;
		}

		public FaceliftKitBookingResponse updateBookingStatus(Long bookingId, BookingStatus newStatus) {
		    FaceliftKitBooking booking = bookingRepository.findById(bookingId)
		            .orElseThrow(() -> new RuntimeException("Booking not found"));

		    booking.setBookingStatus(newStatus);
		    bookingRepository.save(booking);

		    // 💥 Real-time push to user
		    FaceliftBookingStatusUpdate update = new FaceliftBookingStatusUpdate(bookingId, newStatus);
		    messagingTemplate.convertAndSend("/booking-status/update", update); // client should subscribe here

		    FaceliftKitBookingResponse response = new FaceliftKitBookingResponse();
		    response.setBookingId(booking.getId());
		    response.setFaceliftKitId(booking.getFaceliftKit().getId());
		    response.setKitName(booking.getFaceliftKit().getKitName());
		    response.setCarId(booking.getCar().getId());
		    response.setCarModel(booking.getCar().getModel());

		    Customer customer = booking.getCustomer();
		    response.setCustomerName(customer.getName());
		    response.setMobile(customer.getMobile());
		    response.setDropOffDate(booking.getDropOffDate());
		    response.setPickUpDate(booking.getPickUpDate());
		    response.setBookingStatus(booking.getBookingStatus());

		    return response;
		}

	    
			    
}
