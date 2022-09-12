package com.developer.estore.payments.command;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.developer.estore.core.command.ProcessPaymentCommand;
import com.developer.estore.core.event.PaymentProcessedEvent;

@Aggregate
public class PaymentAggregate {
	
	@AggregateIdentifier
	private String paymentId;
	private String orderId;

	public PaymentAggregate() {
	}
	
	@CommandHandler
	public PaymentAggregate(ProcessPaymentCommand processPaymentCommand) {
		
		if(processPaymentCommand.getPaymentDetails() == null) {
			throw new IllegalArgumentException("Missing Payment Details");
		}
		if(processPaymentCommand.getPaymentId() == null) {
			throw new IllegalArgumentException("Missing PaymentId");
		}
		if(processPaymentCommand.getOrderId() == null) {
			throw new IllegalArgumentException("Missing OrderId");
		}
		AggregateLifecycle.apply(new PaymentProcessedEvent(processPaymentCommand.getOrderId(),
					processPaymentCommand.getPaymentId()));
	}
	
	@EventSourcingHandler
	public void on(PaymentProcessedEvent paymentProcessedEvent) {
		this.paymentId = paymentProcessedEvent.getPaymentId();
		this.orderId = paymentProcessedEvent.getOrderId();
	}
}
