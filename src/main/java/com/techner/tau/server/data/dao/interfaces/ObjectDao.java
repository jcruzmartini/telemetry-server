package com.techner.tau.server.data.dao.interfaces;

import java.util.Collection;

public interface ObjectDao<T> {
	public void insert(T entity);

	public T findByCode(T entity, Long codigo, boolean lock);

	public Collection<T> findByQuery(String query);

	public Collection<T> findByParameters(String nombreQuery, String[] parameterNames, Object[] parameterValues);

	public void delete(T entity);
}
