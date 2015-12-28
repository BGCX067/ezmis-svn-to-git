package com.jteap.jhtj.sjxxxdy.web;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.jhtj.ljydy.manager.AppSystemConnManager;
import com.jteap.jhtj.ljydy.model.AppSystemConn;
import com.jteap.jhtj.sjxxxdy.manager.TjInfoItemManager;
import com.jteap.jhtj.sjxxxdy.model.OtherSystem;
import com.jteap.jhtj.sjxxxdy.model.TjInfoItem;
import com.jteap.jhtj.sjydy.manager.TjAppIOManager;
import com.jteap.jhtj.sjydy.model.TjAppIO;
import com.jteap.jhtj.sjydy.model.TjAppSjzd;
import com.jteap.jhtj.yyjkwh.manager.TjDllIOManager;
import com.jteap.jhtj.yyjkwh.model.TjDllIO;
@SuppressWarnings({ "unchecked", "serial" })
public class TjInfoItemAction extends AbstractAction {
	private TjInfoItemManager tjInfoItemManager;
	private TjDllIOManager tjDllIOManager;
	private TjAppIOManager tjAppIOManager;
	private AppSystemConnManager appSystemConnManager;
	

	@Override
	public HibernateEntityDao getManager() {
		return tjInfoItemManager;
	}
	
	
	/**
	 * 
	 *描述：验证连接名称是否唯一
	 *时间：2010-4-1
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String validateNameUniqueAction() throws Exception {
		String kid = request.getParameter("kid");//分类ID
		String item = request.getParameter("item");//分类ID
		TjInfoItem infoitem=this.tjInfoItemManager.findTjInfoItemByKidAndItem(kid, item);
		if(infoitem!=null){
			outputJson("{unique:false}");
		}else{
			outputJson("{unique:true}");
		}
		//outputJson("{unique:true}");
		return NONE;
	}
	
	/**
	 * 
	 *描述：查找dll的全部信息
	 *时间：2010-4-14
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String findDllNameAllAction() throws Exception{
		List<TjDllIO> list= this.tjDllIOManager.getAll();
		String json=JSONUtil.listToJson(list,new String[]{"dname","dcname"});
		outputJson(json);
		return NONE;
	}
	
	/**
	 * 
	 *描述：创建表结构
	 *时间：2010-4-10
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String createTableAction() throws Exception{
		String id=request.getParameter("id");
		String kid=request.getParameter("kid");
		//修改的时候
		if(StringUtils.isNotEmpty(id)){
			TjInfoItem infoItem=this.tjInfoItemManager.get(id);
			if(infoItem!=null){
				kid=infoItem.getKid();
			}
		}
		if(StringUtils.isNotEmpty(kid)){
			String result=this.tjInfoItemManager.createTable(kid);
			outputJson("{success:"+result+"}");
		}else{
			outputJson("{success:false}");
		}
		return NONE;
	}
	
	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		String kid=request.getParameter("kid");
		String item=request.getParameter("item");
		String iname=request.getParameter("iname");
		iname=StringUtil.isoToUTF8(iname);
		String sjlx=request.getParameter("sjlx");
		String qsfs=request.getParameter("qsfs");
		HqlUtil.addCondition(hql, "kid",kid);
		if(StringUtils.isNotEmpty(item)){
			HqlUtil.addCondition(hql, "item",item);
		}
		if(StringUtils.isNotEmpty(iname)){
			HqlUtil.addCondition(hql, "iname",iname,HqlUtil.LOGIC_AND,HqlUtil.TYPE_STRING_LIKE);
		}
		if(StringUtils.isNotEmpty(sjlx)){
			HqlUtil.addCondition(hql, "sjlx",sjlx,HqlUtil.LOGIC_AND);
		}
		if(StringUtils.isNotEmpty(qsfs)){
			HqlUtil.addCondition(hql, "qsfs",qsfs,HqlUtil.LOGIC_AND,HqlUtil.TYPE_NUMBER);
		}
		HqlUtil.addOrder(hql, "iorder","asc");
	}
	
	/**
	 * 
	 *描述：显示从其他系统取数的页面
	 *时间：2010-4-10
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String showOtherSystemPageAction() throws Exception{
		Map paramMap = request.getParameterMap();
		for (Object key : paramMap.keySet()) {
			if (key.equals("id"))
				continue;
			String paramName = (String) key;
			String paramValue = request.getParameter(paramName);
			request.setAttribute(paramName, paramValue);
		}
		return "showOtherSystemPageAction";
	}
	
	/**
	 * 
	 *描述：显示选择的系统信息
	 *时间：2010-4-12
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String showOtherSystemFormAction() throws Exception{
		String sysid=request.getParameter("sysid");
		String sjyid=request.getParameter("sjyid");
		if(StringUtils.isNotEmpty(sysid)&&StringUtils.isNotEmpty(sjyid)){
			AppSystemConn appSystem=this.appSystemConnManager.get(sysid);
			TjAppIO io=this.tjAppIOManager.get(sjyid);
			OtherSystem other=new OtherSystem();
			other.setSystemName(appSystem.getName());
			other.setServer(appSystem.getServer());
			other.setUserId(appSystem.getUserId());
			other.setUserPwd(appSystem.getUserPwd());
			other.setDbName(appSystem.getDbName());
			other.setVname(io.getVname());
			other.setCvname(io.getCvname());
			other.setSqlstr(io.getSqlstr());
			String json=JSONUtil.objectToJson(other, new String[]{"systemName","server","dbName","userId","userPwd","vname","cvname","sqlstr"});
			List<TjAppSjzd> sjzdList=this.tjAppIOManager.findTjAppSjzdBySysidAndVname(sysid, io.getVname());
			String sjzdJson=JSONUtil.listToJson(sjzdList,new String[]{"fname","cfname"});
			//System.out.println(sjzdJson);
			outputJson("{success:true,data:["+json+"],list:["+sjzdJson+"]}");
		}else{
			outputJson("{success:false}");	
		}
		return NONE;
	}
	
	/**
	 * 
	 *描述：显示计算主页
	 *时间：2010-4-13
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String showComputePageAction() throws Exception{
		Map paramMap = request.getParameterMap();
		for (Object key : paramMap.keySet()) {
			if (key.equals("id"))
				continue;
			String paramName = (String) key;
			String paramValue = request.getParameter(paramName);
			request.setAttribute(paramName, paramValue);
		}
		return "showComputePageAction";
	}
	
	/**
	 * 
	 *描述：测试公式是否正确
	 *时间：2010-4-12
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String testCexpAction() throws Exception{
		String kid=request.getParameter("kid");
		String cexp=request.getParameter("cexp");
		boolean isRight=this.tjInfoItemManager.testCexp(kid, cexp);
		outputJson("{success:"+isRight+"}");
		return NONE;
	}
	
	public String findTjAppioByVnameAction() throws Exception{
		String vname=request.getParameter("vname");
		TjAppIO io=this.tjAppIOManager.findUniqueBy("vname", vname);
		String json=JSONUtil.objectToJson(io, new String[]{"id"});
		outputJson("{success:true,data:["+json+"]}");
		return NONE;
	}
	
	
	public String getCfnameByFnameAction() throws Exception{
		String fname=request.getParameter("fname");
		String cfname=this.tjAppIOManager.getNewColumnCName(fname);
		outputJson("{success:true,cfname:'"+cfname+"'}");
		return NONE;
	}
	
	/**
	 * 删除前验证
	 */
	public String removeAction() throws Exception {
		String keys = request.getParameter("ids");
		String kid=request.getParameter("kid");
		try {
			if(StringUtils.isNotEmpty(kid)){
				//生成了数据以后不能删除字段信息
				List list=this.tjInfoItemManager.findData_Table(kid);
				if(list.size()>0){
					outputJson("{success:false,msg:'信息项已生成数据,不能删除'}");
				}else{
					if (keys != null) {
						this.getManager().removeBatch(keys.split(","));
						outputJson("{success:true}");
					}
				}
			}
			
		} catch (Exception ex) {
			String msg = ex.getMessage();
			outputJson("{success:false,msg:'" + msg + "'}");
		}
		return NONE;
	}
	
	

