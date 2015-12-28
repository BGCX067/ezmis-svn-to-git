package com.jteap.yx.aqyxfx.web;


import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.yx.aqyxfx.manager.KkxsjManager;
import com.jteap.yx.aqyxfx.model.Zb;
@SuppressWarnings({"serial","unchecked"})
public class KkxsjAction extends AbstractAction {
	private KkxsjManager kkxsjManager;
	@Override
	public HibernateEntityDao getManager() {
		return null;
	}
	
	public String showFjhtyDataAction() throws Exception{
		String tableName=request.getParameter("tableName");
		String ksrq=request.getParameter("ksrq");
		String jsrq=request.getParameter("jsrq");
		List<Zb> zbList=kkxsjManager.showFjhtyDataByRq(tableName,ksrq,jsrq);
		String json=JSONUtil.listToJson(zbList,this.listJsonProperties());
		outputJson("{success:true,data:["+json+"]}");
		return NONE;
	}
	
	
	@Override
	public String showListAction() throws Exception {
		// 每页大小
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "0";
		String sql="select * from TB_FORM_SJBZB_FXB where xz='强迫停运' or xz='考核非停' order by txsj desc";
		// 开始分页查询
		Page page = this.kkxsjManager.pagedQueryTableData(sql, Integer.parseInt(start), Integer.parseInt(limit));
		List<Map> obj = (List<Map>) page.getResult();
		
		String json=JSONUtil.listToJson(obj);
		long totalCount=page.getTotalCount();
		System.out.println(json);
		StringBuffer dataBlock = new StringBuffer();
		dataBlock.append("{totalCount:'" + totalCount + "',list:"
				+ json + "}");
		this.outputJson(dataBlock.toString());
		return NONE;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{"cs","xs1","xs2","xs3","xs4","ftcs","ftxs1","ftxs2","ftxs3","ftxs4","csbfb","xsbfb"};
	}

	@Override
	public String[] updateJsonProperties() {
		return null;
	}

	public KkxsjManager getKkxsjManager() {
		return kkxsjManager;
	}

	public void setKkxsjManager(KkxsjManager kkxsjManager) {
		this.kkxsjManager = kkxsjManager;
	}

}
