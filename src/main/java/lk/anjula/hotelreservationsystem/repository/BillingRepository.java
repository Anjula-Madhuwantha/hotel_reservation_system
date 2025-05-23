package lk.anjula.hotelreservationsystem.repository;

import lk.anjula.hotelreservationsystem.model.Billing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingRepository extends JpaRepository<Billing, Long> {
}
