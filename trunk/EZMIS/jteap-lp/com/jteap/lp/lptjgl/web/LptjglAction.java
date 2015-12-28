package com.jteap.lp.lptjgl.web;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.lp.lptjgl.manager.LptjglManager;
import com.jteap.system.dict.manager.DictManager;
import com.jteap.system.dict.model.Dict;
import com.jteap.system.group.manager.GroupManager;
import com.jteap.system.group.model.Group;

/**
 * 两票统计管理Action
 * 
 * @author wangyun
 * 
 */
@SuppressWarnings( { "serial", "unchecked" })
public class LptjglAction extends AbstractAction {

	private DictManager dictManager;
	private LptjglManager lptjglManager;
	private GroupManager groupManager;

	/**
	 * 
	 * 描述 : 部门两票合格率统计显示列表
	 * 作者 : wangyun 
	 * 时间 : 2010-10-29 
	 * 异常 : Exception
	 * 
	 */
	public String showBmLpListAction() throws Exception {
		String tableName = request.getParameter("tableName");
		String groupSn = request.getParameter("groupSn");
		String queryParamsSql = request.getParameter("queryParamsSql");
		String sort = request.getParameter("sort");
		String dir = request.getParameter("dir");
		String strIsBm = request.getParameter("isBm");
		Boolean isBm = Boolean.valueOf(strIsBm);

		try {
			// 查询条件
			if (StringUtils.isNotEmpty(queryParamsSql)) {
				queryParamsSql = queryParamsSql.replace("$", "%");
			} else {
				queryParamsSql = " a.jhkssj >=trunc(add_months(last_day(sysdate),-2)+1) "
						+" and a.jhjssj <=trunc(add_months(last_day(sysdate),-1)) ";
			}

			// 排序条件
			String orderSql = "";
			if (StringUtil.isNotEmpty(sort)) {
				orderSql = " order by " + sort + " " + dir;
			}

			String strAddCondition = "";
			String strSql = "";

			if (StringUtil.isEmpty(tableName)) {
				List<Dict> lstDictAll = (List<Dict>) dictManager.findDictByUniqueCatalogName("bmlp");
				if (StringUtil.isNotEmpty(queryParamsSql)) {
					strAddCondition += queryParamsSql;
				}
				strSql = joinBmLpSql(lstDictAll, groupSn, strAddCondition, isBm);
			} else {
				if (StringUtil.isNotEmpty(queryParamsSql)) {
					strAddCondition += queryParamsSql;
				}
				strSql = joinBmLpSql(tableName, groupSn, strAddCondition, isBm);
			}

			strSql = "select * from (" + strSql + ")";
			if (StringUtil.isNotEmpty(orderSql)) {
				strSql += orderSql;
			}
			Page page = lptjglManager.pagedQueryTableData(strSql, 0, 25);
			
			String json = JSONUtil.listToJson((List) page.getResult(), new String[] { "ID", "GROUPNAME", "GROUP_SN", "HG", "BHG",
					"HJ", "HGL" });
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
	 * 描述 : 显示两票不合格清单显示列表
	 * 作者 : wangyun
	 * 时间 : 2010-11-1
	 * 异常 : Exception
	 * 
	 */
	public String showLpbhgqdListAction() throws Exception {
		String tableName = request.getParameter("tableName");
		String tableCName = request.getParameter("tableCName");
		String groupId = request.getParameter("groupId");
		String queryParamsSql = request.getParameter("queryParamsSql");
		String sort = request.getParameter("sort");
		String dir = request.getParameter("dir");

		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "0";

		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);

		try {
			// 查询条件
			if (StringUtils.isNotEmpty(queryParamsSql)) {
				queryParamsSql = queryParamsSql.replace("$", "%");
			} else {
				queryParamsSql = " a.jhkssj >=trunc(add_months(last_day(sysdate),-2)+1) "
						+" and a.jhjssj <=trunc(add_months(last_day(sysdate),-1)) ";
			}

			// 排序条件
			String orderSql = "";
			if (StringUtil.isNotEmpty(sort)) {
				orderSql = " order by " + sort + " " + dir;
			}

			String strAddCondition = "";
			String strSql = "";

			if (StringUtil.isEmpty(tableName)) {
				List<Dict> lstDictAll = (List<Dict>) dictManager.findDictByUniqueCatalogName("LP_PZ2TABLE");
				if (StringUtil.isNotEmpty(queryParamsSql)) {
					strAddCondition += queryParamsSql;
				}
				strSql = joinBhgSql(lstDictAll, groupId, strAddCondition);
			} else {
				if (StringUtil.isNotEmpty(queryParamsSql)) {
					strAddCondition += queryParamsSql;
				}
				strSql = joinBhgSql(tableName, tableCName, groupId, strAddCondition);
			}

			strSql = "select * from (" + strSql + ")";
			if (StringUtil.isNotEmpty(orderSql)) {
				strSql += orderSql;
			}
			Page page = lptjglManager.pagedQueryTableData(strSql, iStart, iLimit);
			String json = JSONUtil.listToJson((List) page.getResult(), new String[] { "ID", "PH", "BZMC", "FZRMC", "STATUS"
					, "JHKSSJ", "JHJSSJ", "PMC"});
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
	 * 描述 : 显示按票种统计列表
	 * 作者 : wangyun
	 * 时间 : 2010-12-28
	 * 参数 : 无
	 * 返回值 : 无
	 * 异常 : Exception
	 *
	 */
	public String showPztjAction() throws Exception {
		String queryParamsSql = request.getParameter("queryParamsSql");
		String sort = request.getParameter("sort");
		String dir = request.getParameter("dir");

		try {
			// 查询条件
			if (StringUtils.isNotEmpty(queryParamsSql)) {
				queryParamsSql = queryParamsSql.replace("$", "%");
			} else {
				queryParamsSql = " jhkssj >=trunc(add_months(last_day(sysdate),-2)+1) "
						+" and jhjssj <=trunc(add_months(last_day(sysdate),-1)) ";
			}

			// 排序条件
			String orderSql = "";
			if (StringUtil.isNotEmpty(sort)) {
				orderSql = " order by " + sort + " " + dir;
			}

			String strAddCondition = "";
			String strSql = "";

			List<Dict> lstDictAll = (List<Dict>) dictManager.findDictByUniqueCatalogName("LP_PZ2TABLE");
			if (StringUtil.isNotEmpty(queryParamsSql)) {
				strAddCondition += queryParamsSql;
			}
			strSql = joinPztjSql(lstDictAll, strAddCondition);

			strSql = "select * from (" + strSql + ")";
			if (StringUtil.isNotEmpty(orderSql)) {
				strSql += orderSql;
			}
			Page page = lptjglManager.pagedQueryTableData(strSql, 0, 20);
			String json = JSONUtil.listToJson((List) page.getResult(), new String[] {"HG", "BHG", "HJ", "PMC", "TABLENAME"});
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
	 * 描述 : 拼接所有两票表的查询sql 
	 * 作者 : wangyun 
	 * 时间 : 2010-10-28 
	 * 参数 : 
	 * 		lstDictAll ： 所有两票表
	 * 		strAddCondition ： 查询条件 
	 * 		orderSql ： 排序条件 
	 * 		isBm : 用来区别部门和班组（true：部门查询；false：班组查询）
	 * 返回值 : 
	 * 		sql ： 拼接好的sql
	 * 
	 */
	private String joinBmLpSql(List<Dict> lstDictAll, String groupSn, String strAddCondition, Boolean isBm) {
		String bmField = isBm?"bzparentid" : "bzid";
		
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select id, groupname, group_sn, hg, bhg, (hg+bhg) as hj, ROUND(hg / (hg + bhg) * 100, 2) as hgl ");
		sbSql.append("from (select g.id, t.groupname, t.group_sn, nvl(g.hg, 0) as hg, nvl(h.bhg, 0) as bhg ");
		sbSql.append("from (select distinct e.id, f.hg from ( ");
		for (Dict dict : lstDictAll) {
			String tableName = dict.getValue();
			sbSql.append("select distinct b.id from ");
			sbSql.append(tableName);
			sbSql.append(" a, tb_sys_group b where a.");
			sbSql.append(bmField);
			sbSql.append("= b.id ");
			if (StringUtil.isNotEmpty(strAddCondition)) {
				sbSql.append(" and ");
				sbSql.append(strAddCondition);
			}
			if (StringUtil.isNotEmpty(groupSn)) {
				Group group = null;
				if (isBm) {
					group = groupManager.findUniqueBy("groupSn", groupSn);
				} else {
					group = groupManager.get(groupSn);
				}
				sbSql.append(" and a.");
				sbSql.append(bmField);
				sbSql.append("='");
				sbSql.append(group.getId());
				sbSql.append("' ");
			}
			sbSql.append("union all ");
		}
		sbSql = new StringBuffer(sbSql.substring(0, sbSql.lastIndexOf("union all ")));
		sbSql.append(") e left join ( select d.id, count(*) as hg from ( ");
		for (Dict dict : lstDictAll) {
			String tableName = dict.getValue();
			sbSql.append("select b.id, a.status, a.bzmc from ");
			sbSql.append(tableName);
			sbSql.append(" a, tb_sys_group b ");
			sbSql.append("where a.");
			sbSql.append(bmField);
			sbSql.append("= b.id and a.status = '合格' ");
			if (StringUtil.isNotEmpty(strAddCondition)) {
				sbSql.append(" and ");
				sbSql.append(strAddCondition);
			}
			if (StringUtil.isNotEmpty(groupSn)) {
				Group group = null;
				if (isBm) {
					group = groupManager.findUniqueBy("groupSn", groupSn);
				} else {
					group = groupManager.get(groupSn);
				}
				sbSql.append(" and a.");
				sbSql.append(bmField);
				sbSql.append("='");
				sbSql.append(group.getId());
				sbSql.append("' ");
			}
			sbSql.append("union all ");
		}
		sbSql = new StringBuffer(sbSql.substring(0, sbSql.lastIndexOf("union all ")));
		sbSql.append(") d group by d.id) f on e.id =  f.id) g, ");
		sbSql.append("(select distinct e.id, f.bhg from ( ");
		for (Dict dict : lstDictAll) {
			String tableName = dict.getValue();
			sbSql.append("select distinct b.id from ");
			sbSql.append(tableName);
			sbSql.append(" a, tb_sys_group b where a.");
			sbSql.append(bmField);
			sbSql.append("= b.id ");
			if (StringUtil.isNotEmpty(strAddCondition)) {
				sbSql.append(" and ");
				sbSql.append(strAddCondition);
			}
			if (StringUtil.isNotEmpty(groupSn)) {
				Group group = null;
				if (isBm) {
					group = groupManager.findUniqueBy("groupSn", groupSn);
				} else {
					group = groupManager.get(groupSn);
				}
				sbSql.append(" and a.");
				sbSql.append(bmField);
				sbSql.append("='");
				sbSql.append(group.getId());
				sbSql.append("' ");
			}
			sbSql.append("union all ");
		}
		sbSql = new StringBuffer(sbSql.substring(0, sbSql.lastIndexOf("union all ")));
		sbSql.append(") e left join ( select d.id, count(*) as bhg from ( ");
		for (Dict dict : lstDictAll) {
			String tableName = dict.getValue();
			sbSql.append("select b.id, a.status, a.bzmc from ");
			sbSql.append(tableName);
			sbSql.append(" a, tb_sys_group b ");
			sbSql.append("where a.");
			sbSql.append(bmField);
			sbSql.append("= b.id and a.status = '不合格' ");
			if (StringUtil.isNotEmpty(strAddCondition)) {
				sbSql.append(" and ");
				sbSql.append(strAddCondition);
			}
			if (StringUtil.isNotEmpty(groupSn)) {
				Group group = null;
				if (isBm) {
					group = groupManager.findUniqueBy("groupSn", groupSn);
				} else {
					group = groupManager.get(groupSn);
				}
				sbSql.append(" and a.");
				sbSql.append(bmField);
				sbSql.append("='");
				sbSql.append(group.getId());
				sbSql.append("' ");
			}
			sbSql.append("union all ");
		}
		sbSql = new StringBuffer(sbSql.substring(0, sbSql.lastIndexOf("union all ")));
		sbSql.append(") d group by d.id) f on e.id =  f.id) h, ");
		sbSql.append("tb_sys_group t where g.id = h.id and g.id = t.id)");

		return sbSql.toString();
	}

	/**
	 * 
	 * 描述 : 拼接指定两票表的查询sql 
	 * 作者 : wangyun 
	 * 时间 : 2010-10-28 
	 * 参数 : 
	 * 		tableName ： 指定表名
	 * 		strAddCondition ： 查询条件 
	 * 		orderSql ： 排序条件 
	 * 		isBm : 用来区别部门和班组（true：部门查询；false：班组查询）
	 * 返回值 : 
	 * 		sql ： 拼接好的sql
	 * 
	 */
	private String joinBmLpSql(String tableName, String groupSn, String strAddCondition, Boolean isBm) {
		String bmField = isBm?"bzparentid" : "bzid";

		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select id, groupname, group_sn, hg, bhg, (hg+bhg) as hj, ROUND(hg / (hg + bhg) * 100, 2) as hgl ");
		sbSql.append("from (select g.id, t.groupname, t.group_sn, nvl(g.hg, 0) as hg, nvl(h.bhg, 0) as bhg ");
		sbSql.append("from (select e.id, f.hg from ( ");
		sbSql.append("select distinct b.id from ");
		sbSql.append(tableName);
		sbSql.append(" a, tb_sys_group b where a.");
		sbSql.append(bmField);
		sbSql.append("= b.id ");
		if (StringUtil.isNotEmpty(strAddCondition)) {
			sbSql.append(" and ");
			sbSql.append(strAddCondition);
		}
		if (StringUtil.isNotEmpty(groupSn)) {
			Group group = null;
			if (isBm) {
				group = groupManager.findUniqueBy("groupSn", groupSn);
			} else {
				group = groupManager.get(groupSn);
			}
			sbSql.append(" and a.");
			sbSql.append(bmField);
			sbSql.append("='");
			sbSql.append(group.getId());
			sbSql.append("' ");
		}
		sbSql.append(") e left join ( select d.id, count(*) as hg from ( ");
		sbSql.append("select b.id, a.status, a.bzmc from ");
		sbSql.append(tableName);
		sbSql.append(" a, tb_sys_group b ");
		sbSql.append("where a.");
		sbSql.append(bmField);
		sbSql.append("= b.id and a.status = '合格' ");
		if (StringUtil.isNotEmpty(strAddCondition)) {
			sbSql.append(" and ");
			sbSql.append(strAddCondition);
		}
		if (StringUtil.isNotEmpty(groupSn)) {
			Group group = null;
			if (isBm) {
				group = groupManager.findUniqueBy("groupSn", groupSn);
			} else {
				group = groupManager.get(groupSn);
			}
			sbSql.append(" and a.");
			sbSql.append(bmField);
			sbSql.append("='");
			sbSql.append(group.getId());
			sbSql.append("' ");
		}
		sbSql.append(") d group by d.id) f on e.id =  f.id) g, ");
		sbSql.append("(select e.id, f.bhg from ( ");
		sbSql.append("select distinct b.id from ");
		sbSql.append(tableName);
		sbSql.append(" a, tb_sys_group b where a.");
		sbSql.append(bmField);
		sbSql.append("= b.id ");
		if (StringUtil.isNotEmpty(strAddCondition)) {
			sbSql.append(" and ");
			sbSql.append(strAddCondition);
		}
		if (StringUtil.isNotEmpty(groupSn)) {
			Group group = null;
			if (isBm) {
				group = groupManager.findUniqueBy("groupSn", groupSn);
			} else {
				group = groupManager.get(groupSn);
			}
			sbSql.append(" and a.");
			sbSql.append(bmField);
			sbSql.append("='");
			sbSql.append(group.getId());
			sbSql.append("' ");
		}
		sbSql.append(") e left join ( select d.id, count(*) as bhg from ( ");
		sbSql.append("select b.id, a.status, a.bzmc from ");
		sbSql.append(tableName);
		sbSql.append(" a, tb_sys_group b ");
		sbSql.append("where a.");
		sbSql.append(bmField);
		sbSql.append("= b.id and a.status = '不合格' ");
		if (StringUtil.isNotEmpty(strAddCondition)) {
			sbSql.append(" and ");
			sbSql.append(strAddCondition);
		}
		if (StringUtil.isNotEmpty(groupSn)) {
			Group group = null;
			if (isBm) {
				group = groupManager.findUniqueBy("groupSn", groupSn);
			} else {
				group = groupManager.get(groupSn);
			}
			sbSql.append(" and a.");
			sbSql.append(bmField);
			sbSql.append("='");
			sbSql.append(group.getId());
			sbSql.append("' ");
		}
		sbSql.append(") d group by d.id) f on e.id =  f.id) h, ");
		sbSql.append("tb_sys_group t where g.id = h.id and g.id = t.id)");

		return sbSql.toString();
	}

	/**
	 * 
	 * 描述 : 拼接两票不合格清单sql
	 * 作者 : wangyun 
	 * 时间 : 2010-10-28 
	 * 参数 : 
	 * 		lstDictAll ： 所有两票表
	 * 		strAddCondition ： 查询条件 
	 * 返回值 : 
	 * 		sql ： 拼接好的sql
	 * 
	 */
	private String joinBhgSql(List<Dict> lstDictAll, String groupId, String strAddCondition) {
		String sql = "";
		for (Dict dict : lstDictAll) {
			String tableName = dict.getValue();
			String tableCName = dict.getKey();
			sql += "select a.ID, a.PH, a.BZMC, a.FZRMC, a.STATUS, to_char(a.JHKSSJ, 'yyyy-MM-dd HH24:mi') as JHKSSJ, "
				+ "to_char(a.JHJSSJ,'yyyy-MM-dd HH24:mi') as JHJSSJ, '"+ tableCName+"' as PMC "
				+ "from " + tableName + " a "
				+ "where a.status = '不合格' ";
			if (StringUtil.isNotEmpty(groupId)) {
				sql += " and a.bzid = '"+groupId+"'";
			}
			if (StringUtil.isNotEmpty(strAddCondition)) {
				sql += " and " + strAddCondition;
			}
			sql += " union all ";
		}
		sql = sql.substring(0, sql.lastIndexOf("union all "));
		return sql;
	}

	/**
	 * 
	 * 描述 : 拼接两票不合格清单sql
	 * 作者 : wangyun
	 * 时间 : 2010-11-2
	 * 参数 : 
	 * 		tableName ： 指定表名
	 * 		tableCName : 票种中文名
	 * 		strAddCondition ： 查询条件 
	 * 		orderSql ： 排序条件 
	 * 返回值 : 
	 * 		sql ： 拼接好的sql
	 * 
	 */
	private String joinBhgSql(String tableName, String tableCName, String groupId, String strAddCondition) {
		 String sql = "select a.ID, a.PH, a.BZMC, a.FZRMC, a.STATUS, to_char(a.JHKSSJ, 'yyyy-MM-dd HH24:mi') as JHKSSJ, "
				+ "to_char(a.JHJSSJ,'yyyy-MM-dd HH24:mi') as JHJSSJ, '"+ tableCName+"' as TABLENAME "
				+ "from " + tableName + " a "
				+ "where a.status = '不合格' ";
		 if (StringUtil.isNotEmpty(groupId)) {
			 sql += " and a.bzid = '"+groupId+"'";
		 }
		 if (StringUtil.isNotEmpty(strAddCondition)) {
			 sql += " and " + strAddCondition;
		 }
		 return sql;
	}

	/**
	 *
	 * 描述 : 拼接按票种统计sql
	 * 作者 : wangyun
	 * 时间 : 2010-12-28
	 * 参数 : 
	 * 		lstDictAll ： 所有两票表
	 * 		strAddCondition ： 查询条件 
	 * 返回值 : 
	 * 		sql ： 拼接好的sql
	 *
	 */
	private String joinPztjSql(List<Dict> lstDictAll, String strAddCondition) {
		StringBuffer sbSql = new StringBuffer();
		for (Dict dict : lstDictAll) {
			String tableName = dict.getValue();
			String tableCName = dict.getKey();
			sbSql.append("select hg, bhg, (hg+bhg) as hj, '");
			sbSql.append(tableCName);
			sbSql.append("' as pmc, '");
			sbSql.append(tableName);
			sbSql.append("' as tableName from ");
			sbSql.append("(select count(*) as hg from ");
			sbSql.append(tableName);
			sbSql.append(" where status = '合格' ");
			if (StringUtil.isNotEmpty(strAddCondition)) {
				sbSql.append(" and ");
				sbSql.append(strAddCondition);
			}
			sbSql.append(") ,");
			sbSql.append("(select count(*) as bhg from ");
			sbSql.append(tableName);
			sbSql.append(" where status = '不合格' ");
			if (StringUtil.isNotEmpty(strAddCondition)) {
				sbSql.append(" and ");
				sbSql.append(strAddCondition);
			}
			sbSql.append(") ");
			sbSql.append(" union all ");
		}
		sbSql = sbSql.delete(sbSql.length() - 10, sbSql.length() -1);
		return sbSql.toString();
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

	public DictManager getDictManager() {
		return dictManager;
	}

	public void setDictManager(DictManager dictManager) {
		this.dictManager = dictManager;
	}

	public LptjglManager getLptjglManager() {
		return lptjglManager;
	}

	public void setLptjglManager(LptjglManager lptjglManager) {
		this.lptjglManager = lptjglManager;
	}

	public GroupManager getGroupManager() {
		return groupManager;
	}

	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

}
