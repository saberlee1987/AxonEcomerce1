package com.saber.ecom.product.inventory.model;

import ch.qos.logback.core.joran.spi.NoAutoStart;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@EqualsAndHashCode
@ToString
public class Inventory {
    @Id
    private String id;
    @Indexed(unique=true)
    private  Long inventoryId;
    private  String sku;
    private  Integer quantity;
}
