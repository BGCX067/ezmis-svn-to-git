package com.jteap.wz.xqjhsq.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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
import com.jteap.system.resource.model.Resource;
import com.jteap.system.role.model.R2R;
import com.jteap.system.role.model.Role;
import com.jteap.wz.xqjhsq.manager.XqjhsqManager;

/**
 * 物资系统继承GroupAction 实现树的过滤
 * 
 * @author caofei
 * 
 */
@SuppressWarnings( { "unused", "unchecked", "serial" })
public class GroupExtendForWzAction extends GroupAction {

	private GroupManager groupManager;
	private DictManager dictManager;
	private XqjhsqManager xqjhsqManager;

	/** 物资相关申请部门数据字典 * */
	private String WZ_SQ_BM = "WZ_SQ_BM";

	/** 物资相关申请部门数据字典(过滤班组) * */
	private String WZ_SQBM = "WZ_SQBM";

	protected Collection getSqbmRootObjects() {
		// 根据父亲节点查询
		String parentId = request.getParameter("parentId");
		if (parentId == null) {
			parentId = request.getParameter("node");
		}
		if (StringUtils.isNotEmpty(parentId) && !parentId.equals("rootNode")) {
			return groupManager.findGroupByParentId(parentId);
		}
		// 获得两票关联部门
		List<Dict> lstDicts = (List<Dict>) dictManager
				.findDictByUniqueCatalogName(WZ_SQ_BM);
		StringBuffer sbGlbm = new StringBuffer();
		for (Dict dict : lstDicts) {
			String key = dict.getKey();
			sbGlbm.append(" '");
			sbGlbm.append(key);
			sbGlbm.append("'");
			sbGlbm.append(",");
		}
		String strGlbm = sbGlbm.substring(0, sbGlbm.lastIndexOf(","));
		String hql = "from Group as g where g.groupName in (" + strGlbm
				+ ") order by g.sortNo";
		return groupManager.find(hql);
	}

	/**
	 * 取得特定節點的子節點結合
	 */
	@Override
	protected Collection getChildren(Object bean) {
		Group group = (Group) bean;
		return xqjhsqManager.findResourcesByGroup(group);
	}

	/**
	 * 显示树形结构的动作
	 * 
	 * @return
	 * @throws Exception
	 */
	public String showWzSqbmTreeAction() throws Exception {
		JsonConfig jsonConfig = getTreeJsonConfig();// 配置对象、循环策略、过滤字段、bean处理器
		TreeActionJsonBeanProcessor jsonBeanProcessor = new TreeActionJsonBeanProcessor();
		jsonConfig.registerJsonBeanProcessor(Group.class, jsonBeanProcessor);
		jsonBeanProcessor
				.setTreeActionJsonBeanHandler(new TreeActionJsonBeanHandler() {
					public void beanHandler(Object obj, Map map) {
						Group group = (Group) obj;
						map.put("qtip", group.getGroupName());
						map.put("leaf",
								group.getChildGroups().size() > 0 ? false
										: true);
						// 只展开第一层
						if (group.getParentGroup() == null)
							map.put("expanded", true);
						else
							map.put("expanded", false);
					}
				});

		// 开始json化
		Collection roots = this.getSqbmRootObjects();
//		Iterator<Group> it = roots.iterator();
		// for (Object object : roots) {
		// Group group = (Group)object;
		// if(group.getGroupName().equals("燃运部")){
		// List<Group> list = new ArrayList<Group>();
		// list = group.getChildGroups();
		// for (int i = 0; i < list.size(); i++) {
		// Group childGroup = (Group)list.get(i);
		// if(childGroup.getGroupName().equals("燃管煤化班")){
		// list.remove(childGroup);
		// continue;
		// }else if(childGroup.getGroupName().equals("输煤运行")){
		// list.remove(childGroup);
		// continue;
		// }
		// }
		// }
		// if(group.getGroupName().equals("发电部")){
		// List<Group> list = new ArrayList<Group>();
		// list = group.getChildGroups();
		// for (int i = 0; i < list.size(); i++) {
		// Group childGroup = (Group)list.get(i);
		// if(childGroup.getGroupName().equals("一值")){
		// list.remove(childGroup);
		// continue;
		// }else if(childGroup.getGroupName().equals("二值")){
		// list.remove(childGroup);
		// continue;
		// }else if(childGroup.getGroupName().equals("三值")){
		// list.remove(childGroup);
		// continue;
		// }else if(childGroup.getGroupName().equals("四值")){
		// list.remove(childGroup);
		// continue;
		// }else if(childGroup.getGroupName().equals("五值")){
		// list.remove(childGroup);
		// continue;
		// }
		// }
		// }
		// }
		JSONArray result = JSONArray.fromObject(roots, jsonConfig);
		// 输出
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

	public XqjhsqManager getXqjhsqManager() {
		return xqjhsqManager;
	}

	public void setXqjhsqManager(XqjhsqManager xqjhsqManager) {
		this.xqjhsqManager = xqjhsqManager;
	}

}
