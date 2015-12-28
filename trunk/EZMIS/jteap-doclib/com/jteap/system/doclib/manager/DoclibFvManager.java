package com.jteap.system.doclib.manager;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.system.doclib.model.DoclibCatalog;
import com.jteap.system.doclib.model.DoclibFv;

/**
 * 文档中心扩展值管理对象
 * 
 * @author caofei
 * 
 */
@SuppressWarnings( { "unchecked", "unused" })
public class DoclibFvManager extends HibernateEntityDao<DoclibFv> {

	/**
	 * 通过doclibId获得DoclibFv集合
	 */
	public Collection<DoclibFv> findDoclibFvByDocId(String docId) {

		StringBuffer hql = new StringBuffer("from DoclibFv as df where ");
		Object args[] = null;
		hql.append("df.doclib.id=?");
		args = new String[] {docId} ;
	
		return find(hql.toString(), args);
	}
	
	/**
	 * 
	 *描述：根据文档ID和字段ID得到值对象
	 *时间：2010-6-24
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public DoclibFv findUniqueDoclibFvByDocidAndFieldid(String docid,String fieldid){
		DoclibFv fv=new DoclibFv();
		fv=(DoclibFv)this.findUniqueByHql("from DoclibFv where doclib.id=? and fieldId=?", new Object[]{docid,fieldid});
		return fv;
	}
}
