package com.wechat.shop.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class AbstractModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -790244029459411374L;

	public static final String IS_DELETED = "isDeleted";
    public static final String CREATE_TIME = "createTime";
    public static final String LAST_MODIFY_TIME = "lastModifyTime";

    @Column(name = "CREATE_TIME", updatable = false)
    private long createTime;

    @Column(name = "LAST_MODIFY_TIME")
    private long lastModifyTime;
    
    @Column(name = "IS_DELETED")
    protected boolean isDeleted;
}
