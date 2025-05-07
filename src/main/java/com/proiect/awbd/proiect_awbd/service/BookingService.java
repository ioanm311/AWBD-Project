package com.proiect.awbd.proiect_awbd.service;

import com.proiect.awbd.proiect_awbd.model.Booking;

import java.util.List;

public interface BookingService {
    Booking saveBooking(Booking booking);
    List<Booking> getAllBookings();
    Booking getBookingById(Long id);
    void deleteBooking(Long id);
}
