package com.developer.estore.product.command.aggregate;

import java.math.BigDecimal;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.developer.estore.core.command.CancelProductReservationCommand;
import com.developer.estore.core.command.ReserveProductCommand;
import com.developer.estore.core.event.ProductReservationCancelledEvent;
import com.developer.estore.core.event.ProductReservedEvent;
import com.developer.estore.product.command.CreateProductCommand;
import com.developer.estore.product.core.event.ProductCreatedEvent;

@Aggregate
public class ProductAggregate {
	
	@AggregateIdentifier
	private String productId;
	private String title;
	private BigDecimal price;
	private Integer quantity;
	
	public ProductAggregate() {
		
	}
	
	@CommandHandler
	public ProductAggregate(CreateProductCommand command) {
		//Validate create product command
		if(command.getPrice().compareTo(BigDecimal.ZERO) <= 0){
			throw new IllegalArgumentException("Price can not be less that or equal to zero.");
		}
		if(null == command.getTitle() ||
				command.getTitle().isBlank()) {
			throw new IllegalArgumentException("Title can not be Empty");
		}
		
		ProductCreatedEvent event = new ProductCreatedEvent();
		
		BeanUtils.copyProperties(command, event);
		
		AggregateLifecycle.apply(event);	
	}
	
	@CommandHandler
	public void handle(ReserveProductCommand reserveProductCommand) {
			
		if(quantity < reserveProductCommand.getQuantity()) {
			throw new IllegalArgumentException("Insufficient number of items in stock");
		}
		
		ProductReservedEvent productReservedEvent = ProductReservedEvent
				.builder()
				.orderId(reserveProductCommand.getOrderId())
				.productId(reserveProductCommand.getProductId())
				.quantity(reserveProductCommand.getQuantity())
				.userId(reserveProductCommand.getUserId())
				.build();
		
		AggregateLifecycle.apply(productReservedEvent);
		
	}
	
	@CommandHandler
	public void handle(CancelProductReservationCommand cancelProductReservationCommand) {
		
		ProductReservationCancelledEvent productReservationCancelledEvent = ProductReservationCancelledEvent
				.builder()
				.orderId(cancelProductReservationCommand.getOrderId())
				.productId(cancelProductReservationCommand.getProductId())
				.quantity(cancelProductReservationCommand.getQuantity())
				.reason(cancelProductReservationCommand.getReason())
				.userId(cancelProductReservationCommand.getUserId())
				.build();
		AggregateLifecycle.apply(productReservationCancelledEvent);
	}
	
	@EventSourcingHandler
	public void on(ProductCreatedEvent productCreatedEvent) {
		this.productId = productCreatedEvent.getProductId();
		this.title = productCreatedEvent.getTitle();
		this.price = productCreatedEvent.getPrice();
		this.quantity = productCreatedEvent.getQuantity();
	}
	
	@EventSourcingHandler
	public void on(ProductReservedEvent productReservedEvent) {
		this.quantity -= productReservedEvent.getQuantity();
	}
	
	@EventSourcingHandler
	public void on(ProductReservationCancelledEvent productReservationCancelledEvent) {
		this.quantity += productReservationCancelledEvent.getQuantity();
	}
}
