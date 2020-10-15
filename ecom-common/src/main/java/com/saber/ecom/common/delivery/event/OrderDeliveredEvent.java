package com.saber.ecom.common.delivery.event;

import lombok.*;

import java.util.Date;
@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderDeliveredEvent {
    private Long orderId;
    private Date deliveredDate;
}
