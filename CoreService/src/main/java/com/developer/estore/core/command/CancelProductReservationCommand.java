package com.developer.estore.core.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CancelProductReservationCommand {

	@TargetAggregateIdentifier
	private final String productId;
	
	private final String orderId;
	private final Integer quantity;
	private final String userId;
	private final String reason;
}
