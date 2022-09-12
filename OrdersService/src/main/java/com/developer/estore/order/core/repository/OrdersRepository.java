package com.developer.estore.order.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.developer.estore.order.core.entity.OrderEntity;

public interface OrdersRepository extends JpaRepository<OrderEntity, String>{

	OrderEntity findByOrderId(String orderId);

}
