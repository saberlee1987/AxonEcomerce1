package com.saber.ecom.common.core.order.event;

import lombok.*;

import java.util.Date;
@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderUpdateEvent {
    private Long orderId;
    private String orderStatus;
    private Date date;
    private String description;
}
