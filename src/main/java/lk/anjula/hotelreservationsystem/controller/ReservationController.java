package lk.anjula.hotelreservationsystem.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lk.anjula.hotelreservationsystem.controller.request.BlockBookingRequest;
import lk.anjula.hotelreservationsystem.controller.request.CheckInRequest;
import lk.anjula.hotelreservationsystem.controller.request.CheckOutRequest;
import lk.anjula.hotelreservationsystem.controller.request.ReservationRequest;
import lk.anjula.hotelreservationsystem.controller.response.BillingResponse;
import lk.anjula.hotelreservationsystem.controller.response.ReservationResponse;
import lk.anjula.hotelreservationsystem.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @RolesAllowed({"ADMIN","CUSTOMER"})
    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@Valid @RequestBody ReservationRequest request) {
        return ResponseEntity.ok(reservationService.createReservation(request));
    }

    @RolesAllowed({"ADMIN","CUSTOMER"})
    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponse> updateReservation(@PathVariable Long id, @Valid @RequestBody ReservationRequest request) {
        return ResponseEntity.ok(reservationService.updateReservation(id, request));
    }

    @RolesAllowed({"ADMIN","CUSTOMER"})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.ok().build();
    }

    @RolesAllowed({"ADMIN","CUSTOMER"})
    @PostMapping("/check-in")
    public ResponseEntity<ReservationResponse> checkIn(@Valid @RequestBody CheckInRequest request) {
        return ResponseEntity.ok(reservationService.checkIn(request));
    }

    @RolesAllowed({"ADMIN","CUSTOMER"})
    @PostMapping("/check-out")
    public ResponseEntity<BillingResponse> checkOut(@Valid @RequestBody CheckOutRequest request) {
        return ResponseEntity.ok(reservationService.checkOut(request));
    }

    @RolesAllowed({"ADMIN","CUSTOMER"})
    @PostMapping("/block-booking")
    public ResponseEntity<Void> createBlockBooking(@Valid @RequestBody BlockBookingRequest request) {
        reservationService.createBlockBooking(request);
        return ResponseEntity.ok().build();
    }

    @RolesAllowed({"ADMIN"})
    @GetMapping
    public ResponseEntity<Page<ReservationResponse>> getAllReservations(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(reservationService.getAllReservations(status, customerId, startDate, endDate, pageable));
    }
}
