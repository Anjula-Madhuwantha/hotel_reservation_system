package lk.anjula.hotelreservationsystem.controller.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationRequest {
    @NotNull
    private Long customerId;
    @NotNull
    private Long roomId;
    @NotNull
    @FutureOrPresent
    private LocalDate checkInDate;
    @NotNull
    @Future
    private LocalDate checkOutDate;
    @Min(1)
    private Integer occupants;
    private String creditCardDetails;
}
