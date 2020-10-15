package com.saber.ecom.common.core.inventory.event;

import lombok.*;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InventoryUpdateEvent {
    private Long id;
    private String sku;
    private Integer quantity;
}
