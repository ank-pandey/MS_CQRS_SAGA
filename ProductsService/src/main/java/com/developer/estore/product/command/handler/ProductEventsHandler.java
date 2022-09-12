package com.developer.estore.product.command.handler;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.developer.estore.core.event.ProductReservationCancelledEvent;
import com.developer.estore.core.event.ProductReservedEvent;
import com.developer.estore.product.core.entity.ProductEntity;
import com.developer.estore.product.core.event.ProductCreatedEvent;
import com.developer.estore.product.core.repository.ProductsRepository;

@Component
@ProcessingGroup("product-group")
public class ProductEventsHandler {
	
	private final ProductsRepository productRepository;
	private static final Logger log = LoggerFactory.getLogger(ProductEventsHandler.class);
	
	public ProductEventsHandler(ProductsRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	@ExceptionHandler(resultType = Exception.class)
	public void handle(Exception exception) throws Exception {
		throw exception;
	}
	
	@ExceptionHandler(resultType = IllegalArgumentException.class)
	public void handle(IllegalArgumentException exception) {
		
	}
	
	@EventHandler
	public void on(ProductCreatedEvent event) throws Exception {
		ProductEntity productEntity = new ProductEntity();
		BeanUtils.copyProperties(event, productEntity);
		try{
			productRepository.save(productEntity);
		}catch (IllegalArgumentException ex){
			ex.printStackTrace();
		}
	}
	
	@EventHandler
	public void on(ProductReservedEvent productReservedEvent) {
		ProductEntity productEntity = productRepository.findByProductId(productReservedEvent.getProductId());
		
		log.debug("ProductReservedEvent: Current Product Quantity: " + productEntity.getQuantity());
		productEntity.setQuantity(productEntity.getQuantity() - productReservedEvent.getQuantity());
		productRepository.save(productEntity);
		log.debug("ProductReservedEvent: New Product Quantity: " + productEntity.getQuantity());
		log.info("ProductReservedEvent is called for productId: " + productReservedEvent.getProductId() + 
				" and orderId: " + productReservedEvent.getOrderId());
	}
	
	@EventHandler
	public void on(ProductReservationCancelledEvent productReservationCancelledEvent) {
		ProductEntity productEntity = productRepository.findByProductId(productReservationCancelledEvent.getProductId());
		log.debug("ProductReservationCancelledEvent: Current Product Quantity: " + productEntity.getQuantity());
		productEntity.setQuantity(productEntity.getQuantity() + productReservationCancelledEvent.getQuantity());
		productRepository.save(productEntity);
		log.debug("ProductReservationCancelledEvent: New Product Quantity: " + productEntity.getQuantity());
	}

}
