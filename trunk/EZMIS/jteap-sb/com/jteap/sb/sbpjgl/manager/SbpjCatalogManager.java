package com.jteap.sb.sbpjgl.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.sb.sbpjgl.model.SbpjCatalog;

@SuppressWarnings("unchecked")
public class SbpjCatalogManager extends HibernateEntityDao<SbpjCatalog> {

	/**
	 * 根据父亲节点编号查询该节点下的所有子节点
	 * @param parentId  
	 * @return  如果parentId为空 返回所有顶层节点
	 * 否则返回所有指定parentId下的所有子节点
	 */
	
	public Collection<SbpjCatalog> findCatalogByParentId(String parentId){
		
		StringBuffer hql=new StringBuffer("from SbpjCatalog as s where ");
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
	 * 通过查询条件获得flbm最大的一个sbpjCatalog对象
	 * @param condition
	 * @return
	 */
	public SbpjCatalog getSbpjCatalogByHql(String condition){
		String hql = "";
		if(condition == ""){
			hql = "from SbpjCatalog s where s.parent.id is null order by s.flbm desc";
		}else{
			hql = "from SbpjCatalog s where s.parent.id='"+condition+"' order by s.flbm desc";
		}
		SbpjCatalog sbpjCatalog = null;
		List<SbpjCatalog> list = new ArrayList<SbpjCatalog>();
		Object args[] = null;
		list = find(hql, args);
		for (int i = 0; i < list.size(); i++) {
			sbpjCatalog = (SbpjCatalog)list.get(0);
		}
		return sbpjCatalog;
	}
	
}
