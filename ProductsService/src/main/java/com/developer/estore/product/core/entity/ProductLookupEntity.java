package com.developer.estore.product.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "productlookup")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductLookupEntity implements Serializable {

	private static final long serialVersionUID = -3007137587314253717L;
	
	@Id
	private String productId;
	@Column(unique = true)
	private String title;

}
