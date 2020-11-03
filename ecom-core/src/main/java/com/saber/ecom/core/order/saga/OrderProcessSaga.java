package com.saber.ecom.core.order.saga;

import com.saber.ecom.common.core.order.command.OrderDeliveryFailureRollbackCommand;
import com.saber.ecom.common.core.order.command.OrderStatus;
import com.saber.ecom.common.core.order.command.OrderUpdateCommand;
import com.saber.ecom.common.core.order.event.OrderCancelEvent;
import com.saber.ecom.common.core.order.event.OrderCreatedEvent;
import com.saber.ecom.common.delivery.event.OrderDeliveredEvent;
import com.saber.ecom.common.delivery.event.OrderDeliveryFailedEvent;
import com.saber.ecom.common.shiping.event.OrderShippedEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.saga.annotation.AbstractAnnotatedSaga;
import org.axonframework.saga.annotation.EndSaga;
import org.axonframework.saga.annotation.SagaEventHandler;
import org.axonframework.saga.annotation.StartSaga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class OrderProcessSaga extends AbstractAnnotatedSaga {

    private Long orderId;
    private transient CommandGateway commandGateway;

    @Autowired
    public OrderProcessSaga(@Qualifier(value = "commandGateway") CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handleOrderCreationEvent(OrderCreatedEvent orderCreatedEvent) {
        log.info("New Order created event request received ====> {}", orderCreatedEvent.getOrderId());
        this.orderId = orderCreatedEvent.getOrderId();
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handleOrderShippedEvent(OrderShippedEvent orderShippedEvent) {
        log.info("OrderProcessSaga.handleOrderShippedEvent ");
        log.info("Order shipping event request received for order =====> {}", orderShippedEvent.getOrderId());
        commandGateway.send(new OrderUpdateCommand(orderShippedEvent.getOrderId(), OrderStatus.SHIPPED));
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handleOrderCanceledEvent(OrderCancelEvent orderCancelEvent) {
        log.info("EndSaga ===> OrderProcessSaga.handleOrderCanceledEvent");
        log.info("Order cancelled by the user ====> {}", orderCancelEvent.getOrderId());
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handleOrderDeliverEvent(OrderDeliveredEvent orderDeliveredEvent) {
        log.info("EndSaga : OrderProcessSaga.handleOrderDeliverEvent");
        log.info("Order Delivered event request received for order ====> {}", orderDeliveredEvent.getOrderId());
        commandGateway.send(new OrderUpdateCommand(orderDeliveredEvent.getOrderId(), OrderStatus.DELIVERED));
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handleOrderDeliveryFailureEvent(OrderDeliveryFailedEvent orderDeliveryFailedEvent){
        log.info("EndSaga : OrderProcessSaga.handleOrderDeliveryFailureEvent");
        log.info("Order delivery failed for Order ====> {}",orderDeliveryFailedEvent.getOrderId());
        commandGateway.send(new OrderDeliveryFailureRollbackCommand(orderDeliveryFailedEvent.getOrderId(),
                orderDeliveryFailedEvent.getFailureReason()));
      }
}
