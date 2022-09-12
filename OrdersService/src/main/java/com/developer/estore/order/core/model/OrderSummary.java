package com.developer.estore.order.core.model;

import lombok.Value;

@Value
public class OrderSummary {

	String orderId;
	OrderStatus orderStatus;
	String message;
}
