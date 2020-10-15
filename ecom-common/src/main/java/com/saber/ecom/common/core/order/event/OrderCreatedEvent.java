package com.saber.ecom.common.core.order.event;

import com.saber.ecom.common.core.dto.LineItemDto;
import lombok.*;

import java.util.Date;
import java.util.List;
@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedEvent {
    private Long orderId;
    private String userId;
    private Double total;
    private Date orderDate;
    private List<LineItemDto> lineItems;
}
