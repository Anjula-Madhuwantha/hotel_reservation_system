package lk.anjula.hotelreservationsystem.service;

import lk.anjula.hotelreservationsystem.controller.request.CustomerAuthRequest;
import lk.anjula.hotelreservationsystem.controller.response.CustomerRes;
import lk.anjula.hotelreservationsystem.controller.response.CustomerResponse;
import lk.anjula.hotelreservationsystem.exception.UserAlreadyRegisteredException;
import lk.anjula.hotelreservationsystem.exception.UserNotFoundException;

import java.util.List;

public interface CustomerService {
//    CustomerResponse createCustomer(Customer customer);
//    CustomerResponse getCustomer(Long id);
    CustomerResponse create(CustomerAuthRequest customerAuthRequest) throws UserAlreadyRegisteredException;
    CustomerResponse login(CustomerAuthRequest customerAuthRequest) throws UserNotFoundException;

    CustomerResponse getCustomerById(Long id) throws UserNotFoundException;
    List<CustomerResponse> getAllCustomers();
}
