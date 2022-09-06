package com.developer.estore.order.command.controller;

import java.util.UUID;

import javax.validation.Valid;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.developer.estore.order.command.CreateOrderCommand;
import com.developer.estore.order.core.model.OrderModel;
import com.developer.estore.order.core.model.OrderStatus;

@RestController
@RequestMapping("/orders")
public class OrdersCommandController {

	private final CommandGateway commandGateway;
	
	@Autowired
	public OrdersCommandController(CommandGateway commandGateway) {
		this.commandGateway = commandGateway;
	}
	
	@PostMapping
	public String createOrder(@Valid @RequestBody OrderModel order) {
		String userId = "27b95829-4f3f-4ddf-8983-151ba010e35b";
		CreateOrderCommand createOrderCommand = CreateOrderCommand.builder()
				.addressId(order.getAddressId())
				.productId(order.getProductId())
				.quantity(order.getQuantity())
				.orderId(UUID.randomUUID().toString())
				.userId(userId)
				.orderStatus(OrderStatus.CREATED)
				.build();
		
		return commandGateway.sendAndWait(createOrderCommand);
	}
}
