package com.example.cargarage.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "facelift_kit_bookings")
public class FaceliftKitBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Links to FaceliftKit
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "facelift_kit_id", nullable = false)
    private FaceliftKit faceliftKit;

    // Links to Customer
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // Links to Car
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    // Drop-off date chosen by user
    private LocalDate dropOffDate;

    // Pick-up date chosen by user (optional, could be null initially)
    private LocalDate pickUpDate;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    // Booking creation timestamp
    private LocalDate bookingDate;

    public FaceliftKitBooking() {
    }

    public FaceliftKitBooking(FaceliftKit faceliftKit, Customer customer, Car car, LocalDate dropOffDate, LocalDate pickUpDate, BookingStatus bookingStatus, LocalDate bookingDate) {
        this.faceliftKit = faceliftKit;
        this.customer = customer;
        this.car = car;
        this.dropOffDate = dropOffDate;
        this.pickUpDate = pickUpDate;
        this.bookingStatus = bookingStatus;
        this.bookingDate = bookingDate;
    }

    // getters & setters

    public Long getId() {
        return id;
    }

    public FaceliftKit getFaceliftKit() {
        return faceliftKit;
    }

    public void setFaceliftKit(FaceliftKit faceliftKit) {
        this.faceliftKit = faceliftKit;
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

    public LocalDate getDropOffDate() {
        return dropOffDate;
    }

    public void setDropOffDate(LocalDate dropOffDate) {
        this.dropOffDate = dropOffDate;
    }

    public LocalDate getPickUpDate() {
        return pickUpDate;
    }

    public void setPickUpDate(LocalDate pickUpDate) {
        this.pickUpDate = pickUpDate;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }
}
