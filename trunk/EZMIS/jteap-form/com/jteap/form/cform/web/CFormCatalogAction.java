package com.jteap.form.cform.web;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.form.cform.manager.CFormCatalogManager;
import com.jteap.form.cform.manager.CFormManager;
import com.jteap.form.cform.model.CForm;
import com.jteap.form.cform.model.CFormCatalog;

/**
 * 数据字典的Action处理类
 * @author tanchang
 *
 */
@SuppressWarnings({ "unchecked", "serial" })
public class CFormCatalogAction extends AbstractTreeAction<CFormCatalog>{

	private CFormCatalogManager cformCatalogManager;
	private CFormManager cformManager;
	
	

	public CFormManager getCformManager() {
		return cformManager;
	}


	public void setCformManager(CFormManager cformManager) {
		this.cformManager = cformManager;
	}


	public CFormCatalogManager getCformCatalogManager() {
		return cformCatalogManager;
	}


	public void setCformCatalogManager(CFormCatalogManager cformCatalogManager) {
		this.cformCatalogManager = cformCatalogManager;
	}


	@Override
	protected Collection getChildren(Object bean) {
		CFormCatalog catalog=(CFormCatalog) bean;
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
		return cformCatalogManager.findCatalogByParentId(parentId);
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
		return cformCatalogManager;
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


	@Override
	protected String beforeDeleteNode(Object node) throws Exception {
		CFormCatalog catalog=(CFormCatalog) node;
		if(catalog.getChildren().size()>0){
			return "不能删除拥有子分类的分类，请先删除其子分类";
		}
		
		long count=cformManager.countCFormByCatalogId(catalog.getId());
		if(count>0){
			return "不能删除拥有表单的分类，请先删除该分类下的所有表单";
		}
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
			CFormCatalog catalog=null;
			
			if(StringUtils.isNotEmpty(id)){
				catalog=cformCatalogManager.get(id);
			}else{
				catalog=(CFormCatalog) this.creatBlankObject();
				if(StringUtils.isNotEmpty(parentId)){
					CFormCatalog parentCatalog=cformCatalogManager.get(parentId);
					catalog.setParent(parentCatalog);
				}
			}
			catalog.setCatalogName(catalogName);
			cformCatalogManager.save(catalog);
			outputJson("{success:true,id:'"+catalog.getId()+"'}");
		}catch(Exception ex){
			outputJson("{success:false,msg:'"+ex.getMessage()+"'}");
		}
		return NONE;
	}
	
	/**
	 * 显示所有目录和目录Item的checkbox树
	 * @return
	 * @throws Exception
	 */
	public String showCatalogAndItemTreeForCheckAction() throws Exception{
		String result = getCatalogAndItemTreeForCheckJson(null) ;
		this.outputJson(result);
		return NONE ;
	}
	
	public String getCatalogAndItemTreeForCheckJson(String parentId) {
		StringBuffer json = new StringBuffer() ;
		StringBuffer hql = new StringBuffer("from com.jteap.form.cform.model.CForm as obj where obj.catalogId=? and obj.newVer='1' order by creatDt desc");
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
			items = cformManager.find(hql.toString(), new Object[]{parentId}) ;
			for(int i=0 ; i<items.size() ; i++) {
				json.append("{") ;
				CForm cform = (CForm) items.get(i) ; 
				json.append("'id':'") ;
				json.append(cform.getId()) ;
				json.append("',") ;
				json.append("'text':'") ;
				json.append(cform.getTitle()+"["+cform.getVersion()+"]") ;
				json.append("',") ;
				json.append("'eformUrl':'") ;
				json.append(cform.getEformUrl()==null?"":cform.getEformUrl()) ;
				json.append("',") ;
				//json.append("'ccCheck':false,") ;
				json.append("'expanded':true,") ;
				json.append("'leaf':true,") ;
				//json.append("'checked':false,") ;
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
		
		hql=new StringBuffer("from CFormCatalog as g where ") ;
		if(StringUtils.isEmpty(parentId)){
			hql.append("g.parent is null") ;
			
		}else{
			hql.append("g.parent.id=?");
			args=new String[]{parentId};
		}
		List catalogs = cformCatalogManager.find(hql.toString(), args) ;
		if(catalogs.size()>0) {
			if(items != null) {
				if(!items.isEmpty()) {
					json.append(",");
				}
			}
		}
		for(int i=0 ; i<catalogs.size() ; i++) {
			json.append("{") ;
			CFormCatalog catalog = (CFormCatalog) catalogs.get(i) ; 
			json.append("'id':'") ;
			json.append(catalog.getId()) ;
			json.append("',") ;
			json.append("'text':'") ;
			json.append(catalog.getCatalogName()) ;
			json.append("',") ;
			//json.append("'ccCheck':false,") ;
			json.append("'expanded':true,") ;
			//json.append("'leaf':true,") ;
			//json.append("'checked':false,") ;
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
