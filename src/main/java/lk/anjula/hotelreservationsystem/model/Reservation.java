package lk.anjula.hotelreservationsystem.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Room room;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer occupants;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    private String creditCardDetails;
}
