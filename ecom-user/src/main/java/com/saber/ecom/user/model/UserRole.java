package com.saber.ecom.user.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "user_role")
@Data
@EqualsAndHashCode(exclude = {"onlineUser"})
@ToString(exclude = {"onlineUser"})
@NoArgsConstructor
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private RoleEnum role;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private OnlineUser onlineUser;

    public UserRole(RoleEnum role) {
        this.role = role;
    }

}
