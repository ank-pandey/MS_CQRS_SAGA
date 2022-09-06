package com.developer.estore.order.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.developer.estore.order.core.model.OrderStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateOrderCommand {

	@TargetAggregateIdentifier
	private final String orderId;
	
	private final String userId;
	private final String productId;
	private final Integer quantity;
	private final String addressId;
	private final OrderStatus orderStatus;
}
