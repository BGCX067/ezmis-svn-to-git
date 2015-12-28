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
import com.jteap.yx.tz.manager.DqsbbhjlManager;
import com.jteap.yx.tz.model.Dqsbbhjl;
import com.jteap.yx.tz.model.Kgfhzjl;

/**
 * 电气设备保护记录Action
 * 
 * @author wangyun
 * 
 */
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class DqsbbhjlAction extends AbstractAction {

	private DqsbbhjlManager dqsbbhjlManager;

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
		String jz = request.getParameter("jz");
		String bhmc = request.getParameter("bhmc");
		String strtysj = request.getParameter("tysj");
		String tyyy = request.getParameter("tyyy");
		String tyzxr = request.getParameter("tyzxr");
		String tyzby = request.getParameter("tyzby");
		String strjysj = request.getParameter("jysj");
		String jyyy = request.getParameter("jyyy");
		String jyzxr = request.getParameter("jyzxr");
		String jyzby = request.getParameter("jyzby");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

		try {
			Dqsbbhjl dqsbbhjl = null;
			if (StringUtil.isNotEmpty(id)) {
				dqsbbhjl = dqsbbhjlManager.get(id);
			} else {
				dqsbbhjl = new Dqsbbhjl();
			}

			
			dqsbbhjl.setJz(jz);
			dqsbbhjl.setBhmc(bhmc);
			dqsbbhjl.setTyyy(tyyy);
			dqsbbhjl.setTyzxr(tyzxr);
			dqsbbhjl.setTyzby(tyzby);
			dqsbbhjl.setJyyy(jyyy);
			dqsbbhjl.setJyzxr(jyzxr);
			dqsbbhjl.setJyzby(jyzby);

			if (StringUtil.isNotEmpty(strtysj)) {
				Date tysj = sdf.parse(strtysj);
				dqsbbhjl.setTysj(tysj);
			}
			if (StringUtil.isNotEmpty(strjysj)) {
				Date jysj = sdf.parse(strjysj);
				dqsbbhjl.setJysj(jysj);
			}

			dqsbbhjlManager.save(dqsbbhjl);
		} catch (Exception e) {
			e.printStackTrace();
			outputJson("{success:false}");
		}
		outputJson("{success:true}");
		return NONE;
	}

	public DqsbbhjlManager getDqsbbhjlManager() {
		return dqsbbhjlManager;
	}

	public void setDqsbbhjlManager(DqsbbhjlManager dqsbbhjlManager) {
		this.dqsbbhjlManager = dqsbbhjlManager;
	}

	@Override
	public HibernateEntityDao getManager() {
		return dqsbbhjlManager;
	}

	@Override
	public String[] listJsonProperties() {

		return new String[] { "id", "jz", "bhmc", "tysj", "tyyy", "tyzxr", "tyzby", "jysj", "jyyy", "jyzxr", "jyzby",
				"time" };
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "id", "jz", "bhmc", "tysj", "tyyy", "tyzxr", "tyzby", "jysj", "jyyy", "jyzxr", "jyzby",
				"time" };
	}
}
