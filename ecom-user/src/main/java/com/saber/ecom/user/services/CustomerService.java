package com.saber.ecom.user.services;

import com.saber.ecom.common.user.dto.CustomerDto;
import com.saber.ecom.common.user.dto.UserCredentialDto;
import com.saber.ecom.user.model.User;

public interface CustomerService {
    void saveCustomer(CustomerDto customerDto);
    User findCustomer(String customerId);
    User validateCustomer(UserCredentialDto userCredentialDto);
}
