package com.jteap.yx.aqyxfx.web;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.yx.aqyxfx.manager.BkhdlCxManager;
import com.jteap.yx.aqyxfx.manager.YyqkfxManager;
/**
 * 用油情况分析 Action
 * @author lvchao
 *
 */

@SuppressWarnings({"serial","unchecked"})
public class YyqkfxAction extends AbstractAction{
	
	private YyqkfxManager yyqkfxManager;
	private BkhdlCxManager bkhdlcxManager;
	
	@Override
	public HibernateEntityDao getManager() {
		// TODO Auto-generated method stub
		return yyqkfxManager;
	}
	@Override
	public String showListAction() throws Exception {
		try {
			String limit = request.getParameter("limit");
			if (StringUtils.isEmpty(limit))
				limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

			// 开始索引
			String start = request.getParameter("start");
			if (StringUtils.isEmpty(start))
				start = "0";

			int iStart = Integer.parseInt(start);
			int iLimit = Integer.parseInt(limit);
			String sql = "select * from TB_FORM_SJBZB_YYFXB  a where 1=1";
			// 添加查询条件
			String sqlWhere = request.getParameter("queryParamsSql");
			if (StringUtils.isNotEmpty(sqlWhere)) {
				String hqlWhereTemp = sqlWhere.replace("$", "%");
				sql += " and " + hqlWhereTemp;
			}
			
			Page page = bkhdlcxManager.pagedQueryTableData(sql, iStart, iLimit);
			String json = JSONUtil.listToJson((List) page.getResult(), listJsonProperties());
			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json + "}";
			outputJson(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	/**
	 * 根据统计期 显示用油情况分析信息
	 * @return
	 * @throws Exception
	 */
	public String showYyqkfxAction()throws Exception{
		String ksrq = request.getParameter("ksrq");
		String jsrq = request.getParameter("jsrq");
		String json = "";
		Map map = yyqkfxManager.findYyqxfx(ksrq,jsrq);
		if(map!=null&&map.size()>1){
			json = "{success:true,data:["+JSONUtil.mapToJson(map)+"]}";
		}else{
			json = "{success:false,msg:'无此统计期的用油情况数据'}";
		}
		this.outputJson(json);
		return NONE;
	}
	
	/**
	 * 根据开始时间 结束时间 检查数据库中是否已经存在
	 * @return
	 * @throws Exception 
	 * @throws Exception
	 */
	public String checkYyqkfxAction() throws Exception{
		String ksrq = request.getParameter("ksrq");
		String jsrq = request.getParameter("jsrq");
		boolean check = yyqkfxManager.checkYyqkfx(ksrq,jsrq);
		String	json = "{success:"+check+"}";
		this.outputJson(json);
		return NONE;
	}
	@Override
	public String[] listJsonProperties() {
		// TODO Auto-generated method stub
		return new String[] {"ID","TJR","KSRQ","JSRQ"};
	}
	
	@Override
	public String[] updateJsonProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	public YyqkfxManager getYyqkfxManager() {
		return yyqkfxManager;
	}

	public void setYyqkfxManager(YyqkfxManager yyqkfxManager) {
		this.yyqkfxManager = yyqkfxManager;
	}
	public BkhdlCxManager getBkhdlcxManager() {
		return bkhdlcxManager;
	}
	public void setBkhdlcxManager(BkhdlCxManager bkhdlcxManager) {
		this.bkhdlcxManager = bkhdlcxManager;
	}
	 
}
