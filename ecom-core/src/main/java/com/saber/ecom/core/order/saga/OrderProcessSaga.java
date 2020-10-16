package com.saber.ecom.core.order.saga;

import com.saber.ecom.common.core.order.event.OrderCancelEvent;
import com.saber.ecom.common.core.order.event.OrderCreatedEvent;
import com.saber.ecom.common.delivery.event.OrderDeliveredEvent;
import com.saber.ecom.common.delivery.event.OrderDeliveryFailedEvent;
import com.saber.ecom.common.shiping.event.OrderShippedEvent;
import com.saber.ecom.core.order.command.OrderDeliveryFailureRollbackCommand;
import com.saber.ecom.core.order.command.OrderUpdateCommand;
import com.saber.ecom.core.order.model.OrderStatus;
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
    @Autowired
    @Qualifier(value = "commandGateway")
    private transient CommandGateway commandGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handleOrderCreationEvent(OrderCreatedEvent orderCreatedEvent) {
        log.debug("New Order created event request received ====> {}", orderCreatedEvent.getOrderId());
        this.orderId = orderCreatedEvent.getOrderId();
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handleOrderShippedEvent(OrderShippedEvent orderShippedEvent) {
        log.debug("OrderProcessSaga.handleOrderShippedEvent ");
        log.debug("Order shipping event request received for order =====> {}", orderShippedEvent.getOrderId());
        commandGateway.send(new OrderUpdateCommand(orderShippedEvent.getOrderId(), OrderStatus.SHIPPED));
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handleOrderCanceledEvent(OrderCancelEvent orderCancelEvent) {
        log.debug("EndSaga ===> OrderProcessSaga.handleOrderCanceledEvent");
        log.debug("Order cancelled by the user ====> {}", orderCancelEvent.getOrderId());
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handleOrderDeliverEvent(OrderDeliveredEvent orderDeliveredEvent) {
        log.debug("EndSaga : OrderProcessSaga.handleOrderDeliverEvent");
        log.debug("Order Delivered event request received for order ====> {}", orderDeliveredEvent.getOrderId());
        commandGateway.send(new OrderUpdateCommand(orderDeliveredEvent.getOrderId(), OrderStatus.DELIVERED));
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handleOrderDeliveryFailureEvent(OrderDeliveryFailedEvent orderDeliveryFailedEvent){
        log.debug("EndSaga : OrderProcessSaga.handleOrderDeliveryFailureEvent");
        log.debug("Order delivery failed for Order ====> {}",orderDeliveryFailedEvent.getOrderId());
        commandGateway.send(new OrderDeliveryFailureRollbackCommand(orderDeliveryFailedEvent.getOrderId(),
                orderDeliveryFailedEvent.getFailureReason()));
      }
}
