package com.saber.ecom.user.repositories;

import com.saber.ecom.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(String userId);
}
