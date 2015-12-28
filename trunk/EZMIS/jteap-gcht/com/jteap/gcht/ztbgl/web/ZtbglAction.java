package com.jteap.gcht.ztbgl.web;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.gcht.ztbgl.manager.ZtbglManager;

/**
 * 招投标管理Action
 * 
 * @author wangyun
 *
 */
@SuppressWarnings({"serial", "unused", "unchecked"})
public class ZtbglAction extends AbstractAction {

	private ZtbglManager ztbglManager;

	/**
	 * 
	 * 描述 : 招标信息显示列表Action
	 * 作者 : wangyun
	 * 时间 : 2010-11-17
	 * 
	 */
	public String showZbxxAction() {
		String zbflId = request.getParameter("zbflId");
		String sort = request.getParameter("sort");
		String dir = request.getParameter("dir");

		// 每页大小
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT", "20");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "0";

		// 排序条件
		String orderSql = "";
		if (StringUtil.isNotEmpty(sort)) {
			orderSql = " order by " + sort + " " + dir;
		}

		// 查询条件
		String sqlWhere = request.getParameter("queryParamsSql");
		String sqlWhereTemp = "";
		if (StringUtils.isNotEmpty(sqlWhere)) {
			sqlWhereTemp = sqlWhere.replace("$", "%");
			if (StringUtil.isNotEmpty(zbflId)) {
				sqlWhereTemp += " and obj.zbflid = '" + zbflId + "'";
			}
		} else {
			if (StringUtil.isNotEmpty(zbflId)) {
				sqlWhereTemp = "obj.zbflid = '" + zbflId + "'";
			}
		}

		try {
			List<Map<String, Object>> list = ztbglManager.findZbxxList(sqlWhereTemp, orderSql);

			// 分页
			int startIndex = Integer.parseInt(start);
			int limitIndex = Integer.parseInt(limit) + startIndex;
			if (limitIndex > list.size()) {
				limitIndex = list.size();
			}
			List<Map<String, Object>> pageList = list.subList(startIndex, limitIndex);

			String[] arrayJson = new String[] { "id", "zbbh", "xmmc", "zbpc", "zbfs", "jhzbsj", "sjzbsj", "zbdz", "zbnr", "zbfl", "zbflid" };
			String json = JSONUtil.listToJson(pageList, arrayJson);
			json = "{totalCount:'" + list.size() + "',list:" + json + "}";
			this.outputJson(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 
	 * 描述 : 中标信息显示列表Action
	 * 作者 : wangyun
	 * 时间 : 2010-11-17
	 * 
	 */
	public String showZhbxxAction() {
		String zbflId = request.getParameter("zbflId");
		String sort = request.getParameter("sort");
		String dir = request.getParameter("dir");

		// 每页大小
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT", "20");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "0";

		// 排序条件
		String orderSql = "";
		if (StringUtil.isNotEmpty(sort)) {
			orderSql = " order by " + sort + " " + dir;
		}

		// 查询条件
		String sqlWhere = request.getParameter("queryParamsSql");
		String sqlWhereTemp = "";
		if (StringUtils.isNotEmpty(sqlWhere)) {
			sqlWhereTemp = sqlWhere.replace("$", "%");
			if (StringUtil.isNotEmpty(zbflId)) {
				sqlWhereTemp += " and obj.zbflid = '" + zbflId + "'";
			}
		} else {
			if (StringUtil.isNotEmpty(zbflId)) {
				sqlWhereTemp = "obj.zbflid = '" + zbflId + "'";
			}
		}

		try {
			List<Map<String, Object>> list = ztbglManager.findZhbxxList(sqlWhereTemp, orderSql);

			// 分页
			int startIndex = Integer.parseInt(start);
			int limitIndex = Integer.parseInt(limit) + startIndex;
			if (limitIndex > list.size()) {
				limitIndex = list.size();
			}
			List<Map<String, Object>> pageList = list.subList(startIndex, limitIndex);

			String[] arrayJson = new String[] { "id", "zbbh", "xmmc", "zbpc", "zbfs", "jhzbsj", "sjzbsj", "zbdz", "zbnr", "zbfl", "zbflid", "zbdw" };
			String json = JSONUtil.listToJson(pageList, arrayJson);
			json = "{totalCount:'" + list.size() + "',list:" + json + "}";
			this.outputJson(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	public ZtbglManager getZtbglManager() {
		return ztbglManager;
	}

	public void setZtbglManager(ZtbglManager ztbglManager) {
		this.ztbglManager = ztbglManager;
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

}
