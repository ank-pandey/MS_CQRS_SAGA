package com.developer.estore.product.command.controller;

import java.util.UUID;

import javax.validation.Valid;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.developer.estore.product.command.CreateProductCommand;
import com.developer.estore.product.core.model.ProductModel;

@RestController
@RequestMapping("/products")
public class ProductsCommandController {
	
	private final Environment env;
	
	private final CommandGateway commandGateway;
	
	@Autowired
	public ProductsCommandController(Environment env, CommandGateway commandGateway) {
		this.env = env;
		this.commandGateway = commandGateway;
		
	}
	
	@PostMapping
	public String createProduct(@Valid @RequestBody ProductModel product ) {
		CreateProductCommand createProductCommand = CreateProductCommand.builder().price(product.getPrice())
			.quantity(product.getQuantity())
			.title(product.getTitle())
			.productId(UUID.randomUUID().toString()).build();
//		String returnValue;
//		try{
//			returnValue =  commandGateway.sendAndWait(createProductCommand);
//		}catch(Exception ex) {
//			returnValue = ex.getLocalizedMessage();
//		}
		return commandGateway.sendAndWait(createProductCommand);
	}
}