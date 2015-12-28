package com.jteap.sb.sbtzgl.web;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.sb.sbtzgl.manager.JctzManager;
import com.jteap.sb.sbtzgl.manager.SbtzCatalogManager;
import com.jteap.sb.sbtzgl.model.Jctz;
import com.jteap.sb.sbtzgl.model.SbtzCatalog;

@SuppressWarnings("serial")
public class JctzAction extends AbstractAction {
	
	private JctzManager jctzManager;
	
	private SbtzCatalogManager sbtzCatalogManager;

	public JctzManager getJctzManager() {
		return jctzManager;
	}

	public SbtzCatalogManager getSbtzCatalogManager() {
		return sbtzCatalogManager;
	}

	public void setSbtzCatalogManager(SbtzCatalogManager sbtzCatalogManager) {
		this.sbtzCatalogManager = sbtzCatalogManager;
	}

	public void setJctzManager(JctzManager jctzManager) {
		this.jctzManager = jctzManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HibernateEntityDao getManager() {
		return jctzManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{
			"id","sbtzCatalog","kks","sbbm","ybmc","yt","xsjgf","dw","sl","azdd","xtth","remark","cjsj"
		};
	}
	
	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"id","sbtzCatalog","kks","sbbm","ybmc","yt","xsjgf","dw","sl","azdd","xtth","remark","cjsj"
		};
	}
	
	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		String flbm = request.getParameter("flbm");
		if (StringUtil.isNotEmpty(flbm)) {
			HqlUtil.addWholeCondition(hql, "sbtzCatalog.flbm like '"+flbm+"%'");
		}
		String hqlWhere = request.getParameter("queryParamsSql");
		if(StringUtils.isNotEmpty(hqlWhere)){
			String hqlWhereTemp = hqlWhere.replace("$", "%");
			HqlUtil.addWholeCondition(hql, hqlWhereTemp);
		}
	}
	
	/**
	 * 保存或修改设备基础台帐信息
	 * @return
	 */
	public String saveOrUpdateAction(){
		try {
			Jctz jctz = new Jctz();
			String id = request.getParameter("id");
			String tzflCatalogId = request.getParameter("tzflCatalogId");
			SbtzCatalog sbtzCatalog = sbtzCatalogManager.get(tzflCatalogId);
			if(StringUtil.isNotEmpty(id)){
				jctz.setId(id);
			}
//			if(StringUtil.isNotEmpty(tzflCatalogId)){
//				jctz.setTzflCatalogId(tzflCatalogId);
//			}
			jctz.setSbtzCatalog(sbtzCatalog);
			jctz.setKks(request.getParameter("kks"));
			jctz.setSbbm(request.getParameter("sbbm"));
			jctz.setYbmc(request.getParameter("ybmc"));
			jctz.setYt(request.getParameter("yt"));
			jctz.setXsjgf(request.getParameter("xsjgf"));
			jctz.setDw(request.getParameter("dw"));
			jctz.setSl(request.getParameter("sl"));
			jctz.setAzdd(request.getParameter("azdd"));
			jctz.setXtth(request.getParameter("xtth"));
			jctz.setRemark(request.getParameter("remark"));
			jctz.setCjsj(new Date());
			jctzManager.save(jctz);
			
			this.outputJson("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	
	
}
