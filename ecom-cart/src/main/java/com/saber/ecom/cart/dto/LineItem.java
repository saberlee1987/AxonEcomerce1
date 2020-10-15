package com.saber.ecom.cart.dto;

import lombok.*;

import java.io.Serializable;
@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LineItem implements Serializable {
    private static final long serialVersionUID = -6963079833195634149L;
    private String id;
    private String name;
    private Double price;
    private Integer quantity;
    private Long inventoryId;

}
