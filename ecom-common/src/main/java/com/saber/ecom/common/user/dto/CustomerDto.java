package com.saber.ecom.common.user.dto;

import lombok.*;

import java.io.Serializable;
@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto implements Serializable {
    private String userName;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String phone;
    private AddressDto billingAddress;
    private AddressDto shippingAddress;
}
