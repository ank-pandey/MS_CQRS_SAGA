package com.developer.estore.core.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductReservedEvent {
	
	private final String productId;
	private final String orderId;
	private final String userId;
	private final Integer quantity;
}
