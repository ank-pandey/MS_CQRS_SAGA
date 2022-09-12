package com.developer.estore.order.command.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.developer.estore.order.command.ApproveOrderCommand;
import com.developer.estore.order.command.CreateOrderCommand;
import com.developer.estore.order.command.RejectOrderCommand;
import com.developer.estore.order.core.event.OrderApprovedEvent;
import com.developer.estore.order.core.event.OrderCreatedEvent;
import com.developer.estore.order.core.event.OrderRejectedEvent;
import com.developer.estore.order.core.model.OrderStatus;

@Aggregate
public class OrderAggregate {
	
	@AggregateIdentifier
	private String orderId;
	private String productId;
	private String userId;
	private Integer quantity;
	private String addressId;
	private OrderStatus orderStatus;

	public OrderAggregate() {
	}
	
	@CommandHandler
	public OrderAggregate(CreateOrderCommand command) {
		
		OrderCreatedEvent event =  new OrderCreatedEvent();
		BeanUtils.copyProperties(command, event);
		AggregateLifecycle.apply(event);
	}
	
	@EventSourcingHandler
	public void on(OrderCreatedEvent event) {
		this.orderId = event.getOrderId();
		this.productId = event.getProductId();
		this.userId = event.getUserId();
		this.addressId = event.getAddressId();
		this.quantity = event.getQuantity();
		this.orderStatus = event.getOrderStatus();
	}
	
	@CommandHandler
	public void handle(ApproveOrderCommand approveOrderCommand) {
		OrderApprovedEvent orderApprovedEvent = new OrderApprovedEvent(approveOrderCommand.getOrderId());
		AggregateLifecycle.apply(orderApprovedEvent);
	}
	
	@EventSourcingHandler
	public void on(OrderApprovedEvent orderApprovedEvent) {
		this.orderStatus = orderApprovedEvent.getOrderStatus();
	}
	
	@CommandHandler
	public void handle(RejectOrderCommand rejectOrderCommand) {
		OrderRejectedEvent orderRejectEvent = new OrderRejectedEvent(rejectOrderCommand.getOrderId(), rejectOrderCommand.getReason());
		AggregateLifecycle.apply(orderRejectEvent);
	}
	
	@EventSourcingHandler
	public void on(OrderRejectedEvent orderRejectedEvent) {
		this.orderStatus = orderRejectedEvent.getOrderStatus();
	}
}
