package com.jteap.jx.qxgl.web;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.web.AbstractTreeAction.TreeActionJsonBeanHandler;
import com.jteap.core.web.AbstractTreeAction.TreeActionJsonBeanProcessor;
import com.jteap.system.dict.manager.DictManager;
import com.jteap.system.dict.model.Dict;
import com.jteap.system.group.manager.GroupManager;
import com.jteap.system.group.model.Group;
import com.jteap.system.group.web.GroupAction;

/**
 * 检修系统继承GroupAction 实现树的过滤
 * 
 * @author wangyun
 *
 */
@SuppressWarnings({"unused", "unchecked","serial"})
public class GroupExtendForJxAction extends GroupAction {

	private GroupManager groupManager;
	private DictManager dictManager;
	
	/** 检修设备缺陷单关联部门数据字典 **/
	private String JX_GLBM_SB = "JX_GLBM_SB";
	/** 检修燃运缺陷单关联部门数据字典 **/
	private String JX_GLBM_RY = "JX_GLBM_RY";

	protected Collection getSbRootObjects() {
		//根据父亲节点查询
		String parentId=request.getParameter("parentId");
		if(parentId==null){
			parentId = request.getParameter("node");
		}
		if(StringUtils.isNotEmpty(parentId) && !parentId.equals("rootNode")){
			return groupManager.findGroupByParentId(parentId);
		}
		// 获得两票关联部门
		List<Dict> lstDicts = (List<Dict>) dictManager.findDictByUniqueCatalogName(JX_GLBM_SB);
		StringBuffer sbGlbm = new StringBuffer();
		for (Dict dict : lstDicts) {
			String key = dict.getKey();
			sbGlbm.append(" '");
			sbGlbm.append(key);
			sbGlbm.append("'");
			sbGlbm.append(",");
		}
		String strGlbm = sbGlbm.substring(0, sbGlbm.lastIndexOf(","));
		String hql = "from Group as g where g.groupName in ("+strGlbm+") order by g.sortNo";
		return groupManager.find(hql);
	}
	
	protected Collection getRyRootObjects() {
		//根据父亲节点查询
		String parentId=request.getParameter("parentId");
		if(parentId==null){
			parentId = request.getParameter("node");
		}
		if(StringUtils.isNotEmpty(parentId) && !parentId.equals("rootNode")){
			return groupManager.findGroupByParentId(parentId);
		}
		// 获得两票关联部门
		List<Dict> lstDicts = (List<Dict>) dictManager.findDictByUniqueCatalogName(JX_GLBM_RY);
		StringBuffer sbGlbm = new StringBuffer();
		for (Dict dict : lstDicts) {
			String key = dict.getKey();
			sbGlbm.append(" '");
			sbGlbm.append(key);
			sbGlbm.append("'");
			sbGlbm.append(",");
		}
		String strGlbm = sbGlbm.substring(0, sbGlbm.lastIndexOf(","));
		String hql = "from Group as g where g.groupName in ("+strGlbm+") order by g.sortNo";
		return groupManager.find(hql);
	}
	
	/**
	 * 显示树形结构的动作
	 * @return
	 * @throws Exception
	 */
	public String showSbQxdTreeAction() throws Exception{
		JsonConfig jsonConfig=getTreeJsonConfig();//配置对象、循环策略、过滤字段、bean处理器
		TreeActionJsonBeanProcessor jsonBeanProcessor=new TreeActionJsonBeanProcessor();
		jsonConfig.registerJsonBeanProcessor(Group.class,jsonBeanProcessor);
		jsonBeanProcessor.setTreeActionJsonBeanHandler(new TreeActionJsonBeanHandler(){
			public void beanHandler(Object obj, Map map) {
				Group group=(Group) obj;
				map.put("qtip", group.getGroupName());
				map.put("leaf", group.getChildGroups().size()>0?false:true);
				//只展开第一层
				if(group.getParentGroup()==null)
					map.put("expanded", true);
				else
					map.put("expanded", false);
			}
		});
		
		//开始json化
		Collection roots=this.getSbRootObjects();
		JSONArray result=JSONArray.fromObject(roots,jsonConfig);
		//输出
		this.outputJson(result.toString());
		return NONE;	
	}

	/**
	 * 显示树形结构的动作
	 * @return
	 * @throws Exception
	 */
	public String showRyQxdTreeAction() throws Exception{
		JsonConfig jsonConfig=getTreeJsonConfig();//配置对象、循环策略、过滤字段、bean处理器
		TreeActionJsonBeanProcessor jsonBeanProcessor=new TreeActionJsonBeanProcessor();
		jsonConfig.registerJsonBeanProcessor(Group.class,jsonBeanProcessor);
		jsonBeanProcessor.setTreeActionJsonBeanHandler(new TreeActionJsonBeanHandler(){
			public void beanHandler(Object obj, Map map) {
				Group group=(Group) obj;
				map.put("qtip", group.getGroupName());
				map.put("leaf", group.getChildGroups().size()>0?false:true);
				//只展开第一层
				if(group.getParentGroup()==null)
					map.put("expanded", true);
				else
					map.put("expanded", false);
			}
		});
		
		//开始json化
		Collection roots=this.getRyRootObjects();
		JSONArray result=JSONArray.fromObject(roots,jsonConfig);
		//输出
		this.outputJson(result.toString());
		return NONE;	
	}

	public GroupManager getGroupManager() {
		return groupManager;
	}

	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	public DictManager getDictManager() {
		return dictManager;
	}

	public void setDictManager(DictManager dictManager) {
		this.dictManager = dictManager;
	}
	
}
