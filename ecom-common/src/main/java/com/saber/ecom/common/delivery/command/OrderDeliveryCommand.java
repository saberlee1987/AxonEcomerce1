package com.saber.ecom.common.delivery.command;

import lombok.*;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderDeliveryCommand {
    private Long orderId;
    private boolean delivered;
    private String reasonForFailure;
}
