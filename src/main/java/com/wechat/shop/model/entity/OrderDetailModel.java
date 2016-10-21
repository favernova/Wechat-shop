package com.wechat.shop.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.wechat.shop.model.enums.UserGroup;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ORDER_DETAIL")
@GenericGenerator(name = "wechat-shop-id", strategy = "uuid")
public class OrderDetailModel extends AbstractModel{
	
	//private static final long serialVersionUID = ;
	
	@Id
    @GeneratedValue(generator = "wechat-shop-id")
	@Column(name = "ID", nullable = false, updatable = false)
	String id;
	
	// @Column(name = "ORDER_ID")
    // long orderId;	
	
	// @Column(name = "PRODUCT_ID")
    // String productId;	
	
	@Column(name = "QUANTITY")
    long quantity;	
	
	@Column(name = "STATUS")
    String status;	
	
	@Column(name = "DELIVER_TIME")
    long delivreTime;
	
	@Column(name = "COMPLETED_TIME")
    long completedTime;
	
}  