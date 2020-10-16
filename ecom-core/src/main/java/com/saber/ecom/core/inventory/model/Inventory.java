package com.saber.ecom.core.inventory.model;

import com.saber.ecom.common.core.inventory.event.InventoryUpdateEvent;
import com.saber.ecom.core.exception.OutOfStockException;
import com.saber.ecom.core.order.model.ProductStockOperation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.axonframework.domain.AbstractAggregateRoot;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString
@Entity
@Table(name = "inventory")
@NoArgsConstructor
public class Inventory extends AbstractAggregateRoot<Long> {
    @Id
    private Long id;
    @Column(name = "sku")
    private String sku;
    @Column(name = "quantity")
    private Integer quantity;

    @Override
    public Long getIdentifier() {
        return this.id;
    }

    public Inventory(Long id, String sku, Integer quantity) {
        this.id = id;
        this.sku = sku;
        this.quantity = quantity;
        registerEvent(new InventoryUpdateEvent(id, sku, quantity));
    }

    public void updateProductStock(Integer count, ProductStockOperation stockOperation) {
        if (stockOperation.equals(ProductStockOperation.DEPRECIATE)) {
            if (this.quantity - count >= 0) {
                this.quantity -= count;
            } else {
                throw new OutOfStockException(this.id);
            }
        } else {
            this.quantity += count;
        }
        registerEvent(new InventoryUpdateEvent(id, sku, quantity));
    }
}
