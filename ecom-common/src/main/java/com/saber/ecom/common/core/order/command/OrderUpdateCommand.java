package com.saber.ecom.common.core.order.command;

import lombok.*;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderUpdateCommand {
    private Long orderId;
    private OrderStatus orderStatus;
}
