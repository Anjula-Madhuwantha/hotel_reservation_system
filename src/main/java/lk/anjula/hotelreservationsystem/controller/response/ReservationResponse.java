package lk.anjula.hotelreservationsystem.controller.response;

import lk.anjula.hotelreservationsystem.model.ReservationStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationResponse {
    private Long id;
    private Long customerId;
    private Long roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer occupants;
    private ReservationStatus reservationStatus;
}
