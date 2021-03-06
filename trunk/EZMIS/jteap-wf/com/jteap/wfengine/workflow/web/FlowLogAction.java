package com.jteap.wfengine.workflow.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.WebUtils;
import com.jteap.core.web.AbstractAction;
import com.jteap.wfengine.workflow.manager.WorkFlowLogManager;


/**
 * 日志信息管理类
 * @author 朱启亮
 * @date 2008-1-15
 */
@SuppressWarnings({"serial","unchecked"})
public class FlowLogAction extends AbstractAction{
	private WorkFlowLogManager workFlowLogManager;
	
	
	public WorkFlowLogManager getWorkFlowLogManager() {
		return workFlowLogManager;
	}

	public void setWorkFlowLogManager(WorkFlowLogManager workFlowLogManager) {
		this.workFlowLogManager = workFlowLogManager;
	}

	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		String pid=request.getParameter("pid");//流程实例编号
		HqlUtil.addCondition(hql, "pi_id",pid);
		super.beforeShowList(request, response, hql);
		
		//查询条件
		String hqlWhere = WebUtils.getRequestParam(request, "queryParamsSql");
		//在一类型日志中查询
		if(StringUtils.isNotEmpty(hqlWhere)){
			String hqlWhereTemp = hqlWhere.replace("$", "%");
			HqlUtil.addWholeCondition(hql, hqlWhereTemp);
		}
	}

	@Override
	public HibernateEntityDao getManager() {
		return workFlowLogManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{"id","pi_id","taskName","taskActor","time","taskResult","nextTaksName","nextTaksActor","porcessDate","remark"};
	}

	@Override
	public String[] updateJsonProperties() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
