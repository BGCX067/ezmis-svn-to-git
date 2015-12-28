/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.fdzbfxykh.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.yx.fdzbfxykh.manager.DirectiveColumnInfoManager;
import com.jteap.yx.fdzbfxykh.manager.DirectiveTableInfoManager;
import com.jteap.yx.fdzbfxykh.model.DirectiveColumnInfo;
import com.jteap.yx.fdzbfxykh.model.DirectiveTableInfo;

/**
 * 小指标字段定义Action
 * @author caihuiwen
 */
@SuppressWarnings({"serial","unchecked"})
public class DirectiveColumnInfoAction extends AbstractAction {
	
	private DirectiveColumnInfoManager directiveColumnInfoManager;
	private DirectiveTableInfoManager directiveTableInfoManager;

	public void setDirectiveTableInfoManager(
			DirectiveTableInfoManager directiveTableInfoManager) {
		this.directiveTableInfoManager = directiveTableInfoManager;
	}

	public void setDirectiveColumnInfoManager(DirectiveColumnInfoManager directiveColumnInfoManager) {
		this.directiveColumnInfoManager = directiveColumnInfoManager;
	}
	
	@Override
	public HibernateEntityDao getManager() {
		return directiveColumnInfoManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{
			"id","directiveId","directiveCode","directiveName","sumOrAvg","dataTable","sisCedianbianma","remark","sortno"
		};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"id","directiveId","directiveCode","directiveName","sumOrAvg","dataTable","sisCedianbianma","remark","sortno"
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
		
		// 添加查询条件
		String directiveId = request.getParameter("directiveId");
		if (StringUtils.isNotEmpty(directiveId)) {
			HqlUtil.addCondition(hql, "directiveId", directiveId);
		}
		HqlUtil.addOrder(hql, "obj.sortno", "asc");
	}
	
	/**
	 * 保存小指标字段定义
	 * @return
	 */
	public String saveColumnInfoAction(){
		String id = request.getParameter("id");
		String directiveId = request.getParameter("directiveId");
		//String beforColumnCode = request.getParameter("beforColumnCode");
		//String modifyType = "";
		
		DirectiveColumnInfo columnInfo = new DirectiveColumnInfo();
		if(id != null && !id.equals("")){
			columnInfo.setId(id);
			//modifyType = "alter";
		}else {
//			modifyType = "add";
		}
		columnInfo.setDirectiveId(directiveId);
		columnInfo.setDirectiveCode(request.getParameter("directiveCode"));
		columnInfo.setDirectiveName(request.getParameter("directiveName"));
		columnInfo.setSumOrAvg(request.getParameter("sumOrAvg"));
		columnInfo.setDataTable(request.getParameter("dataTable"));
		columnInfo.setSisCedianbianma(request.getParameter("sisCedianbianma"));
		columnInfo.setRemark(request.getParameter("remark"));
		columnInfo.setSortno(request.getParameter("sortno"));
		directiveColumnInfoManager.save(columnInfo);
		
		/**【取消保存修改记录】 因为小指标取数逻辑改为 显示小指标时,如果ezmis表中没数据 就从sis表中取出数据、保存到ezmis表中;并且执行修改记录SQL易异常*/
		//保存字段的修改记录
//		directiveTableInfoManager.modifyRecByDefColumnInfoChange(directiveId, columnInfo, modifyType, beforColumnCode, null);
		///将该表定义的同步状态设为 未同步  0
		DirectiveTableInfo tableInfo = directiveTableInfoManager.get(columnInfo.getDirectiveId());
		tableInfo.setFinalManuscript(false);
		directiveTableInfoManager.save(tableInfo);
		try {
			this.outputJson("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 删除小指标字段定义
	 * @return
	 */
	public String removeColumnInfoAction(){
		String id = request.getParameter("ids");
		if(StringUtil.isNotEmpty(id)){
			String[] ids = id.split(",");
			DirectiveColumnInfo columnInfo = directiveColumnInfoManager.get(ids[0]);
			//保存 对应表的修改记录
//			directiveTableInfoManager.modifyRecByDefColumnInfoChange(columnInfo.getDirectiveId(),null, "drop", null, ids);
			///将该表定义的同步状态设为 未同步  0
			DirectiveTableInfo tableInfo = directiveTableInfoManager.get(columnInfo.getDirectiveId());
			tableInfo.setFinalManuscript(false);
			directiveTableInfoManager.save(tableInfo);
			//删除
			directiveColumnInfoManager.removeBatch(id.split(","));
			try {
				this.outputJson("{success:true}");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return NONE;
	}
	
}
