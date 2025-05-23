package lk.anjula.hotelreservationsystem.repository;

import lk.anjula.hotelreservationsystem.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
