package com.saber.ecom.common.core.dto;

import lombok.*;

import java.io.Serializable;
@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LineItemDto implements Serializable {
    private Long inventoryId;
    private Double price;
    private int quantity;
    private String productId;
}
