package com.developer.estore.order.command.handler;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.developer.estore.order.core.entity.OrderEntity;
import com.developer.estore.order.core.event.OrderCreatedEvent;
import com.developer.estore.order.core.repository.OrdersRepository;

@Component
@ProcessingGroup("order-group")
public class OrderEventsHandler {

	private final OrdersRepository orderRepository;
	
	public OrderEventsHandler(OrdersRepository orderRepository) {
		this.orderRepository = orderRepository;
	}
	
	@EventHandler
	public void on(OrderCreatedEvent event) {
		OrderEntity orderEntity = new OrderEntity();
		BeanUtils.copyProperties(event, orderEntity);
		orderRepository.save(orderEntity);
	}
}
