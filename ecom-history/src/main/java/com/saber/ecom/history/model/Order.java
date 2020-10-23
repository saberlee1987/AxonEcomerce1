package com.saber.ecom.history.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    private String id;
    private Long orderId;
    private String userId;
    private String orderStatus;
    private Date creationDate;
    private Date shippedDate;
    private Date deliveredDate;
    private Double totalPrice;
    private Date canceledDate;
    private String deliveryFailReason;
    private List<LineItem> lineItems;
}
