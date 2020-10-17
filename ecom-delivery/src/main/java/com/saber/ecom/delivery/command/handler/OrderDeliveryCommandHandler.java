package com.saber.ecom.delivery.command.handler;

import com.saber.ecom.common.delivery.command.OrderDeliveryCommand;
import com.saber.ecom.common.delivery.event.OrderDeliveredEvent;
import com.saber.ecom.common.delivery.event.OrderDeliveryFailedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.domain.GenericEventMessage;
import org.axonframework.eventhandling.EventBus;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
@Slf4j
public class OrderDeliveryCommandHandler {

    private final EventBus eventBus;

    public OrderDeliveryCommandHandler(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @CommandHandler
    public void handleOrderDelivery(OrderDeliveryCommand orderDeliveryCommand){
        log.debug("OrderDeliveryCommand/Create new Order shipping for ===>{}",orderDeliveryCommand.getOrderId());
        if (orderDeliveryCommand.isDelivered()){
            eventBus.publish(GenericEventMessage.asEventMessage(new OrderDeliveredEvent(orderDeliveryCommand.getOrderId(),new Date())));
        }else{
            eventBus.publish(GenericEventMessage.asEventMessage(new OrderDeliveryFailedEvent(orderDeliveryCommand.getOrderId(), orderDeliveryCommand.getReasonForFailure())));

        }
    }
}
