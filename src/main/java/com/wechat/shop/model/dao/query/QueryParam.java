package com.wechat.shop.model.dao.query;

import org.apache.commons.lang3.Validate;

import lombok.Getter;

@Getter
public class QueryParam {
	public enum Operator{
		IN(" in"),
		NOT_IN(" not in"),
		LIKE(" like"),
		NOT_LIKE(" not like"),
		EQUAL("="),
		NOT_EQUAL("<>"),
		GREATER_THAN(">"),
		LESS_THAN("<"),
		GREAT_THAN_EQUAL(">="),
		LESS_THAN_EQUAL("<="),
		IS_NULL(" is null"),
		IS_NOT_NULL(" is not null"),
		WHERE(" where");
		
		Operator(String sql){
			this.sql = sql;
		}
		String sql;
		
		@Override
		public String toString() {
			return sql.toString();
		}
	}
	
	public enum Bracket{
		NO_BRACKET(""),
		LEFT_BRACKET("("),
		RIGHT_BRACKET(")");
		
		String sql;
		
		Bracket(String sql){
			this.sql = sql;
		}
		
		@Override
		public String toString() {
			return sql.toString();
		}
	}
	
	public enum Logic{
		AND(" and"),
		OR(" or");
		
		String sql;
		Logic(String sql){
			this.sql = sql;
		}
		
		@Override
		public String toString() {
			return sql.toString();
		}
	}
	
	private Operator operator;
	private Logic logicParam;
	private Bracket bracketParam;
	
	private String paramName;
	private Object paramValue;
	
	
	
	void init(Logic logicParam, Bracket bracketParam, Operator operator,
			String paramName, Object paramValue) {
		Validate.notBlank(paramName, "paramName cannot be null or empty");
		Validate.notNull(logicParam, "paramLogic cannot be null");
		Validate.notNull(bracketParam, "paramBracket cannot be null");
		Validate.notNull(operator, "operator cannot be null");
		Validate.notNull(paramValue, "paramValue cannot be null");

		this.operator = operator;
		this.logicParam = logicParam;
		this.bracketParam = bracketParam;
		this.paramName = paramName;
		this.paramValue = paramValue;
	}



	public QueryParam(String paramName, Object paramValue) {
		init(Logic.AND, Bracket.NO_BRACKET, Operator.EQUAL, paramName, paramValue);
	}



	public QueryParam(Logic logicParam,
			Bracket bracketParam, Operator operator, String paramName, Object paramValue) {
		init(logicParam, bracketParam, operator, paramName, paramValue);
	}



	public QueryParam(Operator operator, String paramName, Object paramValue) {
		init(Logic.AND, Bracket.NO_BRACKET, operator, paramName, paramValue);
	}
	
	public boolean isWhereClause(){
		return Operator.WHERE.equals(operator);
	}
	
	public boolean isInOperation(){
		return Operator.IN.equals(operator) || Operator.NOT_IN.equals(operator);
	}
	
	public boolean isIsOperator(){
		return Operator.IS_NOT_NULL.equals(operator) || Operator.IS_NULL.equals(operator);
	}
}
