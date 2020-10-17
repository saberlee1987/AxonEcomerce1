package com.saber.ecom.delivery.controllers;

import com.saber.ecom.common.delivery.command.OrderDeliveryCommand;
import com.saber.ecom.common.delivery.dto.DeliveryDTO;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/delivery")
public class OrderDeliverController {

    private final CommandGateway commandGateway;

    public OrderDeliverController(@Qualifier(value = "commandGateway") CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }
    @PostMapping
    @ResponseBody
    public void deliverOrder(@RequestBody DeliveryDTO deliveryDTO){
        OrderDeliveryCommand orderDeliveryCommand = new OrderDeliveryCommand(deliveryDTO.getOrderId(),deliveryDTO.isDelivered(),deliveryDTO.getReasonForFailure());
        commandGateway.send(orderDeliveryCommand);

    }
}
