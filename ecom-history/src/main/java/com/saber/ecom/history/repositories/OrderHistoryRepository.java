package com.saber.ecom.history.repositories;

import com.saber.ecom.history.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface OrderHistoryRepository extends MongoRepository<Order, String> {
    Order findByOrderId(Long orderId);
    List<Order> findByUserId(String userId);
    List<Order> findByOrderStatus(String orderStatus);
    Order findById(String id);
    Page<Order> findAll(Pageable pageable);
}
