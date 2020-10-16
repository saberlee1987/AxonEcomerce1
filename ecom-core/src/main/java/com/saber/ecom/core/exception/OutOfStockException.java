package com.saber.ecom.core.exception;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OutOfStockException extends RuntimeException {
    private Long inventoryId;

    @Override
    public String getMessage() {
        return String.format("No Stock for product ===> %d",inventoryId);
    }
}
