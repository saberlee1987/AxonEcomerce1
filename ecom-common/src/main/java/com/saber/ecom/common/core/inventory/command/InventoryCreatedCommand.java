package com.saber.ecom.common.core.inventory.command;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
public class InventoryCreatedCommand {
    private String sku;
    private Integer quantity;
}
