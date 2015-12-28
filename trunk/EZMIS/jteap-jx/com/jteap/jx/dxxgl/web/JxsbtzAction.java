package com.jteap.jx.dxxgl.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.jx.dxxgl.manager.JxsbtzManager;
import com.jteap.jx.dxxgl.model.Jxsbtz;
import com.jteap.system.dict.manager.DictManager;
import com.jteap.system.dict.model.Dict;

@SuppressWarnings({"serial", "unchecked"})
public class JxsbtzAction extends AbstractAction {

	private JxsbtzManager jxsbtzManager;
	private DictManager dictManager;

	/**
	 * 
	 * 描述 : 显示检修专业树
	 * 作者 : wangyun
	 * 时间 : Aug 17, 2010
	 * 异常 :
	 * 
	 */
	public String showJxzyTreeAction() {
		try {
			List<Dict> dicts = (List<Dict>) dictManager.findDictByUniqueCatalogName("SSZY");
			StringBuffer sb = new StringBuffer();
			sb.append("[");
			for (Dict dict : dicts) {
				sb.append("{");
				sb.append("'id':'");
				sb.append(dict.getId());
				sb.append("','text':'");
				sb.append(dict.getKey());
				sb.append("','value':'");
				sb.append(dict.getValue());
				sb.append("','leaf':true},");
			}
			String ret = "";
			if (dicts.size() > 0) {
				ret = sb.substring(0, sb.lastIndexOf(","));
			}
			ret += "]";
			outputJson(ret);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 
	 * 描述 : 保存检修设备详细信息
	 * 作者 : wangyun
	 * 时间 : Aug 17, 2010
	 * 异常 : Exception
	 */
	public String saveOrUpdateAction() throws Exception {
		String id = request.getParameter("id");
		String zyId = request.getParameter("zyId");
		String sbmc = request.getParameter("sbmc");
		String jxzq = request.getParameter("jxzq");
		String xmxh = request.getParameter("xmxh");
		String xmjb = request.getParameter("xmjb");
		
		try {
			Jxsbtz jxsbtz = null;
			if (StringUtil.isEmpty(id)) {
				jxsbtz = new Jxsbtz();
				jxsbtz.setSszy(zyId);
				int maxNo = jxsbtzManager.findMaxSortNoBySszy(zyId);
				jxsbtz.setSortNo(maxNo + 1);
			} else {
				jxsbtz = jxsbtzManager.get(id);
			}
			
			jxsbtz.setXmjb(xmjb);
			jxsbtz.setSbmc(sbmc);
			jxsbtz.setJxzq(jxzq);
			jxsbtz.setXmxh(xmxh);
			
			jxsbtzManager.save(jxsbtz);
			outputJson("{success:true}");
		} catch (Exception e) {
			outputJson("{success:false,msg:'数据库异常，请联系管理员'}");
			e.printStackTrace();
		}
		return NONE;
	}

	@Override
	protected void beforeShowList(HttpServletRequest request, HttpServletResponse response, StringBuffer hql) {
		String sszy = request.getParameter("sszy");
		if (StringUtil.isNotEmpty(sszy)) {
			HqlUtil.addCondition(hql, "sszy", sszy);
		}
		String hqlWhere = request.getParameter("queryParamsSql");
		if (StringUtil.isNotEmpty(hqlWhere)) {
			hqlWhere = hqlWhere.replaceAll("@", "#");
			HqlUtil.addWholeCondition(hql, hqlWhere);
		}
		HqlUtil.addOrder(hql, "sortNo", "asc");
	}

	@Override
	public HibernateEntityDao getManager() {
		return jxsbtzManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] {"id", "sszy", "sbmc", "jxzq", "xmjb", "xmxh", "sortNo"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] {"id", "sszy", "sbmc", "jxzq", "xmjb", "xmxh", "sortNo"};
	}

	public JxsbtzManager getJxsbtzManager() {
		return jxsbtzManager;
	}

	public void setJxsbtzManager(JxsbtzManager jxsbtzManager) {
		this.jxsbtzManager = jxsbtzManager;
	}

	public DictManager getDictManager() {
		return dictManager;
	}

	public void setDictManager(DictManager dictManager) {
		this.dictManager = dictManager;
	}

}
