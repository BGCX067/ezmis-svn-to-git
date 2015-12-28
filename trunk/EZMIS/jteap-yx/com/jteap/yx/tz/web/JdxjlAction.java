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
import com.jteap.yx.tz.manager.JdxjlManager;
import com.jteap.yx.tz.model.Dqsbbhjl;
import com.jteap.yx.tz.model.Jdxjl;

/**
 * 接地线记录Action
 * 
 * @author wangyun
 * 
 */
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class JdxjlAction extends AbstractAction {

	private JdxjlManager jdxjlManager;

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
		String jdxbh = request.getParameter("jdxbh");
		String zsdd = request.getParameter("zsdd");
		String zsr = request.getParameter("zsr");
		String ccr = request.getParameter("ccr");
		String strzssj = request.getParameter("zssj");
		String strccsj = request.getParameter("ccsj");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

		try {
			Jdxjl jdxjl = null;
			if (StringUtil.isNotEmpty(id)) {
				jdxjl = jdxjlManager.get(id);
			} else {
				jdxjl = new Jdxjl();
			}
			
			jdxjl.setJdxbh(jdxbh);
			jdxjl.setJz(jz);
			jdxjl.setZsdd(zsdd);
			jdxjl.setZsr(zsr);
			jdxjl.setCcr(ccr);

			if (StringUtil.isNotEmpty(strzssj)) {
				Date zssj = sdf.parse(strzssj);
				jdxjl.setZssj(zssj);
			}
			if (StringUtil.isNotEmpty(strccsj)) {
				Date ccsj = sdf.parse(strccsj);
				jdxjl.setCcsj(ccsj);
			}

			jdxjlManager.save(jdxjl);
		} catch (Exception e) {
			e.printStackTrace();
			outputJson("{success:false}");
		}
		outputJson("{success:true}");
		return NONE;
	}

	public JdxjlManager getJdxjlManager() {
		return jdxjlManager;
	}

	public void setJdxjlManager(JdxjlManager jdxjlManager) {
		this.jdxjlManager = jdxjlManager;
	}

	@Override
	public HibernateEntityDao getManager() {
		return jdxjlManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] { "id", "jdxbh", "jz", "zsdd", "zssj", "zsr", "ccsj", "ccr", "time" };
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "id", "jdxbh", "jz", "zsdd", "zssj", "zsr", "ccsj", "ccr", "time" };
	}
}
