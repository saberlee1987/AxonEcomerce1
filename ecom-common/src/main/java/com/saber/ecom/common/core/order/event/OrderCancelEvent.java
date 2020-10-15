package com.saber.ecom.common.core.order.event;

import lombok.*;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderCancelEvent {
    private Long orderId;
}
