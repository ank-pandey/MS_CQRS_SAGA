package com.developer.estore.product.command.interceptor;

import java.util.List;
import java.util.function.BiFunction;

import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.developer.estore.product.command.CreateProductCommand;
import com.developer.estore.product.core.entity.ProductLookupEntity;
import com.developer.estore.product.core.repository.ProductLookupRepository;

@Component
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>>{

	private static final Logger LOGGER = LoggerFactory.getLogger(CreateProductCommandInterceptor.class);
	private final ProductLookupRepository productLookupRepository;
	
	public CreateProductCommandInterceptor(ProductLookupRepository productLookupRepository) {
		this.productLookupRepository = productLookupRepository;
	}
	
	@Override
	public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
			List<? extends CommandMessage<?>> messages){
		return (index, command) -> {
			LOGGER.info("Intercepted command: " + command.getPayloadType());
			
			if(CreateProductCommand.class.equals(command.getPayloadType())) {
				CreateProductCommand createProductCommand = (CreateProductCommand) command.getPayload();
				ProductLookupEntity productLookupEntity = productLookupRepository.findByProductIdOrTitle(
									createProductCommand.getProductId(), createProductCommand.getTitle());
				if(null != productLookupEntity) {
					throw new IllegalStateException(
					String.format("Product with productId %s or title %s is already exist", 
						createProductCommand.getProductId(), createProductCommand.getTitle()));
				}
			}
			return command;
		};
	}

}
