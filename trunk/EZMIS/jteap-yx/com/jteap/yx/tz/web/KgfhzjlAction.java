/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
package com.jteap.yx.tz.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.yx.tz.manager.KgfhzjlManager;
import com.jteap.yx.tz.model.Kgfhzjl;

/**
 * 开关分合闸记录Action
 * 
 * @author wangyun
 * 
 */
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class KgfhzjlAction extends AbstractAction {

	private KgfhzjlManager kgfhzjlManager;

	public KgfhzjlManager getKgfhzjlManager() {
		return kgfhzjlManager;
	}

	public void setKgfhzjlManager(KgfhzjlManager kgfhzjlManager) {
		this.kgfhzjlManager = kgfhzjlManager;
	}

	@Override
	public HibernateEntityDao getManager() {
		return kgfhzjlManager;
	}

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
		String jzlx = request.getParameter("jzlx");
		String kgmc = request.getParameter("kgmc");
		String strhzsj = request.getParameter("hzsj");
		String hzyy = request.getParameter("hzyy");
		String hzjlr = request.getParameter("hzjlr");
		String strfzsj = request.getParameter("fzsj");
		String fzyy = request.getParameter("fzyy");
		String fzjlr = request.getParameter("fzjlr");
		String yxsj = request.getParameter("yxsj");
		String dzcs = request.getParameter("dzcs");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

		try {
			Kgfhzjl kgfhzjl = null;
			if (StringUtil.isNotEmpty(id)) {
				kgfhzjl = kgfhzjlManager.get(id);
			} else {
				kgfhzjl = new Kgfhzjl();
			}

			kgfhzjl.setJzlx(jzlx);
			kgfhzjl.setKgmc(kgmc);
			kgfhzjl.setHzyy(hzyy);
			kgfhzjl.setHzjlr(hzjlr);
			kgfhzjl.setFzjlr(fzjlr);
			kgfhzjl.setFzyy(fzyy);

			if (StringUtil.isNotEmpty(dzcs)) {
				kgfhzjl.setDzcs(Integer.valueOf(dzcs));
			}
			if (StringUtil.isNotEmpty(yxsj)) {
				kgfhzjl.setYxsj(Integer.valueOf(yxsj));
			}

			if (StringUtil.isNotEmpty(strhzsj)) {
				Date dtHzsj = sdf.parse(strhzsj);
				kgfhzjl.setHzsj(dtHzsj);
			}
			if (StringUtil.isNotEmpty(strfzsj)) {
				Date dtFzsj = sdf.parse(strfzsj);
				kgfhzjl.setFzsj(dtFzsj);
			}

			kgfhzjlManager.save(kgfhzjl);
		} catch (Exception e) {
			e.printStackTrace();
			outputJson("{success:false}");
		}
		outputJson("{success:true}");
		return NONE;
	}

	@Override
	public String[] listJsonProperties() {

		return new String[] { "id", "jzlx", "kgmc", "hzsj", "hzyy", "hzjlr", "fzsj", "fzyy", "fzjlr", "yxsj", "dzcs",
				"time" };
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "id", "jzlx", "kgmc", "hzsj", "hzyy", "hzjlr", "fzsj", "fzyy", "fzjlr", "yxsj", "dzcs",
				"time" };
	}
}
