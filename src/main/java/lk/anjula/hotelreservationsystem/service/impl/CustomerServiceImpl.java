package lk.anjula.hotelreservationsystem.service.impl;

import lk.anjula.hotelreservationsystem.controller.response.CustomerResponse;
import lk.anjula.hotelreservationsystem.exception.ResourceNotFoundException;
import lk.anjula.hotelreservationsystem.model.Customer;
import lk.anjula.hotelreservationsystem.repository.CustomerRepository;
import lk.anjula.hotelreservationsystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public CustomerResponse createCustomer(Customer customer) {
        Customer saved = customerRepository.save(customer);
        CustomerResponse response = new CustomerResponse();
        response.setId(saved.getId());
        response.setName(saved.getName());
        response.setEmail(saved.getEmail());
        response.setPhone(saved.getPhone());
        return response;
    }

    @Override
    public CustomerResponse getCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setEmail(customer.getEmail());
        response.setPhone(customer.getPhone());
        return response;
    }
}
