package lk.anjula.hotelreservationsystem.service;

import lk.anjula.hotelreservationsystem.controller.request.BlockBookingRequest;
import lk.anjula.hotelreservationsystem.controller.request.CheckInRequest;
import lk.anjula.hotelreservationsystem.controller.request.CheckOutRequest;
import lk.anjula.hotelreservationsystem.controller.request.ReservationRequest;
import lk.anjula.hotelreservationsystem.controller.response.BillingResponse;
import lk.anjula.hotelreservationsystem.controller.response.ReservationResponse;

public interface ReservationService {
    ReservationResponse createReservation(ReservationRequest request);
    ReservationResponse updateReservation(Long id, ReservationRequest request);
    void cancelReservation(Long id);
    ReservationResponse checkIn(CheckInRequest request);
    BillingResponse checkOut(CheckOutRequest request);
    void handleNoShowsAndBilling();
    void createBlockBooking(BlockBookingRequest request);
}
