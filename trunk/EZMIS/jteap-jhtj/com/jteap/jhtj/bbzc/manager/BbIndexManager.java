package com.jteap.jhtj.bbzc.manager;

import java.util.ArrayList;
import java.util.List;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.jhtj.bbzc.model.BbIndex;
@SuppressWarnings({ "unchecked", "serial" })
public class BbIndexManager extends HibernateEntityDao<BbIndex> {
	/**
	 * 
	 *描述：根据报表编码和分类ID查找报表模板对象
	 *时间：2010-4-20
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public BbIndex findBbIndexByBbbmAndFlid(String bbbm,String flid){
		BbIndex index=(BbIndex)this.findUniqueByHql("from BbIndex where bbbm='"+bbbm+"' and flid='"+flid+"'");
		return index;
	}
	
	/**
	 * 
	 *描述：获取模板列表
	 *时间：2010-4-26
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public List<BbIndex> findBbindexListByFlid(String flid){
		List<BbIndex> list=new ArrayList<BbIndex>();
		String hql="from BbIndex where flid='"+flid+"' order by sortno";
		list=this.find(hql);
		return list;
	}
}
