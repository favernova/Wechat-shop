package com.wechat.shop.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wechat.shop.annotations.RequiresLogin;
import com.wechat.shop.annotations.RequiresRecognizedUser;


@Controller
public class DemoController {
	@RequiresLogin
    @RequestMapping("/index")
    public String index(){
        return "demo";
    }
	
	@RequiresRecognizedUser
    @RequestMapping("/index1")
    public String index1(){
        return "demo";
    }
}

