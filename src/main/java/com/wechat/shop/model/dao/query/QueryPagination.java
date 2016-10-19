package com.wechat.shop.model.dao.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QueryPagination {
	int firstResult;
	int maxResult;
}
