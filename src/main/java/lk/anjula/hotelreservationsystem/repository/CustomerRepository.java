package lk.anjula.hotelreservationsystem.repository;

import lk.anjula.hotelreservationsystem.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByUsername(String username);
    Customer findByEmail(String email);

    @Query("SELECT c.role FROM Customer c WHERE c.username = :username")
    String findRoleByUsername(String username);
}
