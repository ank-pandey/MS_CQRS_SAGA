package com.developer.estore.order.query;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import com.developer.estore.order.core.entity.OrderEntity;
import com.developer.estore.order.core.model.OrderSummary;
import com.developer.estore.order.core.repository.OrdersRepository;

@Component
public class OrderQueriesHandler {

	OrdersRepository ordersRepository;
	
	public OrderQueriesHandler(OrdersRepository ordersRepository) {
		this.ordersRepository = ordersRepository;
	}
	@QueryHandler
	public OrderSummary findOrder(FindOrderQuery findOrderQuery) {
		OrderEntity orderEntity = ordersRepository.findByOrderId(findOrderQuery.getOrderId());
		return new OrderSummary(orderEntity.getOrderId(), orderEntity.getOrderStatus(), "");
	}
}
