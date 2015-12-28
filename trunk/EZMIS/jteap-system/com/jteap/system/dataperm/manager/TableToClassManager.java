/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.system.dataperm.manager;

import java.util.*;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.system.dataperm.model.*;
@SuppressWarnings({ "serial", "unchecked", "unused" })
public class TableToClassManager extends HibernateEntityDao<TableToClass>{
	public Collection<TableToClass> findTableToClassByParentId(String parentId){
		
		StringBuffer hql=new StringBuffer("from TableToClass as g where ");
		Object args[]=null;
		if(StringUtil.isEmpty(parentId)){
			hql.append("g.parent is null");
			
		}else{
			hql.append("g.parent.id=?");
			args=new String[]{parentId};
		}
		return find(hql.toString(),args);
	}
	
	/**
	 * 查询排序号最大值
	 * @return
	 */
	public long findMaxOrder(){
		String hql = "select max(torder) from TableToClass tc";
		List<Long> resultList = this.find(hql);
		long max = 0;
		if(resultList.size()>0){
			Long max2 = resultList.get(0);
			if(max2!=null)
				max = max2;
		}
		return max;
	}
}
