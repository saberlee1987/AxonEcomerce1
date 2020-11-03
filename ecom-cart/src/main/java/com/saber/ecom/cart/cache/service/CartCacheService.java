package com.saber.ecom.cart.cache.service;

import com.saber.ecom.cart.dto.CustomerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
@Slf4j
public class CartCacheService {
    private final CacheManager cacheManager;

    public CartCacheService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void updateUserCartInCache(CustomerDto userCart) {
        log.info("Updating/adding user " + userCart.getUserId() + " into cache");
        Objects.requireNonNull(cacheManager.getCache("customerCache"))
                .put(userCart.getUserId(), userCart);
    }

    public CustomerDto getCustomerDto(String userId) {
        Cache.ValueWrapper value = cacheManager.getCache("customerCache").get(userId);
        if (value != null) {
            log.info("Returning cache for the customer =====> " + userId);
            log.info("Customer DTO ======> "+value.get());
            return (CustomerDto) value.get();
        } else {
            log.info("Customer is not existing the cache ==============> " + userId);
            CustomerDto userCartDto = new CustomerDto(userId);
            updateUserCartInCache(userCartDto);
            log.info("Customer is added to the cache ==========> " + userId);
            return userCartDto;
        }
    }

}
