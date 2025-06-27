package com.example.cargarage.dto.websocket;

import com.example.cargarage.model.BookingStatus;

public class FaceliftBookingStatusUpdate {
    private Long bookingId;
    private BookingStatus newStatus;

    public FaceliftBookingStatusUpdate(Long bookingId, BookingStatus newStatus) {
        this.bookingId = bookingId;
        this.newStatus = newStatus;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public BookingStatus getNewStatus() {
        return newStatus;
    }
}
