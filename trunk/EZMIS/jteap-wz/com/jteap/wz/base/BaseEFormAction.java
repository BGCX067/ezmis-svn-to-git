package com.jteap.wz.base;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.support.Page;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.form.dbdef.manager.DefColumnInfoManager;
import com.jteap.form.dbdef.manager.DefTableInfoManager;
import com.jteap.form.dbdef.manager.PhysicTableManager;
import com.jteap.form.dbdef.model.DefColumnInfo;
import com.jteap.form.dbdef.model.DefColumnType;
import com.jteap.form.dbdef.model.DefTableInfo;
import com.jteap.form.eform.manager.EFormManager;
import com.jteap.form.eform.model.EForm;
/**
 * 用于EForm表单在增删改查时的特殊操作，继承该类，重写父类方法即可
 * */
@SuppressWarnings("unchecked")
public abstract class BaseEFormAction  extends AbstractAction{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1691686315007594311L;

	private EFormManager eformManager;
	
	private PhysicTableManager physicTableManager;
	
	private DefTableInfoManager defTableInfoManager;
	
	private DefColumnInfoManager defColumnInfoManager;
	
	/**
	 * 根据指定的表单，找到对应的物理表，并将该物理表中的数据列出来
	 * @return
	 * @throws Exception 
	 */
	
