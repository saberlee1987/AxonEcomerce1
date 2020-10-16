package com.saber.ecom.core.order.model;

import com.saber.ecom.common.core.dto.LineItemDto;
import com.saber.ecom.common.core.order.event.OrderCancelEvent;
import com.saber.ecom.common.core.order.event.OrderCreatedEvent;
import com.saber.ecom.common.core.order.event.OrderUpdateEvent;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.domain.AbstractAggregateRoot;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "customer_order")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class Order extends AbstractAggregateRoot<Long> {
    @Id
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;
    @Column(name = "total")
    private Double total;
    @Column(name = "order_date")
    private Date orderDate;
    @Column(name = "user_id")
    private String userId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order", orphanRemoval = true)
    private Set<LineItem> lineItems = new HashSet<>();

    @Override
    public Long getIdentifier() {
        return this.id;
    }

    public void notifyOrderCreation() {
        List<LineItemDto> lineItemDtoList = new ArrayList<>();
        for (LineItem lineItem : lineItems) {
            LineItemDto lineItemDto = new LineItemDto();
            lineItemDto.setProductId(lineItem.getProduct());
            lineItemDto.setPrice(lineItem.getPrice());
            lineItemDto.setQuantity(lineItem.getQuantity());
            lineItemDto.setInventoryId(lineItem.getInventoryId());
            lineItemDtoList.add(lineItemDto);
        }
        registerEvent(new OrderCreatedEvent(id, userId, orderStatus.name(), total, orderDate, lineItemDtoList));
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        registerEvent(new OrderUpdateEvent(this.id, orderStatus.name(), new Date(), null));
    }

    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCELLED;
        registerEvent(new OrderUpdateEvent(this.id, orderStatus.name(), new Date(), null));
        log.debug("Registered OrderUpdateEvent ");
        registerEvent(new OrderCancelEvent(this.id));
        log.debug("Registered OrderCancelEvent");
    }

    public void notifyOrderFailure(String failureReason) {
        this.orderStatus = OrderStatus.DELIVERY_FAILED;
        registerEvent(new OrderUpdateEvent(this.id, orderStatus.name(), new Date(), null));
    }

    public void addLineItem(LineItem lineItem) {
        lineItems.add(lineItem);
        lineItem.setOrder(this);
    }
}
