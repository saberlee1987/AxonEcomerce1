package com.saber.ecom.common.shiping.command;

import lombok.*;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderShippingCommand {
    private Long orderId;
}
