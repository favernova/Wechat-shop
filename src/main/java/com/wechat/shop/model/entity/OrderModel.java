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
@Table(name = "ORDER")
@GenericGenerator(name = "wechat-shop-id", strategy = "uuid")  
public class OrderModel extends AbstractModel{
	
	//private static final long serialVersionUID = ;
	
	@Id
    @GeneratedValue(generator = "wechat-shop-id")
	@Column(name = "ID", nullable = false, updatable = false)
	int id;
	
	// @Column(name = "USER_ID")
    // String userId;
	
	@Column(name = "STATUS")
    String status;
	
	@Column(name = "DELIVER_TIME")
    long deliverTime;
	
	@Column(name = "COMPLETED_TIME")
    long completedTime;
	
}