package com.wechat.shop.annotations.impl;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.wechat.shop.annotations.RequiresLogin;
import com.wechat.shop.model.dao.UserDao;
import com.wechat.shop.model.entity.UserModel;
import com.wechat.shop.model.enums.UserGroup;

public class RequiresLoginInteceptor extends HandlerInterceptorAdapter {

	@Autowired
	UserDao userDao;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		if(handler != null && handler instanceof HandlerMethod){
			HandlerMethod handlerMethod = (HandlerMethod)handler;
			RequiresLogin requiredLogin = handlerMethod.getMethodAnnotation(RequiresLogin.class);
			if(requiredLogin == null){
				return true;
			}
			HttpSession session = request.getSession();
			String userId = (String) session.getAttribute("user");
			Long lastLogInTime = (Long) session.getAttribute("loginTime");
			if(StringUtils.isNotBlank(userId) && lastLogInTime != null && ((new Date()).getTime() - lastLogInTime.longValue()) < requiredLogin.maxIntevalLoginTime()){
				if(requiredLogin.needAdmin() || requiredLogin.needRoot()){
					UserModel user = userDao.find(userId);
					if(user != null){
						if(UserGroup.ADMIN.equals(user.getUserGroup()) && !requiredLogin.needRoot() || 
								UserGroup.ROOT.equals((user.getUserGroup()))){
							return true;
						}
					}
				}else{
					return true;
				} 
			}
			session.removeAttribute("loginTime");
		    response.sendRedirect("login");  
		    return false;  
		}
		return true;
	}

}
