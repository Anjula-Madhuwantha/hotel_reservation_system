package lk.anjula.hotelreservationsystem.repository;

import lk.anjula.hotelreservationsystem.model.BlockBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockBookingRepository extends JpaRepository<BlockBooking, Long> {
}
