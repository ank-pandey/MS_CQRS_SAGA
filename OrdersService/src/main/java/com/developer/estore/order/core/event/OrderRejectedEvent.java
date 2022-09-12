package com.developer.estore.order.core.event;

import com.developer.estore.order.core.model.OrderStatus;

import lombok.Value;

@Value
public class OrderRejectedEvent {

	private final String orderId;
	private final String reason;
	private final OrderStatus orderStatus = OrderStatus.REJECTED;
}
