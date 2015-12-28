package com.jteap.bz.bzinfo.web;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;

import com.jteap.bz.bzinfo.manager.BzInfoManager;
import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.utils.WebUtils;
import com.jteap.core.web.AbstractAction;
import com.jteap.form.dbdef.manager.PhysicTableManager;
import com.jteap.form.dbdef.model.DefColumnInfo;
import com.jteap.form.dbdef.model.DefColumnType;
import com.jteap.form.dbdef.model.DefTableInfo;
import com.jteap.form.eform.manager.EFormManager;
import com.jteap.form.eform.model.EForm;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.P2Role;
import com.jteap.system.person.model.Person;
import com.jteap.system.role.manager.RoleManager;
import com.jteap.system.role.model.Role;
/**
 * 班主模块处理类
 * @author lvchao
 *
 */
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class BzInfoAction extends AbstractAction  {

	private BzInfoManager bzInfoManager;
	
	private EFormManager eformManager;
	
	private PersonManager personManager;
	
	private RoleManager roleManager;
	
	private PhysicTableManager physicTableManager;
	/**
	 * 根据班组标识获得班组信息及班组年度计划
	 * @return
	 * @throws Exception  
	 */
	public String getBZInfoByBsAction() throws Exception{
		return NONE;
	}
	 
	/**
	 * 根据班组标识，得到当前班组并放入session
	 * @return
	 * @throws Exception 
	 */
	public String toHomePageByBzAction() throws Exception{
		String currentPersonId = (String) sessionAttrs.get(Constants.SESSION_CURRENT_PERSONID);
		String bs = request.getParameter("bs");
		String loginName = (String)sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME);
		try{
			String schema = SystemConfig.getProperty("jdbc.schema");
			//通过编码获得班组信息
			String bzsql = "select ID from tb_form_bzinfo bz where bz.bzbs='"+bs+"'";
			log.info(bzsql);
			Map bzInfo = null;
			String dqbz = null;
			try {
				bzInfo = bzInfoManager.queryForMap(bzsql);
			} catch (EmptyResultDataAccessException ex) {   
				//忽略此类错误[EmptyResultDataAccessException],允许查询为空时!");
				outErrorMsg("该班组维护中");
				return NONE;
			}
			if(bzInfo!=null)
				dqbz = (String)bzInfo.get("ID");
			//过滤班组成员 获取当前班组
			String sql = "select * from tb_form_bzry where bzbm='"+dqbz+"'";
			List<Map> lst = bzInfoManager.queryForList(sql);
			String isMyBz = "no";
			for (Map map : lst) {
				String yhdlm = (String)map.get("YHDLM");
				if (loginName.equals(yhdlm)) {
					isMyBz = "MyBZ";
					break;
				}
			}
			//过滤登陆用户名的角色 如果角色是检修，发电，燃运主任的话 则具有所有班组权限
			Person person = personManager.findPersonByLoginName(loginName);
			if(person!=null){
				//检修，发电，燃运部 角色内码
				String role_sn = "WZ_JXBZR,WZ_FDBZR,WZ_RYBZR";
				for(P2Role pr :person.getRoles()){
					Role role =pr.getRole();
					if(StringUtils.isNotEmpty(role.getRoleSn())){
						if(role_sn.indexOf(role.getRoleSn())!=-1){
							isMyBz = "MyBZ";
							break;
						}
					}
				}
			}
			String moduleId = request.getParameter("moduleId");
			response.sendRedirect(request.getContextPath()+"/jteap/bz/module.jsp?moduleId="+moduleId+"&dqbz="+dqbz+"&isMyBz="+isMyBz);
//			return "BZHOMEPAGE";
		}catch(Exception ex){
			ex.printStackTrace();
			this.outputJson("{success:false}");
		}
		return NONE;
	}
	
	/**
	 * 取得我的班组
	 * @throws IOException 
	 * */
	public String getMyBzHomePage() throws IOException{
		String currentPersonId = (String) sessionAttrs.get(Constants.SESSION_CURRENT_PERSONID);
		String loginName = (String)request.getSession().getAttribute(Constants.SESSION_CURRENT_PERSON_LOGINNAME);
		String dqbz = request.getParameter("dqbz");
		String mybz = null;
		try{
			String myBzSql = "SELECT BZBM FROM TB_FORM_BZRY WHERE YHDLM='"+loginName+"'";			
			Object obj = bzInfoManager.queryForObject(myBzSql,String.class);
			if(obj!=null)
				mybz = (String)obj;
		} catch (EmptyResultDataAccessException ex) {
			if(dqbz==null){
				outErrorMsg("你没有关联到任何班组");
				return NONE;
			}
		}
		if(StringUtils.isEmpty(dqbz)){
			dqbz = mybz;//当前班组
		}
		try{
			String schema = SystemConfig.getProperty("jdbc.schema");
			//通过编码获得班组信息
			String bzsql = "select bz.* from tb_form_bzinfo bz where bz.id='"+dqbz+"'";
		//	String bzsql = "select bz.* from tb_form_bzinfo bz,tb_form_bzry ry" +
		//			" where bz.id=ry.bzbm and ry.yhdlm='"+loginName+"'";
			log.info(bzsql);
			Map bzInfo = null;
			try {
				bzInfo = bzInfoManager.queryForMap(bzsql);
			} catch (EmptyResultDataAccessException ex) {   
				//忽略此类错误[EmptyResultDataAccessException],允许查询为空时!");
				outErrorMsg("你没有关联到任何班组");
				return NONE;
			}
			//通过班组ID，年份获得班组年工作计划
			String jhsql = "select * from tb_form_ngzjh jh where jh.bzbm='"+bzInfo.get("ID")+"' and jh.nf='"+DateUtils.getDate("yyyy")+"' and rownum<2 order   by  jh.nf  desc";
			List jhList = bzInfoManager.queryForList(jhsql);
			//将班组亮点替换成HTML格式输出
			String bzld = (String)bzInfo.get("BZLD");
			bzInfo.put("BZLD",StringUtil.formatHTML(bzld));
			//替换班组年度计划
			if(jhList.size()>0){
				for(int i=0;i<jhList.size();i++){
					Map ndjh = (Map)jhList.get(i);
					String nj = (String)ndjh.get("jhnr");
					ndjh.put("jhnr", StringUtil.formatHTML(nj));
					ndjh.put("ts", nj.replaceAll("</br>", ""));
					jhList.set(i, ndjh);
				}
				///////////////////////////////////////
				request.setAttribute("jh",jhList.get(0));
			}
			///////////////////////////////////////
			request.setAttribute("bzInfo",bzInfo);
			//取得班组通知标题
			String tzhql = "select * from tb_form_tzzl";
			List tzList = bzInfoManager.queryForList(tzhql);
			////////////////////////////////////////
			request.setAttribute("tzzls", tzList);
			return SUCCESS;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return NONE;
	}
	/**
	 * 取得我的班组
	 * */
	public String findUserInAllBz(){
		String loginName = request.getParameter("loginName");
		try{
			String schema = SystemConfig.getProperty("jdbc.schema");
			//通过编码获得班组信息
			String bzsql = "select bz.bzm from tb_form_bzinfo bz,tb_form_bzry ry" +
					" where bz.id=ry.bzbm and ry.yhdlm='"+loginName+"'";
			log.info(bzsql);
			Map bzInfo = null;
			try {
				bzInfo = bzInfoManager.queryForMap(bzsql);
			} catch (EmptyResultDataAccessException ex) {   
				//忽略此类错误[EmptyResultDataAccessException],允许查询为空时!");
				this.outputJson("{success:true}");
				return NONE;
			}
			this.outputJson("{success:false,msg:'该人员已所属班组:"+bzInfo.get("BZM")+"'}");
			return NONE;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return NONE;
	}
	public String outErrorMsg(String error) throws IOException{
		String msg = "<script>alert('"+error+"');window.history.go(-1);</script>";
		response.getWriter().println(msg);
		return NONE;
	}
	/**
	 * 根据指定的表单，找到对应的物理表，并将该物理表中的数据列出来
	 * @return
	 * @throws Exception 
	 */
	public String showEFormRecListAction() throws Exception{		
		try{
			Page page = this.getEFormRecListPage();
			String json=JSONUtil.listToJson((List)page.getResult());
			json="{totalCount:'" + page.getTotalCount() + "',list:"+ json + "}";
			//System.out.println(json);
			this.outputJson(json);
		
		}catch(Exception ex){
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}
	public Page getEFormRecListPage(){
		String dqbz = request.getParameter("dqbz");//(String)sessionAttrs.get("DQBZ");//当前班组
		String ifAll = request.getParameter("all");//判断是否显示全部
		String loginName = (String)sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME);
		String mybz = null;
		String loginId = (String)sessionAttrs.get(Constants.SESSION_CURRENT_PERSONID);
		boolean zgFlag = false;
		// root用户登录时不需要查找所属班组，直接显示所有班组信息
		if (!Constants.ADMINISTRATOR_ACCOUNT.equals(loginName)) {
			try{
				String myBzSql = "SELECT BZBM FROM TB_FORM_BZRY WHERE YHDLM='"+loginName+"'";			
				Object obj = bzInfoManager.queryForObject(myBzSql,String.class);
				if(obj!=null)
					mybz = (String)obj;
			} catch (EmptyResultDataAccessException ex) {
//				return null;
			}
		} 
		
		String formSn = request.getParameter("formSn");
		EForm eform = eformManager.getEFormBySn(formSn);
		
		String currentPersonId = (String) sessionAttrs.get(Constants.SESSION_CURRENT_PERSONID);

		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");
		
		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "0";
		
		int iStart=Integer.parseInt(start)+1;
		int iLimit=Integer.parseInt(limit);
		String filterKey = null;//过滤班组编码
		String date = "";	//日期过滤字段
		//加入管理员配置，不过滤任何班组
		
		try{
		
			DefTableInfo tableInfo=eform.getDefTable();
			Collection<DefColumnInfo> cols=tableInfo.getColumns();
			StringBuffer selectColSb=new StringBuffer();
			if (!Constants.ADMINISTRATOR_ACCOUNT.equals(loginName)) {
				Collection<Role> Roles = roleManager.findRoleByPerson(personManager.findPersonByLoginName(loginName));
				for(Role r:Roles){
					if("bzzg".equals(r.getRoleSn())&&"TB_FORM_BZINFO".equals(tableInfo.getTableCode())){
						zgFlag=true;
						break;
					}
				}
			}
			
			if(tableInfo.getTableCode().equals("TB_FORM_BZINFO")){//该表单需要过滤班组编码
				filterKey = "ID";
			}
			for (DefColumnInfo defColumnInfo : cols) {
				if(defColumnInfo.getColumncode().equals("BZBM")){//该表单需要过滤班组编码
					filterKey = "BZBM";
				}
				if(defColumnInfo.getColumntype().equals(DefColumnType.CTYPE_BLOB) ||defColumnInfo.getColumntype().equals(DefColumnType.CTYPE_CLOB)){
					//selectColSb.append(defColumnInfo.getColumncode()+",");
				}
				if(defColumnInfo.getColumntype().equals(DefColumnType.CTYPE_DATE)){
					if(tableInfo.getTableCode().equals("TB_FORM_BZTZ")){
						selectColSb.append(defColumnInfo.getColumncode()+" as "+defColumnInfo.getColumncode()+",");
					}else{
						selectColSb.append("to_char("+defColumnInfo.getColumncode()+",'YYYY-MM-DD') as "+defColumnInfo.getColumncode()+",");
					}
				}else if(defColumnInfo.getColumntype().indexOf(DefColumnType.CTYPE_TIMESTAMP)>=0){
					selectColSb.append("to_char("+defColumnInfo.getColumncode()+",'YYYY-MM-DD') as "+defColumnInfo.getColumncode()+",");
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
			}else{//默认排序
				if(!tableInfo.getTableCode().equals("TB_FORM_BZINFO")){
					tmpOrder="ORDER BY scsj desc";
				}
				//班长日志
				if(tableInfo.getTableCode().equals("TB_FORM_BZRZ")){
					tmpOrder="ORDER BY sj desc";
				}
				//班委会记录
				if(tableInfo.getTableCode().equals("TB_FORM_BWHJL")){
					tmpOrder = " order by sj desc";
				}
				//月技术培训记录
				if(tableInfo.getTableCode().equals("TB_FORM_YJSPXJL")){
					tmpOrder = "order by jksj desc";
				}
			}
			String sql="SELECT "+selectColSb.toString()+" FROM "+tableInfo.getSchema()+"."+tableInfo.getTableCode();
			if (currentPersonId.equals(Constants.ADMINISTRATOR_ID)||zgFlag==true) {//如果是管理员，那么不过滤人员所在班组
				filterKey = null;
			}else{
				if(StringUtils.isEmpty(dqbz)){
					dqbz = mybz;//当前班组
				}
			}
			String queryParamsSql = request.getParameter("queryParamsSql");
			
			//System.out.println("ifAll:---------------------------"+ifAll);
			if(StringUtils.isNotEmpty(queryParamsSql)){
				sql += " WHERE " + queryParamsSql.replace("$", "%");
			}else{
				if(StringUtil.isEmpty(ifAll)){
					sql= bzInfoManager.getMaxDate(sql,tableInfo.getTableCode());
				 }
			}
			if(filterKey!=null){
				if(sql.indexOf("WHERE")==-1)
					sql +=" WHERE ";
				else
					sql +=" AND ";
				sql += filterKey+" ='"+dqbz+"'";
			}
			sql +=" "+ tmpOrder;
			//System.out.println(sql);
			return physicTableManager.pagedQueryTableData(sql, iStart, iLimit);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	//导出Excel
	public String exportExcel() throws Exception {

		// Excel表的头信息（逗号表达式）,由于前台是通过window.open(url)来传输此中文,则需要通过转码
		String paraHeader = WebUtils.getRequestParam(request, "paraHeader");

		// 表索引信息（逗号表达式）
		String paraDataIndex = request.getParameter("paraDataIndex");

		// 宽度(逗号表达式)
		String paraWidth = request.getParameter("paraWidth");
		
		String formSn = request.getParameter("formSn");

		List list = (List)this.getEFormRecListPage().getResult();

		// 调用导出方法
		export(list, paraHeader, paraDataIndex, paraWidth);

		return NONE;
	}
	@Override
	public String[] listJsonProperties() {
		return null;
	}

	public void setBzInfoManager(BzInfoManager bzInfoManager) {
		this.bzInfoManager = bzInfoManager;
	}

	public void setEformManager(EFormManager eformManager) {
		this.eformManager = eformManager;
	}

	public void setPhysicTableManager(PhysicTableManager physicTableManager) {
		this.physicTableManager = physicTableManager;
	}
	@Override
	public HibernateEntityDao getManager() {
		// TODO Auto-generated method stub
		return null;
	}
	 
	@Override
	public String[] updateJsonProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}
	
	
}
