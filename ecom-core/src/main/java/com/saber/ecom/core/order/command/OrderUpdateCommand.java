package com.saber.ecom.core.order.command;

import com.saber.ecom.core.order.model.OrderStatus;
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
