package com.example.cargarage.service.user;

import com.example.cargarage.model.common.*;
import com.example.cargarage.repository.common.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

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
    private FaceliftkitRepositoy faceliftkitRepository; // Added for Facelift Kits

    // Register a new customer
    public Customer registerCustomer(Customer customer) {
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

    // Book an appointment
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
    }

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

    // Fetch all available discounts
    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }

    // Fetch discounts available for a specific user
    public List<Discount> getDiscountsByUserId(Long userId) {
        return discountRepository.findByCustomerId(userId);
    }

    // Fetch customer profile by ID
    public Optional<Customer> findCustomerById(Long customerId) {
        return userRepository.findById(customerId);
    }

    // Fetch all facelift kits
    public List<FaceliftKit> getAllKits() {
        return faceliftkitRepository.findAll();
    }
}
