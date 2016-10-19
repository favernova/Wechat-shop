package com.wechat.shop.model.dao.impl.hibernate;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wechat.shop.model.dao.GenericDao;
import com.wechat.shop.model.dao.query.QueryPagination;
import com.wechat.shop.model.dao.query.QueryParam;
import com.wechat.shop.model.dao.query.QueryParam.Bracket;
import com.wechat.shop.model.dao.query.SortParam;
import com.wechat.shop.model.entity.AbstractModel;

@Repository
public abstract class DaoHibernate<K extends Serializable, V> extends
		HibernateDaoSupport implements GenericDao<K, V> {
	
	private final Class<V> type;
	
	private static final String SINGLE_SPACE = " ";
	
    @Autowired
    public void init(SessionFactory factory) {
        setSessionFactory(factory);
    }

    public DaoHibernate(Class<V> type) {
        this.type = type;
    }
	
    
    @Override
    @Transactional
    public V save(V object) {
        if (object instanceof AbstractModel) {
            Date now = new Date();
            AbstractModel entity = (AbstractModel) object;
            if (entity.getCreateTime() == 0) {
                entity.setCreateTime(now.getTime());
            }
            entity.setLastModifyTime(now.getTime());
        }
        this.getHibernateTemplate().saveOrUpdate(object);
        return object;
    }

	@Override
	public void delete(K id) {
        V value = findExisting(id);
        deleteByObject(value);
	
	}

	@Override
    @Transactional(readOnly = true)
    public V findExisting(K id) {
        V value = this.find(id);
        if (value != null) {
            return value;
        }
        throw new DataRetrievalFailureException("Can not find " + type + " with ID " + id);
    }
    
	@Override
	public void deleteByObject(V object) {
        AbstractModel entity = (AbstractModel) object;

        if (!entity.isDeleted()) {
            entity.setDeleted(true);
            entity.setLastModifyTime((new Date()).getTime());
            this.getHibernateTemplate().update(entity);
        }
	}

	@Override
	public V find(K id) {
        V value = this.getHibernateTemplate().get(type, id);
        if (value != null) {
            AbstractModel entity = (AbstractModel) value;
            if (!entity.isDeleted()) {
                return value;
            }
        }
        return null;
	}

	@Override
	public long findCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	protected int updateAllByParams(Map<String, Object> data, List<QueryParam> params){
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("update ").append(type.getName()).append(" o");
		this.appendUpdateClause(sqlBuilder, data);
		this.addWhereClause(sqlBuilder, params);
		return this.getHibernateTemplate().executeWithNativeSession(session->{
			Query<V> query = session.createQuery(sqlBuilder.toString(), type);
			this.buildQueryParams(query, params);
			this.appendUpdateQueryParam(query, data);
			return query.executeUpdate();
		});
	}
	
    protected List<V> findAllByParams(List<QueryParam> params) {
        return this.findAllByParams(null, params, null, null);
    }

    protected List<V> findAllByParams(List<QueryParam> params, List<SortParam> sortParams,
            QueryPagination page) {
        return this.findAllByParams(null, params, sortParams, page);
    }
	
	protected List<V> findAllByParams(String[] columns,
			List<QueryParam> queryParams, List<SortParam> sortParams,
			QueryPagination page) {
		String hql = this.buildSelectHql(columns, queryParams, sortParams);
		return (List<V>) this.getHibernateTemplate().executeWithNativeSession(session->{
			Query<V> query = session.createQuery(hql, type);
			this.buildQueryParams(query, queryParams);
			if(page != null){
				query.setFirstResult(page.getFirstResult());
				query.setMaxResults(page.getMaxResult());
			}
			return query.getResultList();
		});
	}
	
	void appendUpdateQueryParam(Query<V> query, Map<String, Object> data){
		for(Entry<String, Object> entry : data.entrySet()){
			query.setParameter(entry.getKey(), entry.getValue());
		}
	}

	void appendUpdateClause(StringBuilder sqlBuilder, Map<String, Object> data){
		boolean begin = true;
		for(Entry<String, Object> entry : data.entrySet()){
			if(begin){
				sqlBuilder.append(" set");
				begin = false;
			}else{
				sqlBuilder.append(" ,");
			}
			String key = entry.getKey();
			sqlBuilder.append(" o.").append(key).append(":=").append(key);
		}
	}
	
    protected int deleteByParams(List<QueryParam> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ").append(this.type.getName()).append(" o");
        this.addWhereClause(sql, params);
        return super.getHibernateTemplate().executeWithNativeSession(session -> {
            Query<V> query = session.createQuery(sql.toString(), type);
            buildQueryParams(query, params);
            return query.executeUpdate();
        });
    }
    
	String buildSelectHql(String[] columns, List<QueryParam> queryParams, List<SortParam> sortParams){
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select");
		appendColumns(sqlBuilder, columns);
		sqlBuilder.append(" from ").append(this.type.getName()).append(" o");
		this.addWhereClause(sqlBuilder, queryParams);
		this.addSortParams(sqlBuilder, sortParams);
		return sqlBuilder.toString();
	}

	protected void addWhereClause(StringBuilder sqlBuilder, List<QueryParam> params){
        if (params == null || params.isEmpty()) {
            return;
        }
        int index = 0;
		boolean beginWhere = true;
		for(QueryParam param : params){
			if(beginWhere){
				sqlBuilder.append(" where");
				beginWhere = false;
			}else{
				sqlBuilder.append(SINGLE_SPACE).append(param.getLogicParam().toString());
			}
			if(Bracket.LEFT_BRACKET.equals(param.getBracketParam())){
				sqlBuilder.append(SINGLE_SPACE).append(Bracket.LEFT_BRACKET.toString());
			}
			if(param.isInOperation()){
                Validate.notNull(param.getParamValue(), "paramValue cannot be null");
                Validate.isInstanceOf(Collection.class, param.getParamValue(),
                        "paramValue should be an instance of Collection");
                Collection<?> list = (Collection<?>) param.getParamValue();
                if(CollectionUtils.isNotEmpty(list)){
                	sqlBuilder.append(" o.").append(param.getParamName()).append(param.getOperator().toString())
                	.append("SINGLE_SPACE").append(Bracket.LEFT_BRACKET.toString());
                	sqlBuilder.append("?").append((index++));
                	for(int i = 1;i < list.size(); i++){
                		sqlBuilder.append(",").append("?").append((index++));
                	}
                	sqlBuilder.append(Bracket.RIGHT_BRACKET.toString());
                }
			}else if(param.isIsOperator()){
				sqlBuilder.append(" o.").append(param.getParamName()).append(param.getOperator().toString());
			}else{
				sqlBuilder.append(" o.").append(param.getParamName()).append(param.getOperator().toString()).append(" ?").append((index++));
			}
			if(Bracket.RIGHT_BRACKET.equals(param.getBracketParam())){
				sqlBuilder.append(SINGLE_SPACE).append(Bracket.RIGHT_BRACKET.toString());
			}
		}
	}
	
	protected void addSortParams(StringBuilder sqlBuilder, List<SortParam> params){
		if(CollectionUtils.isEmpty(params)){
			return;
		}
		boolean beginSort = true;
		for(SortParam param : params){
			if(beginSort){
				sqlBuilder.append(" sort by ");
				beginSort = false;
			}else{
				sqlBuilder.append(",");
			}
			sqlBuilder.append(param.toString());
		}
	}
	
	protected void appendColumns(StringBuilder sqlBuilder, String[] columns){
		if(columns == null || columns.length == 0){
			sqlBuilder.append(" o");
		}else{
			sqlBuilder.append(" new ").append(type.getName()).append("(");
			boolean begin = true;
			for(String columnName : columns){
				if(begin){
					begin = false;
				}else{
					sqlBuilder.append(",");
				}
				sqlBuilder.append("o.").append(columnName);
			}
			sqlBuilder.append(")");
		}
	}
	
	protected void buildQueryParams(Query<V> query, List<QueryParam> queryParmas){
		if(CollectionUtils.isEmpty(queryParmas)){
			return;
		}
		int index = 0;
		for(QueryParam param : queryParmas){
			if(param.isInOperation()){
				Collection<?> list = (Collection<?>) param.getParamValue();
				for(Object o : list){
					query.setParameter(index++, o);
				}
			}else if(param.isIsOperator() || param.isWhereClause()){

			}else{
				query.setParameter(index++, param.getParamValue());
			}
		}
	}

    @SuppressWarnings("unchecked")
	protected List<V> findByNamedQueryAndNamedParam(String query, String[] paramNames,
            Object[] values) {
        return (List<V>) getHibernateTemplate().findByNamedQueryAndNamedParam(query, paramNames,
                values);
    }
    
    protected void prepareDeleteData(Map<String, Object> updateData) {
        updateData.put(AbstractModel.IS_DELETED, Boolean.TRUE);
        updateData.put(AbstractModel.LAST_MODIFY_TIME, new Date());
    }
}
