package com.hadoopmonitor.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateCallback;

/**
 * Copyright @ 2012 sohu.com Inc. All right reserved.
 * <p>
 * DAO基类实现
 * </p>
 * 
 * @author liuchong
 * @since 2012-7-30
 */
public class BaseDAOImpl<T> extends HibernateDaoSupport implements BaseDAO<T> {
	protected Class<T> entityClass;

	@SuppressWarnings("unchecked")
	public BaseDAOImpl() {
		ParameterizedType type = (ParameterizedType)getClass()
				.getGenericSuperclass();
		entityClass = (Class<T>)type.getActualTypeArguments()[0];
	}

	// 获取所有数据
	public List<T> findAll() {
		@SuppressWarnings("unchecked")
		List<T> lists = getHibernateTemplate().find(
				"from " + entityClass.getName());
		return lists;
	}

	public List<T> findByExample(T t) {
		@SuppressWarnings("unchecked")
		List<T> lists = getHibernateTemplate().findByExample(t);
		return lists;
	}

	// 保存指定实体类
	public void save(T t) {
		getHibernateTemplate().save(t);
	}

	// 删除指定实体类
	public void delete(T t) {
		getHibernateTemplate().delete(t);
	}

	// 更新或者保存指定实体
	public void saveOrUpdate(T t) {
		getHibernateTemplate().saveOrUpdate(t);
	}

	// merge指定实体
	public T merge(T t) {
		return getHibernateTemplate().merge(t);
	}

	// 查询指定hql并返回集合
	@SuppressWarnings("unchecked")
	public List<T> find(String hql, Object... values) {
		return (List<T>)getHibernateTemplate().find(hql, values);
	}

	// 查询指定实体的总记录数
	public int getCount(final String hql) {
		int count = DataAccessUtils.intResult(getHibernateTemplate().find(hql));
		return count;
	}

	// 根据传入的偏移量和步长来查询数据
	public List<T> getListForPage(final String hql, final int offset,
			final int length) {
		@SuppressWarnings("unchecked")
		List<T> list = getHibernateTemplate().executeFind(
				new HibernateCallback<List<T>>() {
					public List<T> doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createQuery(hql);
						query.setFirstResult(offset);
						query.setMaxResults(length);
						return query.list();
					}
				});
		return list;
	}

	@Override
	public T get(Serializable id) {
		return getHibernateTemplate().get(entityClass, id);
	}

	@Override
	public T load(Serializable id) {
		return getHibernateTemplate().load(entityClass, id);
	}

	@Override
	public void update(T t) {
		getHibernateTemplate().update(t);
	}

	@Override
	public void deleteByKey(Serializable id) {
		delete(load(id));
	}

	@Override
	public int bulkUpdate(String hql) {
		return getHibernateTemplate().bulkUpdate(hql);
	}

	@Override
	public int bulkUpdate(String hql, Object... values) {
		return getHibernateTemplate().bulkUpdate(hql, values);
	}
}
