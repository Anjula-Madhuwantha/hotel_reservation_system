package lk.anjula.hotelreservationsystem.controller.request;

import jakarta.validation.constraints.NotNull;
import lk.anjula.hotelreservationsystem.model.PaymentMethod;
import lombok.Data;

@Data
public class CheckOutRequest {
    @NotNull
    private Long reservationId;
    private PaymentMethod paymentMethod; // CASH, CREDIT_CARD
    private Double optionalCharges;
}
