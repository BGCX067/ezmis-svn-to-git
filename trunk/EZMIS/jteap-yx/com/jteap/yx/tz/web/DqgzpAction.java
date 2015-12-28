/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
package com.jteap.yx.tz.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.system.person.model.P2Role;
import com.jteap.system.person.model.Person;
import com.jteap.system.role.manager.RoleManager;
import com.jteap.system.role.model.Role;
import com.jteap.yx.tz.manager.DqgzpManager;
import com.jteap.yx.tz.model.Dqgzp;

/**
 * 电气工作票Action
 * 
 * @author wangyun
 * 
 */
@SuppressWarnings( { "unused", "unchecked", "serial" })
public class DqgzpAction extends AbstractAction {

	private DqgzpManager dqgzpManager;
	private RoleManager roleManager;

	@Override
	protected void beforeShowList(HttpServletRequest request, HttpServletResponse response, StringBuffer hql) {
		String hqlWhere = request.getParameter("queryParamsSql");
		if (StringUtils.isNotEmpty(hqlWhere)) {
			String hqlWhereTemp = hqlWhere.replace("$", "%");
			HqlUtil.addWholeCondition(hql, hqlWhereTemp);
		}
		String type = request.getParameter("type");
		if (StringUtil.isNotEmpty(type)) {
			HqlUtil.addCondition(hql, "type", type);
		}
	}

	public String saveOrUpdateAction() throws Exception {
		String id = request.getParameter("id");
		String gzpbh = request.getParameter("gzpbh");
		String gzpzt = request.getParameter("gzpzt");
		String ddjrw = request.getParameter("ddjrw");
		String gzfzr = request.getParameter("gzfzr");
		String gzbry = request.getParameter("gzbry");
		String gzpdjr = request.getParameter("gzpdjr");
		String xkr = request.getParameter("xkr");
		String gzpzjr = request.getParameter("gzpzjr");
		String gzpzfr = request.getParameter("gzpzfr");
		String zjjd = request.getParameter("zjjd");
		String zjjcqk = request.getParameter("zjjcqk");
		String spr = request.getParameter("spr");
		String pzzz = request.getParameter("pzzz");
		String pzyqzz = request.getParameter("pzyqzz");
		String zfyy = request.getParameter("zfyy");
		String pzzzmc = request.getParameter("pzzzmc");
		String xkrmc = request.getParameter("xkrmc");
		String zjrmc = request.getParameter("zjrmc");
		String pzyqzzmc = request.getParameter("pzyqzzmc");
		String zfrmc = request.getParameter("zfrmc");
		String strspsj = request.getParameter("spsj");
		String strpzgzqx = request.getParameter("pzgzqx");
		String stryqsj = request.getParameter("yqsj");
		String stryqsxsj = request.getParameter("yqsxsj");
		String strzjsj = request.getParameter("zjsj");
		String strzfsj = request.getParameter("zfsj");
		String strxksj = request.getParameter("xksj");
		String type = request.getParameter("type");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

		try {
			Dqgzp dqgzp = null;
			if (StringUtil.isNotEmpty(id)) {
				dqgzp = dqgzpManager.get(id);
			} else {
				dqgzp = new Dqgzp();
			}

			dqgzp.setGzpbh(gzpbh);
			dqgzp.setGzpzt(gzpzt);
			dqgzp.setDdjrw(ddjrw);
			dqgzp.setGzfzr(gzfzr);
			dqgzp.setGzbry(gzbry);
			dqgzp.setGzpdjr(gzpdjr);
			dqgzp.setXkr(xkr);
			dqgzp.setGzpzjr(gzpzjr);
			dqgzp.setGzpzfr(gzpzfr);
			dqgzp.setZjjd(zjjd);
			dqgzp.setZjjcqk(zjjcqk);
			dqgzp.setSpr(spr);
			dqgzp.setPzzz(pzzz);
			dqgzp.setPzyqzz(pzyqzz);
			dqgzp.setZfyy(zfyy);
			dqgzp.setPzzzmc(pzzzmc);
			dqgzp.setXkrmc(xkrmc);
			dqgzp.setZjrmc(zjrmc);
			dqgzp.setPzyqzzmc(pzyqzzmc);
			dqgzp.setZfrmc(zfrmc);
			dqgzp.setType(type);

			if (StringUtil.isNotEmpty(strspsj)) {
				Date spsj = sdf.parse(strspsj);
				dqgzp.setSpsj(spsj);
			}
			if (StringUtil.isNotEmpty(strxksj)) {
				Date xksj = sdf.parse(strxksj);
				dqgzp.setXksj(xksj);
			}
			if (StringUtil.isNotEmpty(strpzgzqx)) {
				Date pzgzqx = sdf.parse(strpzgzqx);
				dqgzp.setPzgzqx(pzgzqx);
			}
			if (StringUtil.isNotEmpty(stryqsj)) {
				Date yqsj = sdf.parse(stryqsj);
				dqgzp.setYqsj(yqsj);
			}
			if (StringUtil.isNotEmpty(stryqsxsj)) {
				Date yqsxsj = sdf.parse(stryqsxsj);
				dqgzp.setYqsxsj(yqsxsj);
			}
			if (StringUtil.isNotEmpty(strzjsj)) {
				Date zjsj = sdf.parse(strzjsj);
				dqgzp.setZjsj(zjsj);
			}
			if (StringUtil.isNotEmpty(strzfsj)) {
				Date zfsj = sdf.parse(strzfsj);
				dqgzp.setZfsj(zfsj);
			}

			dqgzpManager.save(dqgzp);
			id = dqgzp.getId();
		} catch (Exception e) {
			e.printStackTrace();
			outputJson("{success:false}");
		}
		outputJson("{success:true,id:'"+id+"'}");
		return NONE;
	}

