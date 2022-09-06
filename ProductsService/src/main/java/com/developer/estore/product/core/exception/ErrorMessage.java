package com.developer.estore.product.core.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMessage {
	
	private final LocalDateTime timestamp;
	
	private final String message;
}
