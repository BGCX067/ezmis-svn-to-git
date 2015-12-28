package com.jteap.system.doclib.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.system.doclib.model.Doclib;
import com.jteap.system.doclib.model.DoclibAttach;
import com.jteap.system.doclib.model.DoclibCatalog;
import com.jteap.system.doclib.model.DoclibCatalogField;
import com.jteap.system.doclib.model.DoclibFv;

/**
 * 文档中心管理对象
 * 
 * @author caofei
 * 
 */
@SuppressWarnings( { "unchecked", "unused" })
public class DoclibManager extends HibernateEntityDao<Doclib> {

	/**
	 * 通过doclibId获得文档对象
	 */
	public Doclib findDoclibById(String id) {
		String hql = "from Doclib where id = ?";
		List<Doclib> list = this.find(hql, id);
		Doclib doclib = new Doclib();
		Iterator<Doclib> iterator = list.iterator();
		while (iterator.hasNext()) {
			doclib = (Doclib) iterator.next();
		}
		return doclib;
	}

	/**
	 * 通过doclibId获得分类对象
	 */
	public DoclibCatalog findDoclibCatalogById(String id) {
		Doclib doclib = findDoclibById(id);
		String catalogId = (String) doclib.getDoclibCatalog().getId();
		String hql = "from DoclibCatalog where id = ?";
		List<DoclibCatalog> list = this.find(hql, catalogId);
		DoclibCatalog doclibCatalog = new DoclibCatalog();
		Iterator<DoclibCatalog> iterator = list.iterator();
		while (iterator.hasNext()) {
			doclibCatalog = (DoclibCatalog) iterator.next();
		}
		return doclibCatalog;
	}

	/**
	 * 通过doclibId获得附件对象
	 */
	public DoclibAttach findDoclibAttachById(String id) {
		String hql = "from DoclibAttach where DOCLIB_ID = ?";
		List<DoclibAttach> list = this.find(hql, id);
		DoclibAttach doclibAttach = new DoclibAttach();
		Iterator<DoclibAttach> iterator = list.iterator();
		while (iterator.hasNext()) {
			doclibAttach = (DoclibAttach) iterator.next();
		}
		return doclibAttach;
	}

	/**
	 * 通过doclibId获得DoclibCatalogField对象
	 */
	public DoclibCatalogField findDoclibCatalogFieldById(String id) {
		DoclibCatalog doclibCatalog = findDoclibCatalogById(id);
		String catalogId = (String) doclibCatalog.getId();
		String hql = "from DoclibCatalogField where CATALOG_ID = ?";
		List<DoclibCatalogField> list = this.find(hql, catalogId);
		DoclibCatalogField doclibCatalogField = new DoclibCatalogField();
		Iterator<DoclibCatalogField> iterator = list.iterator();
		while (iterator.hasNext()) {
			doclibCatalogField = (DoclibCatalogField) iterator.next();
		}
		return doclibCatalogField;
	}

	/**
	 * 通过doclibId获得DoclibFv对象
	 */
	public DoclibFv findDoclibFvById(String id) {
		String hql = "from DoclibFv where DOCLIB_ID = ?";
		List<DoclibFv> list = this.find(hql, id);
		DoclibFv doclibFv = new DoclibFv();
		Iterator<DoclibFv> iterator = list.iterator();
		while (iterator.hasNext()) {
			doclibFv = (DoclibFv) iterator.next();
		}
		return doclibFv;
	}

	/**
	 * 
	 * @param catalogId
	 * @return
	 */
	public Long countDoclibByCatalogId(String catalogId) {
		String hql = "select count(*) from Doclib where doclibCatalog.id=?";
		List list = this.find(hql, new Object[] { catalogId });
		Long count = (Long) list.get(0);
		return count;
	}
	
	/**
	 * 
	 * @param levelId
	 * @return
	 */
	public Long countDoclibByLevelId(String levelId) {
		String hql = "select count(*) from Doclib where doclibLevel.id =?";
		List list = this.find(hql, new Object[] { levelId });
		Long count = (Long) list.get(0);
		return count;
	}
	
	/**
	 * 删除文档，级联删除文档扩展字段值和文档附件
	 * @param o
	 * @param kyes
	 */
	public void remove(Object o,Serializable[] kyes) {
		removeBatch( o.getClass(), kyes);
	}
	
	
	/**
	 * 
	 *描述：重载分页(根据分类ID)
	 *时间：2010-6-23
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public Page pagedQuery(int startIndex,int pagesize,String hql,Object... values){
		Page page = pagedQueryWithStartIndex(hql,startIndex, pagesize,values);
		return page;
	}
	
	/**
	 * 根据时间倒序 返回指定条数的对象
	 * @param catalogCode
	 * @param n
	 * @return
	 */
	public List<Doclib> findDoclibListByCatalogId(String catalogCode,int n){
		String hql = "FROM Doclib WHERE doclibCatalog.catalogCode=?";
		hql+="order by createdt desc";
		List list = this.find(hql, new Object[] { catalogCode });
		if(n!=0){
			List rList = new ArrayList();
			int size=0;
			if(list.size()>=n){
				size=n-1;
			}else{
				size = list.size();
			}
			for(int i = 0;i<size;i++){
				rList.add(list.get(i));
			}
			return rList;
		}
		return list;
	}
	public List<Doclib> findDoclibListByCatalogId(String catalogid){
		List<Doclib> list=new ArrayList<Doclib>();
		String hql="from Doclib where doclibCatalog.id=?";
		list=this.find(hql, new Object[]{catalogid});
		return list;
	}

}
