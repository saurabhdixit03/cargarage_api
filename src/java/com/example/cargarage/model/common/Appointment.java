package com.example.cargarage.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonManagedReference
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id", nullable = false)
    @JsonManagedReference
    private Car car;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_id", nullable = false)
    @JsonManagedReference
    private Services service;

    private String appointmentDate;
    @JsonIgnoreProperties
    private String status;

    public Appointment() {}

    public Appointment(Long id, Customer customer, Car car, Services service, String appointmentDate, String status) {
        this.id = id;
        this.customer = customer;
        this.car = car;
        this.service = service;
        this.appointmentDate = appointmentDate;
        this.status = status;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Car getCar() { return car; }
    public void setCar(Car car) { this.car = car; }

    public Services getService() { return service; }
    public void setService(Services service) { this.service = service; }

    public String getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(String appointmentDate) { this.appointmentDate = appointmentDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", customer=" + customer +
                ", car=" + car +
                ", service=" + service +
                ", appointmentDate='" + appointmentDate + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
