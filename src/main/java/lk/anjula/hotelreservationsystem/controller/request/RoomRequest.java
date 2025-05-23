package lk.anjula.hotelreservationsystem.controller.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lk.anjula.hotelreservationsystem.model.RoomType;
import lombok.Data;

@Data
public class RoomRequest {
    @NotBlank(message = "Room number is required")
    private String roomNumber;
    @NotNull(message = "Room type is required")
    private RoomType roomType;
    @Positive(message = "Price per night must be positive")
    private Double pricePerNight;
    @Min(value = 1, message = "Max occupants must be at least 1")
    private Integer maxOccupants;
    @NotNull(message = "Availability status is required")
    private Boolean isAvailable;
}
