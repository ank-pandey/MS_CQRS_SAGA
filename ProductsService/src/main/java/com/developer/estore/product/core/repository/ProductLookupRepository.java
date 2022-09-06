package com.developer.estore.product.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.developer.estore.product.core.entity.ProductLookupEntity;

public interface ProductLookupRepository extends JpaRepository<ProductLookupEntity, String> {

	ProductLookupEntity findByProductIdOrTitle(String productId, String title);

}