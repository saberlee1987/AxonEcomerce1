package com.saber.ecom.user.repositories;

import com.saber.ecom.user.model.OnlineUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OnlineUserRepository extends JpaRepository<OnlineUser, Long> {
    OnlineUser findByScreenName(String screenName);
}
