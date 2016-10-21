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
@Table(name = "PRODUCT")
@GenericGenerator(name = "wechat-shop-id", strategy = "uuid")
public class ProductModel extends AbstractModel{

	//private static final long serialVersionUID = ;

	@Id
	@GeneratedValue(generator = "wechat-shop-id")
	@Column(name = "ID", nullable = false, updatable = false)
	String id;

	@Column(name = "TITLE")
	String title;

	@Column(name = "PRODUCT_SEQ")
	String productSeq;	

	@Column(name = "BROWSE_PATH")
	String browsePath;

	@Column(name = "IMAGE_URL")
	String imageUrl;

	@Column(name = "TYPE")
	String type;
	
	// @Column(name = "PATENT_ID")
	// String patentId;
	
	@Column(name = "QUANTITY")
	long quantity;
	
	@Column(name = "PRICE_INTEGRAL")
	long priceIntegral;
	

	//`ATTRIBUTES_TEXT` longtext,
  
  
	//`ITEM_STATUS` varchar(20) DEFAULT NULL COMMENT 'ACTIVE, INACTIVE. ',
	

	//`SEARCH_TERMS` mediumtext,
	@Column(name = "SEARCH_TERMS")
    private long lastVisitedTime;

}