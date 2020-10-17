package com.saber.ecom.common.core.order.command;

import com.saber.ecom.common.core.dto.LineItemDto;
import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedCommand {
    private String userId;
    private List<LineItemDto> lineItems;
}
