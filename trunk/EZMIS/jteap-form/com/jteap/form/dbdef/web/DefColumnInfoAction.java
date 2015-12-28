package com.jteap.form.dbdef.web;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.BeanUtils;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.form.dbdef.manager.DefColumnInfoManager;
import com.jteap.form.dbdef.manager.DefTableInfoManager;
import com.jteap.form.dbdef.model.DefColumnInfo;
import com.jteap.form.dbdef.model.DefTableInfo;

/**
 * 数据字典的Action处理类
 * @author tanchang
 *
 */
@SuppressWarnings({ "unchecked", "serial" })
public class DefColumnInfoAction extends AbstractAction{

	private DefColumnInfoManager defColumnInfoManager;
	private DefTableInfoManager defTableInfoManager;

	public void setDefTableInfoManager(DefTableInfoManager defTableInfoManager) {
		this.defTableInfoManager = defTableInfoManager;
	}

	public void setDefColumnInfoManager(DefColumnInfoManager defColumnInfoManager) {
		this.defColumnInfoManager = defColumnInfoManager;
	}

	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		String tableid=request.getParameter("tableid");
		if(StringUtil.isNotEmpty(tableid)){
			HqlUtil.addCondition(hql,"table.id",tableid);
		}
		//默认给出排序字段
		if(!isHaveSortField())
			HqlUtil.addOrder(hql, "columnorder", "asc");
		//不分页
		request.setAttribute(PAGE_FLAG,PAGE_FLAG_NO);
		
		
	}


	/**
	 * 由于布尔型变量是以checkbox方式提交的，而checkbox在没有check的状态下，是不会提交的，
	 * 所以需要针对布尔型变量进行特殊
	 */
	@Override
	protected void beforeSaveUpdate(HttpServletRequest request,
			HttpServletResponse response, Object obj, Object originalObject) {
		DefColumnInfo column=(DefColumnInfo) obj;
		if(request.getParameter("allownull")==null)
			column.setAllownull(false);
		
		if(request.getParameter("pk")==null)
			column.setPk(false);
		String id = request.getParameter("id");
		if(StringUtil.isEmpty(id)){
			Date d = new Date();
			try {
				BeanUtils.setProperty(obj, "columnorder", d.getTime());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		String tableId = request.getParameter("tableid");
		DefTableInfo table = this.defTableInfoManager.get(tableId);
		column.setTable(table);
	}


	@Override
	public HibernateEntityDao getManager() {
		return defColumnInfoManager;
	}


	@Override
	public String[] listJsonProperties() {
		return new String[]{"id","columncode","format","columnname","columntype","columnlength","defaultvalue","allownull","pk","columnprec","columnorder","comm"};
	}


	@Override
	public String[] updateJsonProperties() {
		return new String[]{"columncode","columnname","format","columntype","columnlength","defaultvalue","allownull","pk","columnprec","columnorder","comm"};
	}
	
	/**
	 * 根据tableid取得该表的所有列定义对象并json化输出
	 * @return
	 * @throws Exception
	 */
	public String getColumnInfoAction() throws Exception{
		String tableid=request.getParameter("tableid");
		DefTableInfo table = defColumnInfoManager.get(DefTableInfo.class,tableid);
		Collection<DefColumnInfo> list=table.getColumns();
		String json = JSONUtil.listToJson(list, listJsonProperties());
		this.outputJson("{success:true,data:"+json+"}");
		return NONE;
		
	}
	
	
	/**
	 * 向上或者向下移动字段,即改变字段的排序号
	 * @author 肖平松
	 * @version Aug 31, 2009
	 * @return
	 * @throws Exception
	 */
	public String moveFieldAction() throws Exception{
		try{
			String[] ids=request.getParameterValues("ids");
			String dir = request.getParameter("dir");
			String table_id = request.getParameter("table_id");
			DefTableInfo table = defColumnInfoManager.get(DefTableInfo.class,table_id);
			Object[] defColumnList= table.getColumns().toArray();
			if(dir.equals("up")){
				for(int i = 0;i < defColumnList.length;i++){
					DefColumnInfo d = (DefColumnInfo) defColumnList[i];
					int idnumber = 0;
					for(int j = 0;j < ids.length;j++){
						if(d.getId().equals(ids[j]) && idnumber < ids.length){
							ids[j] = "";
							break;
						}else{
							idnumber++;
						}
					}
					if(idnumber >= ids.length)	break;
				}
				for(int i = defColumnList.length - 1;i > 0;i--){
					DefColumnInfo d = (DefColumnInfo) defColumnList[i];
					for(int j = 0;j < ids.length;j++){
						if(d.getId().equals(ids[j])){
							changeColumn(d,(DefColumnInfo) defColumnList[i-1]);
						}
					}
				}
			}
			else if(dir.equals("down")){
				for(int i = defColumnList.length - 1;i >= 0 ;i--){
					DefColumnInfo d = (DefColumnInfo) defColumnList[i];
					int idnumber = 0;
					for(int j = 0;j < ids.length;j++){
						if(d.getId().equals(ids[j]) && idnumber < ids.length){
							ids[j] = "";
							break;
						}else{
							idnumber++;
						}
					}
					if(idnumber >= ids.length)	break;
				}
				for(int i = 0;i < defColumnList.length - 1;i++){
					DefColumnInfo d = (DefColumnInfo) defColumnList[i];
					for(int j = 0;j < ids.length;j++){
						if(d.getId().equals(ids[j])){
							changeColumn(d,(DefColumnInfo) defColumnList[i+1]);
						}
					}
				}
			}
			this.outputJson("{success:true,msg:'保存数据成功!'}");
		}catch(Exception ex){
			this.outputJson("{success:false,msg:'ssss'}");
		}
		return NONE;
	}

	/**
	 * 给每个字段重新设定排序号
	 * @author 肖平松
	 * @version Aug 31, 2009
	 * @return
	 * @throws Exception
	 */
	public String resetSortNoAction() throws Exception {
		String[] ids = request.getParameterValues("ids");
		for(int i = 0;i < ids.length;i++){
			Object obj = null;
			Object originalObject = null;
			// 有id表示修改，否则表示创建
			
			if (StringUtils.isNotEmpty(ids[i])) {
				obj = getManager().get(ids[i]);
				originalObject = this.getManager().getEntityClass().newInstance();
				BeanUtils.copyProperties(originalObject, obj);
			} else {
				obj = creatBlankObject();
			}
			Date d = new Date();
			BeanUtils.setProperty(obj, "columnorder", d.getTime());
			getManager().save(obj);
			Thread.sleep(100);
		}

		getOut().print("{success:true}");
		return NONE;
	}

	/**
	 * 交换两个字段的排序号
	 * @author 肖平松
	 * @version Aug 31, 2009
	 * @param preColumn 前一个排序号
	 * @param d 现在的排序号
	 */
	private void changeColumn(DefColumnInfo preColumn, DefColumnInfo d) {
		if(preColumn.getColumnorder() == d.getColumnorder())	return;
		// TODO Auto-generated method stub
		long order = preColumn.getColumnorder();
		preColumn.setColumnorder(d.getColumnorder());
		d.setColumnorder(order);
		defColumnInfoManager.save(preColumn);
		defColumnInfoManager.save(d);
	}
	
	/**
	 * 根据tableid取得该表的所有列定义对象并json化输出
	 * 主要用于在查询定义表数据时，不要序列化大字段中的数据
	 * @return
	 * @throws Exception
	 */
	public String getColumnInfoButLobAction() throws Exception{
		String tableid=request.getParameter("tableid");
		List list=defColumnInfoManager.findColumnInfoByTableIdButLOB(tableid);
		String json = JSONUtil.listToJson(list, listJsonProperties());
		this.outputJson("{success:true,data:"+json+"}");
		return NONE;
		
	}
	
	/**
	 * 根据表定义编号查询所拥有的列定义对象
	 * @return
	 * @throws Exception 
	 */
	public String findColumnListByTableIdAction() throws Exception{
		try{
			String tableId=request.getParameter("defTableId");
			List<DefColumnInfo> defColumnList= defColumnInfoManager.findColumnInfoByTableId(tableId);
			String json=JSONUtil.listToJson(defColumnList, new String[]{"id","columncode","columnname","columntype","pk","columnlength","defaultvalue"});
			this.outputJson("{success:true,list:"+json+"}");
		}catch(Exception ex){
			ex.printStackTrace();
			this.outputJson("{success:false,msg:'ssss'}");
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
			
			this.outputJson("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 判断字段是否已经存在.
	 * @throws Exception
	 */
	public String isExistColumnAction() throws Exception{
		String tableId = request.getParameter("tableid");
		String columnCode = request.getParameter("columnCode");
		if(defColumnInfoManager.isExistColumn(tableId, columnCode)){
			//存在
			this.outputJson("{success:true,exist:true}");
		}else{
			//不存在
			this.outputJson("{success:true,exist:false}");
		}
		return NONE;
	}

	/**
	 * 删除字段
	 */
	public String removeAction() throws Exception {
		try {
			String id = request.getParameter("ids");
			if(StringUtil.isNotEmpty(id)){
				String[] ids = id.split(",");
				DefColumnInfo defColumnInfo = defColumnInfoManager.get(ids[0]);
				//保存 对应表的修改记录
				defTableInfoManager.modifyRecByDefColumnInfoChange(defColumnInfo, "drop", null, ids, false);
				//删除
				defColumnInfoManager.removeBatch(id.split(","));
				this.outputJson("{success:true}");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
//	
//	/**
//	 * 根据tableId,columnCode查询需要修改的DefColumnInfo对象.
//	 */
//	public String showUpdateByTableId_ColumnCode() throws Exception {
//		String columnCode = request.getParameter("columnCode");
//		String tableId = request.getParameter("tableId");
//		
//		if(StringUtil.isNotEmpty(columnCode) && StringUtil.isNotEmpty(tableId)){
//			List<DefColumnInfo> list = defColumnInfoManager.find("from DefColumnInfo as col where col.table.id=? and col.columncode = ?", tableId,columnCode);
//			if(null != list && list.size() != 0){
//				String json = JSONUtil.listToJson(list, listJsonProperties());
//				this.outputJson("{success:true,data:"+json+"}");
//				if(list.size()>1){
//					System.out.println("存在多个相同的字段名 columnCode,位于表ID:"+tableId);
//				}
//			}
//		}
//		
//		return NONE;
//	}
//
//	/**
//	 * 添加主键ID.
//	 * @author caihuiwen.
//	 * @throws Exception
//	 */
//	public String savePkByTableid() throws Exception{
//		String tableId = request.getParameter("tableId");
//		defColumnInfoManager.addPrimaryKeyByTableId(tableId);
//		this.outputJson("{success:true}");
//		return NONE;
//	}
////	
	
}
