package lk.anjula.hotelreservationsystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class BlockBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String travelCompanyName;
    private Integer numberOfRooms;
    private Double discountedRate;
    private LocalDate startDate;
    private LocalDate endDate;
}
