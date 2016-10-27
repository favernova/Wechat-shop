package com.wechat.shop.model.dao.impl.hibernate;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wechat.shop.model.dao.UserDao;
import com.wechat.shop.model.dao.query.QueryParam;
import com.wechat.shop.model.entity.UserModel;


@Service
@Transactional
public class UserDaoHibernateImpl extends DaoHibernate<String, UserModel> implements UserDao{

	public UserDaoHibernateImpl() {
		super(UserModel.class);
	}

	@Override
	public UserModel findByUserName(String userName) {
		List<QueryParam> params = new ArrayList<QueryParam>();
		params.add(new QueryParam("userName", userName));
		List<UserModel> result = findAllByParams(params);
		if(CollectionUtils.isNotEmpty(result)){
			return result.get(0);
		}else{
			return null;
		}
	}

	
	@Override
	@Transactional(readOnly = false)
	public UserModel verifyUser(String userName, String password) {
		UserModel model = findByUserName(userName);
		if(model != null && password != null){
			if(password.equals(model.getPassword())){
				model.setLastVisitedTime((new Date()).getTime());
				this.save(model);
				return model;
			}
		}
		return null;
	}

}
