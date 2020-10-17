package com.saber.ecom.product.inventory.event.handler;

import com.saber.ecom.common.core.inventory.event.InventoryUpdateEvent;
import com.saber.ecom.product.inventory.model.Inventory;
import com.saber.ecom.product.inventory.repositories.InventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InventoryHandler {

    private InventoryRepository inventoryRepository;

    public InventoryHandler(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @EventHandler
    public void handleInventoryUpdates(InventoryUpdateEvent event) {
        log.debug("Inventory creation/update  message received -------->" + event.getId() + "/" + event.getSku());
        Inventory inventory = inventoryRepository.findByInventoryId(event.getId());
        if (inventory == null) {
            log.debug("Inventory not existing - creating new one");
            inventory = new Inventory();
            inventory.setSku(event.getSku());
            inventory.setInventoryId(event.getId());
        } else {
            log.debug("Inventory existing - updating  ");
        }
        inventory.setQuantity(event.getQuantity());
        inventoryRepository.save(inventory);
    }
}
