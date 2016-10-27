package com.wechat.shop.controller;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.wechat.shop.model.dao.UserDao;
import com.wechat.shop.model.entity.UserModel;
import com.wechat.shop.model.enums.UserGroup;
import com.wechat.shop.utils.MD5Utils;

@Controller
public class LoginController {
	
	@Autowired
	UserDao userDao;

    @RequestMapping("/login")
    public ModelAndView loginPage(HttpServletRequest request){
    	ModelAndView mav = new ModelAndView("login");
    	String userId = (String)request.getSession().getAttribute("user");
    	Long lastLogInTime = (Long) request.getSession().getAttribute("loginTime");
    	if(StringUtils.isNotBlank(userId) && lastLogInTime != null){
    		UserModel user = userDao.find(userId);
    		if(user != null){
    			mav.addObject("name", user.getUserName());
    		}
    	}
        return mav;
    }
    
    @RequestMapping(value="/login.do", method=RequestMethod.POST)
    public String login(HttpServletRequest request, HttpServletResponse response, @RequestParam String userName, @RequestParam String password){
    	if(StringUtils.isNoneEmpty(userName, password)){
    		System.out.println(userName + " " + password);
    		UserModel userModel = userDao.verifyUser(userName, MD5Utils.EncodeByMd5(password));
    		if(userModel != null){
    			request.getSession().setAttribute("user", userModel.getId());
    			request.getSession().setAttribute("loginTime", userModel.getLastVisitedTime());
    			Cookie cookie = new Cookie("user", userModel.getId());
    			cookie.setMaxAge(60 * 60 * 24 * 7);
    			response.addCookie(cookie);
    			return "redirect:/index";
    		}
    	}
        return "redirect:/login";
    }
    
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
    	HttpSession session = request.getSession();
    	session.removeAttribute("loginTime");
    	session.removeAttribute("user");
    	return "redirect:/login";
    }
    
    @RequestMapping("/register")
    public ModelAndView regsiterPage(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
    	ModelAndView mav = new ModelAndView("register");
    	String error = request.getParameter("error");
		if(error != null){
	    	error = URLDecoder.decode(error, "UTF-8");
			mav.addObject("error", error);
		}
        return mav;
    }
    
    @RequestMapping(value="/register.do", method=RequestMethod.POST)
    public String regsiter(HttpServletRequest request, HttpServletResponse response, @RequestParam String userName, @RequestParam String password, @RequestParam String password2, @RequestParam String email) throws IOException{
    	String errorMsg = "";
    	if(StringUtils.isNoneEmpty(userName, password, password2, email)){
    		UserModel user = userDao.findByUserName(userName);
    		if(user != null){
    			errorMsg = "用户名已经存在，请选用其他用户名";
    		}else if((!password.equals(password2)) || password.length() < 8){
    			errorMsg = "两次输入的密码不匹配，或密码过于简单，请重新输入";
    		}
    		if(StringUtils.isEmpty(errorMsg)){
	    		UserModel newUser = new UserModel();
	    		newUser.setEmail(email);
	    		newUser.setPassword(MD5Utils.EncodeByMd5(password));
	    		newUser.setUserName(userName);
	    		newUser.setUserGroup(UserGroup.USER);
	    		newUser = userDao.save(newUser);
	    		if(StringUtils.isNoneEmpty(newUser.getId())){
	    			return "redirect:/login";
	    		}else{
	    			errorMsg = "系统内部错误，请稍后再试";
	    		}
    		}
    	}else{
    		errorMsg = "必填信息（用户名，密码，邮箱）没有填，请重新填写";
    	}
    	
    	String redirectUrl = URLEncoder.encode(errorMsg, "UTF-8");
    	redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
        return "redirect:/register?error=" + redirectUrl;
    }
}
