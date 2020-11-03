package com.saber.ecom.common.user.dto;

import lombok.*;

import java.io.Serializable;
@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto implements Serializable {
    private String apartment;
    private String street;
    private String province;
    private String state;
    private String pin;
    private String country;
}
