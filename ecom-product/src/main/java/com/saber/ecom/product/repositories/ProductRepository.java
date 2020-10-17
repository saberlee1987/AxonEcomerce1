package com.saber.ecom.product.repositories;

import com.saber.ecom.product.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product>findByProductCategoryName(@Param("productCategoryName")String productCategoryName);
    List<Product> findByCode(@Param("code")String code);
    @Query("{ 'name':{$regex:?0,$options:'i'}}")
    List<Product>findByNameRegex(@Param("name") String name);
}
