package com.developer.estore.product;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.PropagatingErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

import com.developer.estore.core.config.AxonConfig;
import com.developer.estore.product.command.interceptor.CreateProductCommandInterceptor;
import com.developer.estore.product.core.exception.ProductServiceEventsExceptionHandler;

@EnableDiscoveryClient
@SpringBootApplication
@Import({AxonConfig.class})
public class ProductsServiceApplication {
	
	@Autowired
	ApplicationContext context;

	public static void main(String[] args) {
		SpringApplication.run(ProductsServiceApplication.class, args);
	}
	
	@Autowired
	public void registerCreateProductCommandInterceptor(ApplicationContext context, CommandBus commandBus) {
		
		commandBus.registerDispatchInterceptor(context.getBean(CreateProductCommandInterceptor.class));
	}
	
	@Autowired
	public void configure(EventProcessingConfigurer config) {
		config.registerListenerInvocationErrorHandler("product-group", 
				conf ->new ProductServiceEventsExceptionHandler());
//		config.registerListenerInvocationErrorHandler("product-group", 
//				conf ->PropagatingErrorHandler.INSTANCE);
	}
}
