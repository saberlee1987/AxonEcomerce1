package com.saber.ecom.common.delivery.event;

import lombok.*;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderDeliveryFailedEvent {
    private Long orderId;
    private String failureReason;

}
