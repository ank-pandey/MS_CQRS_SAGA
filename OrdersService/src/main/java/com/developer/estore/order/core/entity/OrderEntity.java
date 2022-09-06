package com.developer.estore.order.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.developer.estore.order.core.model.OrderStatus;

import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class OrderEntity implements Serializable {

	private static final long serialVersionUID = 3030411762748714048L;
	@Id
	@Column(unique = true)
	public String orderId;
	
	private String productId;
	private String userId;
	private Integer quantity;
	private String addressId;
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;
}
