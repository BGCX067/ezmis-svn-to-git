package com.jteap.lp.pkgl.web;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jteap.form.dbdef.model.DefTableInfo;
import com.jteap.form.eform.manager.EFormCatalogManager;
import com.jteap.form.eform.manager.EFormManager;
import com.jteap.form.eform.model.EForm;
import com.jteap.form.eform.model.EFormCatalog;
import com.jteap.form.eform.web.EFormCatalogAction;
import com.jteap.wfengine.workflow.manager.FlowConfigManager;
import com.jteap.wfengine.workflow.model.FlowCatalog;
import com.jteap.wfengine.workflow.model.FlowConfig;
import com.jteap.wfengine.workflow.web.FlowCatalogAction;

/**
 * 两票系统继承EFormCatalogAction 实现表单显示过滤
 * 
 * @author wangyun
 *
 */
@SuppressWarnings({"unchecked","unused","serial"})
public class FlowCatalogExtendForLpAction extends FlowCatalogAction {

	private static String LP_FORM_NAME = "两票管理";
	private FlowConfigManager flowConfigManager;
	
	/**
	 * 
	 * 描述 : 获得所有的两票管理子系统表单树
	 * 作者 : wangyun
	 * 时间 : Sep 27, 2010
	 * 异常 :Exception
	 * 
	 */
	public String showCatalogAndItemTreeAction() throws Exception {
		FlowCatalog flowCatalog = super.getFlowCatalogManager().findUniqueBy("catalogName", LP_FORM_NAME);
		
		String result = getCatalogAndItemTreeForCheckJson(flowCatalog.getId()) ;
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
		StringBuffer hql = new StringBuffer("from FlowConfig as obj where obj.flowCatalog.id=? and obj.newVer='1' order by createrTime desc");
		Object args[]=null;
		
		List items = null ;
		json.append("[") ;
		if(parentId != null && !"".equals(parentId)){
			items = flowConfigManager.find(hql.toString(), new Object[]{parentId}) ;
			for(int i=0 ; i<items.size() ; i++) {
				json.append("{") ;
				FlowConfig flowConfig = (FlowConfig) items.get(i) ; 
				json.append("'id':'") ;
				json.append(flowConfig.getNm()) ;
				json.append("',") ;
				json.append("'text':'") ;
				json.append(flowConfig.getName()) ;
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
		
		hql=new StringBuffer("from FlowCatalog as g where ") ;
		if(StringUtils.isEmpty(parentId)){
			hql.append("g.parent is null") ;
			
		}else{
			hql.append("g.parent.id=?");
			args=new String[]{parentId};
		}
		List catalogs = super.getFlowCatalogManager().find(hql.toString(), args) ;
		if(catalogs.size()>0) {
			if(items != null) {
				if(!items.isEmpty()) {
					json.append(",");
				}
			}
		}
		for(int i=0 ; i<catalogs.size() ; i++) {
			json.append("{") ;
			FlowCatalog catalog = (FlowCatalog) catalogs.get(i) ; 
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

	public FlowConfigManager getFlowConfigManager() {
		return flowConfigManager;
	}

	public void setFlowConfigManager(FlowConfigManager flowConfigManager) {
		this.flowConfigManager = flowConfigManager;
	}

}
