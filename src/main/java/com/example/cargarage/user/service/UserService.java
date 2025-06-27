package com.example.cargarage.user.service;

import com.example.cargarage.dto.AppointmentUpdateRequestDTO;
import com.example.cargarage.dto.AppointmentUpdateResponseDTO;
import com.example.cargarage.dto.FaceliftKitBookingRequest;
import com.example.cargarage.dto.FaceliftKitBookingResponse;
import com.example.cargarage.dto.UserAppointmentRequestDTO;
import com.example.cargarage.dto.UserAppointmentResponseDTO;
import com.example.cargarage.dto.UserDiscountResponseDTO;
import com.example.cargarage.dto.UserFaceliftKitDTO;
import com.example.cargarage.model.*;
import com.example.cargarage.repository.*;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
	
	// REPOSRITORY INJECTION

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private ServiceRepository servicesRepository;

    @Autowired
    private FaceliftkitRepositoy faceliftkitRepository; 
    
    @Autowired
    private FaceliftKitBookingRepository bookingRepository;
    
    // ---------------------------------------------------- USER MANAGEMENT -----------------------------------------------

    // Register a new customer
    public Customer registerCustomer(Customer customer) {
        customer.setCustomerType(CustomerType.REGULAR); // 👈 Default set on registration
        return userRepository.save(customer);
    }

    // Find a customer by email
    public Customer findCustomerByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    // Fetch all available services
    public List<Services> getAllServices() {
        return servicesRepository.findAll();
    }

    // Search services by name
    public List<Services> searchServicesByName(String name) {
        return servicesRepository.findByNameContainingIgnoreCase(name);
    }

    /* Book an appointment
    public Appointment bookAppointment(Long customerId, Appointment appointment) {
        Customer customer = userRepository.findById(customerId)
                              .orElseThrow(() -> new RuntimeException("Customer not found!"));
        appointment.setCustomer(customer); // set the Customer Object
        appointment.setStatus("pending"); // Default status
        return appointmentRepository.save(appointment);
    }

    // Update appointments
    public Appointment updateAppointment(Long customerId, Long appointmentId, Appointment updatedAppointment) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found!"));

        if (!appointment.getCustomer().getId().equals(customerId)) {
            throw new RuntimeException("Unauthorized access to this appointment.");
        }

        appointment.setAppointmentDate(updatedAppointment.getAppointmentDate());

        return appointmentRepository.save(appointment);
    }

    // Fetch all appointments by user
    public List<Appointment> getAppointmentsByCustomerId(Long customerId) {
        return appointmentRepository.findByCustomerId(customerId);
    }

    // Fetch a specific appointment by ID
    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }  */

    // Update customer details
    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        Optional<Customer> existingCustomerOpt = userRepository.findById(id);
        if (existingCustomerOpt.isPresent()) {
            Customer existingCustomer = existingCustomerOpt.get();
            existingCustomer.setName(updatedCustomer.getName());
            existingCustomer.setEmail(updatedCustomer.getEmail());
            existingCustomer.setPassword(updatedCustomer.getPassword());
            existingCustomer.setAddress(updatedCustomer.getAddress());
            existingCustomer.setMobile(updatedCustomer.getMobile());

            return userRepository.save(existingCustomer);
        }
        return null;
    }

    // Fetch all cars owned by a customer
    public List<Car> getCarsByCustomerId(Long customerId) {
        return carRepository.findByCustomerId(customerId);
    }
    
    public Car addCarToCustomer(Long customer, Car car) {
        Optional<Customer> customerOpt = userRepository.findById(customer);
        if (customerOpt.isPresent()) {
            car.setCustomer(customerOpt.get());
            return carRepository.save(car);
        }
        throw new IllegalArgumentException("Customer not found");
    }

    // Update car details
    public Car updateCar(Long carId, Car updatedCar) {
        Optional<Car> existingCarOpt = carRepository.findById(carId);
        if (existingCarOpt.isPresent()) {
            Car existingCar = existingCarOpt.get();
            existingCar.setMake(updatedCar.getMake());
            existingCar.setModel(updatedCar.getModel());
            existingCar.setColor(updatedCar.getColor());
            existingCar.setLicensePlate(updatedCar.getLicensePlate());
            return carRepository.save(existingCar);
        }
        return null;
    }

    // Delete a car owned by the user
    public String deleteCar(Long carId) {
        if (carRepository.existsById(carId)) {
            carRepository.deleteById(carId);
            return "Car deleted successfully.";
        }
        return "Car not found.";
    }
    
    
    // --------------------------------------------------- Discount Management ----------------------------------------------------

    // Discount Management
    public List<UserDiscountResponseDTO> getDiscountsByUserId(Long userId) {
        return userRepository.findById(userId)
            .map(user -> {
                List<Discount> discounts = new ArrayList<>();

                // User-specific discounts
                List<Discount> userSpecificDiscounts = discountRepository.findByCustomerId(userId);
                discounts.addAll(userSpecificDiscounts);

                // General discounts based on customer type
                if (user.getCustomerType() == CustomerType.REGULAR || user.getCustomerType() == CustomerType.REPEAT) {
                    discounts.addAll(discountRepository.findByCustomerType(CustomerType.REGULAR));
                }
                if (user.getCustomerType() == CustomerType.REPEAT) {
                    discounts.addAll(discountRepository.findByCustomerType(CustomerType.REPEAT));
                }

                return discounts.stream().map(discount -> {
                    UserDiscountResponseDTO dto = new UserDiscountResponseDTO();
                    dto.setId(discount.getId());
                    dto.setName(discount.getName());
                    dto.setDescription(discount.getDescription());
                    dto.setDiscountPercentage(discount.getDiscountPercentage());
                    dto.setValidUntil(discount.getValidUntil());
                    dto.setCustomerType(discount.getCustomerType());
                    
                    List<Long> serviceIds = discount.getServices().stream()
                            .map(Services::getId)
                            .collect(Collectors.toList());
                    dto.setServiceIds(serviceIds);

                    dto.setServiceIds(serviceIds);
                    dto.setDiscountId(discount.getId());
                    // dto.setCustomerId(discount.getCustomerId());
                    //   dto.getServiceIds(discount.getServiceids());  Develop later 

                    List<String> serviceNames = discount.getServices().stream()
                            .map(Services::getName)
                            .collect(Collectors.toList());
                    dto.setServiceNames(serviceNames);

                    return dto;
                }).collect(Collectors.toList());
            })
            .orElse(Collections.emptyList());
    }
      
    // Fetch customer profile by ID
    public Optional<Customer> findCustomerById(Long customerId) {
        return userRepository.findById(customerId);
    }

    
    // -------------------------------------------------- DTO implementation for appointments -----------------------------------------------
    
    // Book an appointment (Updated to set customerType)
    public List<UserAppointmentResponseDTO> bookAppointment(Long customerId, UserAppointmentRequestDTO requestDTO) {
        Customer customer = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Car car = carRepository.findById(requestDTO.getCarId())
                .orElseThrow(() -> new RuntimeException("Car not found"));

        List<Long> serviceIds = requestDTO.getServiceIds();
        if (serviceIds == null || serviceIds.isEmpty()) {
            throw new RuntimeException("At least one service must be selected");
        }

        // Determine if customer is "repeat" based on existing appointments
        long appointmentCount = appointmentRepository.countByCustomerId(customerId);
        if (appointmentCount > 0) {
        	customer.setCustomerType(CustomerType.REPEAT);  // ✅ Correct

        } else {
        	customer.setCustomerType(CustomerType.REGULAR);  // ✅ Correct
        }

        // Save updated customer type
        userRepository.save(customer);

        List<UserAppointmentResponseDTO> responseList = new ArrayList<>();
        List<Appointment> savedAppointments = new ArrayList<>();
        
        Long discountId = requestDTO.getDiscountId(); // get it once
        Discount discount = null;
        
        Double budget = requestDTO.getBudget();

        if (discountId != null) {
            discount = discountRepository.findById(discountId).orElse(null);
        }

        for (Long serviceId : serviceIds) {
            Services service = servicesRepository.findById(serviceId)
                    .orElseThrow(() -> new RuntimeException("Service not found"));

            Appointment appointment = new Appointment();
            appointment.setCustomer(customer);
            appointment.setCar(car);
            appointment.setService(service);
            appointment.setBudget(budget);
            appointment.setAppointmentDate(requestDTO.getAppointmentDate());
            appointment.setStatus("Pending");
            appointment.setDiscount(discount);

            Appointment savedAppointment = appointmentRepository.save(appointment);
            savedAppointments.add(savedAppointment);
        }

        // ✅ Set groupId to the first appointment's ID
        Long groupId = savedAppointments.get(0).getId();
        for (Appointment appt : savedAppointments) {
            appt.setGroupId(groupId);
        }
        appointmentRepository.saveAll(savedAppointments); // save updated groupId

        for (Appointment savedAppointment : savedAppointments) {
            responseList.add(new UserAppointmentResponseDTO(
                    savedAppointment.getId(),
                    savedAppointment.getAppointmentDate(),
                    savedAppointment.getStatus(),
                    customer.getName(),
                    customer.getMobile(),
                    car.getId(),
                    car.getModel(),
                    car.getLicensePlate(),
                    savedAppointment.getService().getName(), budget, groupId, 
                    discount != null ? discount.getId() : null // ✅ send actual discount I
            ));
        }

        return responseList;
    }

    
    // Get all appointments for a customer
    public List<UserAppointmentResponseDTO> getAppointmentsByCustomerId(Long customerId) {
        List<Appointment> appointments = appointmentRepository.findAppointmentsByCustomerWithDetails(customerId);
        return appointments.stream()
                .map(appointment -> new UserAppointmentResponseDTO(
                        appointment.getId(),
                        appointment.getAppointmentDate(),
                        appointment.getStatus(),
                        appointment.getCustomer().getName(),
                        appointment.getCustomer().getMobile(),
                        appointment.getCar().getId(),
                        appointment.getCar().getModel(),
                        appointment.getCar().getLicensePlate(),
                        appointment.getService().getName(), 
                        appointment.getService().getBudget(),
                        appointment.getGroupId(),
                        appointment.getDiscount() != null ? appointment.getDiscount().getId() : null))
                .collect(Collectors.toList());
    }
    
    // Appointment Rescheduling
    public List<AppointmentUpdateResponseDTO> rescheduleByCar(Long carId, AppointmentUpdateRequestDTO requestDTO) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found"));

        // Fetch all upcoming appointments for this car
        List<Appointment> existingAppointments = appointmentRepository.findByCarIdAndStatus(carId, "Pending");
        if (existingAppointments.isEmpty()) {
            throw new RuntimeException("No active appointments found for this car");
        }

        Customer customer = existingAppointments.get(0).getCustomer(); // assumed same

        // Delete old appointments
        appointmentRepository.deleteAll(existingAppointments);

        // Create new ones
        List<Services> services = servicesRepository.findAllById(requestDTO.getServiceIds());
        if (services.isEmpty()) {
            throw new RuntimeException("At least one service must be selected");
        }

        List<Appointment> newAppointments = new ArrayList<>();
        for (Services service : services) {
            Appointment a = new Appointment();
            a.setCar(car);
            a.setCustomer(customer);
            a.setAppointmentDate(requestDTO.getAppointmentDate());
            a.setStatus("Pending");
            a.setService(service);
            newAppointments.add(a);
        }

        return appointmentRepository.saveAll(newAppointments).stream().map(app -> {
            AppointmentUpdateResponseDTO dto = new AppointmentUpdateResponseDTO();
            dto.setAppointmentId(app.getId());
            dto.setAppointmentDate(app.getAppointmentDate());
            dto.setStatus(app.getStatus());
            dto.setCarId(car.getId());
            dto.setCarModel(car.getModel());
            dto.setServiceNames(List.of(app.getService().getName()));
            return dto;
        }).collect(Collectors.toList());
    }

    
    // Delete Appointment Controller service logic
    @Transactional
    public void deleteAppointmentGroup(Long groupId, Long customerId) {
        List<Appointment> appointments = appointmentRepository.findByGroupId(groupId);

        if (appointments.isEmpty()) {
            throw new RuntimeException("No appointments found with groupId: " + groupId);
        }

        // Validate if the appointment belongs to the customer
        if (!appointments.get(0).getCustomer().getId().equals(customerId)) {
            throw new RuntimeException("Unauthorized delete attempt.");
        }

        appointmentRepository.deleteAll(appointments);
    }
    
    // ---------------------------------------------------------- Fetch all facelift kits ----------------------------------------------------
    
    // Fetch Facelift kits 
    public List<UserFaceliftKitDTO> getFaceliftKitsForUserCar(Long customerId) {
        List<Car> cars = carRepository.findByCustomerId(customerId);
        List<UserFaceliftKitDTO> carFaceliftInfoList = new ArrayList<>();

        for (Car car : cars) {
            List<FaceliftKit> kitsForCar = faceliftkitRepository
                .findByCarMakeAndCarModelAndIsActiveTrue(car.getMake(), car.getModel());

            for (FaceliftKit kit : kitsForCar) {
                UserFaceliftKitDTO dto = new UserFaceliftKitDTO(
                    kit.getId(),                        // faceliftKitId
                    car.getId(),                        // carId
                    car.getMake() + " " + car.getModel(), // carName
                    kit.getKitName(),
                    kit.getDescription(),
                    kit.getPrice(),
                    kit.getImages()
                );
                carFaceliftInfoList.add(dto);
            }
        }

        return carFaceliftInfoList;
    }
    
    // ************************************* Facelift kit booking ***************************************
    
    public List<FaceliftKitBookingResponse> getBookingsForCustomer(Long customerId) {
        Customer customer = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        List<FaceliftKitBooking> bookings = bookingRepository.findByCustomer(customer);
        List<FaceliftKitBookingResponse> responses = new ArrayList<>();

        for (FaceliftKitBooking booking : bookings) {
            FaceliftKitBookingResponse response = new FaceliftKitBookingResponse();
            response.setBookingId(booking.getId());

            // Defensive null checks for related entities
            if (booking.getFaceliftKit() != null) {
                response.setFaceliftKitId(booking.getFaceliftKit().getId());
                response.setKitName(booking.getFaceliftKit().getKitName());
            }
            if (booking.getCar() != null) {
                response.setCarId(booking.getCar().getId());
                response.setCarModel(booking.getCar().getModel());
            }
            response.setDropOffDate(booking.getDropOffDate());
            response.setPickUpDate(booking.getPickUpDate());
            response.setBookingStatus(booking.getBookingStatus());

            responses.add(response);
        }
        return responses;
    }

    public FaceliftKitBookingResponse bookFaceliftKit(Long customerId, FaceliftKitBookingRequest request) {
        if (request.getCarId() == null || request.getCarId() == 0) {
            throw new RuntimeException("Invalid Car ID");
        }
        if (request.getFaceliftKitId() == null || request.getFaceliftKitId() == 0) {
            throw new RuntimeException("Invalid Facelift Kit ID");
        }
        if (request.getDropOffDate() == null) {
            throw new RuntimeException("Drop-off date is required");
        }

        Customer customer = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Car car = carRepository.findById(request.getCarId())
                .orElseThrow(() -> new RuntimeException("Car not found"));

        FaceliftKit kit = faceliftkitRepository.findById(request.getFaceliftKitId())
                .orElseThrow(() -> new RuntimeException("Facelift kit not found"));

        FaceliftKitBooking booking = new FaceliftKitBooking();
        booking.setCustomer(customer);
        booking.setCar(car);
        booking.setFaceliftKit(kit);
        booking.setDropOffDate(request.getDropOffDate());
        booking.setPickUpDate(request.getPickUpDate());
        booking.setBookingStatus(BookingStatus.REQUESTED);
        booking.setBookingDate(LocalDate.now());

        bookingRepository.save(booking);

        FaceliftKitBookingResponse response = new FaceliftKitBookingResponse();
        response.setBookingId(booking.getId());
        response.setFaceliftKitId(kit.getId());
        response.setKitName(kit.getKitName());
        response.setCarId(car.getId());
        response.setCarModel(car.getModel());
        response.setDropOffDate(booking.getDropOffDate());
        response.setPickUpDate(booking.getPickUpDate());
        response.setBookingStatus(booking.getBookingStatus());

        return response;
    }

   
}
