package com.wechat.shop.model.dao.query;

import lombok.Getter;

import org.apache.commons.lang3.Validate;

@Getter
public class SortParam {
	String paramName;
	boolean asc;
	
    public SortParam(String paramName, boolean asc) {
        Validate.notBlank(paramName, "paramName cannot be null or empty");

        this.paramName = paramName;
        this.asc = asc;
    }

    @Override
    public String toString() {
        String ret;
        if (asc) {
            ret = paramName + " asc";
        } else {
            ret = paramName + " desc";
        }
        return ret;
    }
}
