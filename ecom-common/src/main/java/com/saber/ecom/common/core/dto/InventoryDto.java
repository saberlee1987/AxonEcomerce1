package com.saber.ecom.common.core.dto;

import lombok.*;

import java.io.Serializable;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDto implements Serializable {
    private Long id;
    private String sku;
    private Integer quantity;


}
