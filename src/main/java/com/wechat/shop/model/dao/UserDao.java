package com.wechat.shop.model.dao;

import com.wechat.shop.model.entity.UserModel;

public interface UserDao extends GenericDao<String, UserModel>{
	UserModel findByUserName(String userName); 
	
	UserModel verifyUser(String userName, String password);
}
