package com.saber.ecom.cart.dto;

import lombok.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto implements Serializable {
    private static final long serialVersionUID= 4167534888915644648L;
    private String userId;
    private Timestamp activeSince;
    private String coupen;
    private List<LineItem> lineItems;

    public CustomerDto(String userId){
        this.userId=userId;
    }
}
