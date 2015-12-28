package com.jteap.form.eform.web;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.form.dbdef.model.DefTableInfo;
import com.jteap.form.eform.manager.EFormCatalogManager;
import com.jteap.form.eform.manager.EFormManager;
import com.jteap.form.eform.model.EForm;
import com.jteap.form.eform.model.EFormCatalog;

/**
 * 自定义表单分类Action处理类
 * @author tanchang
 *
 */
@SuppressWarnings({ "unchecked", "serial" })
public class EFormCatalogAction extends AbstractTreeAction<EFormCatalog>{

	private EFormCatalogManager eformCatalogManager;
	private EFormManager eformManager;



	public EFormCatalogManager getEformCatalogManager() {
		return eformCatalogManager;
	}


	public void setEformCatalogManager(EFormCatalogManager eformCatalogManager) {
		this.eformCatalogManager = eformCatalogManager;
	}


	public EFormManager getEformManager() {
		return eformManager;
	}


	public void setEformManager(EFormManager eformManager) {
		this.eformManager = eformManager;
	}


	@Override
	protected Collection getChildren(Object bean) {
		EFormCatalog catalog=(EFormCatalog) bean;
		return catalog.getChildren();
	}

	@Override
	protected String getParentPropertyName(Class beanClass) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected Collection getRootObjects() throws Exception {
		String parentId=request.getParameter("parentId");
		return eformCatalogManager.findCatalogByParentId(parentId);
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
		return eformCatalogManager;
	}


	@Override
	public String[] listJsonProperties() {
		return new String[]{"catalogName","id"};
	}


	@Override
	public String[] updateJsonProperties() {
		// TODO Auto-generated method stub
		return null;
	}


	
	/**
	 * 新建分类节点动作
	 * @return
	 * @throws Exception 
	 */
	public String saveOrUpdateCatalogAction() throws Exception{
		String catalogName=request.getParameter("nodeName");
		String parentId=request.getParameter("parentId");
		String id=request.getParameter("id");

		try{
			EFormCatalog catalog=null;
			
			if(StringUtils.isNotEmpty(id)){
				catalog=eformCatalogManager.get(id);
			}else{
				catalog=(EFormCatalog) this.creatBlankObject();
				if(StringUtils.isNotEmpty(parentId)){
					EFormCatalog parentCatalog=eformCatalogManager.get(parentId);
					catalog.setParent(parentCatalog);
				}
			}
			catalog.setCatalogName(catalogName);
			eformCatalogManager.save(catalog);
			outputJson("{success:true,id:'"+catalog.getId()+"'}");
		}catch(Exception ex){
			outputJson("{success:false,msg:'"+ex.getMessage()+"'}");
		}
		return NONE;
	}
	
	/**
	 * 
	 * 描述 : 显示所有目录和目录Item的checkbox树
	 * 作者 : wangyun
	 * 时间 : Jun 28, 2010
	 */
	public String showCatalogAndItemTreeForCheckAction() throws Exception {
		String result = getCatalogAndItemTreeForCheckJson(null) ;
		this.outputJson(result);
		return NONE ;
	}
	
	/**
	 * 
	 * 描述 : 根据自定表单的ID获得所有的
	 * 作者 : wangyun
	 * 时间 : Sep 7, 2010
	 * 异常 :Exception
	 * 
	 */
	public String showCatalogAndItemTreeForCheckByFormIdAction() throws Exception {
		String formId = request.getParameter("formId");
		EForm eform = eformManager.get(formId);
		EFormCatalog catalog = eform.getCatalog();
		DefTableInfo defTable = eform.getDefTable();
		String result = getCatalogAndItemTreeForCheckJsonByTableId(defTable.getId(), catalog.getId()) ;
		this.outputJson(result);
		return NONE;
	}
	
