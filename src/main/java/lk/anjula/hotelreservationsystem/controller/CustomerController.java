package lk.anjula.hotelreservationsystem.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lk.anjula.hotelreservationsystem.controller.request.CustomerAuthRequest;
import lk.anjula.hotelreservationsystem.controller.response.CustomerRes;
import lk.anjula.hotelreservationsystem.controller.response.CustomerResponse;
import lk.anjula.hotelreservationsystem.controller.response.MessageResponse;
import lk.anjula.hotelreservationsystem.exception.UserAlreadyRegisteredException;
import lk.anjula.hotelreservationsystem.exception.UserNotFoundException;
import lk.anjula.hotelreservationsystem.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {

    private CustomerService customerService;

    @PostMapping("/sign-up")
    public ResponseEntity<CustomerResponse> authenticate(@Valid @RequestBody CustomerAuthRequest customerAuthRequest) throws UserAlreadyRegisteredException {

        CustomerResponse customerResponse = customerService.create(customerAuthRequest);
        MessageResponse messageResponse = MessageResponse.builder()
                .message("Customer registered successfully").build();
        return new ResponseEntity<>(customerResponse, HttpStatus.OK);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<CustomerResponse> login(@RequestBody CustomerAuthRequest customerAuthRequest) throws UserNotFoundException {

        CustomerResponse customerResponse = customerService.login(customerAuthRequest);
        return new ResponseEntity<>(customerResponse, HttpStatus.OK);
    }

    @RolesAllowed("ADMIN")
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id) throws UserNotFoundException {
        CustomerResponse customer = customerService.getCustomerById(id);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @RolesAllowed("ADMIN")
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        List<CustomerResponse> customers = customerService.getAllCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @RolesAllowed("ADMIN")
    @GetMapping("/admin")
    public String sayHiAdmin() {
        return "Hi Admin";
    }

    @RolesAllowed("CUSTOMER")
    @GetMapping("/customer")
    public String sayHiShopOwner() {
        return "Hi Customer";
    }

    @RolesAllowed("TRAVEL_COMPANY")
    @GetMapping("/travel-company")
    public String sayHiTravelCompany() {
        return "Hi Travel company";
    }
}
