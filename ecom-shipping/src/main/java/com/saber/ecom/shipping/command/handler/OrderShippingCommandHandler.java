package com.saber.ecom.shipping.command.handler;


import com.saber.ecom.common.shiping.command.OrderShippingCommand;
import com.saber.ecom.common.shiping.event.OrderShippedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.domain.GenericEventMessage;
import org.axonframework.eventhandling.EventBus;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
@Slf4j
public class OrderShippingCommandHandler {

    private final EventBus eventBus;

    public OrderShippingCommandHandler(EventBus eventBus) {
        this.eventBus = eventBus;
    }
    @CommandHandler
    public void handleOrderShipping(OrderShippingCommand orderShippingCommand){
        log.info("OrderShippingCommandHandler/Create new Order Shipping for {}",orderShippingCommand.getOrderId());
        eventBus.publish(GenericEventMessage.asEventMessage(new OrderShippedEvent(orderShippingCommand.getOrderId(),new Date())));
    }

}
