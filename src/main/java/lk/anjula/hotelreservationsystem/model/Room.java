package lk.anjula.hotelreservationsystem.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomNumber;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    private Double pricePerNight;
    private Integer maxOccupants;
    private Boolean isAvailable;
}
