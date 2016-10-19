package com.wechat.shop.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wechat.shop.model.dao.UserDao;

@Controller
public class DemoController {
	
	@Autowired
	UserDao userDao;

    @RequestMapping("/index")
    public String index(){
    	boolean a = userDao.verifyUser("User", "safafd");
    	if(a ){
    		System.out.println("log in");
    	}else{
    		System.out.println("not such entity");
    	}
        return "demo";
    }
}

