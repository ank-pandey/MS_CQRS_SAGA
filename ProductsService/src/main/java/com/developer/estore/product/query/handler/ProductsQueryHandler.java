package com.developer.estore.product.query.handler;

import java.util.ArrayList;
import java.util.List;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.developer.estore.product.core.entity.ProductEntity;
import com.developer.estore.product.core.repository.ProductsRepository;
import com.developer.estore.product.query.FindProductsQuery;
import com.developer.estore.product.query.model.ProductQueryModel;

@Component
public class ProductsQueryHandler {
	
	private final ProductsRepository productsRepository;
	
	public ProductsQueryHandler(ProductsRepository productsRepository) {
		this.productsRepository = productsRepository;	
	}
	
	@QueryHandler
	public List<ProductQueryModel> findProducts(FindProductsQuery query){
		
		List<ProductQueryModel> products = new ArrayList<>();
		
		List<ProductEntity> storedProducts = productsRepository.findAll();
		for(ProductEntity productEntity: storedProducts) {
			ProductQueryModel productQueryModel = new ProductQueryModel();
			BeanUtils.copyProperties(productEntity, productQueryModel);
			products.add(productQueryModel);
		}
		return products;	
	}
}