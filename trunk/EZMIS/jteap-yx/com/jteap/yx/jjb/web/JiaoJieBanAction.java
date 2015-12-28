/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.jjb.web;

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
import com.jteap.core.utils.MD5;
import com.jteap.core.web.AbstractAction;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.P2Role;
import com.jteap.system.person.model.Person;
import com.jteap.system.role.manager.RoleManager;
import com.jteap.system.role.model.Role;
import com.jteap.yx.jjb.manager.JiaoJieBanManager;
import com.jteap.yx.jjb.model.JiaoJieBan;

/**
 * 交接班Action
 * @author caihuiwen
 */
@SuppressWarnings({"serial", "unchecked"})
public class JiaoJieBanAction extends AbstractAction {
	
	private JiaoJieBanManager jiaoJieBanManager;
	private RoleManager roleManager;
	private PersonManager personManager;
	
	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	public void setJiaoJieBanManager(JiaoJieBanManager jiaoJieBanManager) {
		this.jiaoJieBanManager = jiaoJieBanManager;
	}
	
	@Override
	public HibernateEntityDao getManager() {
		return jiaoJieBanManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{
			"id","jjbsj","jiaobanbc","jiaobanzb","jiaobanr","jiaobanrId",
			"jiebanbc","jiebanzb","jiebanr","jiebanrId","gwlb","doDate","time"
		};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"id","jjbsj","jiaobanbc","jiaobanzb","jiaobanr","jiaobanrId",
			"jiebanbc","jiebanzb","jiebanr","jiebanrId","gwlb","doDate","time"
		};
	}
	
	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		String hqlWhere = request.getParameter("queryParamsSql");
		if(StringUtils.isNotEmpty(hqlWhere)){
			String hqlWhereTemp = hqlWhere.replace("$", " ");
			HqlUtil.addWholeCondition(hql, hqlWhereTemp);
		}
		HqlUtil.addOrder(hql, "doDate", "desc");
	}
	
	/**
	 * 获取交接班人员
	 * @return
	 */
	public String findJiaoJieRenAction(){
		String gwlb = request.getParameter("gwlb");
		if(StringUtils.isEmpty(gwlb)){
			return NONE;
		}
		if("1".equals(gwlb)){
			gwlb = "#1机长";
		}else if("2".equals(gwlb)){
			gwlb = "#2机长";
		}else if("3".equals(gwlb)){
			gwlb = "#3机长";
		}else if("4".equals(gwlb)){
			gwlb = "#4机长";
		}
		
		List<Map> jsonList = new ArrayList<Map>();
		List<Role> list = roleManager.find("from Role r where r.parentRole.rolename=? and r.rolename=?", new Object[]{"运行管理子系统",gwlb});
		if(list.size() > 0){
			Set<P2Role> p2RoleSet = list.get(0).getPersons();
			for(P2Role p2Role : p2RoleSet){
				Map<String, String> jsonMap = new HashMap<String, String>();
				
				Person person = p2Role.getPerson();
				jsonMap.put("id", person.getId().toString());
				jsonMap.put("userName", person.getUserName());
				jsonList.add(jsonMap);
			}
			String json = JSONUtil.listToJson(jsonList);
			
			try {
				this.outputJson(json);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return NONE;
	}
	/**
	 * 通过岗位获取本岗位最后接班人
	 * @return
	 */
	public String getJieBanRenAction(){
		String gwlb = request.getParameter("gwlb");
		StringBuffer hql = new StringBuffer("from JiaoJieBan t ");
		if(StringUtils.isEmpty(gwlb)){
			return NONE;
		}
		
		if("1".equals(gwlb)){
			gwlb = "#1机长";
		}else if("2".equals(gwlb)){
			gwlb = "#2机长";
		}else if("3".equals(gwlb)){
			gwlb = "#3机长";
		}else if("4".equals(gwlb)){
			gwlb = "#4机长";
		}else if(gwlb.indexOf("控") != -1) {
		    gwlb = "电气";
		}
		hql.append(" where t.gwlb = '"+gwlb+"' ");
		hql.append("order by t.doDate desc");
		List<JiaoJieBan> jjbList = jiaoJieBanManager.find(hql.toString());
		String json = "";
		if(jjbList.size()>0){
			Person person = personManager.get(jjbList.get(0).getJiebanrId());
			json = "{success:true,username:'"+person.getUserLoginName2()+"',username2:'"+person.getUserName()+"'}";
			
		}else{
			json = "{success:false}";
		}
		try {
			this.outputJson(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	
	
	/**
	 * 保存交班信息
	 */
	public String saveAction(){
		try {
			//交班人Id
			String jiaobanrId = request.getParameter("jiaobanr");
			//交班人密码
			String jiaobrpass = request.getParameter("jiaobrpass");
			//接班人Id
			String jiebanrId = request.getParameter("jiebanr");
			//接班人密码
			String jiebrpass = request.getParameter("jiebrpass");
			
			Person jiaobanPerson = personManager.get(jiaobanrId);
			Person jiebanPerson = personManager.get(jiebanrId);
			
			MD5 md5 = new MD5();
			//验证交班人密码
			if(!md5.getMD5ofStr(jiaobrpass).equals(jiaobanPerson.getUserPwd())){
				this.outputJson("{success:true,jiaobrpass:false}");
				return NONE;
			}
			//验证接班人密码
			if(!md5.getMD5ofStr(jiebrpass).equals(jiebanPerson.getUserPwd())){
				this.outputJson("{success:true,jiebrpass:false}");
				return NONE;
			}
			
			JiaoJieBan jiaoJieBan = new JiaoJieBan();
			
			String jiaobanren = jiaobanPerson.getUserName();
			String jiebanren = jiebanPerson.getUserName();
			//预备值长
			String jiaobanYz = request.getParameter("jiaobanYz");
			String jiebanYz = request.getParameter("jiebanYz");
			if(StringUtils.isNotEmpty(jiaobanYz)){
				Person jiaobanYzPerson = personManager.get(jiaobanYz);
				jiaobanrId += "," + jiaobanYz;
				jiaobanren += "," + jiaobanYzPerson.getUserName();
			}
			if(StringUtils.isNotEmpty(jiebanYz)){
				Person jiebanYzPerson = personManager.get(jiebanYz);
				jiebanrId += "," + jiebanYz;
				jiebanren += "," + jiebanYzPerson.getUserName();
			}
			
			//交接班时间
			jiaoJieBan.setJjbsj(request.getParameter("jjbsj"));
			//交班班次
			jiaoJieBan.setJiaobanbc(request.getParameter("jiaobanbc"));
			//交班值别
			jiaoJieBan.setJiaobanzb(request.getParameter("jiaobanzb"));
			//交班人
			jiaoJieBan.setJiaobanr(jiaobanren);
			//交班人ID
			jiaoJieBan.setJiaobanrId(jiaobanrId);
			
			//接班班次
			jiaoJieBan.setJiebanbc(request.getParameter("jiebanbc"));
			//接班值别
			jiaoJieBan.setJiebanzb(request.getParameter("jiebanzb"));
			//接班人
			jiaoJieBan.setJiebanr(jiebanren);
			//接班人ID
			jiaoJieBan.setJiebanrId(jiebanrId);
			
			//交班完成时间
			jiaoJieBan.setDoDate(new Date());
			//岗位类别
			jiaoJieBan.setGwlb(request.getParameter("gwlb"));
			jiaoJieBanManager.save(jiaoJieBan);
			
			this.outputJson("{success:true,isright:'yes'}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
}
