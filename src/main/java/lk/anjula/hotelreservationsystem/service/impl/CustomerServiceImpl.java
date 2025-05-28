package lk.anjula.hotelreservationsystem.service.impl;

import lk.anjula.hotelreservationsystem.controller.request.CustomerAuthRequest;
import lk.anjula.hotelreservationsystem.controller.response.CustomerRes;
import lk.anjula.hotelreservationsystem.controller.response.CustomerResponse;
import lk.anjula.hotelreservationsystem.exception.UserAlreadyRegisteredException;
import lk.anjula.hotelreservationsystem.exception.UserNotFoundException;
import lk.anjula.hotelreservationsystem.model.Customer;
import lk.anjula.hotelreservationsystem.repository.CustomerRepository;
import lk.anjula.hotelreservationsystem.security.ApplicationConfig;
import lk.anjula.hotelreservationsystem.security.JwtService;
import lk.anjula.hotelreservationsystem.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final ApplicationConfig applicationConfig;

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
//        customer.setPassword(passwordEncoder.encode(customerAuthRequest.getPassword()));
        customer.setPassword(applicationConfig.passwordEncoder().encode(customerAuthRequest.getPassword()));
        customer.setRole(customerAuthRequest.getRole());
        customerRepository.save(customer);


//        Customer saved = customerRepository.save(customer);

        String role = customerAuthRequest.getRole().toString();
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("username", customer.getUsername());
        extraClaims.put("password", customer.getPassword());
        extraClaims.put("roles", List.of("ROLE_" + customer.getRole().name()));
        extraClaims.put("name", customer.getName());
        extraClaims.put("email", customer.getEmail());
        extraClaims.put("phone", customer.getPhone());

        String token = jwtService.generateToken(customer, extraClaims);
        return CustomerResponse.builder()
                .token(token)
                .role(customer.getRole())
                .build();

//        String token = jwtService.generateToken(customer, new java.util.HashMap<>());
//        return CustomerResponse.builder()
//                .id(saved.getId())
//                .name(saved.getName())
//                .email(saved.getEmail())
//                .phone(saved.getPhone())
//                .username(saved.getUsername())
//                .token(token)
//                .role(saved.getRole())
//                .build();
    }

    @Override
    public CustomerResponse login(CustomerAuthRequest customerAuthRequest) throws UserNotFoundException {
        Customer customer = customerRepository.findByUsername(customerAuthRequest.getUsername());
//        if (customer == null || !passwordEncoder.matches(customerAuthRequest.getPassword(), customer.getPassword())) {
//            throw new UserNotFoundException("Invalid username or password");
//        }
        if (customer != null) {
            if (!applicationConfig.passwordEncoder().matches(customerAuthRequest.getPassword(), customer.getPassword())) {
                throw new RuntimeException("Invalid username or password");
            }

        String role = customerRepository.findRoleByUsername(customer.getUsername());

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("username", customer.getUsername());
        extraClaims.put("password", customer.getPassword());
        extraClaims.put("roles", List.of("ROLE_" + customer.getRole().name()));
        extraClaims.put("name", customer.getName());
        extraClaims.put("email", customer.getEmail());
        extraClaims.put("phone", customer.getPhone());

        String token = jwtService.generateToken(customer, extraClaims);
        System.out.println(token);
        System.out.println(role);
        return CustomerResponse.builder()
                .id(customer.getId())
                .token(token)
                .role(customer.getRole())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .username(customer.getUsername())
                .build();
        }
        else {
            throw new UserNotFoundException("User not found with username" + customerAuthRequest.getUsername());
        }

//        String token = jwtService.generateToken(customer, new java.util.HashMap<>());
//        return CustomerResponse.builder()
//                .id(customer.getId())
//                .name(customer.getName())
//                .email(customer.getEmail())
//                .phone(customer.getPhone())
//                .username(customer.getUsername())
//                .token(token)
//                .role(customer.getRole())
//                .build();
    }

    @Override
    public CustomerResponse getCustomerById(Long id) throws UserNotFoundException {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Customer not found with id: " + id));

        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .username(customer.getUsername())
                .role(customer.getRole())
                .build();
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream().map(customer -> CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .username(customer.getUsername())
                .role(customer.getRole())
                .build()).toList();
    }
}
