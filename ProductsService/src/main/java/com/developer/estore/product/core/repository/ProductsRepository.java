package com.developer.estore.product.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.developer.estore.product.core.entity.ProductEntity;

public interface ProductsRepository extends JpaRepository<ProductEntity, String> {

	ProductEntity findByProductId(String productId);

}
