package com.wechat.shop.model.dao;

public interface GenericDao<K, V>{
	
	V save(V object);
	
    /**
     * Delete logically.
     * Note: This method will throw DataRetrievalFailureException when record does not exist
     *
     * @param id primary key.
     */
    void delete(K id);
    
    void deleteByObject(V object);
    
    /**
     * Find by id.
     *
     * @param id primary key.
     * @return null if record not found.
     */
    V find(K id);
    
    long findCount();

	V findExisting(K id);
}
