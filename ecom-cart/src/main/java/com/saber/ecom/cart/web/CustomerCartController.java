package com.saber.ecom.cart.web;

import com.saber.ecom.cart.cache.service.CartCacheService;
import com.saber.ecom.cart.dto.CustomerDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/customerCart")
public class CustomerCartController {

    private CartCacheService cartCacheService;

    public CustomerCartController(CartCacheService cartCacheService) {
        this.cartCacheService = cartCacheService;
    }

    @PostMapping
    public HttpEntity<Void> updateCustomerCartToCache(@RequestBody CustomerDto customerDto) {
        cartCacheService.updateUserCartInCache(customerDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}")
    public HttpEntity<CustomerDto> getCustomerCartFromCache(@PathVariable(name = "userId") String userId) {
        CustomerDto customerDto = cartCacheService.getCustomerDto(userId);
        return new ResponseEntity<>(customerDto, HttpStatus.OK);
    }

}
