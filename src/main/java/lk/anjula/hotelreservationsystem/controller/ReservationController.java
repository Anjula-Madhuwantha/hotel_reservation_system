package lk.anjula.hotelreservationsystem.controller;

import jakarta.validation.Valid;
import lk.anjula.hotelreservationsystem.controller.request.BlockBookingRequest;
import lk.anjula.hotelreservationsystem.controller.request.CheckInRequest;
import lk.anjula.hotelreservationsystem.controller.request.CheckOutRequest;
import lk.anjula.hotelreservationsystem.controller.request.ReservationRequest;
import lk.anjula.hotelreservationsystem.controller.response.BillingResponse;
import lk.anjula.hotelreservationsystem.controller.response.ReservationResponse;
import lk.anjula.hotelreservationsystem.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@Valid @RequestBody ReservationRequest request) {
        return ResponseEntity.ok(reservationService.createReservation(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponse> updateReservation(@PathVariable Long id, @Valid @RequestBody ReservationRequest request) {
        return ResponseEntity.ok(reservationService.updateReservation(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/check-in")
    public ResponseEntity<ReservationResponse> checkIn(@Valid @RequestBody CheckInRequest request) {
        return ResponseEntity.ok(reservationService.checkIn(request));
    }

    @PostMapping("/check-out")
    public ResponseEntity<BillingResponse> checkOut(@Valid @RequestBody CheckOutRequest request) {
        return ResponseEntity.ok(reservationService.checkOut(request));
    }

    @PostMapping("/block-booking")
    public ResponseEntity<Void> createBlockBooking(@Valid @RequestBody BlockBookingRequest request) {
        reservationService.createBlockBooking(request);
        return ResponseEntity.ok().build();
    }
}
