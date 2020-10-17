package com.saber.ecom.product.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
@Data
@EqualsAndHashCode
@ToString
public class Product {
    @Id
    private String id;
    private String name;
    private String code;
    private String title;
    private String description;
    private String imgUrl;
    private Double price;
    private String productCategoryName;

}
