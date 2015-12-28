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
import com.jteap.yx.tz.manager.SbjycljlManager;
import com.jteap.yx.tz.model.Dqsbbhjl;
import com.jteap.yx.tz.model.Sbjycljl;

/**
 * 设备绝缘测量记录Action
 * 
 * @author wangyun
 * 
 */
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class SbjycljlAction extends AbstractAction {

	private SbjycljlManager sbjycljlManager;

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
		String strsj = request.getParameter("sj");
		String sbmc = request.getParameter("sbmc");
		String clxm = request.getParameter("clxm");
		String strR15 = request.getParameter("r15");
		String strR60 = request.getParameter("r60");
		String strR1560 = request.getParameter("r1560");
		String syyb = request.getParameter("syyb");
		String tq = request.getParameter("tq");
		String clr = request.getParameter("clr");
		String jhr = request.getParameter("jhr");
		String bz = request.getParameter("bz");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

		try {
			Sbjycljl sbjycljl = null;
			if (StringUtil.isNotEmpty(id)) {
				sbjycljl = sbjycljlManager.get(id);
			} else {
				sbjycljl = new Sbjycljl();
			}

			sbjycljl.setSbmc(sbmc);
			sbjycljl.setClxm(clxm);
			sbjycljl.setSyyb(syyb);
			sbjycljl.setTq(tq);
			sbjycljl.setClr(clr);
			sbjycljl.setJhr(jhr);
			sbjycljl.setBz(bz);
			
			if (StringUtil.isNotEmpty(strsj)) {
				Date sj = sdf.parse(strsj);
				sbjycljl.setSj(sj);
			}
			
			if (StringUtil.isNotEmpty(strR15)) {
				Double r15 = Double.valueOf(strR15);
				sbjycljl.setR15(r15);
			}
			if (StringUtil.isNotEmpty(strR60)) {
				Double r60 = Double.valueOf(strR60);
				sbjycljl.setR60(r60);
			}
			if (StringUtil.isNotEmpty(strR1560)) {
				Double r1560 = Double.valueOf(strR1560);
				sbjycljl.setR1560(r1560);
			}

			sbjycljlManager.save(sbjycljl);
		} catch (Exception e) {
			e.printStackTrace();
			outputJson("{success:false}");
		}
		outputJson("{success:true}");
		return NONE;
	}

	public SbjycljlManager getSbjycljlManager() {
		return sbjycljlManager;
	}

	public void setSbjycljlManager(SbjycljlManager sbjycljlManager) {
		this.sbjycljlManager = sbjycljlManager;
	}

	@Override
	public HibernateEntityDao getManager() {
		return sbjycljlManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] { "id", "sj", "sbmc", "clxm", "r15", "r60", "r1560", "syyb", "tq", "clr", "jhr", "bz",
				"time" };
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "id", "sj", "sbmc", "clxm", "r15", "r60", "r1560", "syyb", "tq", "clr", "jhr", "bz",
				"time" };
	}
}