	/**
	 * 
	 * 描述 : 通过角色内码获得该角色内所有人员名单 作者 : wangyun 时间 : 2010-12-6 异常 : Exception
	 * 
	 */
	public String getGxRoleListAction() throws Exception {
		String roleSn = request.getParameter("roleSn");

		Role role = roleManager.findUniqueBy("roleSn", roleSn);
		Set<P2Role> p2roles = role.getPersons();
		List<Map> lst = new ArrayList<Map>();
		for (P2Role p2role : p2roles) {
			Map<String, String> map = new HashMap<String, String>();
			Person person = p2role.getPerson();
			String id = (String) person.getId();
			String name = person.getUserName();
			map.put("id", id);
			map.put("name", name);
			lst.add(map);
		}
		String json = JSONUtil.listToJson(lst);
		outputJson(json);
		return NONE;
	}

	public DqgzpManager getDqgzpManager() {
		return dqgzpManager;
	}

	public void setDqgzpManager(DqgzpManager dqgzpManager) {
		this.dqgzpManager = dqgzpManager;
	}

	@Override
	public HibernateEntityDao getManager() {
		return dqgzpManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] { "id", "gzpbh", "gzpzt", "ddjrw", "gzfzr", "gzbry", "gzpdjr", "spsj", "xksj", "gzpzjr",
				"zjsj", "gzpzfr", "zfsj", "zjjd", "spr", "pzgzqx", "pzzz", "xkr", "yqsj", "pzyqzz", "yqsxsj", "zfyy",
				"zjjcqk","time", "pzzzmc", "xkrmc", "zjrmc", "pzyqzzmc", "zfrmc"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "id", "gzpbh", "gzpzt", "ddjrw", "gzfzr", "gzbry", "gzpdjr", "spsj", "xksj", "gzpzjr",
				"zjsj", "gzpzfr", "zfsj", "zjjd", "spr", "pzgzqx", "pzzz", "xkr", "yqsj", "pzyqzz", "yqsxsj", "zfyy",
				"zjjcqk","time", "pzzzmc", "xkrmc", "zjrmc", "pzyqzzmc", "zfrmc"};
	}

	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

}
