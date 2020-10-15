package com.saber.ecom.common.core.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
@Data
@EqualsAndHashCode
@ToString
public class OrderDto implements Serializable {
    private String userId;
    private List<LineItemDto> lineItems;
}