	/**
	 * 
	 * 描述 : 显示所有目录和目录Item的checkbox树
	 * 作者 : wangyun
	 * 时间 : Jun 28, 2010
	 * 参数 : 
	 * 		parentId : 父节点ID
	 */
	public String getCatalogAndItemTreeForCheckJson(String parentId) {
		StringBuffer json = new StringBuffer() ;
		StringBuffer hql = new StringBuffer("from com.jteap.form.eform.model.EForm as obj where obj.catalog.id=? and obj.newVer='1' order by creatDt desc");
		Object args[]=null;
		
		/*
		"id":"40288ab82262c35901226338b7b10017",
		"text":"生产管理",
		"ccCheck":true,
		"children":[],
		"sortNo":0,
		"leaf":true,
		"expanded":true,
		"checked":false
		*/
		List items = null ;
		json.append("[") ;
		if(parentId != null && !"".equals(parentId)){
			items = eformManager.find(hql.toString(), new Object[]{parentId}) ;
			for(int i=0 ; i<items.size() ; i++) {
				json.append("{") ;
				EForm eform = (EForm) items.get(i) ; 
				json.append("'id':'") ;
				json.append(eform.getId()) ;
				json.append("',") ;
				json.append("'text':'") ;
				json.append(eform.getTitle()+"["+eform.getVersion()+"]") ;
				json.append("',") ;
				json.append("'expanded':true,") ;
				json.append("'leaf':true,") ;
				json.append("'isItem':true,") ;
				json.append("'cls':'x-tree-node-leaf',") ;
				json.append("'children':[]") ;
				if(i==items.size()-1){
					json.append("}") ;
				} else {
					json.append("},") ;
				}
			}
		}
		
		hql=new StringBuffer("from EFormCatalog as g where ") ;
		if(StringUtils.isEmpty(parentId)){
			hql.append("g.parent is null") ;
			
		}else{
			hql.append("g.parent.id=?");
			args=new String[]{parentId};
		}
		List catalogs = eformCatalogManager.find(hql.toString(), args) ;
		if(catalogs.size()>0) {
			if(items != null) {
				if(!items.isEmpty()) {
					json.append(",");
				}
			}
		}
		for(int i=0 ; i<catalogs.size() ; i++) {
			json.append("{") ;
			EFormCatalog catalog = (EFormCatalog) catalogs.get(i) ; 
			json.append("'id':'") ;
			json.append(catalog.getId()) ;
			json.append("',") ;
			json.append("'text':'") ;
			json.append(catalog.getCatalogName()) ;
			json.append("',") ;
			json.append("'expanded':true,") ;
			json.append("'isItem':false,") ;
			json.append("'cls':'x-tree-node-collapsed',") ;
			json.append("'children':") ;
			json.append(getCatalogAndItemTreeForCheckJson(catalog.getId()));
			if(i==catalogs.size()-1){
				json.append("}") ;
			} else {
				json.append("},") ;
			}
		}
		json.append("]") ;
		return json.toString() ;
	}
	
	/**
	 * 
	 * 描述 : 根据表名和表分类ID获得所有相同表的表单树
	 * 作者 : wangyun
	 * 时间 : Sep 7, 2010
	 * 参数 : 
	 * 		tableId ： 表ID
	 * 		parentId ： 表分类ID
	 * 异常 :
	 */
	public String getCatalogAndItemTreeForCheckJsonByTableId(String tableId, String parentId) {
		StringBuffer json = new StringBuffer() ;
		StringBuffer hql = new StringBuffer("from com.jteap.form.eform.model.EForm as obj where obj.defTable.id=? and obj.newVer='1' order by creatDt desc");
		Object args[]=null;
		
		/*
		"id":"40288ab82262c35901226338b7b10017",
		"text":"生产管理",
		"ccCheck":true,
		"children":[],
		"sortNo":0,
		"leaf":true,
		"expanded":true,
		"checked":false
		*/
		List items = null ;
		json.append("[") ;
		if(tableId != null && !"".equals(tableId)){
			items = eformManager.find(hql.toString(), new Object[]{tableId}) ;
			for(int i=0 ; i<items.size() ; i++) {
				json.append("{") ;
				EForm eform = (EForm) items.get(i) ; 
				json.append("'id':'") ;
				json.append(eform.getId()) ;
				json.append("',") ;
				json.append("'text':'") ;
				json.append(eform.getTitle()+"["+eform.getVersion()+"]") ;
				json.append("',") ;
				json.append("'expanded':true,") ;
				json.append("'leaf':true,") ;
				json.append("'isItem':true,") ;
				json.append("'cls':'x-tree-node-leaf',") ;
				json.append("'children':[]") ;
				if(i==items.size()-1){
					json.append("}") ;
				} else {
					json.append("},") ;
				}
			}
		}
		
		hql=new StringBuffer("from EFormCatalog as g where ") ;
		if(StringUtils.isEmpty(parentId)){
			hql.append("g.parent is null") ;
			
		}else{
			hql.append("g.parent.id=?");
			args=new String[]{parentId};
		}
		List catalogs = eformCatalogManager.find(hql.toString(), args) ;
		if(catalogs.size()>0) {
			if(items != null) {
				if(!items.isEmpty()) {
					json.append(",");
				}
			}
		}
		for(int i=0 ; i<catalogs.size() ; i++) {
			json.append("{") ;
			EFormCatalog catalog = (EFormCatalog) catalogs.get(i) ; 
			json.append("'id':'") ;
			json.append(catalog.getId()) ;
			json.append("',") ;
			json.append("'text':'") ;
			json.append(catalog.getCatalogName()) ;
			json.append("',") ;
			json.append("'expanded':true,") ;
			json.append("'isItem':false,") ;
			json.append("'cls':'x-tree-node-collapsed',") ;
			json.append("'children':") ;
			json.append(getCatalogAndItemTreeForCheckJson(catalog.getId()));
			if(i==catalogs.size()-1){
				json.append("}") ;
			} else {
				json.append("},") ;
			}
		}
		json.append("]") ;
		return json.toString() ;
	}
}
