package lk.anjula.hotelreservationsystem.controller.response;

import lombok.Data;
import java.time.LocalDate;

@Data
public class BillingResponse {
    private Long id;
    private Long reservationId;
    private Double amount;
    private String paymentMethod;
    private LocalDate billingDate;
    private String status;
    private Double optionalCharges;
}
