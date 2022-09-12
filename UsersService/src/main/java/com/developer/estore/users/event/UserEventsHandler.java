package com.developer.estore.users.event;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import com.developer.estore.core.model.PaymentDetails;
import com.developer.estore.core.model.User;
import com.developer.estore.core.query.FetchUserPaymentDetailsQuery;

@Component
public class UserEventsHandler {

	 @QueryHandler
	 public User findUserPaymentDetails(FetchUserPaymentDetailsQuery query) {
	        
	        PaymentDetails paymentDetails = PaymentDetails.builder()
	                .cardNumber("123Card")
	                .cvv("123")
	                .name("TEST NAME")
	                .validUntilMonth(12)
	                .validUntilYear(2030)
	                .build();
	
	        User user = User.builder()
	                .firstName("Test")
	                .lastName("Name")
	                .userId(query.getUserId())
	                .paymentDetails(paymentDetails)
	                .build();

        return user;
    }
}
