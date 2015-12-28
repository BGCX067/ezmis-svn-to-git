package com.jteap.lp.lpzhcx.web;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.lp.lpzhcx.manager.LpzhcxManager;
import com.jteap.system.dict.manager.DictManager;
import com.jteap.system.dict.model.Dict;
import com.jteap.system.group.manager.GroupManager;
import com.jteap.system.group.model.Group;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.Person;
import com.jteap.system.role.manager.RoleManager;
import com.jteap.system.role.model.Role;

/**
 * 两票综合查询Action
 * 
 * @author wangyun
 *
 */
@SuppressWarnings({"serial", "unchecked", "unused"})
public class LpzhcxAction extends AbstractAction {

	private LpzhcxManager lpzhcxManager;
	private DictManager dictManager;
	private RoleManager roleManager;
	private PersonManager personManager;
	private GroupManager groupManager;

	public String showListAction() throws Exception {
		String cxid = request.getParameter("cxid");
		String tableName = request.getParameter("tableName");
		String tableCname = request.getParameter("tableCname");
		String queryParamsSql = request.getParameter("queryParamsSql");
		String sort = request.getParameter("sort");
		String dir = request.getParameter("dir");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "0";

		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);

		// 查询条件
		if (StringUtils.isNotEmpty(queryParamsSql)) {
			queryParamsSql = queryParamsSql.replace("$", "%");
		}

		// 排序条件
		String orderSql = "";
		if (StringUtil.isNotEmpty(sort)) {
			orderSql = " order by " + sort + " " + dir;
		}

		// 获得当前登录人ID
		String curPersonId = sessionAttrs.get(Constants.SESSION_CURRENT_PERSONID).toString();
		String strAddCondition = "";
		String strSql = "";
		try {
			if (StringUtil.isEmpty(tableName)) {
				List<Dict> lstDictAll = (List<Dict>) dictManager.findDictByUniqueCatalogName("bmlp");
				if ("qc".equals(cxid)) {
					if (StringUtil.isNotEmpty(queryParamsSql)) {
						strAddCondition += queryParamsSql;
					}
				} else {
					// 值长查询
					if ("zzcx".equals(cxid)) {
						// 获得值长人员ID拼接字符串
						String strPersons = getZzPersonUserNames();
						strAddCondition = " a.FZR1 in (" + strPersons + ") ";
						// 个人查询
					} else if ("grcx".equals(cxid)) {
						strAddCondition = " a.FZR1 = '" + curPersonId + "'";
						// 部门查询
					} else {
						// 获得查询部门下子班组的ID拼接字符串
						String strGroups = getGroupIds(cxid);
						strAddCondition = " a.BZID in (" + strGroups + ") ";
					}
					if (StringUtil.isNotEmpty(queryParamsSql)) {
						strAddCondition += " and " + queryParamsSql;
					}
				}
				strSql = joinSql(lstDictAll, strAddCondition, orderSql);
			} else {
				if ("qc".equals(cxid)) {
					if (StringUtil.isNotEmpty(queryParamsSql)) {
						strAddCondition += queryParamsSql;
					}
				} else {
					// 值长查询
					if ("zzcx".equals(cxid)) {
						// 获得值长人员ID拼接字符串
						String strPersons = getZzPersonIds();
						strAddCondition = " a.FZR1 in (" + strPersons + ") ";
						// 个人查询
					} else if ("grcx".equals(cxid)) {
						strAddCondition = " a.FZR1 = '" + curPersonId + "'";
						// 部门查询
					} else {
						// 获得查询部门下子班组的ID拼接字符串
						String strGroups = getGroupIds(cxid);
						strAddCondition = " a.BZID in (" + strGroups + ") ";
					}
					if (StringUtil.isNotEmpty(queryParamsSql)) {
						strAddCondition += " and " + queryParamsSql;
					}
				}
				strSql = joinSql(tableName, tableCname, strAddCondition, orderSql);
			}
			strSql = "select * from (" + strSql + ")";
			if (StringUtil.isNotEmpty(orderSql)) {
				strSql += orderSql;
			}
			Page page = lpzhcxManager.pagedQueryTableData(strSql, iStart, iLimit);
			String json = JSONUtil.listToJson((List) page.getResult(), 
					new String[] { "ID", "PH", "BZMC", "FZRMC", "STATUS", "JHKSSJ", "JHJSSJ", "PMC"});
			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json + "}";
			this.outputJson(json);
		} catch (Exception e) {
			this.outputJson("{success:false}");
			e.printStackTrace();
		}
		
