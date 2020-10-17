package com.saber.ecom.core.inventory.command.handler;

import com.saber.ecom.common.core.inventory.command.InventoryCreatedCommand;
import com.saber.ecom.core.inventory.model.Inventory;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Slf4j
public class InventoryCreationCommandHandler {

    @Autowired
    @Qualifier(value = "inventoryRepository")
    private Repository<Inventory> inventoryRepository;

    @CommandHandler
    public void handleInventoryCreation(InventoryCreatedCommand inventoryCreatedCommand) {
        int id = new Random().nextInt();
        log.debug("InventoryCreationCommandHandler / Create new Inventory is executing =========>id ==" + id);
        Inventory inventory = new Inventory((long) id, inventoryCreatedCommand.getSku(), inventoryCreatedCommand.getQuantity());
        inventoryRepository.add(inventory);
    }
}
