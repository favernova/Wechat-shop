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
@Table(name = "USER")
@GenericGenerator(name = "wechat-shop-id", strategy = "uuid")  
public class UserModel extends AbstractModel{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 5012033721312252818L;

	@Id
    @GeneratedValue(generator = "wechat-shop-id")
	@Column(name = "ID", nullable = false, updatable = false)
	String id;
    
    @Column(name = "USER_NAME")
    String userName;
    
    @Column(name = "PASSWORD")
    String password;
    
    @Column(name = "USER_GROUP")
    @Enumerated(EnumType.STRING)
    UserGroup userGroup;
    
    @Column(name = "WECHAT_ID")
    String wechatId;
    
    @Column(name = "EMAIL")
    String email;
    
    @Column(name = "LAST_VISITED_TIME")
    private long lastVisitedTime;
}
