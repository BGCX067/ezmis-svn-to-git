package com.jteap.lp.pkgl.web;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;

import com.jteap.system.dict.manager.DictManager;
import com.jteap.system.dict.model.Dict;
import com.jteap.system.group.manager.GroupManager;
import com.jteap.system.group.model.Group;
import com.jteap.system.group.web.GroupAction;

/**
 * 两票系统继承GroupAction 实现树的过滤
 * 
 * @author wangyun
 *
 */
@SuppressWarnings({"unused", "unchecked","serial"})
public class GroupExtendForLpAction extends GroupAction {

	private GroupManager groupManager;
	private DictManager dictManager;
	
	/** 两票关联部门数据字典 **/
	private String LP_GLBM_DIC = "LP_GLBM";

	/**
	 * 获得全部部门树的根节点
	 */
	@Override
	protected Collection getRootObjects() {
		//根据父亲节点查询
		String parentId=request.getParameter("parentId");
		if(parentId==null){
			parentId = request.getParameter("node");
		}
		if(StringUtils.isNotEmpty(parentId) && !parentId.equals("rootNode")){
			return groupManager.findGroupByParentId(parentId);
		}
		return groupManager.findRootGroups();
	}
	
	/**
	 * 显示树形结构的动作
	 * @throws Exception
	 */
	public String showTreeAction() throws Exception{
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
		Collection roots=this.getRootObjects();
		JSONArray result=JSONArray.fromObject(roots,jsonConfig);
		//输出
		this.outputJson(result.toString());
		return NONE;	
	}

	/**
	 * 获得检修部、燃运部、发电部树形结构的根节点
	 * 
	 */
	protected Collection getGzpRootObjects() {
		//根据父亲节点查询
		String parentId=request.getParameter("parentId");
		if(parentId==null){
			parentId = request.getParameter("node");
		}
		if(StringUtils.isNotEmpty(parentId) && !parentId.equals("rootNode")){
			return groupManager.findGroupByParentId(parentId);
		}
		// 获得两票关联部门
		List<Dict> lstDicts = (List<Dict>) dictManager.findDictByUniqueCatalogName(LP_GLBM_DIC);
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
	 * 显示检修部、燃运部、发电部树形结构的动作
	 * @return
	 * @throws Exception
	 */
	public String showGzpTreeAction() throws Exception {
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
		Collection roots=this.getGzpRootObjects();
		JSONArray result=JSONArray.fromObject(roots,jsonConfig);
		//输出
		this.outputJson(result.toString());
		return NONE;	
	}

	/**
	 * 获得根据部门的标识取得部门树的根节点
	 * 
	 */
	protected Collection getRootBySnObjects() {
		//根据父亲节点查询
		String parentId=request.getParameter("parentId");
		if(parentId==null){
			parentId = request.getParameter("node");
		}
		if(StringUtils.isNotEmpty(parentId) && !parentId.equals("rootNode")){
			return groupManager.findGroupByParentId(parentId);
		} else {
			String groupSn = request.getParameter("groupSn");
			Group group = groupManager.findUniqueBy("groupSn", groupSn);
			if (group != null) {
				String hql = "from Group as g where g.id = '"+group.getId()+"' order by g.sortNo";
				return groupManager.find(hql);
			} else {
				// 获得两票关联部门
				List<Dict> lstDicts = (List<Dict>) dictManager.findDictByUniqueCatalogName(LP_GLBM_DIC);
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
		}
	}

	/**
	 * 根据部门的标识取得部门树
	 * @throws Exception
	 */
	public String showTreeBySnAction() throws Exception {
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
		Collection roots=this.getRootBySnObjects();
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
