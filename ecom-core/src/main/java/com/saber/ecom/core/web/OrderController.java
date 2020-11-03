package com.saber.ecom.core.web;

import com.saber.ecom.common.core.dto.OrderDto;
import com.saber.ecom.common.core.order.command.OrderCancelCommand;
import com.saber.ecom.common.core.order.command.OrderCreatedCommand;
import com.saber.ecom.core.exception.OutOfStockException;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/order")
@Slf4j
public class OrderController {

    private final CommandGateway commandGateway;

    public OrderController(@Qualifier(value = "commandGateway") CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    @ResponseBody
    public OrderCreationStatus createOrder(@RequestBody OrderDto orderDto) {
        try {
            OrderCreatedCommand orderCreatedCommand = new OrderCreatedCommand();
            orderCreatedCommand.setUserId(orderDto.getUserId());
            orderCreatedCommand.setLineItems(orderDto.getLineItems());
            commandGateway.sendAndWait(orderCreatedCommand);
            return OrderCreationStatus.SUCCESS;
        } catch (CommandExecutionException ex) {
            Throwable e = ex.getCause();
            log.error("Error while creating new Order ==> {} ", e.getMessage());
            if (e instanceof OutOfStockException) {
                return OrderCreationStatus.OUT_OF_STOCK;
            } else {
                return OrderCreationStatus.FAILED;
            }
        }
    }


    @RequestMapping(value = "/{orderId}", method = RequestMethod.DELETE)
    @Transactional
    @ResponseBody
    public void cancelOrder(@PathVariable Long orderId) {
        OrderCancelCommand orderCancelCommand = new OrderCancelCommand(orderId);
        commandGateway.send(orderCancelCommand);
    }

}
