package com.wechat.shop.model.dao;

import com.wechat.shop.model.entity.UserModel;

public interface UserDao extends GenericDao<String, UserModel>{
	UserModel findByUserName(String userName); 
	
	boolean verifyUser(String userName, String password);
}
