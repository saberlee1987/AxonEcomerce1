package com.saber.ecom.history.model;

import lombok.*;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LineItem {
    private String productId;
    private Integer quantity;
    private Double price;

}
