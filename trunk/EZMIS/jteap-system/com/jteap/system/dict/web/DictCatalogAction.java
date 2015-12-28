package com.jteap.system.dict.web;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.system.dict.manager.DictCatalogManager;
import com.jteap.system.dict.manager.DictManager;
import com.jteap.system.dict.model.DictCatalog;

/**
 * 数据字典类型的Action处理类
 *描述：
 *时间：2010-4-2
 *作者：谭畅
 *
 */
@SuppressWarnings({ "unchecked", "serial" })
public class DictCatalogAction extends AbstractTreeAction<DictCatalog> {

	//字典类型管理器
	private DictCatalogManager dictCatalogManager;

	//数据字典管理器
	private DictManager dictManager;

	/**
	 * 列表之前的预处理
	 */
	@Override
	public void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		String parentId=request.getParameter("parentId");
		if(StringUtils.isNotEmpty(parentId)){
			HqlUtil.addCondition(hql, "dict.id", parentId);
		}
	}

	@Override
	protected void beforeSaveUpdate(HttpServletRequest request,
			HttpServletResponse response, Object obj, Object originalObject) {
		// TODO Auto-generated method stub
		String parentId = request.getParameter("parentId");
		DictCatalog dc = (DictCatalog) obj;
		if(StringUtil.isNotEmpty(parentId)){
			DictCatalog pdc = dictCatalogManager.get(parentId);
			if(pdc!=null)
				dc.setParentDictCatalog(pdc);
		}
	}

	/**
	 * catalog唯一性验证
	 * @param request uniqueName 数据字典唯一性字段
	 * @return
	 * @throws Exception
	 */
	public String validateCatalogUniqueAction() throws Exception{
		String uniqueName=request.getParameter("uniqueName");
		DictCatalog dc=dictCatalogManager.findUniqueBy("uniqueName", uniqueName);
		if(dc!=null){
			outputJson("{unique:false}");	
		}else{
			outputJson("{unique:true}");
		}
		return NONE;
	}

	public DictCatalogManager getDictCatalogManager() {
		return dictCatalogManager;
	}

	public void setDictCatalogManager(DictCatalogManager DictCatalogManager) {
		this.dictCatalogManager = DictCatalogManager;
	}
	
	public DictManager getDictManager() {
		return dictManager;
	}

	public void setdictManager(DictManager dictManager) {
		this.dictManager = dictManager;
	}
	
	//列表时使用的JSON字符串
	@Override
	public String[] listJsonProperties() {
		return new String[] { "catalogName", "id", "childDictCatalog","uniqueName" };
	}

	//更新时使用的JSON字符串
	@Override
	public String[] updateJsonProperties() {
		return new String[] { "catalogName", "parentDictCatalog", "remark","id","uniqueName" };
	}

	//获取目标类型的子类型集合
	@Override
	protected Collection getChildren(Object bean) {
		DictCatalog catalog=(DictCatalog) bean;
		return catalog.getChildDictCatalog();
	}

	@Override
	protected String getParentPropertyName(Class beanClass) {

		return "parentDictCatalog";
	}

	@Override
	protected Collection getRootObjects() {

		return dictCatalogManager.findRootDictCatalog();
	}

	@Override
	protected String getSortNoPropertyName(Class beanClass) {

		return "sortNo";
	}

	@Override
	protected String getTextPropertyName(Class beanClass) {

		return "catalogName";
	}

	@Override
	public HibernateEntityDao getManager() {

		return dictCatalogManager;
	}
}
