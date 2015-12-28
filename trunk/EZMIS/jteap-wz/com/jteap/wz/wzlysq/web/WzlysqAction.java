package com.jteap.wz.wzlysq.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.system.dict.manager.DictManager;
import com.jteap.wfengine.workflow.manager.JbpmOperateManager;
import com.jteap.wfengine.workflow.manager.WorkFlowLogManager;
import com.jteap.wfengine.workflow.util.WFConstants;
import com.jteap.wz.lydgl.model.Lydmx;
import com.jteap.wz.wzlysq.manager.WzlysqDetailManager;
import com.jteap.wz.wzlysq.manager.WzlysqJDBCManager;
import com.jteap.wz.wzlysq.manager.WzlysqManager;
import com.jteap.wz.wzlysq.model.Wzlysq;
import com.jteap.wz.wzlysq.model.WzlysqDetail;
import com.jteap.wz.xqjh.manager.XqjhDetailManager;
import com.jteap.wz.xqjh.model.XqjhDetail;

@SuppressWarnings( { "unchecked", "serial", "unused" })
public class WzlysqAction extends AbstractAction{

	private WzlysqManager wzlysqManager;
	private WzlysqJDBCManager wzlysqJDBCManager;
	private WzlysqDetailManager wzlysqDetailManager;
	private XqjhDetailManager xqjhDetailManager;

	private DictManager dictManager;
	private DataSource dataSource;
	private JbpmOperateManager jbpmOperateManager;
	private WorkFlowLogManager workFlowLogManager;

	public WorkFlowLogManager getWorkFlowLogManager() {
		return workFlowLogManager;
	}

	public void setWorkFlowLogManager(WorkFlowLogManager workFlowLogManager) {
		this.workFlowLogManager = workFlowLogManager;
	}
	
	public XqjhDetailManager getXqjhDetailManager() {
		return xqjhDetailManager;
	}

	public void setXqjhDetailManager(XqjhDetailManager xqjhDetailManager) {
		this.xqjhDetailManager = xqjhDetailManager;
	}

	public WzlysqDetailManager getWzlysqDetailManager() {
		return wzlysqDetailManager;
	}

	public void setWzlysqDetailManager(WzlysqDetailManager wzlysqDetailManager) {
		this.wzlysqDetailManager = wzlysqDetailManager;
	}

	public JbpmOperateManager getJbpmOperateManager() {
		return jbpmOperateManager;
	}

	public void setJbpmOperateManager(JbpmOperateManager jbpmOperateManager) {
		this.jbpmOperateManager = jbpmOperateManager;
	}

	public WzlysqJDBCManager getWzlysqJDBCManager() {
		return wzlysqJDBCManager;
	}

	public void setWzlysqJDBCManager(WzlysqJDBCManager wzlysqJDBCManager) {
		this.wzlysqJDBCManager = wzlysqJDBCManager;
	}

	public DictManager getDictManager() {
		return dictManager;
	}

