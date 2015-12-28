/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.dqgzgl.web;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.P2G;
import com.jteap.system.person.model.P2Role;
import com.jteap.system.person.model.Person;
import com.jteap.wfengine.tasktodo.manager.TaskToDoManager;
import com.jteap.yx.dqgzgl.manager.DqgzHandleManager;
import com.jteap.yx.dqgzgl.model.DqgzHandle;

/**
 * 定期工作处理Action
 * @author caihuiwen
 */
@SuppressWarnings("serial")
public class DqgzHanleAction extends AbstractAction{
	
	private DqgzHandleManager dqgzHandleManager;
	private PersonManager personManager;
	private TaskToDoManager taskToDoManager;
	
	public void setTaskToDoManager(TaskToDoManager taskToDoManager) {
		this.taskToDoManager = taskToDoManager;
	}

	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}

	public void setDqgzHandleManager(DqgzHandleManager dqgzHandleManager) {
		this.dqgzHandleManager = dqgzHandleManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HibernateEntityDao getManager() {
		return dqgzHandleManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{
			"id","dqgzSetId","fzbm","fzgw","gzgl","dqgzzy",
			"bc","dqgzMc","dqgzNr","dqgzFl","dqgzCreateDt",
			"chuliRen","chuliNr","chuliDt","status","time"
		};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"id","dqgzSetId","fzbm","fzgw","gzgl","dqgzzy",
			"bc","dqgzMc","dqgzNr","dqgzFl","dqgzCreateDt",
			"chuliRen","chuliNr","chuliDt","status","time"
		};
	}

	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		String hqlWhere = request.getParameter("queryParamsSql");
		
		//获取当前登录用户
		Person person = personManager.getCurrentPerson(this.sessionAttrs);
		//不是root用户, 获取当前登录人员的 部门、岗位信息, 根据部门、岗位来筛选查询
		if(!person.isRootPerson()){
			
			//用户所属部门
			Set<P2G> groupSet = person.getGroups();
			int groupIndex = 0;
			for(Iterator<P2G> iterator=groupSet.iterator(); iterator.hasNext();){
				P2G p2g = iterator.next();
				if(groupIndex == 0){
					hqlWhere += " obj.fzbm='" + p2g.getGroup().getGroupName() + "' and ";
				}else {
					hqlWhere += " obj.fzbm='" + p2g.getGroup().getGroupName() + "' or  ";
				}
				groupIndex++;
			}
			hqlWhere = hqlWhere.substring(0,hqlWhere.length()-5);
			
			//用户所属岗位
			Set<P2Role> roleSet = person.getRoles();
			int roleIndex = 0;
			for(Iterator<P2Role> iterator=roleSet.iterator(); iterator.hasNext();){
				P2Role p2Role = iterator.next();
				if(roleIndex == 0){
					hqlWhere += " obj.fzgw='" + p2Role.getRole().getRolename() + "' and ";
				}else {
					hqlWhere += " obj.fzgw='" + p2Role.getRole().getRolename() + "' or  ";
				}
			}
			hqlWhere = hqlWhere.substring(0,hqlWhere.length()-5);
			
			if(hqlWhere == null || hqlWhere == ""){
				hqlWhere = "?queryParamsSql=" + hqlWhere;
			}
		}
		
		if(StringUtils.isNotEmpty(hqlWhere)){
			String hqlWhereTemp = hqlWhere.replace("$", " ");
			HqlUtil.addWholeCondition(hql, hqlWhereTemp);
		}
		
		HqlUtil.addOrder(hql, "obj.dqgzCreateDt", "DESC");
	}
	
	/**
	 * 保存定期工作处理
	 * @return
	 */
	public String saveAction(){
		try {
			String id = request.getParameter("id");
			//处理时间
			String chuliDt = request.getParameter("chuliDt");
			//处理人
			String chuliRen = request.getParameter("chuliRen");
			//处理内容
			String chuliNr = request.getParameter("chuliNr");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			DqgzHandle dqgzHandle = dqgzHandleManager.get(id);
			dqgzHandle.setChuliDt(dateFormat.parse(chuliDt));
			dqgzHandle.setChuliRen(chuliRen);
			dqgzHandle.setChuliNr(chuliNr);
			dqgzHandle.setStatus("已完成");
			
			//待办事项Id
			String taskId = dqgzHandle.getTaskId();
			//完成人
			String dealPeson = request.getParameter("curPersonLoginName");
			//弹出窗口url (改变参数)
			String alertWindowUrl = "url=/jteap/yx/dqgzgl/handle/handle.jsp?wancheng=wancheng";
			//保存待办事项
			if(taskId != null && taskId != ""){
				taskToDoManager.saveDeals(taskId, false, dqgzHandle.getStatus(), alertWindowUrl, dealPeson, dqgzHandle.getChuliDt());
			}
			
			this.outputJson("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 根据专业 显示详细统计报表
	 * @return
	 */
	/*@SuppressWarnings("unchecked")
	public String showDetailTongJi(){
		try {
			// 每页大小
			String limit = request.getParameter("limit");
			if (StringUtils.isEmpty(limit))
				limit = Constants.PAGE_DEFAULT_LIMIT;
	
			// 开始索引
			String start = request.getParameter("start");
			if (StringUtils.isEmpty(start))
				start = "0";
			
			String dqgzzy = request.getParameter("dqgzzy"); 
			String hql = "from DqgzHandle d where d.dqgzzy='" + dqgzzy + "'";
			List<DqgzHandle> list = dqgzHandleManager.find(hql);
			
			//分页
			int startIndex = Integer.parseInt(start);
			int limitIndex = Integer.parseInt(limit) + startIndex;
			if(limitIndex > list.size()){
				limitIndex = list.size();
			}
			List<DqgzHandle> pageList = list.subList(startIndex, limitIndex);
			
			String json = JSONUtil.listToJson(pageList, listJsonProperties());
			json = "{totalCount:'" + list.size() + "',list:"+ json + "}";
			
			this.outputJson(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}*/
	
}
