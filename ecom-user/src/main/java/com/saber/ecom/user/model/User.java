package com.saber.ecom.user.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name = "user_info")
@Data
@EqualsAndHashCode
@ToString
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id",unique = true)
    private String userId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phone;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "billing_address_id")
    private Address billingAddress;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shipping_address_id")
    private Address shippingAddress;
}