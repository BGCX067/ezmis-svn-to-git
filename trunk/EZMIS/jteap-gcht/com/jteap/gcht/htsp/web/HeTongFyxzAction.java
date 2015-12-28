package com.jteap.gcht.htsp.web;

import java.util.Collection;


import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.gcht.htsp.manager.HeTongFyxzManager;
import com.jteap.gcht.htsp.model.HeTongFyxz;
@SuppressWarnings({ "unchecked", "serial" })
public class HeTongFyxzAction extends AbstractTreeAction<HeTongFyxz> {

	private HeTongFyxzManager heTongFyxzManager;
	
	
	public HeTongFyxzManager getHeTongFyxzManager() {
		return heTongFyxzManager;
	}

	public void setHeTongFyxzManager(HeTongFyxzManager heTongFyxzManager) {
		this.heTongFyxzManager = heTongFyxzManager;
	}

	@Override
	protected Collection getChildren(Object bean) {
		HeTongFyxz catalog=(HeTongFyxz) bean;
		return catalog.getChildren();
	}

	@Override
	protected String getParentPropertyName(Class beanClass) {
		return "";
	}

	@Override
	protected Collection getRootObjects() throws Exception {
		String parentId=request.getParameter("parentId");
		String flbm = request.getParameter("flbm");
		if(StringUtil.isNotEmpty(flbm)){
			return heTongFyxzManager.findCatalogByParentId(parentId,flbm);
		}else{
			return heTongFyxzManager.findCatalogByParentId(parentId);
		}
	}
	
	
	@Override
	protected String getSortNoPropertyName(Class beanClass) {
		return "sortNo";
	}

	@Override
	protected String getTextPropertyName(Class beanClass) {
		return "fyxzName";
	}

	@Override
	public HibernateEntityDao getManager() {
		return heTongFyxzManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{
			"id","fyxzName","sortno","flbm"
		};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
				"id","fyxzName","sortno","flbm"
		};
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
		String flbm = request.getParameter("flbm");
		
		try{
			HeTongFyxz catalog=null;
			
			if(StringUtils.isNotEmpty(id)){
				catalog=this.heTongFyxzManager.get(id);
			}else{
				catalog=(HeTongFyxz) this.creatBlankObject();
				if(StringUtils.isNotEmpty(parentId)){
					HeTongFyxz parentCatalog=this.heTongFyxzManager.get(parentId);
					catalog.setParent(parentCatalog);
				}
			}
			catalog.setFyxzName(catalogName);
			catalog.setFlbm(flbm);
			this.heTongFyxzManager.save(catalog);
			outputJson("{success:true,id:'"+catalog.getId()+"'}");
		}catch(Exception ex){
			outputJson("{success:false,msg:'"+ex.getMessage()+"'}");
		}
		return NONE;
	}

}
