package com.developer.estore.payments.event;

import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.developer.estore.core.event.PaymentProcessedEvent;
import com.developer.estore.payments.repository.PaymentEntity;
import com.developer.estore.payments.repository.PaymentRepository;

@Component
public class PaymentEventsHandler {

	private final Logger LOGGER = LoggerFactory.getLogger(PaymentEventsHandler.class);
	private final PaymentRepository paymentRepository;
	
	public PaymentEventsHandler(PaymentRepository paymentRepository) {
		this.paymentRepository = paymentRepository;
	}
	
	@EventHandler
	public void on(PaymentProcessedEvent event) {
		LOGGER.info("PaymentProcessedEvent is called for orderId: " + event.getOrderId());
		PaymentEntity entity = PaymentEntity.builder()
				.paymentId(event.getPaymentId())
				.orderId(event.getOrderId())
				.build();
		
		paymentRepository.save(entity);
	}
}
