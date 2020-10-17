package com.saber.ecom.product.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
@Data
@EqualsAndHashCode
@ToString
public class ProductCategory {
    @Id
    private String id;
    private String name;
    private String title;
    private String description;
    private String imgUrl;
}
