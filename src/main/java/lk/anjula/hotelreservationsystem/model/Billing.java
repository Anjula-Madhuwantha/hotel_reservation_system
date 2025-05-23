package lk.anjula.hotelreservationsystem.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Billing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Reservation reservation;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private LocalDate billingDate;

    @Enumerated(EnumType.STRING)
    private BillingStatus billingStatus;

    private Double optionalCharges;
}
