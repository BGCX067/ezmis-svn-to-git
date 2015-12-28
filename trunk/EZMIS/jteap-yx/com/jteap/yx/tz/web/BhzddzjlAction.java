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
import com.jteap.yx.tz.manager.BhzddzjlManager;
import com.jteap.yx.tz.model.Bhzddzjl;
import com.jteap.yx.tz.model.Dqsbbhjl;

/**
 * 保护及自动装置动作记录Action
 * 
 * @author wangyun
 * 
 */
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class BhzddzjlAction extends AbstractAction {

	private BhzddzjlManager bhzddzjlManager;

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
		String sbmc = request.getParameter("sbmc");
		String strdzsj = request.getParameter("dzsj");
		String bhmc = request.getParameter("bhmc");
		String gzpxh = request.getParameter("gzpxh");
		String jcr = request.getParameter("jcr");
		String fgr = request.getParameter("fgr");
		String bz = request.getParameter("bz");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

		try {
			Bhzddzjl dqsbbhjl = null;
			if (StringUtil.isNotEmpty(id)) {
				dqsbbhjl = bhzddzjlManager.get(id);
			} else {
				dqsbbhjl = new Bhzddzjl();
			}
			
			dqsbbhjl.setSbmc(sbmc);
			dqsbbhjl.setBhmc(bhmc);
			dqsbbhjl.setGzpxh(gzpxh);
			dqsbbhjl.setJcr(jcr);
			dqsbbhjl.setFgr(fgr);
			dqsbbhjl.setBz(bz);

			if (StringUtil.isNotEmpty(strdzsj)) {
				Date dzsj = sdf.parse(strdzsj);
				dqsbbhjl.setDzsj(dzsj);
			}

			bhzddzjlManager.save(dqsbbhjl);
		} catch (Exception e) {
			e.printStackTrace();
			outputJson("{success:false}");
		}
		outputJson("{success:true}");
		return NONE;
	}

	public BhzddzjlManager getBhzddzjlManager() {
		return bhzddzjlManager;
	}

	public void setBhzddzjlManager(BhzddzjlManager bhzddzjlManager) {
		this.bhzddzjlManager = bhzddzjlManager;
	}

	@Override
	public HibernateEntityDao getManager() {
		return bhzddzjlManager;
	}

	@Override
	public String[] listJsonProperties() {

		return new String[] { "id", "sbmc", "dzsj", "bhmc", "gzpxh", "jcr", "fgr", "bz", "time" };
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "id", "sbmc", "dzsj", "bhmc", "gzpxh", "jcr", "fgr", "bz", "time" };
	}
}
