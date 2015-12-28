/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
package com.jteap.yx.tz.web;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.yx.tz.manager.LqljlManager;
import com.jteap.yx.tz.model.Lqljl;
import com.jteap.yx.tz.model.Sbjycljl;

/**
 * 漏氢量记录(300MW)Action
 * 
 * @author wangyun
 * 
 */
@SuppressWarnings({ "serial", "unchecked", "unused" })
public class LqljlAction extends AbstractAction {

	private LqljlManager lqljlManager;

	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		String hqlWhere = request.getParameter("queryParamsSql");
		if (StringUtils.isNotEmpty(hqlWhere)) {
			String hqlWhereTemp = hqlWhere.replace("$", "%");
			HqlUtil.addWholeCondition(hql, hqlWhereTemp);
		}
	}

	public String showListAction() throws Exception {
		try {
			StringBuffer hql = getPageBaseHql();

			beforeShowList(this.request, this.response, hql);
			Object pageFlag = this.request.getAttribute("PAGE_FLAG");
			if (pageFlag == null)
				pageFlag = this.request.getParameter("PAGE_FLAG");
			String json;
			if ((pageFlag != null)
					&& (pageFlag.toString().equals("PAGE_FLAG_NO"))) {
				Collection list = getManager().find(hql.toString(),
						this.showListHqlValues.toArray());
				System.out.println(list);
				json = JSONUtil.listToJson(list, listJsonProperties());
				json = "{totalCount:'" + list.size() + "',list:" + json + "}";
			} else {
				json = getPageCollectionJson(hql.toString(),
						this.showListHqlValues.toArray());
			}
			System.out.println(json);
			outputJson(json);
		} catch (Exception ex) {
			throw new BusinessException("显示列表异常", ex);
		}
		return "none";
	}

	public String saveOrUpdateAction() throws Exception {

		String id = request.getParameter("id");
		String strJz = request.getParameter("jz");
		String strKssj = request.getParameter("kssj");
		String strKsqy = request.getParameter("ksqy");
		String strKsqw = request.getParameter("ksqw");
		String strJssj = request.getParameter("jssj");
		String strJsqy = request.getParameter("jsqy");
		String strJsqw = request.getParameter("jsqw");
		String stryxsj = request.getParameter("yxsj");
		String strLql = request.getParameter("lql");
		String strLqlv = request.getParameter("lqlv");
		String txr1 = request.getParameter("txr1");
		String txr2 = request.getParameter("txr2");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		try {
			Lqljl lqljl300 = null;
			if (StringUtil.isNotEmpty(id)) {
				lqljl300 = lqljlManager.get(id);
			} else {
				lqljl300 = new Lqljl();
			}

			lqljl300.setTxr1(txr1);
			lqljl300.setTxr2(txr2);

			if (StringUtil.isNotEmpty(strKssj)) {
				Date kssj = sdf.parse(strKssj);
				lqljl300.setKssj(kssj);
			}
			if (StringUtil.isNotEmpty(strJz)) {
				lqljl300.setJz(strJz);
			}
			if (StringUtil.isNotEmpty(strKsqy)) {
				Double ksqy = Double.valueOf(strKsqy);
				lqljl300.setKsqy(ksqy);
			}
			if (StringUtil.isNotEmpty(strKsqw)) {
				Double ksqw = Double.valueOf(strKsqw);
				lqljl300.setKsqw(ksqw);
			}
			if (StringUtil.isNotEmpty(strJssj)) {
				Date jssj = sdf.parse(strJssj);
				lqljl300.setJssj(jssj);
			}
			if (StringUtil.isNotEmpty(strJsqy)) {
				Double jsqy = Double.valueOf(strJsqy);
				lqljl300.setJsqy(jsqy);
			}
			if (StringUtil.isNotEmpty(strJsqw)) {
				double jsqw = Double.valueOf(strJsqw);
				lqljl300.setJsqw(jsqw);
			}
			if (StringUtil.isNotEmpty(stryxsj)) {
				Double yxsj = Double.valueOf(stryxsj);
				lqljl300.setYxsj(yxsj);
			}
			if (StringUtil.isNotEmpty(strLql)) {
				Double lql = Double.valueOf(strLql);
				lqljl300.setLql(lql);
			}
			if (StringUtil.isNotEmpty(strLqlv)) {
				Double lqlv = Double.valueOf(strLqlv);
				lqljl300.setLqlv(lqlv);
			}
			if (StringUtil.isNotEmpty(stryxsj)) {
				Double yxsj = Double.valueOf(stryxsj);
				lqljl300.setYxsj(yxsj);
			}

			lqljlManager.save(lqljl300);
		} catch (Exception e) {
			e.printStackTrace();
			outputJson("{success:false}");
		}
		outputJson("{success:true}");
		return NONE;
	}

	@Override
	public HibernateEntityDao getManager() {
		return lqljlManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] { "id", "kssj", "ksqy", "ksqw", "jz", "jssj",
				"jsqy", "jsqw", "yxsj", "lql", "lqlv", "txr1", "txr2","time" };
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "id", "kssj", "ksqy", "ksqw", "jz", "jssj",
				"jsqy", "jsqw", "yxsj", "lql", "lqlv", "txr1", "txr2" ,"time"};
	}

	public LqljlManager getLqljlManager() {
		return lqljlManager;
	}

	public void setLqljlManager(LqljlManager lqljlManager) {
		this.lqljlManager = lqljlManager;
	}
}
