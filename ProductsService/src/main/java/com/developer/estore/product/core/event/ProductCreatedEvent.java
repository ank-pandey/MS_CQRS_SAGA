package com.developer.estore.product.core.event;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreatedEvent {
	
	private String productId;
	private String title;
	private BigDecimal price;
	private Integer quantity;
}