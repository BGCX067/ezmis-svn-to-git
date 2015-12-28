/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.yx.tz.web;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.yx.tz.manager.Lqljl600Manager;
import com.jteap.yx.tz.model.Lqljl600;

/**
 * 漏氢量记录(600MW)Action
 * 
 * @author wangyun
 * 
 */
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class Lqljl600Action extends AbstractAction {

	private Lqljl600Manager lqljl600Manager;

	@Override
	protected void beforeShowList(HttpServletRequest request, HttpServletResponse response, StringBuffer hql) {
		String hqlWhere = request.getParameter("queryParamsSql");
		if (StringUtils.isNotEmpty(hqlWhere)) {
			String hqlWhereTemp = hqlWhere.replace("$", "%");
			HqlUtil.addWholeCondition(hql, hqlWhereTemp);
		}
	}

	public String saveOrUpdateAction() throws Exception {
		String id = request.getParameter("id");
		String strkssj = request.getParameter("kssj");
		String strksqyp1 = request.getParameter("ksqyp1");
		String strqwxt1 = request.getParameter("qwxt1");
		String strqwxt2 = request.getParameter("qwxt2");
		String strqwdt1 = request.getParameter("qwdt1");
		String strjssj = request.getParameter("jssj");
		String strjsqyp2 = request.getParameter("jsqyp2");
		String strqwdt2 = request.getParameter("qwdt2");
		String stryxsj = request.getParameter("yxsj");
		String strlql = request.getParameter("lql");
		String strlqlv = request.getParameter("lqlv");
		String txr1 = request.getParameter("txr1");
		String txr2 = request.getParameter("txr2");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh");

		try {
			Lqljl600 lqljl600 = null;
			if (StringUtil.isNotEmpty(id)) {
				lqljl600 = lqljl600Manager.get(id);
			} else {
				lqljl600 = new Lqljl600();
			}

			lqljl600.setTxr1(txr1);
			lqljl600.setTxr2(txr2);
			
			if (StringUtil.isNotEmpty(strkssj)) {
				Date kssj = sdf.parse(strkssj);
				lqljl600.setKssj(kssj);
			}
			if (StringUtil.isNotEmpty(strksqyp1)) {
				Double ksqyp1 = Double.valueOf(strksqyp1);
				lqljl600.setKsqyp1(ksqyp1);
			}
			if (StringUtil.isNotEmpty(strqwxt1)) {
				Double qwxt1 = Double.valueOf(strqwxt1);
				lqljl600.setQwxt1(qwxt1);
			}
			if (StringUtil.isNotEmpty(strqwxt2)) {
				Double qwxt2 = Double.valueOf(strqwxt2);
				lqljl600.setQwxt2(qwxt2);
			}
			if (StringUtil.isNotEmpty(strqwdt1)) {
				Double qwdt1 = Double.valueOf(strqwdt1);
				lqljl600.setQwdt1(qwdt1);
			}
			if (StringUtil.isNotEmpty(strjssj)) {
				Date jssj = sdf.parse(strjssj);
				lqljl600.setJssj(jssj);
			}
			if (StringUtil.isNotEmpty(strjsqyp2)) {
				Double jsqyp2 = Double.valueOf(strjsqyp2);
				lqljl600.setJsqyp2(jsqyp2);
			}
			if (StringUtil.isNotEmpty(strqwdt2)) {
				Double qwdt2 = Double.valueOf(strqwdt2);
				lqljl600.setQwdt2(qwdt2);
			}
			if (StringUtil.isNotEmpty(strlql)) {
				Double lql = Double.valueOf(strlql);
				lqljl600.setLql(lql);
			}
			if (StringUtil.isNotEmpty(strlqlv)) {
				Double lqlv = Double.valueOf(strlqlv);
				lqljl600.setLqlv(lqlv);
			}
			if (StringUtil.isNotEmpty(stryxsj)) {
				Double yxsj = Double.valueOf(stryxsj);
				lqljl600.setYxsj(yxsj);
			}
			
			lqljl600Manager.save(lqljl600);
		} catch (Exception e) {
			e.printStackTrace();
			outputJson("{success:false}");
		}
		outputJson("{success:true}");
		return NONE;
	}

	public Lqljl600Manager getLqljl600Manager() {
		return lqljl600Manager;
	}

	public void setLqljl600Manager(Lqljl600Manager lqljl600Manager) {
		this.lqljl600Manager = lqljl600Manager;
	}
	
	
	@Override
	public HibernateEntityDao getManager() {
		return lqljl600Manager;
	}

	@Override
	public String[] listJsonProperties() {
	
		return new String[]{
			"id",
			"kssj",
			"ksqyp1",
			"qwxt1",
			"qwxt2",
			"qwdt1",
			"jssj",
			"jsqyp2",
			"qwdt2",
			"yxsj",
			"lql",
			"lqlv",
			"txr1",
			"txr2",
		"time"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"id",
			"kssj",
			"ksqyp1",
			"qwxt1",
			"qwxt2",
			"qwdt1",
			"jssj",
			"jsqyp2",
			"qwdt2",
			"yxsj",
			"lql",
			"lqlv",
			"txr1",
			"txr2",
		"time"};
	}
}
