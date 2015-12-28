package com.jteap.system.dataperm.util;

import java.util.List;

import org.hibernate.Query;

import com.jteap.core.dao.support.Page;

@SuppressWarnings("unchecked")
public interface DataPermAdviceInterface {
	public Page pagedQueryWithStartIndex(String hql, int startIndex, int pageSize, Object... values);
	public Page pagedQuery(String hql, int pageNo, int pageSize, Object... values);
	public List find(String hql, Object... values);
	public Query createQuery(String hql, Object... values);
	public List find(String hql,boolean bUseQueryCache, Object... values);
}
