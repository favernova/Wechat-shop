package com.wechat.shop.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresLogin {

	static final long MAX_LOGIN_INTEVAL = 60 * 60 * 1000;
	
	boolean needAdmin() default false;
	
	boolean needRoot() default false;
	
	boolean allowWechatRecognized() default false;
	
	long maxIntevalLoginTime() default MAX_LOGIN_INTEVAL;
}
