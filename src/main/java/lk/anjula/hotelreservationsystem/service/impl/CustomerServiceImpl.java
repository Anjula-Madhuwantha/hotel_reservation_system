package lk.anjula.hotelreservationsystem.service.impl;

import lk.anjula.hotelreservationsystem.controller.request.CustomerAuthRequest;
import lk.anjula.hotelreservationsystem.controller.response.CustomerResponse;
import lk.anjula.hotelreservationsystem.exception.UserAlreadyRegisteredException;
import lk.anjula.hotelreservationsystem.exception.UserNotFoundException;
import lk.anjula.hotelreservationsystem.model.Customer;
import lk.anjula.hotelreservationsystem.repository.CustomerRepository;
import lk.anjula.hotelreservationsystem.security.ApplicationConfig;
import lk.anjula.hotelreservationsystem.security.JwtService;
import lk.anjula.hotelreservationsystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Override
    public CustomerResponse create(CustomerAuthRequest customerAuthRequest) throws UserAlreadyRegisteredException {
        Customer existingCustomer = customerRepository.findByUsername(customerAuthRequest.getUsername());
        if (existingCustomer != null) {
            throw new UserAlreadyRegisteredException("Customer already registered with username: " + customerAuthRequest.getUsername());
        }

        Customer existingEmailCustomer = customerRepository.findByEmail(customerAuthRequest.getEmail());
        if (existingEmailCustomer != null) {
            throw new UserAlreadyRegisteredException("Customer already registered with email: " + customerAuthRequest.getEmail());
        }

        Customer customer = new Customer();
        customer.setName(customerAuthRequest.getName());
        customer.setEmail(customerAuthRequest.getEmail());
        customer.setPhone(customerAuthRequest.getPhone());
        customer.setUsername(customerAuthRequest.getUsername());
        customer.setPassword(passwordEncoder.encode(customerAuthRequest.getPassword()));
        customer.setRole(customerAuthRequest.getRole());

        Customer saved = customerRepository.save(customer);

        String token = jwtService.generateToken(customer, new java.util.HashMap<>());
        return CustomerResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .email(saved.getEmail())
                .phone(saved.getPhone())
                .username(saved.getUsername())
                .token(token)
                .role(saved.getRole())
                .build();
    }

    @Override
    public CustomerResponse login(CustomerAuthRequest customerAuthRequest) throws UserNotFoundException {
        Customer customer = customerRepository.findByUsername(customerAuthRequest.getUsername());
        if (customer == null || !passwordEncoder.matches(customerAuthRequest.getPassword(), customer.getPassword())) {
            throw new UserNotFoundException("Invalid username or password");
        }

        String token = jwtService.generateToken(customer, new java.util.HashMap<>());
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .username(customer.getUsername())
                .token(token)
                .role(customer.getRole())
                .build();
    }
}
