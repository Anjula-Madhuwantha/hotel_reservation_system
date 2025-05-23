package lk.anjula.hotelreservationsystem.controller.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BlockBookingRequest {
    @NotBlank
    private String travelCompanyName;
    @Min(3)
    private Integer numberOfRooms;
    @Positive
    private Double discountedRate;
    @NotNull
    @FutureOrPresent
    private LocalDate startDate;
    @NotNull
    @Future
    private LocalDate endDate;
}
