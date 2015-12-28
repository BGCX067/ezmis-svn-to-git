package com.jteap.system.log.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.system.log.manager.LogManager;


/**
 * 日志信息管理类
 * @author 朱启亮
 * @date 2008-1-15
 */
@SuppressWarnings({"serial","unchecked"})
public class LogAction extends AbstractAction{
	//日志管理器
	private LogManager logManager;
	
	/**
	 * 返回JSON字符串数组
	 * @return
	 * @author 朱启亮
	 * @date 2008-1-15
	 */
	public String[] listJsonProperties(){
		return new String[]{"id","dt","log","type","personName","personLoginName","time","ip"};
	}

//	
//	@Override
//	protected String getPageBaseHqlExt() {
//		return "select new com.jteap.system.log.model.Log(id,dt,type,personLoginName,personName,ip) from "
//		+ getManager().getEntityClass().getName() + " as obj";
//	}


	/**
	 * 列表之前的预处理,取出某类型的日志
	 * @param request
	 * @param response
	 * @param hql
	 * @author 朱启亮
	 * @date 2008-1-15
	 */
	public void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql){
		try{
			String type = request.getParameter("type");
			String hqlWhere = StringUtil.isoToUTF8(request.getParameter("queryParamsSql"));
			
			//取一类型的日志
			if(StringUtils.isNotEmpty(type)){
				HqlUtil.addCondition(hql, "type",type);
			}
			//在一类型日志中查询
			if(StringUtils.isNotEmpty(hqlWhere)){
				String hqlWhereTemp = hqlWhere.replace("$", "%");
				HqlUtil.addWholeCondition(hql, hqlWhereTemp);
			}
		}catch(Exception ex){
			throw new BusinessException(ex);
		}
	}

	/**
	 * 修改对象的时候需要序列化的字段
	 * @return
	 * @author 朱启亮
	 * @date 2008-1-15
	 */
	public String[] updateJsonProperties() {
		return null;
	}	
	
	/**
	 * 删除某一类型的日志动作
	 * @return
	 * @author 朱启亮
	 * @date 2008-1-15
	 */
	public String removeLogsByType(){
		try{
			String type = request.getParameter("type");
			if(type!=null&&!type.equals("")){
				logManager.removeLogsByType(type);
				outputJson("{success:true}");
			}
		}catch(Exception ex){
			throw new BusinessException(ex);
		}
		return NONE;
	}

	/***************************get××× and set××× method开始***************************/
	public LogManager getLogManager() {
		return logManager;
	}	
	public void setLogManager(LogManager logManager) {
		this.logManager = logManager;
	}
	public  HibernateEntityDao getManager(){
		return logManager;
	}
	/***************************get××× and set××× method结束***************************/
}
