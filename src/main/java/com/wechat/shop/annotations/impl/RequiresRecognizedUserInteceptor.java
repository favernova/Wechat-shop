package com.wechat.shop.annotations.impl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.wechat.shop.annotations.RequiresRecognizedUser;
import com.wechat.shop.model.dao.UserDao;
import com.wechat.shop.model.entity.UserModel;

public class RequiresRecognizedUserInteceptor extends HandlerInterceptorAdapter{

	@Autowired
	UserDao userDao;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		if(handler != null && handler instanceof HandlerMethod){
			HandlerMethod handlerMethod = (HandlerMethod)handler;
			RequiresRecognizedUser requiresRecognizedUser = handlerMethod.getMethodAnnotation(RequiresRecognizedUser.class);
			if(requiresRecognizedUser == null){
				return true;
			}
			String userId = (String) request.getSession().getAttribute("user");
			if(StringUtils.isNotBlank(userId)){
				return true;
			}
			Cookie[] cookies = request.getCookies();
			for(Cookie cookie : cookies){
				String cookieName = cookie.getName();
				if(cookieName.equals("user")){
					userId = cookie.getValue();
					UserModel user = userDao.find(userId);
					if(user != null){
						request.getSession().setAttribute("user", userId);
						return true;
					}
					break;
				}
			}
		    response.sendRedirect("login");  
		    return false;  
		}
		return true;
	}

	
}
