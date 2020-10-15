package com.saber.ecom.common.shiping.event;

import lombok.*;
import java.util.Date;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderShippedEvent {
    private Long orderId;
    private Date shippingDate;
}
