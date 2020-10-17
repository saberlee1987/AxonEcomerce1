package com.saber.ecom.product.inventory.repositories;

import com.saber.ecom.product.inventory.model.Inventory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

public interface InventoryRepository extends MongoRepository<Inventory,String> {
     Inventory findByInventoryId(@Param("inventoryId") Long  inventoryId);
     Inventory findBySku(@Param("sku") String  sku);
}
