package com.saber.ecom.shipping.web;

import com.saber.ecom.common.shiping.command.OrderShippingCommand;
import com.saber.ecom.common.shiping.dto.ShippingDTO;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shipping")
public class OrderShippingController {

    private final CommandGateway commandGateway;

    public OrderShippingController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping
    @ResponseBody
    public void shipOrder(@RequestBody ShippingDTO shippingDTO){
        OrderShippingCommand orderShippingCommand= new OrderShippingCommand();
        orderShippingCommand.setOrderId(shippingDTO.getOrderId());
        commandGateway.send(orderShippingCommand);
    }
}
