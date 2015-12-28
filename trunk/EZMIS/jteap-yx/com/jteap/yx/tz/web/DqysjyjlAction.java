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
import com.jteap.yx.tz.manager.DqysjyjlManager;
import com.jteap.yx.tz.model.Dqsbbhjl;
import com.jteap.yx.tz.model.Dqysjyjl;

/**
 * 电气钥匙借用记录Action
 * 
 * @author wangyun
 * 
 */
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class DqysjyjlAction extends AbstractAction {

	private DqysjyjlManager dqysjyjlManager;

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
		String strjysj = request.getParameter("jysj");
		String jyys = request.getParameter("jyys");
		String jyyy = request.getParameter("jyyy");
		String jyr = request.getParameter("jyr");
		String jczby = request.getParameter("jczby");
		String jczz = request.getParameter("jczz");
		String strghsj = request.getParameter("ghsj");
		String ghr = request.getParameter("ghr");
		String shzby = request.getParameter("shzby");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

		try {
			Dqysjyjl dqysjyjl = null;
			if (StringUtil.isNotEmpty(id)) {
				dqysjyjl = dqysjyjlManager.get(id);
			} else {
				dqysjyjl = new Dqysjyjl();
			}

			dqysjyjl.setJyys(jyys);
			dqysjyjl.setJyyy(jyyy);
			dqysjyjl.setJyr(jyr);
			dqysjyjl.setJczby(jczby);
			dqysjyjl.setJczz(jczz);
			
			dqysjyjl.setGhr(ghr);
			dqysjyjl.setShzby(shzby);
			
			if (StringUtil.isNotEmpty(strjysj)) {
				Date jysj = sdf.parse(strjysj);
				dqysjyjl.setJysj(jysj);
			}
			if (StringUtil.isNotEmpty(strghsj)) {
				Date ghsj = sdf.parse(strghsj);
				dqysjyjl.setGhsj(ghsj);
			}

			dqysjyjlManager.save(dqysjyjl);
		} catch (Exception e) {
			e.printStackTrace();
			outputJson("{success:false}");
		}
		outputJson("{success:true}");
		return NONE;
	}

	public DqysjyjlManager getDqysjyjlManager() {
		return dqysjyjlManager;
	}

	public void setDqysjyjlManager(DqysjyjlManager dqysjyjlManager) {
		this.dqysjyjlManager = dqysjyjlManager;
	}

	@Override
	public HibernateEntityDao getManager() {
		return dqysjyjlManager;
	}

	@Override
	public String[] listJsonProperties() {

		return new String[] { "id", "jysj", "jyys", "jyyy", "jyr", "jczby", "jczz", "ghsj", "ghr", "shzby", "time" };
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "id", "jysj", "jyys", "jyyy", "jyr", "jczby", "jczz", "ghsj", "ghr", "shzby", "time" };
	}
}
