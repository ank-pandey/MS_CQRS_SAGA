package com.developer.estore.order.core.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderModel {
	
	@NotBlank(message = "Order productId is a required field")
	private String productId;
	@Min(value = 1, message = "Product Quantity cannot be lower than 1")
	@Max(value = 5, message = "Product Quantity cannot be larger than 5")
	private Integer quantity;
	@NotBlank(message = "Order addressId is a required field")
	private String addressId;

}
