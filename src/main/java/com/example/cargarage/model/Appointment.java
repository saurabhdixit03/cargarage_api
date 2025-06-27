package com.example.cargarage.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

// This entity represents appointments made by customers for car services.
// It links to customer, car, service, and optionally a discount.
@Entity
public class Appointment {

    // Primary key - unique ID for each appointment
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Group ID to link multiple appointments (e.g., rescheduled ones)
    private Long groupId;

    // Customer who made the appointment
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonManagedReference
    private Customer customer;

    // Car for which the appointment is booked
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id", nullable = false)
    @JsonManagedReference
    private Car car;

    // Service selected for the appointment
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_id", nullable = false)
    @JsonManagedReference
    private Services service;

    // Final budget for the service
    private Double budget;

    // Date of the appointment
    private String appointmentDate;

    // Status of the appointment (e.g., pending, completed)
    @JsonIgnoreProperties
    private String status;

    // Optional discount applied to the appointment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id", nullable = true)
    private Discount discount;

    // Default constructor for JPA
    public Appointment() {}

    // Constructor for manually creating Appointment objects
    public Appointment(Long id, Customer customer, Car car, Services service, Double budget, String appointmentDate, String status, Long groupId) {
        this.id = id;
        this.customer = customer;
        this.car = car;
        this.service = service;
        this.budget = budget;
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.groupId = groupId;
    }

    // Method to update certain fields of the appointment
    public void updateAppointment(Car car, String appointmentDate, Services service) {
        this.car = car;
        this.appointmentDate = appointmentDate;
        this.service = service;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Services getService() {
        return service;
    }

    public void setService(Services service) {
        this.service = service;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    // toString method for logging/debugging
    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", groupId=" + groupId +
                ", customer=" + customer +
                ", car=" + car +
                ", service=" + service +
                ", budget=" + budget +
                ", appointmentDate='" + appointmentDate + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
