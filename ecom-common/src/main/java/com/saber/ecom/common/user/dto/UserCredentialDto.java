package com.saber.ecom.common.user.dto;

import lombok.*;

import java.io.Serializable;
@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentialDto implements Serializable {
    private String userName;
    private String password;
}
