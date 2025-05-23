package lk.anjula.hotelreservationsystem.controller.response;

import lk.anjula.hotelreservationsystem.model.RoomType;
import lombok.Data;

@Data
public class RoomResponse {
    private Long id;
    private String roomNumber;
    private RoomType roomType;
    private Double pricePerNight;
    private Integer maxOccupants;
    private Boolean isAvailable;
}