		return NONE;
	}

	/**
	 * 
	 * 描述 : 获得查询部门下子班组的ID拼接字符串
	 * 作者 : wangyun
	 * 时间 : 2010-10-28
	 * 参数 : 
	 * 		cxid : 部门唯一标识
	 * 返回值 : 
	 * 		strGroups ： 部门下子班组的ID拼接字符串
	 * 
	 */
	private String getGroupIds(String cxid) {
		Group group = groupManager.findUniqueBy("groupSn", cxid);
		
		List<Group> lstGroup = (List<Group>) groupManager.findGroupByParentId(group.getId().toString());
		
		String strGroups = "";
		for (Group groupx : lstGroup) {
			strGroups += "'" + groupx.getId() + "', ";
		}
		strGroups = strGroups.substring(0, strGroups.lastIndexOf(","));
		return strGroups;
	}

	/**
	 * 
	 * 描述 : 获得值长人员ID拼接字符串
	 * 作者 : wangyun
	 * 时间 : 2010-10-28
	 * 参数 : 
	 * 		无
	 * 返回值 : 
	 * 		strPersons ： 值长ID拼接字符串
	 * 
	 */
	private String getZzPersonIds() {
		Role role = roleManager.findUniqueBy("roleSn", "ZZ");
		
		List<Person> lstPerson = (List<Person>) personManager.findPersonByRoleIds((String)role.getId());
		
		String strPersons = "";
		for (Person person : lstPerson) {
			strPersons += "'" + person.getId() + "',";
		}
		
		strPersons = strPersons.substring(0, strPersons.lastIndexOf(","));
		return strPersons;
	}
	/**
	 * 
	 * 描述 : 获得值长人员姓名拼接字符串
	 * 作者 : wangyun
	 * 时间 : 2010-10-28
	 * 参数 : 
	 * 		无
	 * 返回值 : 
	 * 		strPersons ： 值长姓名拼接字符串
	 * 
	 */
	private String getZzPersonUserNames() {
		Role role = roleManager.findUniqueBy("roleSn", "ZZ");
		
		List<Person> lstPerson = (List<Person>) personManager.findPersonByRoleIds((String)role.getId());
		
		String strPersons = "";
		for (Person person : lstPerson) {
			strPersons += "'" + person.getUserName() + "',";
		}
		
		strPersons = strPersons.substring(0, strPersons.lastIndexOf(","));
		return strPersons;
	}
	/**
	 * 
	 * 描述 : 拼接所有两票表的查询sql
	 * 作者 : wangyun
	 * 时间 : 2010-10-28
	 * 参数 : 
	 * 		lstDictAll ： 所有两票表
	 * 		strAddCondition ： 查询条件
	 * 		orderSql ： 排序条件
	 * 返回值 : 
	 * 		sql ： 拼接好的sql
	 * 
	 */
	private String joinSql(List<Dict> lstDictAll, String strAddCondition, String orderSql) {
		String sql = "";
		for (Dict dict : lstDictAll) {
			String tableCName = dict.getKey();
			String tableName = dict.getValue();
			sql += "select a.ID, a.PH, a.BZMC, a.FZRMC, a.STATUS, to_char(a.JHKSSJ, 'yyyy-MM-dd HH24:mi') as JHKSSJ, "
				+ "to_char(a.JHJSSJ,'yyyy-MM-dd HH24:mi') as JHJSSJ, '"+ tableCName+"' as PMC "
				+ "from " + tableName + " a ";
			if (StringUtil.isNotEmpty(strAddCondition)) {
				sql += " where " + strAddCondition;
			}
			sql += " union all ";
		}
		sql = sql.substring(0, sql.lastIndexOf("union all "));
		return sql;
	}
	
	/**
	 * 
	 * 描述 : 拼接指定两票表的查询sql
	 * 作者 : wangyun
	 * 时间 : 2010-10-28
	 * 参数 : 
	 * 		tableName ： 指定表名
	 * 		tableCname ： 指定表中文名
	 * 		strAddCondition ： 查询条件
	 * 		orderSql ： 排序条件
	 * 返回值 : 
	 * 		sql ： 拼接好的sql
	 * 
	 */
	private String joinSql(String tableName, String tableCname, String strAddCondition, String orderSql) {
		 String sql = "select a.ID, a.PH, a.BZMC, a.FZRMC, a.STATUS, to_char(a.JHKSSJ, 'yyyy-MM-dd HH24:mi') as JHKSSJ, "
				+ "to_char(a.JHJSSJ,'yyyy-MM-dd HH24:mi') as JHJSSJ, '"+ tableCname+"' as PMC "
				+ "from " + tableName + " a ";
			 if (StringUtil.isNotEmpty(strAddCondition)) {
				 sql += " where " + strAddCondition;
			 }
			 return sql;
	}

	@Override
	public HibernateEntityDao getManager() {
		return null;
	}

	@Override
	public String[] listJsonProperties() {
		return null;
	}

	@Override
	public String[] updateJsonProperties() {
		return null;
	}

	public LpzhcxManager getLpzhcxManager() {
		return lpzhcxManager;
	}

	public void setLpzhcxManager(LpzhcxManager lpzhcxManager) {
		this.lpzhcxManager = lpzhcxManager;
	}


	public DictManager getDictManager() {
		return dictManager;
	}

	public void setDictManager(DictManager dictManager) {
		this.dictManager = dictManager;
	}

	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	public PersonManager getPersonManager() {
		return personManager;
	}

	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}

	public GroupManager getGroupManager() {
		return groupManager;
	}

	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

}
