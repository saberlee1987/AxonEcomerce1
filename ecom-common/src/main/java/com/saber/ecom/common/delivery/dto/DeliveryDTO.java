package com.saber.ecom.common.delivery.dto;

import lombok.*;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDTO {
    private Long orderId;
    private boolean delivered;
    private String reasonForFailure;

}
