package lk.anjula.hotelreservationsystem.repository;

import lk.anjula.hotelreservationsystem.model.Reservation;
import lk.anjula.hotelreservationsystem.model.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByReservationStatusAndCheckInDate(ReservationStatus status, LocalDate date);
    List<Reservation> findByCheckInDateBetween(LocalDate start, LocalDate end);

    @Query("SELECT r FROM Reservation r WHERE " +
            "(:status IS NULL OR r.reservationStatus = :status) AND " +
            "(:customerId IS NULL OR r.customer.id = :customerId) AND " +
            "(:startDate IS NULL OR r.checkInDate >= :startDate) AND " +
            "(:endDate IS NULL OR r.checkOutDate <= :endDate)")
    Page<Reservation> findByFilters(
            @Param("status") ReservationStatus status,
            @Param("customerId") Long customerId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);
}
