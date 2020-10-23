package com.saber.ecom.history.event.handler;

import com.saber.ecom.common.core.dto.LineItemDto;
import com.saber.ecom.common.core.order.command.OrderStatus;
import com.saber.ecom.common.core.order.event.OrderCreatedEvent;
import com.saber.ecom.common.core.order.event.OrderUpdateEvent;
import com.saber.ecom.history.model.LineItem;
import com.saber.ecom.history.model.Order;
import com.saber.ecom.history.repositories.OrderHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class OrderHistoryEventHandler {

    private final OrderHistoryRepository orderHistoryRepository;

    public OrderHistoryEventHandler(OrderHistoryRepository orderHistoryRepository) {
        this.orderHistoryRepository = orderHistoryRepository;
    }

    @EventHandler
    public void handleOrderCreationEvent(OrderCreatedEvent event) {
        log.info("New Order Creation message received ====>{} for user {}", event.getOrderId(), event.getUserId());
        Order order = new Order();
        order.setOrderId(event.getOrderId());
        order.setUserId(event.getUserId());
        order.setCreationDate(new Date());
        order.setOrderStatus(event.getOrderStatus());
        List<LineItem> lineItems = new ArrayList<>();
        if (event.getLineItems() != null) {
            for (LineItemDto lineItemDto : event.getLineItems()) {
                LineItem lineItem = new LineItem();
                lineItem.setProductId(lineItemDto.getProductId());
                lineItem.setPrice(lineItemDto.getPrice());
                lineItem.setQuantity(lineItemDto.getQuantity());
                lineItems.add(lineItem);
            }
        }
        order.setLineItems(lineItems);
        orderHistoryRepository.save(order);

    }

    @EventHandler
    public void handleOrderUpdatedEvent(OrderUpdateEvent event) {
        log.info("Order update message received ======> {} / {}", event.getOrderId(), event.getOrderStatus());
        Order order = orderHistoryRepository.findByOrderId(event.getOrderId());
        order.setOrderStatus(event.getOrderStatus());
        if (order.getOrderStatus().equals(OrderStatus.SHIPPED.name())) {
            order.setShippedDate(event.getDate());
        } else if (order.getOrderStatus().equals(OrderStatus.DELIVERED.name())) {
            order.setDeliveredDate(event.getDate());
        } else if (event.getOrderStatus().equals(OrderStatus.CANCELLED.name())) {
            order.setCanceledDate(event.getDate());
        } else if (event.getOrderStatus().equals(OrderStatus.DELIVERY_FAILED.name())) {
            order.setDeliveryFailReason(event.getDescription());
        }
        orderHistoryRepository.save(order);
    }
}
