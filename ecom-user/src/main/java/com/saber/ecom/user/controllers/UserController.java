package com.saber.ecom.user.controllers;

import com.saber.ecom.common.user.dto.CustomerDto;
import com.saber.ecom.common.user.dto.UserCredentialDto;
import com.saber.ecom.user.exception.InvalidUserException;
import com.saber.ecom.user.model.User;
import com.saber.ecom.user.repositories.OnlineUserRepository;
import com.saber.ecom.user.services.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/customer")
@Slf4j
public class UserController {

    private final CustomerService customerService;

    public UserController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<String> addCustomer(@RequestBody CustomerDto customerDto) {
        log.info("Creating customer With First Name ====> {}", customerDto.getFirstName());
        try {
            customerService.saveCustomer(customerDto);
            return ResponseEntity.ok("Success");
        } catch (Exception ex) {
            log.error("Customer Existing with SameName ====> {}", customerDto.getFirstName());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("FAIL");
        }
    }

    @PostMapping(value = "/validateCustomer")
    public ResponseEntity<User> validateCredential(@RequestParam String userId, @RequestParam String password) {
        UserCredentialDto userCredentialDto = new UserCredentialDto();
        userCredentialDto.setUserName(userId);
        userCredentialDto.setPassword(password);
        try {
            User user = customerService.validateCustomer(userCredentialDto);
            return ResponseEntity.ok(user);
        } catch (InvalidUserException ex) {
            return ResponseEntity.notFound().build();
        }
    }

}
