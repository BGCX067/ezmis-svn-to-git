package com.jteap.jhtj.bbsjydy.manager;

import java.util.ArrayList;
import java.util.List;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.jhtj.bbsjydy.model.BbSjzd;
@SuppressWarnings( { "unchecked" })
public class BbSjzdManager extends HibernateEntityDao<BbSjzd> {
	/**
	 * 
	 * 描述:根据数据源ID得到字段列表
	 * 时间:Oct 29, 2010
	 * 作者:童贝
	 * 参数:
	 * 返回值:List<BbSjzd>
	 * 抛出异常:
	 */
	public List<BbSjzd> getSjzdListByBBioid(String bbioid){
		List<BbSjzd> sjzdList=new ArrayList<BbSjzd>();
		String hql="from BbSjzd where bbioid='"+bbioid+"' order by forder";
		sjzdList=this.find(hql);
		return sjzdList;
	}
}
