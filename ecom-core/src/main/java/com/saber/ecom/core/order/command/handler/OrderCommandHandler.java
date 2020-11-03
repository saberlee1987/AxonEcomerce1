package com.saber.ecom.core.order.command.handler;

import com.saber.ecom.common.core.dto.LineItemDto;
import com.saber.ecom.common.core.order.command.OrderCancelCommand;
import com.saber.ecom.common.core.order.command.OrderCreatedCommand;
import com.saber.ecom.common.core.order.command.OrderDeliveryFailureRollbackCommand;
import com.saber.ecom.common.core.order.command.OrderStatus;
import com.saber.ecom.core.inventory.model.Inventory;
import com.saber.ecom.core.order.model.LineItem;
import com.saber.ecom.core.order.model.Order;
import com.saber.ecom.core.order.model.ProductStockOperation;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.Repository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;

@Component
@Slf4j
public class OrderCommandHandler {
    private final Repository<Order> orderRepository;
    private final Repository<Inventory> inventoryRepository;

    public OrderCommandHandler(@Qualifier(value = "orderRepository") Repository<Order> orderRepository,
                               @Qualifier(value = "inventoryRepository") Repository<Inventory> inventoryRepository) {
        this.orderRepository = orderRepository;
        this.inventoryRepository = inventoryRepository;
    }


    @CommandHandler
    public void handleNewOrder(OrderCreatedCommand orderCreatedCommand) {
        Random random = new Random();
        log.info("OrderCreatedCommand/create new order is executing ====>" + orderCreatedCommand);
        Order order = new Order();
        order.setId((long) random.nextInt());
        order.setOrderDate(new Date());
        order.setOrderStatus(OrderStatus.PAID);
        order.setUserId(orderCreatedCommand.getUserId());
        double total = 0;

        if (orderCreatedCommand.getLineItems() != null) {

            for (LineItemDto lineItem : orderCreatedCommand.getLineItems()) {
                if (lineItem.getInventoryId() != null) {
                    LineItem item = new LineItem();
                    item.setId(random.nextLong());
                    item.setQuantity(lineItem.getQuantity());
                    item.setPrice(lineItem.getPrice());
                    item.setInventoryId(lineItem.getInventoryId());
                    item.setProduct(lineItem.getProductId());
                    total += lineItem.getPrice();
                    order.addLineItem(item);
                    Inventory inventory = inventoryRepository.load(lineItem.getInventoryId());
                    inventory.updateProductStock(lineItem.getQuantity(), ProductStockOperation.DEPRECIATE);
                }
            }
        }
        order.setTotal(total);
        order.notifyOrderCreation();
        orderRepository.add(order);

    }

    @CommandHandler
    public void handleOrderCancel(OrderCancelCommand orderCancelCommand) {
        log.info("Order Cancelling Command is executing =====>{}", orderCancelCommand.getOrderId());
        Order order = orderRepository.load(orderCancelCommand.getOrderId());
        order.cancelOrder();
        rollbackInventory(order);
    }

    @CommandHandler
    public void handleOrderDeliveryFailure(OrderDeliveryFailureRollbackCommand orderDeliveryFailureRollbackCommand) {
        log.info("Order delivery failure command is executing ====> {}", orderDeliveryFailureRollbackCommand.getOrderId());
        Order order = orderRepository.load(orderDeliveryFailureRollbackCommand.getOrderId());
        order.updateOrderStatus(OrderStatus.DELIVERY_FAILED);
        rollbackInventory(order);
    }

    private void rollbackInventory(Order order) {
        for (LineItem lineItem : order.getLineItems()) {
            Inventory inventory = inventoryRepository.load(lineItem.getInventoryId());
            inventory.updateProductStock(lineItem.getQuantity(), ProductStockOperation.ADD);
        }
    }

}
