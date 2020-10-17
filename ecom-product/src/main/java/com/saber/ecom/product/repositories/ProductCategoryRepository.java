package com.saber.ecom.product.repositories;

import com.saber.ecom.product.model.ProductCategory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductCategoryRepository extends MongoRepository<ProductCategory,String> {

}
