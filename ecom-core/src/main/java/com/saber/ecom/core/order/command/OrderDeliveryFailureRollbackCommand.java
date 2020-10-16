package com.saber.ecom.core.order.command;

import lombok.*;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderDeliveryFailureRollbackCommand {
    private Long orderId;
    private String failureReason;
}