	public String showEFormRecListAction() throws Exception{
		String formSn = request.getParameter("formSn");
		EForm eform = eformManager.getEFormBySn(formSn);
		
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");
		
		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "1";
		
		int iStart=Integer.parseInt(start);
		int iLimit=Integer.parseInt(limit);
		try{
		
			DefTableInfo tableInfo=eform.getDefTable();
			Collection<DefColumnInfo> cols=tableInfo.getColumns();
			StringBuffer selectColSb=new StringBuffer();
			for (DefColumnInfo defColumnInfo : cols) {
				if(defColumnInfo.getColumntype().equals(DefColumnType.CTYPE_BLOB) ||defColumnInfo.getColumntype().equals(DefColumnType.CTYPE_CLOB)){
					continue;
				}
				if(defColumnInfo.getColumntype().equals(DefColumnType.CTYPE_DATE)){
					selectColSb.append("to_char("+defColumnInfo.getColumncode()+",'YYYY-MM-DD') as "+defColumnInfo.getColumncode()+",");
				}else if(defColumnInfo.getColumntype().indexOf(DefColumnType.CTYPE_TIMESTAMP)>=0){
					selectColSb.append("to_char("+defColumnInfo.getColumncode()+",'YYYY-MM-DD hh24:mi:ss') as "+defColumnInfo.getColumncode()+",");
				}else{
					selectColSb.append(defColumnInfo.getColumncode()+",");
				}
			}
			if(selectColSb.length()>0)
				selectColSb.deleteCharAt(selectColSb.length()-1);
			
			
			String orderBy = request.getParameter("sort");
			String tmpOrder="";
			if (StringUtils.isNotEmpty(orderBy)) {
				String dir = request.getParameter("dir");
				if (StringUtils.isEmpty(dir))
					dir = "asc";
				tmpOrder="ORDER BY "+orderBy+" "+dir+"";
			}
			
			String sql="SELECT "+selectColSb.toString()+" FROM "+tableInfo.getSchema()+"."+tableInfo.getTableCode()+" "+ tmpOrder;
			Page page =physicTableManager.pagedQueryTableData(sql, iStart, iLimit);
			
			
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
	 * 根据自定义表单formSn和记录ID删除自定义表单记录
	 * @return
	 * @throws Exception 
	 */
	public String delEFormRecAction() throws Exception{
		String formSn = request.getParameter("formSn");
		String ids = request.getParameter("ids");
		try{
			EForm eform = eformManager.getEFormBySn(formSn);
			DefTableInfo tableInfo=eform.getDefTable();
			StringBuffer idsSB = new StringBuffer();
			String[] tmpIds = ids.split(",");
			for (String id : tmpIds) {
				idsSB.append("'"+id+"',");
			}
			if(idsSB.length()>0){
				idsSB = idsSB.deleteCharAt(idsSB.length()-1);
			}
			
			String schema = SystemConfig.getProperty("jdbc.schema");
			String pk = physicTableManager.findPrimaryKeyColumnNameList(schema, tableInfo.getTableCode()).get(0);
			String sql = "DELETE "+schema+"."+tableInfo.getTableCode()+" WHERE "+pk+" IN ("+idsSB+")";
			log.info(sql);
			physicTableManager.executeSqlBatch(sql);
			this.outputJson("{success:true}");
		}catch(Exception ex){
			ex.printStackTrace();
			this.outputJson("{success:false}");
		}
		return NONE;
	}
	
	
	
	

	/**
	 * 保存字段信息,并保存表的修改记录.
	 * @author caihuiwen.
	 * @return
	 * @throws Exception
	 */
	public String saveColumnInfoAction()throws Exception{
		try {
			DefColumnInfo defColumnInfo = null;
			String modifyType = null;
			String beforColumnCode = request.getParameter("beforcolumncode");
			String id = request.getParameter("id");
			String tableid = request.getParameter("tableid");
			String columnlength = request.getParameter("columnlength");
			String columnorder = request.getParameter("columnorder");
			String formSn = request.getParameter("formSn");
			
			if(StringUtil.isNotEmpty(id)){
				defColumnInfo = defColumnInfoManager.get(id);
				modifyType = "alter";
			}else {
				defColumnInfo = new DefColumnInfo();
				modifyType = "add";
			}
			
			if (StringUtil.isNotEmpty(tableid)) {
				defColumnInfo.setTable(defTableInfoManager.get(tableid));
			}
			if (StringUtil.isNotEmpty(columnlength)) {
				defColumnInfo.setColumnlength(Integer.parseInt(columnlength));
			}
			if (StringUtil.isNotEmpty(columnorder)) {
				defColumnInfo.setColumnorder(Long.parseLong(columnorder));
			}
			
			String allownull = request.getParameter("allownull");
			if(StringUtil.isNotEmpty(allownull)){
				if("on".equals(allownull)){
					defColumnInfo.setAllownull(true);
				}else {
					defColumnInfo.setAllownull(Boolean.parseBoolean(allownull));
				}
			}else{
				defColumnInfo.setAllownull(false);
			}
			String pk = request.getParameter("pk");
			if(StringUtil.isNotEmpty(pk)){
				if("on".equals(pk)){
					defColumnInfo.setPk(true);
				}else{
					defColumnInfo.setPk(Boolean.parseBoolean(pk));
				}
			}else{
				defColumnInfo.setPk(false);
			}
			
			defColumnInfo.setColumntype(request.getParameter("columntype"));
			defColumnInfo.setColumncode(request.getParameter("columncode"));
			defColumnInfo.setColumnname(request.getParameter("columnname"));
			defColumnInfo.setDefaultvalue(request.getParameter("defaultvalue"));
			defColumnInfo.setFormat(request.getParameter("format"));
			defColumnInfo.setComm(request.getParameter("comm"));
			String columnprec = request.getParameter("columnprec");
			if (StringUtil.isNotEmpty(columnprec)) {
				defColumnInfo.setColumnprec(Integer.parseInt(columnprec));
			}
			
			//唯一键
			boolean unique = false;
			if(StringUtil.isNotEmpty(request.getParameter("chkUnique"))){
				unique = Boolean.parseBoolean(request.getParameter("chkUnique"));
			}
			
			//保存 对应表的修改记录
			defTableInfoManager.modifyRecByDefColumnInfoChange(defColumnInfo, modifyType, beforColumnCode, null, unique);
			//保存字段信息
			defColumnInfoManager.save(defColumnInfo);
			
			//将关联到此数据定义的 表单定稿设为[未定稿]
			EForm eForm = eformManager.getEFormBySn(formSn);
			if(null != eForm){
				eForm.setFinalManuscript(false);
				eformManager.save(eForm);
			}
			
			this.outputJson("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}

}
