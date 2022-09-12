package com.developer.estore.order.command.handler;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.developer.estore.order.core.entity.OrderEntity;
import com.developer.estore.order.core.event.OrderApprovedEvent;
import com.developer.estore.order.core.event.OrderCreatedEvent;
import com.developer.estore.order.core.event.OrderRejectedEvent;
import com.developer.estore.order.core.repository.OrdersRepository;

@Component
@ProcessingGroup("order-group")
public class OrderEventsHandler {

	private final OrdersRepository orderRepository;
	
	public OrderEventsHandler(OrdersRepository orderRepository) {
		this.orderRepository = orderRepository;
	}
	
	@EventHandler
	public void on(OrderCreatedEvent event) throws Exception {
		OrderEntity orderEntity = new OrderEntity();
		BeanUtils.copyProperties(event, orderEntity);
		this.orderRepository.save(orderEntity);
	}
	
	@EventHandler
	public void on(OrderApprovedEvent orderApprovedEvent) throws Exception {
		OrderEntity orderEntity = orderRepository.findByOrderId(orderApprovedEvent.getOrderId());
		if(orderEntity == null) {
			
			return;
		}
		orderEntity.setOrderStatus(orderApprovedEvent.getOrderStatus());
		orderRepository.save(orderEntity);
	}
	
	@EventHandler
	public void on(OrderRejectedEvent orderRejectedEvent) throws Exception {
		OrderEntity orderEntity = orderRepository.findByOrderId(orderRejectedEvent.getOrderId());
		if(orderEntity == null) {
					
			return;
		}
		orderEntity.setOrderStatus(orderRejectedEvent.getOrderStatus());
		orderRepository.save(orderEntity);
	}
}
