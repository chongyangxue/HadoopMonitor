package com.hadoopmonitor.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright @ 2012 sohu.com Inc. 
 * All right reserved.
 * <p>
 * DAO基类接口
 * </p>
 * @author liuchong
 * @since 2012-7-4
 */
public interface BaseDAO<T> {
	
	/**
	 * 根据主键获取实体。如果没有相应的实体，返回 null
	 * @param id
	 * @return
	 */
	public T get(Serializable id);
	
	/**
	 * 根据主键获取实体。如果没有相应的实体，抛出异常
	 * @param id
	 * @return
	 */
	public T load(Serializable id);

	/**
	 * 保存指定实体
	 * @param t
	 */
	public void save(T t);
	
	/**
	 * 更新指定实体
	 * @param t
	 */
	public void update(T t);
	
	/**
	 * 更新或者保存指定实体 
	 * @param t
	 */
	public void saveOrUpdate(T t);
	
	/**
	 * 合并指定实体
	 * @param t
	 */
	public T merge(T t);
	
	/**
	 * 根据主键删除指定实体
	 * @param id
	 */
	public void deleteByKey(Serializable id);

	/**
	 * 删除指定实体
	 * @param t
	 */
	public void delete(T t);

	
	/**
	 * 获取所有数据
	 * @return
	 */
	public List<T> findAll();
	
	/**
	 * 获取指定实体数据
	 * @param t
	 * @return
	 */
	public List<T> findByExample(T t);

	/**
	 * 查询指定hql并返回集合
	 * @param hql
	 * @param values
	 * @return
	 */
	public List<T> find(String hql, Object... values);
	
	/**
	 * 使用HSQL语句直接增加、更新、删除实体
	 * @param queryString
	 * @return
	 */
	public int bulkUpdate(String hql);
	
	/**
	 * 使用带参数的HSQL语句增加、更新、删除实体
	 * @param hql
	 * @param values
	 * @return
	 */
	public int bulkUpdate(String hql, Object... values);
	
	/**
	 * 查询指定实体的总记录数
	 * @param hql
	 * @return
	 */
	public int getCount(final String hql);

	/**
	 * 根据传入的偏移量和步长来查询数据
	 * @param hql
	 * @param offset
	 * @param length
	 * @return
	 */
	public List<T> getListForPage(final String hql, final int offset, final int length);
	
}
