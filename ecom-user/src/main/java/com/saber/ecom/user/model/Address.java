package com.saber.ecom.user.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "address")
@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Address implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "APARTMENT")
    private String apartment;
    @Column(name = "STREET")
    private String street;
    @Column(name = "PROVINCE")
    private String province;
    @Column(name = "STATE")
    private String state;
    @Column(name = "PIN")
    private String pin;
    @Column(name = "COUNTRY")
    private String country;
}
