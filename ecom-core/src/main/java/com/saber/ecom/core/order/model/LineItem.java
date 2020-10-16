package com.saber.ecom.core.order.model;

import lombok.*;
import org.axonframework.domain.AbstractAggregateRoot;

import javax.persistence.*;

@Entity
@Table(name = "line_item")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LineItem extends AbstractAggregateRoot<Long> {
    @Id
    private Long id;
    @Column(name = "product")
    private String product;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "price")
    private Double price;
    @Column(name = "inventory_id")
    private Long inventoryId;
    @ManyToOne
    @JoinColumn(name = "order_id",nullable = false)
    private Order order;

    @Override
    public Long getIdentifier() {
        return this.id;
    }
}
