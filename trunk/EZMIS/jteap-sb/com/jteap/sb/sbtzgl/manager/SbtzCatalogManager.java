package com.jteap.sb.sbtzgl.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.sb.sbtzgl.model.SbtzCatalog;

@SuppressWarnings("unchecked")
public class SbtzCatalogManager extends HibernateEntityDao<SbtzCatalog> {

	/**
	 * 根据父亲节点编号查询该节点下的所有子节点
	 * @param parentId  
	 * @return  如果parentId为空 返回所有顶层节点
	 * 否则返回所有指定parentId下的所有子节点
	 */
	
	public Collection<SbtzCatalog> findCatalogByParentId(String parentId){
		
		StringBuffer hql=new StringBuffer("from SbtzCatalog as s where ");
		Object args[]=null;
		if(StringUtils.isEmpty(parentId)){
			hql.append("s.parent is null");
			
		}else{
			hql.append("s.parent.id=?");
			args=new String[]{parentId};
		}
		return find(hql.toString(),args);
	}
	
	
	/**
	 * 通过查询条件获得flbm最大的一个sbtzCatalog对象
	 * @param condition
	 * @return
	 */
	public SbtzCatalog getSbtzCatalogByHql(String condition){
		String hql = "";
		if(condition == ""){
			hql = "from SbtzCatalog s where s.parent.id is null order by s.flbm desc";
		}else{
			hql = "from SbtzCatalog s where s.parent.id='"+condition+"' order by s.flbm desc";
		}
		SbtzCatalog sbtzCatalog = null;
		List<SbtzCatalog> list = new ArrayList<SbtzCatalog>();
		Object args[] = null;
		list = find(hql, args);
		for (int i = 0; i < list.size(); i++) {
			sbtzCatalog = (SbtzCatalog)list.get(0);
		}
		return sbtzCatalog;
	}
	
}
