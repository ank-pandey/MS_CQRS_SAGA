package com.developer.estore.product.core.model;

import java.math.BigDecimal;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductModel {

	@NotBlank(message = "Product title is a required field")
	private String title;
	@Min(value=1, message = "Product price cannot be lower than 1")
	private BigDecimal price;
	@Min(value = 1, message = "Product quantity cannot be lower than 1")
	@Max(value = 5, message = "Product quantity cannot be larger than 5")
	private Integer quantity;
}
