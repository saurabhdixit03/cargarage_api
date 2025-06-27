package com.example.cargarage.dto;

import java.time.LocalDate;

import com.example.cargarage.model.BookingStatus;

public class FaceliftKitBookingResponse {

    private Long bookingId;
    private String customerName;
    private String mobile;
    private Long faceliftKitId;
    private String kitName;
    private Long carId;
    private String carModel;
    private LocalDate dropOffDate;
    private LocalDate pickUpDate;
    private BookingStatus bookingStatus;

    // getters & setters

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }
    
    public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Long getFaceliftKitId() {
        return faceliftKitId;
    }

    public void setFaceliftKitId(Long faceliftKitId) {
        this.faceliftKitId = faceliftKitId;
    }

    public String getKitName() {
        return kitName;
    }

    public void setKitName(String kitName) {
        this.kitName = kitName;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
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
}