	@Override
	public String[] listJsonProperties() {
		return new String[]{"id","kid","item","sjlx","iname","dw","itype","cd","xsw","qsfs","sid","vname","fname","cexp","corder","jsfs","dname","dfunId","iorder","isvisible"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{"id","kid","item","sjlx","iname","dw","itype","cd","xsw","qsfs","sid","vname","fname","cexp","corder","jsfs","dname","dfunId","iorder","isvisible"};
	}

	public TjInfoItemManager getTjInfoItemManager() {
		return tjInfoItemManager;
	}

	public void setTjInfoItemManager(TjInfoItemManager tjInfoItemManager) {
		this.tjInfoItemManager = tjInfoItemManager;
	}


	public TjDllIOManager getTjDllIOManager() {
		return tjDllIOManager;
	}


	public void setTjDllIOManager(TjDllIOManager tjDllIOManager) {
		this.tjDllIOManager = tjDllIOManager;
	}


	public TjAppIOManager getTjAppIOManager() {
		return tjAppIOManager;
	}


	public void setTjAppIOManager(TjAppIOManager tjAppIOManager) {
		this.tjAppIOManager = tjAppIOManager;
	}


	public AppSystemConnManager getAppSystemConnManager() {
		return appSystemConnManager;
	}


	public void setAppSystemConnManager(AppSystemConnManager appSystemConnManager) {
		this.appSystemConnManager = appSystemConnManager;
	}
	

	
}
