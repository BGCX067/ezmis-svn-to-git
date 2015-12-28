package com.jteap.system.doclib.manager;

import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HZ2PY;
import com.jteap.system.doclib.model.DoclibCatalog;
import com.jteap.system.resource.model.Module;

/**
 * 文档中心分类管理对象
 * 
 * @author caofei
 * 
 */
@SuppressWarnings("unchecked")
public class DoclibCatalogManager extends HibernateEntityDao<DoclibCatalog> {

	/**
	 * 根据父亲节点编号查询该节点下的所有子节点
	 * 
	 * @param parentId
	 * @return 如果parentId为空 返回所有顶层节点 否则返回所有指定parentId下的所有子节点
	 */

	public Collection<DoclibCatalog> findCatalogByParentId(String parentId) {

		StringBuffer hql = new StringBuffer("from DoclibCatalog as g where ");
		Object args[] = null;
		if (StringUtils.isEmpty(parentId)) {
			hql.append("g.parent is null order by sortno");

		} else {
			hql.append("g.parent.id=? order by sortno");
			args = new String[] { parentId };
		}
		return find(hql.toString(), args);
	}
	/**
	 * 查询当前文档分类下的所有子分类的id
	 * 
	 */
	public StringBuffer getChlids(StringBuffer childids,StringBuffer parids,String docid){
		//判断当前对象是否是上级对象
		if(parids.toString().indexOf(docid)!=-1){
		List<DoclibCatalog> list =this.find("from DoclibCatalog where parent.id='"+docid+"'");
			for(DoclibCatalog doc:list){
				childids.append("'"+doc.getId()+"',");
				getChlids(childids,parids,doc.getId());
			}
		}
		return childids;
	}
	/**
	 * 根据分类编号取得该分类存放表单文件的目录名称
	 * 
	 * @param catalogId
	 * @return
	 */
	public String getCatalogDirectoryName(String catalogId) {
		String hql = "select title from DoclibCatalog where id=?";
		List list = this.find(hql, new Object[] { catalogId });
		String catalogTitle = list.get(0).toString();
		return HZ2PY.getPy1(catalogTitle, false).toUpperCase();
	}
	
	public Collection<DoclibCatalog> findCatalogByCode(String catalogCode){
		StringBuffer hql = new StringBuffer("from DoclibCatalog as g where ");
		Object args[] = null;
		hql.append("g.catalogCode =?");
		args = new String[] { catalogCode };
		return find(hql.toString(), args);
	}
	
	
	public Module findModuleByCatalogCode(String catalogCode){
		Module module=new Module();
		String prx="system/doclib/index.jsp?catalogCode="+catalogCode;
		String hql="from Module where link=?";
		module=(Module)this.findUniqueByHql(hql, new Object[]{prx});
		return module;
	}
	
	public Long getSortNo(){
		Long result=new Long(0);
		String hql="select max(sortno) from DoclibCatalog";
		List<Long> list=this.find(hql);
		if(list.size()>0){
			result=list.get(0);
		}
		result++;
		return result;
	}
	
	public DoclibCatalog getDoclibCatalogByCatalogCode(String catalogCode){
		DoclibCatalog dc=(DoclibCatalog)this.findUniqueBy("catalogCode", catalogCode);	
		return dc;
	}
	
}
