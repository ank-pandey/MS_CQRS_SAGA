package com.developer.estore.product.query.controller;

import java.util.List;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.developer.estore.product.query.FindProductsQuery;
import com.developer.estore.product.query.model.ProductQueryModel;

@RestController
@RequestMapping("/products")
public class ProductsQueryController {

	@Autowired
	QueryGateway queryGateway;
	@GetMapping
	public List<ProductQueryModel> getProducts(){
		
		FindProductsQuery findProductsQuery = new FindProductsQuery();
		return queryGateway.query(findProductsQuery, ResponseTypes.multipleInstancesOf(ProductQueryModel.class)).join();	
	}
}
