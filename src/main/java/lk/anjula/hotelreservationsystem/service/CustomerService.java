package lk.anjula.hotelreservationsystem.service;

import lk.anjula.hotelreservationsystem.controller.response.CustomerResponse;
import lk.anjula.hotelreservationsystem.model.Customer;

public interface CustomerService {
    CustomerResponse createCustomer(Customer customer);
    CustomerResponse getCustomer(Long id);
}
