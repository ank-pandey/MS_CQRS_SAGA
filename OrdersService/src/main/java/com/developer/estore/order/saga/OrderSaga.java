package com.developer.estore.order.saga;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.developer.estore.core.command.CancelProductReservationCommand;
import com.developer.estore.core.command.ProcessPaymentCommand;
import com.developer.estore.core.command.ProcessPaymentCommand.ProcessPaymentCommandBuilder;
import com.developer.estore.core.command.ReserveProductCommand;
import com.developer.estore.core.event.PaymentProcessedEvent;
import com.developer.estore.core.event.ProductReservationCancelledEvent;
import com.developer.estore.core.event.ProductReservedEvent;
import com.developer.estore.core.model.User;
import com.developer.estore.core.query.FetchUserPaymentDetailsQuery;
import com.developer.estore.order.command.ApproveOrderCommand;
import com.developer.estore.order.command.RejectOrderCommand;
import com.developer.estore.order.core.event.OrderApprovedEvent;
import com.developer.estore.order.core.event.OrderCreatedEvent;
import com.developer.estore.order.core.event.OrderRejectedEvent;

@Saga
public class OrderSaga {
	
	@Autowired
	private transient CommandGateway commandGateway;
	
	@Autowired
	private transient QueryGateway queryGateway;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderSaga.class);
	
	@StartSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderCreatedEvent orderCreatedEvent) {
		
		ReserveProductCommand reserveProductCommand = ReserveProductCommand.builder()
													.orderId(orderCreatedEvent.getOrderId())
													.productId(orderCreatedEvent.getProductId())
													.quantity(orderCreatedEvent.getQuantity())
													.userId(orderCreatedEvent.getUserId())
													.build();
		LOGGER.info("OrderCreatedEvent handled for orderId: " + orderCreatedEvent.getOrderId() + "and productId: " 
													+ orderCreatedEvent.getProductId());
		commandGateway.send(reserveProductCommand, new CommandCallback<ReserveProductCommand, Object>() {

			@Override
			public void onResult(CommandMessage<? extends ReserveProductCommand> commandMessage,
					CommandResultMessage<? extends Object> commandResultMessage) {
				if(commandResultMessage.isExceptional()) {
					 //Start a compensating transaction
				}
				
			}
		
		});
	}
	
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(ProductReservedEvent productReservedEvent) {
		LOGGER.info("ProductReservedEvent is called for productId: " + productReservedEvent.getProductId() + 
				" and orderId: " + productReservedEvent.getOrderId());
		FetchUserPaymentDetailsQuery fetchUserPaymentDetailsQuery = new FetchUserPaymentDetailsQuery(productReservedEvent.getUserId());
			
		User userPaymentDetails = null;
		try{
			userPaymentDetails = queryGateway.query(fetchUserPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();
		} catch(Exception ex){
			LOGGER.error(ex.getMessage());
			cancelProductReservation(productReservedEvent, ex.getMessage());
			return;
		}
		
		if(userPaymentDetails == null) {
			cancelProductReservation(productReservedEvent, "Could not fetch user payment details");
			return;
		}
		
		LOGGER.info("Successfully fetched user payment details for user: " + userPaymentDetails.getFirstName());
		
		ProcessPaymentCommand processPaymentCommand = ProcessPaymentCommand.builder()
							.orderId(productReservedEvent.getOrderId())
							.paymentDetails(userPaymentDetails.getPaymentDetails())
							.paymentId(UUID.randomUUID().toString())
							.build();
		String result = null;
		try{
			result = commandGateway.sendAndWait(processPaymentCommand, 10, TimeUnit.SECONDS);
		} catch(Exception ex) {
			LOGGER.error(ex.getMessage());
			cancelProductReservation(productReservedEvent, ex.getMessage());
			return;
		}
		
		if(null == result) {
			LOGGER.info("The ProcessPaymentCommand is NULL, Initiating a compensating transaction.");
			cancelProductReservation(productReservedEvent, "Could not process user payment with provided payment details");
			return;
		}
	}
	
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(PaymentProcessedEvent paymentProcessedEvent) {
		
		ApproveOrderCommand approveOrderCommand = new ApproveOrderCommand(paymentProcessedEvent.getOrderId());
		
		commandGateway.send(approveOrderCommand);
	}
	
	@EndSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderApprovedEvent orderApprovedEvent) {
		LOGGER.info("Order is approved. Order Saga is complete for orderId: " + orderApprovedEvent.getOrderId());
		//SagaLifecycle.end();
	}
	
	private void cancelProductReservation(ProductReservedEvent productReservedEvent, String reason) {
		CancelProductReservationCommand cancelProductReservationCommand = CancelProductReservationCommand
						.builder()
						.orderId(productReservedEvent.getOrderId())
						.productId(productReservedEvent.getProductId())
						.quantity(productReservedEvent.getQuantity())
						.userId(productReservedEvent.getUserId())
						.reason(reason)
						.build();
		commandGateway.send(cancelProductReservationCommand);
	}
	
	@SagaEventHandler(associationProperty = "orderId") 
	public void handle(ProductReservationCancelledEvent productReservationCancelledEvent) {
		// Create and Reject order command
		RejectOrderCommand rejectOrderCommand = new RejectOrderCommand(productReservationCancelledEvent.getOrderId(),
				productReservationCancelledEvent.getReason());
		commandGateway.send(rejectOrderCommand);
	}
	
	@EndSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderRejectedEvent orderRejectedEvent) {
		LOGGER.info("Successfully rejected order with orderId: " + orderRejectedEvent.getOrderId());
	}
}