	public void setDictManager(DictManager dictManager) {
		this.dictManager = dictManager;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public WzlysqManager getWzlysqManager() {
		return wzlysqManager;
	}

	public void setWzlysqManager(WzlysqManager wzlysqManager) {
		this.wzlysqManager = wzlysqManager;
	}
	/**http://localhost:7001/EZMIS/jteap/wz/wzlysq/wzlysqAction!initLysqmxAction.do
	 * 初始化领用申请明细和需求计划明细
	 * @return
	 */
	public String initLysqmxAction(){
		DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		ResultSet xqmxRs = null;
		Statement xqmxSt = null;
		ResultSet lysqmxRs = null;
		Statement lysqmxSt = null;
		try{
			conn = dataSource.getConnection();
			st = conn.createStatement();
			lysqmxSt = conn.createStatement();
			xqmxSt = conn.createStatement();
			
			//查询申请时间小于6月9号 并且已完成的领用申请主单
			String sql = "select t.id from tb_wz_ylysq t where t.FLOW_STATUS='已完成' and t.sqsj <to_date('2011-6-9','yyyy-mm-dd') ";
			rs = st.executeQuery(sql);
			while(rs.next()){
				int allCount = 0;
				//lydcount++;
				//查询出该主单下的领用申请明细
				List<WzlysqDetail> lydmxList = wzlysqDetailManager.find("from WzlysqDetail t where t.wzlysq.id=?", rs.getString(1));
				for(WzlysqDetail sqmx :lydmxList){
					allCount++;
					//领用单明细id
					String lysqmxId = sqmx.getId();
					//物资编码
					String wzbm = sqmx.getWzbm();
					//领用数量
					double sqsl = sqmx.getSqsl();
					//根据明细的物资编码 查询需求计划明细中 对应的需求计划明细id
					//xqmxRs = xqmxSt.executeQuery("select t.id from tb_wz_xqjh_detail t where t.wzbm = '"+wzbm+"'");
					List<XqjhDetail> xqmxList = xqjhDetailManager.find("from XqjhDetail t where t.wzbm=?", wzbm);
					//记录该物资对应的需求计划明细里有几条记录
					int count = 0;
					String xqjhmxId = "";
					double cgsl=0;
					double dhsl=0;
					double pzsl = 0;
					if(xqmxList.size()>1){
						for(XqjhDetail xqmx :xqmxList){
							if(xqmx.getPzsl()==sqsl&&xqmx.getCgsl()==sqsl&&xqmx.getDhsl()==sqsl){
								count++;
								xqjhmxId = xqmx.getId();
								cgsl = xqmx.getCgsl();
								pzsl = xqmx.getPzsl();
								dhsl = xqmx.getDhsl();
							}
						}
						if(count==1){
							xqmxSt.execute("update tb_wz_xqjh_detail t set t.slsl = "+sqsl+ " where t.wzbm ='"+wzbm+"' and t.pzsl="+pzsl+" and t.cgsl = "+cgsl+" and t.dhsl = "+dhsl+"");
							//修改领用申请对应的需求计划申请编号
							lysqmxSt.execute("update tb_wz_ylysqmx t set t.xqjhmxbm = '"+xqjhmxId+"' where t.id = '"+lysqmxId+"'");
						}else{
							System.out.println("领用申请明细ID："+lysqmxId+" 物资编码："+wzbm+"对应需求计划明细数量:"+count);
						}
					}else{
						XqjhDetail xqmx = xqmxList.get(0);
						//如果为1条 则直接修改
//						System.out.println("修改完成：领用单明细ID："+lysqmxId+"----需求计划明细id:"+xqjhmxId);
						//修改需求计划明细申领数量
						xqmxSt.execute("update tb_wz_xqjh_detail t set t.slsl = "+sqsl+ " where t.wzbm ='"+wzbm+"'");
						//修改领用申请对应的需求计划申请编号
						lysqmxSt.execute("update tb_wz_ylysqmx t set t.xqjhmxbm = '"+xqmx.getId()+"' where t.id = '"+lysqmxId+"'");
					}
				}
//				System.out.println(allCount);
			}
			//System.out.println("主单数量："+lydcount);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return NONE;
	}
	/**
	 * 物资领用申请流程-待处理
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String showDclAction()throws Exception{
		String LYDQF = request.getParameter("LYDQF");
		/** ************************ */
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "1";

		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);
		
		String userLoginName = sessionAttrs.get(
				Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
		// 角色（专业）过滤
		// String userRole =
		// sessionAttrs.get(Constants.SESSION_CURRENT_PERSONROLES).toString();
		// String[] userRoles = userRole.split(",");
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("select a.FLOW_TOPIC, ");
			sbSql.append("a.ID as TASKTODOID, ");
			sbSql.append("a.FLOW_NAME, ");
			sbSql.append("a.FLOW_INSTANCE_ID, ");
			sbSql.append("a.CURRENT_TASKNAME, ");
			sbSql.append("a.POST_PERSON, ");
			sbSql.append("to_char(a.POST_TIME,'yyyy-MM-dd HH:mi:ss') as POST_TIME, ");
			sbSql.append("a.TOKEN, ");
			sbSql.append("a.CURSIGNIN, ");
			sbSql.append("b.*, ");
			sbSql.append("d.personname as curSignInName ");
			sbSql.append("from tb_wf_todo a, ");
			sbSql.append("tb_wz_ylysq b, ");
			sbSql.append("jbpm_variableinstance c, ");
			sbSql.append("tb_sys_person d ");
			sbSql.append("where a.current_process_person like '%");
			sbSql.append(userLoginName);
			sbSql.append("%' ");
			sbSql.append("and a.flag = '1'");
			sbSql.append("and a.flow_instance_id = c.processinstance_ ");
			sbSql.append("and b.id = c.stringvalue_ ");
			sbSql.append("and c.name_='docid' ");
			sbSql.append("and b.flow_status != '已作废' ");
			sbSql.append("and b.LYDQF = '"+LYDQF+"' ");
			sbSql.append("and d.id(+) = a.cursignin order by b.bh desc");
			String sql = sbSql.toString();
			Page page = wzlysqJDBCManager.pagedQueryTableData(sql, iStart, iLimit);
			/** ************************ */

			String json = JSONUtil.listToJson((List) page.getResult(),
					new String[] { "FLOW_TOPIC", "TASKTODOID", "FLOW_NAME",
							"FLOW_INSTANCE_ID", "CURRENT_TASKNAME",
							"POST_PERSON", "POST_TIME", "TOKEN", "CURSIGNIN",
							"ID",
							"BH",
							"CZR",
							"LYBM",
							"GCLB",
							"SQSJ",
							"GCXM",
							"ZT",
							"XQJHSQBH",
							"SQBMMC",
							"CZYXM",
							"CZY",
							"FLOW_STATUS",
							"CURSIGNINNAME" });
			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json
					+ "}";
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 物资领用申请流程-已处理
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String showYclAction()throws Exception{
		String LYDQF = request.getParameter("LYDQF");
		/** ************************** */
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "1";

		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);
		String userLoginName = sessionAttrs.get(
				Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("select a.*,");
			sbSql.append("b.processinstance_,");
			sbSql.append("c.id_,");
			sbSql.append("c.version_,");
			sbSql.append("c.start_,");
			sbSql.append("c.end_,");
			sbSql.append("d.flow_name,");
			sbSql.append("d.id as FLOW_CONFIG_ID,");
			sbSql.append("d.flow_form_id ");
			//sbSql.append("from tb_wz_ylysq a,");
			sbSql.append("from (select * from tb_wz_ylysq t " +
					"where t.id not in (select t.id from tb_wz_ylysq t " +
					"where t.flow_status is null and t.flow_status = '已作废')) a,");
			sbSql.append("jbpm_variableinstance b,");
			sbSql.append("jbpm_processinstance c,");
			sbSql.append("tb_wf_flowconfig d,");
			sbSql.append("tb_wf_log e ");
			sbSql.append("where b.name_ = 'docid' ");
			sbSql.append("and b.stringvalue_ = a.id ");
			sbSql.append("and b.processinstance_ = c.id_ ");
			sbSql.append("and c.processdefinition_ = d.pd_id ");
			sbSql.append("and e.pi_id = c.id_ ");
			//sbSql.append("and a.flow_status is not null and a.flow_status != '已作废' ");
			sbSql.append("and a.LYDQF = '"+LYDQF+"' ");
			sbSql.append("and e.id in (select max(id) from tb_wf_log where ");
			sbSql.append("task_login_name ='");
			sbSql.append(userLoginName);
			sbSql.append("' group by pi_id) ");
			sbSql.append("and  not exists ");
			sbSql.append("(select 1 from tb_wf_todo f  where a.id=docid and f.flow_instance_id = c.id_ and f.flag = '1' and f.CURRENT_PROCESS_PERSON like '%"
							+ userLoginName + "%') order by c.start_ desc ");
			String sql = sbSql.toString();
			Page page = wzlysqJDBCManager.pagedQueryTableData(sql, iStart, iLimit);
			/** ************************** */

			String json = JSONUtil.listToJson((List) page.getResult(),
					listJsonProperties());

			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json
					+ "}";
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 物资领用申请流程-草稿箱
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String showCgxAction()throws Exception{
		/** ********************* */
		String LYDQF = request.getParameter("LYDQF");
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "1";

		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);
		String userLoginName = sessionAttrs.get(
				Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("select a.*,");
			sbSql.append("b.processinstance_,");
			sbSql.append("c.id_,");
			sbSql.append("c.version_,");
			sbSql.append("c.start_,");
			sbSql.append("c.end_,");
			sbSql.append("d.flow_name,");
			sbSql.append("d.id as FLOW_CONFIG_ID,");
			sbSql.append("d.flow_form_id ");
			sbSql.append("from tb_wz_ylysq a,");
			sbSql.append("jbpm_variableinstance b,");
			sbSql.append("jbpm_processinstance c,");
			sbSql.append("tb_wf_flowconfig d,");
			sbSql.append("tb_wf_log e ");
			sbSql.append("where b.name_ = 'docid' ");
			sbSql.append("and b.stringvalue_ = a.id ");
			sbSql.append("and b.processinstance_ = c.id_ ");
			sbSql.append("and c.processdefinition_ = d.pd_id ");
			sbSql.append("and e.pi_id = c.id_ ");
			sbSql.append("and e.task_login_name ='");
			sbSql.append(userLoginName);
			sbSql.append("' ");
			sbSql.append("and a.flow_status is null ");
			sbSql.append("and a.LYDQF = '"+LYDQF+"' ");
			sbSql.append("and e.id in (select max(id) from tb_wf_log group by pi_id) ");
			String sql = sbSql.toString();
			Page page = wzlysqJDBCManager.pagedQueryTableData(sql, iStart, iLimit);
			/** ********************* */

			String json = JSONUtil.listToJson((List) page.getResult(),
					listJsonProperties());

			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json
					+ "}";
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}
	
	@SuppressWarnings("unchecked")
	public String showZfAction()throws Exception{
		/*****************************/
		String LYDQF = request.getParameter("LYDQF");
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "1";

		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);
		String userLoginName = sessionAttrs.get(
				Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("select a.*,");
			sbSql.append("b.processinstance_,");
			sbSql.append("c.id_,");
			sbSql.append("c.version_,");
			sbSql.append("c.start_,");
			sbSql.append("c.end_,");
			sbSql.append("d.flow_name,");
			sbSql.append("d.id as FLOW_CONFIG_ID,");
			sbSql.append("d.flow_form_id ");
			sbSql.append("from tb_wz_ylysq a,");
			sbSql.append("jbpm_variableinstance b,");
			sbSql.append("jbpm_processinstance c,");
			sbSql.append("tb_wf_flowconfig d,");
			sbSql.append("tb_wf_log e ");
			sbSql.append("where b.name_ = 'docid' ");
			sbSql.append("and b.stringvalue_ = a.id ");
			sbSql.append("and b.processinstance_ = c.id_ ");
			sbSql.append("and c.processdefinition_ = d.pd_id ");
			sbSql.append("and e.pi_id = c.id_ ");
			sbSql.append("and a.flow_status ='已作废' ");
			sbSql.append("and a.LYDQF = '"+LYDQF+"' ");
			sbSql.append("and e.id in (select max(id) from tb_wf_log where ");
			sbSql.append("task_login_name ='");
			sbSql.append(userLoginName);
			sbSql.append("' group by pi_id) order by a.bh desc");
			String sql = sbSql.toString();
			Page page = wzlysqJDBCManager.pagedQueryTableData(sql, iStart, iLimit);
		/*****************************/

		String json = JSONUtil.listToJson((List) page.getResult(),
				listJsonProperties());

		json = "{totalCount:'" + page.getTotalCount() + "',list:" + json
				+ "}";
		this.outputJson(json);
	} catch (Exception ex) {
		this.outputJson("{success:false}");
		ex.printStackTrace();
	}
	return NONE;
	}
	
	@SuppressWarnings("unchecked")
	public String showAllListAction()throws Exception{
		/** ************************** */
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "1";

		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("select a.*,");
			sbSql.append("b.processinstance_,");
			sbSql.append("c.id_,");
			sbSql.append("c.version_,");
			sbSql.append("c.start_,");
			sbSql.append("c.end_,");
			sbSql.append("d.flow_name,");
			sbSql.append("d.id as FLOW_CONFIG_ID,");
			sbSql.append("d.flow_form_id ");
			sbSql.append("from tb_wz_ylysq a,");
			sbSql.append("jbpm_variableinstance b,");
			sbSql.append("jbpm_processinstance c,");
			sbSql.append("tb_wf_flowconfig d,");
			sbSql.append("tb_wf_log e ");
			sbSql.append("where b.name_ = 'docid' ");
			sbSql.append("and b.stringvalue_ = a.id ");
			sbSql.append("and b.processinstance_ = c.id_ ");
			sbSql.append("and c.processdefinition_ = d.pd_id ");
			sbSql.append("and e.pi_id = c.id_ ");
			sbSql.append("and a.flow_status is not null ");
			sbSql.append("and e.id in (select max(id) from tb_wf_log group by pi_id)");
			String sql = sbSql.toString();
			Page page = wzlysqJDBCManager.pagedQueryTableData(sql, iStart, iLimit);
			/** ************************** */

			String json = JSONUtil.listToJson((List) page.getResult(),
					listJsonProperties());

			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json
					+ "}";
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 物资领用申请流程-作废处理
	 * @return
	 * @throws Exception
	 */
	public String cancelWzlyAction() throws Exception {
		String curLoginName = sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
		String id = request.getParameter("id");
		String pid = request.getParameter("pid");
		String token = request.getParameter("token");
		String curTaskName = request.getParameter("curTaskName");
		Connection conn = null;
		try {
			conn = this.dataSource.getConnection();
			String sql = "update tb_wz_ylysq t set t.flow_status = '已作废' where t.id ='"
					+ id + "'";
			String sqls = "update tb_wf_todo t set t.flag = '0', t.status = '已处理', t.deal_person = '"+curLoginName+"', t.deal_time = sysdate where t.docid ='"+id+"' and t.status = '待处理' and t.flag = '1'";
			defeasanceNodeAction(pid,token,curTaskName);
			Statement st = conn.createStatement();
			st.executeUpdate(sql);
			st.executeUpdate(sqls);
			st.close();
			this.outputJson("{success:true}");
		} catch (Exception e) {
			this.outputJson("{success:false}");
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return NONE;
	}
	
	/**
	 * 
	 * 描述 : 作废操作
	 * 作者 : caofei
	 * 时间 : 2010-05-27
	 * 参数 : 
	 * 异常 ： IOException 
	 */
	public String defeasanceNodeAction(String pid, String token, String curTaskName) throws IOException {
		String curUserName = sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_NAME).toString();//当前处理人
		String curUserLoginName = this.getCurUserLoginName();
		String curLoginName = sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
		
		try {
			ProcessInstance pi = jbpmOperateManager.getJbpmTemplate().findProcessInstance(Long.parseLong(pid));
			pi.getContextInstance().setTransientVariable(WFConstants.WF_VAR_LOGINNAME, this.getCurUserLoginName());
			boolean isCanCancel = true;
			// 找到所有未完成任务实例
			Collection<TaskInstance> taskInstances = jbpmOperateManager.getUnfinishedTaskInstance(pi, null);
			//当前任务实例
			TaskInstance ti = jbpmOperateManager.findCurrentTaskInstance(Long.parseLong(pid), this.getCurUserLoginName(),token);
			//记录流程日志
			workFlowLogManager.addFlowLog(ti.getProcessInstance().getId(), curTaskName, curUserName, curLoginName, "", "", "作废流程", "");
		}catch (Exception ex) {
			writeOpSuccessScript("作废失败");
			ex.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 获取当前用户登录名
	 */
	private String getCurUserLoginName(){
		return sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
	}
	
	
	
	@Override
	public String[] listJsonProperties() {
		// TODO Auto-generated method stub
		return new String[]{
			"ID_", "VERSION_", "START_", "END_",
			"PROCESSINSTANCE_", "FLOW_NAME", "FLOW_CONFIG_ID",
			"FLOW_FORM_ID","ID",
			"BH",
			"CZR",
			"LYBM",
			"GCLB",
			"SQSJ",
			"GCXM",
			"ZT",
			"XQJHSQBH",
			"SQBMMC",
			"CZYXM",
			"CZY",
			"FLOW_STATUS",
			"CURSIGNINNAME"
		};
	}

	@Override
	public String[] updateJsonProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HibernateEntityDao getManager() {
		// TODO Auto-generated method stub
		return null;
	}

}
