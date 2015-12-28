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
import com.jteap.yx.tz.manager.BhdzxgjlManager;
import com.jteap.yx.tz.model.Bhdzxgjl;

/**
 * 保护定值修改记录Action
 * 
 * @author wangyun
 * 
 */
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class BhdzxgjlAction extends AbstractAction {

	private BhdzxgjlManager bhdzxgjlManager;

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
		String bhmc = request.getParameter("bhmc");
		String strggsj = request.getParameter("ggsj");
		String bhmcgq1 = request.getParameter("bhmcgq1");
		String bhmcgq2 = request.getParameter("bhmcgq2");
		String bhmcgq3 = request.getParameter("bhmcgq3");
		String bhmcgq4 = request.getParameter("bhmcgq4");
		String bhmcgq5 = request.getParameter("bhmcgq5");
		String zdzgq1 = request.getParameter("zdzgq1");
		String zdzgq2 = request.getParameter("zdzgq2");
		String zdzgq3 = request.getParameter("zdzgq3");
		String zdzgq4 = request.getParameter("zdzgq4");
		String zdzgq5 = request.getParameter("zdzgq5");
		String bhmcgh1 = request.getParameter("bhmcgh1");
		String bhmcgh2 = request.getParameter("bhmcgh2");
		String bhmcgh3 = request.getParameter("bhmcgh3");
		String bhmcgh4 = request.getParameter("bhmcgh4");
		String bhmcgh5 = request.getParameter("bhmcgh5");
		String zdzgh1 = request.getParameter("zdzgh1");
		String zdzgh2 = request.getParameter("zdzgh2");
		String zdzgh3 = request.getParameter("zdzgh3");
		String zdzgh4 = request.getParameter("zdzgh4");
		String zdzgh5 = request.getParameter("zdzgh5");
		String ggyy = request.getParameter("ggyy");
		String ggflr = request.getParameter("ggflr");
		String ggzhr = request.getParameter("ggzhr");
		String ggyhjc = request.getParameter("ggyhjc");
		String bhmchfq1 = request.getParameter("bhmchfq1");
		String bhmchfq2 = request.getParameter("bhmchfq2");
		String bhmchfq3 = request.getParameter("bhmchfq3");
		String bhmchfq4 = request.getParameter("bhmchfq4");
		String bhmchfq5 = request.getParameter("bhmchfq5");
		String zdzhfq1 = request.getParameter("zdzhfq1");
		String zdzhfq2 = request.getParameter("zdzhfq2");
		String zdzhfq3 = request.getParameter("zdzhfq3");
		String zdzhfq4 = request.getParameter("zdzhfq4");
		String zdzhfq5 = request.getParameter("zdzhfq5");
		String bhmchfh1 = request.getParameter("bhmchfh1");
		String bhmchfh2 = request.getParameter("bhmchfh2");
		String bhmchfh3 = request.getParameter("bhmchfh3");
		String bhmchfh4 = request.getParameter("bhmchfh4");
		String bhmchfh5 = request.getParameter("bhmchfh5");
		String zdzhfh1 = request.getParameter("zdzhfh1");
		String zdzhfh2 = request.getParameter("zdzhfh2");
		String zdzhfh3 = request.getParameter("zdzhfh3");
		String zdzhfh4 = request.getParameter("zdzhfh4");
		String zdzhfh5 = request.getParameter("zdzhfh5");
		String hfyy = request.getParameter("hfyy");
		String hfflr = request.getParameter("hfflr");
		String hfzhr = request.getParameter("hfzhr");
		String hfyhjc = request.getParameter("hfyhjc");
		String bz = request.getParameter("bz");
		String strhfsj =  request.getParameter("hfsj");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

		try {
			Bhdzxgjl bhdzxgjl = null;
			if (StringUtil.isNotEmpty(id)) {
				bhdzxgjl = bhdzxgjlManager.get(id);
			} else {
				bhdzxgjl = new Bhdzxgjl();
			}
			
			bhdzxgjl.setBhmc(bhmc);
			bhdzxgjl.setBhmcgq1(bhmcgq1);
			bhdzxgjl.setBhmcgq2(bhmcgq2);
			bhdzxgjl.setBhmcgq3(bhmcgq3);
			bhdzxgjl.setBhmcgq4(bhmcgq4);
			bhdzxgjl.setBhmcgq5(bhmcgq5);
			bhdzxgjl.setZdzgq1(zdzgq1);
			bhdzxgjl.setZdzgq2(zdzgq2);
			bhdzxgjl.setZdzgq3(zdzgq3);
			bhdzxgjl.setZdzgq4(zdzgq4);
			bhdzxgjl.setZdzgq5(zdzgq5);
			bhdzxgjl.setBhmcgh1(bhmcgh1);
			bhdzxgjl.setBhmcgh2(bhmcgh2);
			bhdzxgjl.setBhmcgh3(bhmcgh3);
			bhdzxgjl.setBhmcgh4(bhmcgh4);
			bhdzxgjl.setBhmcgh5(bhmcgh5);
			bhdzxgjl.setZdzgh1(zdzgh1);
			bhdzxgjl.setZdzgh2(zdzgh2);
			bhdzxgjl.setZdzgh3(zdzgh3);
			bhdzxgjl.setZdzgh4(zdzgh4);
			bhdzxgjl.setZdzgh5(zdzgh5);
			bhdzxgjl.setGgyy(ggyy);
			bhdzxgjl.setGgflr(ggflr);
			bhdzxgjl.setGgzhr(ggzhr);
			bhdzxgjl.setGgyhjc(ggyhjc);
			bhdzxgjl.setBhmchfq1(bhmchfq1);
			bhdzxgjl.setBhmchfq2(bhmchfq2);
			bhdzxgjl.setBhmchfq3(bhmchfq3);
			bhdzxgjl.setBhmchfq4(bhmchfq4);
			bhdzxgjl.setBhmchfq5(bhmchfq5);
			bhdzxgjl.setZdzhfq1(zdzhfq1);
			bhdzxgjl.setZdzhfq2(zdzhfq2);
			bhdzxgjl.setZdzhfq3(zdzhfq3);
			bhdzxgjl.setZdzhfq4(zdzhfq4);
			bhdzxgjl.setZdzhfq5(zdzhfq5);
			bhdzxgjl.setBhmchfh1(bhmchfh1);
			bhdzxgjl.setBhmchfh2(bhmchfh2);
			bhdzxgjl.setBhmchfh3(bhmchfh3);
			bhdzxgjl.setBhmchfh4(bhmchfh4);
			bhdzxgjl.setBhmchfh5(bhmchfh5);
			bhdzxgjl.setZdzhfh1(zdzhfh1);
			bhdzxgjl.setZdzhfh2(zdzhfh2);
			bhdzxgjl.setZdzhfh3(zdzhfh3);
			bhdzxgjl.setZdzhfh4(zdzhfh4);
			bhdzxgjl.setZdzhfh5(zdzhfh5);
			bhdzxgjl.setHfyy(hfyy);
			bhdzxgjl.setHfflr(hfflr);
			bhdzxgjl.setHfzhr(hfzhr);
			bhdzxgjl.setHfyhjc(hfyhjc);
			bhdzxgjl.setBz(bz);
			if (StringUtil.isNotEmpty(strggsj)) {
				Date ggsj = sdf.parse(strggsj);
				bhdzxgjl.setGgsj(ggsj);
			}
			if (StringUtil.isNotEmpty(strhfsj)) {
				Date hfsj = sdf.parse(strhfsj);
				bhdzxgjl.setHfsj(hfsj);
			}

			bhdzxgjlManager.save(bhdzxgjl);
		} catch (Exception e) {
			e.printStackTrace();
			outputJson("{success:false}");
		}
		outputJson("{success:true}");
		return NONE;
	}

	public BhdzxgjlManager getBhdzxgjlManager() {
		return bhdzxgjlManager;
	}

	public void setBhdzxgjlManager(BhdzxgjlManager bhdzxgjlManager) {
		this.bhdzxgjlManager = bhdzxgjlManager;
	}

	@Override
	public HibernateEntityDao getManager() {
		return bhdzxgjlManager;
	}

	@Override
	public String[] listJsonProperties() {

		return new String[] { "id", "bhmc", "ggsj", "bhmcgq1", "bhmcgq2", "bhmcgq3", "bhmcgq4", "bhmcgq5", "zdzgq1",
				"zdzgq2", "zdzgq3", "zdzgq4", "zdzgq5", "bhmcgh1", "bhmcgh2", "bhmcgh3", "bhmcgh4", "bhmcgh5",
				"zdzgh1", "zdzgh2", "zdzgh3", "zdzgh4", "zdzgh5", "ggyy", "ggflr", "ggzhr", "ggyhjc", "bhmchfq1",
				"bhmchfq2", "bhmchfq3", "bhmchfq4", "bhmchfq5", "zdzhfq1", "zdzhfq2", "zdzhfq3", "zdzhfq4", "zdzhfq5",
				"bhmchfh1", "bhmchfh2", "bhmchfh3", "bhmchfh4", "bhmchfh5", "zdzhfh1", "zdzhfh2", "zdzhfh3", "zdzhfh4",
				"zdzhfh5", "hfyy", "hfflr", "hfzhr", "hfyhjc", "bz", "hfsj", "time" };
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "id", "bhmc", "ggsj", "bhmcgq1", "bhmcgq2", "bhmcgq3", "bhmcgq4", "bhmcgq5", "zdzgq1",
				"zdzgq2", "zdzgq3", "zdzgq4", "zdzgq5", "bhmcgh1", "bhmcgh2", "bhmcgh3", "bhmcgh4", "bhmcgh5",
				"zdzgh1", "zdzgh2", "zdzgh3", "zdzgh4", "zdzgh5", "ggyy", "ggflr", "ggzhr", "ggyhjc", "bhmchfq1",
				"bhmchfq2", "bhmchfq3", "bhmchfq4", "bhmchfq5", "zdzhfq1", "zdzhfq2", "zdzhfq3", "zdzhfq4", "zdzhfq5",
				"bhmchfh1", "bhmchfh2", "bhmchfh3", "bhmchfh4", "bhmchfh5", "zdzhfh1", "zdzhfh2", "zdzhfh3", "zdzhfh4",
				"zdzhfh5", "hfyy", "hfflr", "hfzhr", "hfyhjc", "bz", "hfsj", "time" };
	}
}
