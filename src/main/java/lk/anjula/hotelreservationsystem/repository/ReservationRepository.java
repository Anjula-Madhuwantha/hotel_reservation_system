package lk.anjula.hotelreservationsystem.repository;

import lk.anjula.hotelreservationsystem.model.Reservation;
import lk.anjula.hotelreservationsystem.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByReservationStatusAndCheckInDate(ReservationStatus status, LocalDate date);
    List<Reservation> findByCheckInDateBetween(LocalDate start, LocalDate end);
}
