package com.awbd.roomdetails_service.repository;

import com.awbd.roomdetails_service.model.RoomDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomDetailsRepository extends JpaRepository<RoomDetails, Long> {
}
