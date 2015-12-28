package com.jteap.system.jdbc.web;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.system.jdbc.manager.JdbcManager;
import com.jteap.system.jdbc.model.TableInfo;

/**
 * 数据库物理表处理动作对象
 * @author tanchang
 *
 */
@SuppressWarnings({ "unchecked", "serial" })
public class JdbcAction extends AbstractTreeAction<TableInfo>{
	private JdbcManager jdbcManager;


	public JdbcManager getJdbcManager() {
		return jdbcManager;
	}

	public void setJdbcManager(JdbcManager jdbcManager) {
		this.jdbcManager = jdbcManager;
	}

	@Override
	protected Collection getChildren(Object bean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getParentPropertyName(Class beanClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Collection getRootObjects() throws Exception {
		String key=request.getParameter("key");
		List list=jdbcManager.findTableList(key);
		return list;
	}

	@Override
	protected String getSortNoPropertyName(Class beanClass) {
		return "fullName";
	}

	@Override
	protected String getTextPropertyName(Class beanClass) {
		return "fullName";
	}

	@Override
	public HibernateEntityDao getManager() {
	
		return null;
	}

	@Override
	public String[] listJsonProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] updateJsonProperties() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 显示指定表数据
	 * @return
	 * @throws Exception 
	 */
	public String showTableDataBySqlAction() throws Exception{
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");
		
		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "0";
		
		int iStart=Integer.parseInt(start);
		int iLimit=Integer.parseInt(limit);
		try{
			String sql= request.getParameter("sql");
			Page page =jdbcManager.pagedQueryTableData(sql, iStart, iLimit);
			String json=JSONUtil.listToJson((List)page.getResult());
			json="{totalCount:'" + page.getTotalCount() + "',list:"+ json + "}";
			this.outputJson(json);
		}catch(Exception ex){
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}
	
	
	/**
	 * 执行指定的SQL语句并以JSON字符串的方式返回结果集
	 * @return
	 * @throws Exception 
	 */
	public String doQueryBySqlAction() throws Exception{
		try{
			String sql= request.getParameter("sql");
			List list = jdbcManager.querySqlData(sql);
			String json=JSONUtil.listToJson(list);
			json="{success:true,list:"+ json + "}";
			this.outputJson(json);
		}catch(Exception ex){
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}
}
