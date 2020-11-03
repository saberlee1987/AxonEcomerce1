package com.saber.ecom.user.services.impl;

import com.saber.ecom.common.user.dto.AddressDto;
import com.saber.ecom.common.user.dto.CustomerDto;
import com.saber.ecom.common.user.dto.UserCredentialDto;
import com.saber.ecom.user.exception.InvalidUserException;
import com.saber.ecom.user.model.*;
import com.saber.ecom.user.repositories.OnlineUserRepository;
import com.saber.ecom.user.repositories.UserRepository;
import com.saber.ecom.user.services.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final OnlineUserRepository onlineUserRepository;
    private final UserRepository userRepository;

    public CustomerServiceImpl(OnlineUserRepository onlineUserRepository, UserRepository userRepository) {
        this.onlineUserRepository = onlineUserRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void saveCustomer(CustomerDto customerDto) {
        log.info("Start Save Customer");
        log.info("Saving Customer with First Name : {}", customerDto.getFirstName());
        OnlineUser onlineUser = new OnlineUser();
        onlineUser.setScreenName(customerDto.getUserName());
        onlineUser.setPassword(customerDto.getPassword());
        onlineUser.setActive(true);
        onlineUser.addRole(new UserRole(RoleEnum.CUSTOMER_READ));
        onlineUser.addRole(new UserRole(RoleEnum.ORDER_READ));
        onlineUser.addRole(new UserRole(RoleEnum.PRODUCT_READ));
        onlineUser.addRole(new UserRole(RoleEnum.ORDER_WRITE));

        onlineUserRepository.save(onlineUser);

        User user = new User();
        user.setUserId(customerDto.getUserName());
        user.setFirstName(customerDto.getFirstName());
        user.setLastName(customerDto.getLastName());
        user.setEmail(customerDto.getEmail());
        user.setPhone(customerDto.getPhone());

        if (customerDto.getBillingAddress() != null) {
            Address billingAddress = new Address();
            AddressDto addressDto = customerDto.getBillingAddress();
            billingAddress.setApartment(addressDto.getApartment());
            billingAddress.setCountry(addressDto.getCountry());
            billingAddress.setPin(addressDto.getPin());
            billingAddress.setProvince(addressDto.getProvince());
            billingAddress.setState(addressDto.getState());
            billingAddress.setStreet(addressDto.getStreet());
            user.setBillingAddress(billingAddress);
        }
        if (customerDto.getShippingAddress() != null) {
            Address shippingAddress = new Address();
            AddressDto addressDto = customerDto.getShippingAddress();
            shippingAddress.setApartment(addressDto.getApartment());
            shippingAddress.setCountry(addressDto.getCountry());
            shippingAddress.setPin(addressDto.getPin());
            shippingAddress.setProvince(addressDto.getProvince());
            shippingAddress.setState(addressDto.getState());
            shippingAddress.setStreet(addressDto.getStreet());
            user.setShippingAddress(shippingAddress);
        }
        userRepository.save(user);
        log.info("End Save Customer ");
    }

    @Override
    @Transactional
    public User findCustomer(String customerId) {
        log.info("Start Find Customer By Id ===>  {}", customerId);
        log.info("Finding Customer With CustomerId ===> {}", customerId);
        User user = userRepository.findByUserId(customerId);
        if (user == null) {
            throw new InvalidUserException(customerId);
        }
        log.info("Ending Find Customer");
        return user;
    }


    public User validateCustomer(UserCredentialDto userCredentialDto) {
        log.info("Start Validate Customer");
        log.info("Validating Customer With Username : {} ", userCredentialDto.getUserName());
        OnlineUser onlineUser = onlineUserRepository.findByScreenName(userCredentialDto.getUserName());
        if (onlineUser == null || (!userCredentialDto.getPassword().equals(onlineUser.getPassword()))) {
            throw new InvalidUserException(userCredentialDto.getUserName());
        }
        User user = userRepository.findByUserId(userCredentialDto.getUserName());
        log.info("Ending Validate Customer ");
        return user;
    }
}
