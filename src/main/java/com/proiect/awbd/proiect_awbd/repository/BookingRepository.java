package com.proiect.awbd.proiect_awbd.repository;

import com.proiect.awbd.proiect_awbd.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
