package com.saber.ecom.user.model;

import com.saber.ecom.user.repositories.UserRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_credential")
@Data
@EqualsAndHashCode(exclude = {"userRoles"})
@ToString(exclude = {"userRoles"})
public class OnlineUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", unique = true, nullable = false)
    private String screenName;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "active", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean active;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "onlineUser", orphanRemoval = true)
    private Set<UserRole> userRoles = new HashSet<>();

    public void addRole(UserRole userRole) {
        userRole.setOnlineUser(this);
        userRoles.add(userRole);
    }
}
