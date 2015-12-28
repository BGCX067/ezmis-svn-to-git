package com.jteap.wz.wzlysq.actionHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;

import com.jteap.core.Constants;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.utils.UUIDGenerator;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.form.dbdef.model.DefTableInfo;
import com.jteap.form.eform.manager.EFormManager;
import com.jteap.form.eform.model.EForm;
import com.jteap.system.jdbc.manager.JdbcManager;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.wfengine.tasktodo.manager.TaskToDoManager;
import com.jteap.wfengine.workflow.actionHandler.LeaveNodeEventHandler;
import com.jteap.wfengine.workflow.manager.FlowConfigManager;
import com.jteap.wfengine.workflow.manager.JbpmOperateManager;
import com.jteap.wfengine.workflow.manager.WorkFlowLogManager;
import com.jteap.wfengine.workflow.model.FlowConfig;
import com.jteap.wfengine.workflow.util.WFConstants;
import com.jteap.wz.tjrw.util.TjrwUtils;
import com.jteap.wz.xqjh.manager.XqjhDetailManager;
import com.jteap.wz.xqjh.manager.XqjhManager;
import com.jteap.wz.xqjh.model.Xqjh;
import com.jteap.wz.xqjh.model.XqjhDetail;
import com.jteap.wz.xqjhsq.manager.XqjhsqDetailManager;
import com.jteap.wz.xqjhsq.manager.XqjhsqJDBCManager;
import com.jteap.wz.xqjhsq.manager.XqjhsqManager;
import com.jteap.wz.xqjhsq.model.Xqjhsq;
import com.jteap.wz.xqjhsq.model.XqjhsqDetail;
import com.jteap.wz.yhdgl.manager.YhdglManager;
import com.jteap.wz.yhdgl.model.Yhdgl;

/**
 * 描述 : 作者 : caofei 时间 : Oct 19, 2010 参数 : 返回值 : 异常 :
 */
@SuppressWarnings( { "unchecked", "unused" })
public class WzlysqNodeLeaveEventHandler extends LeaveNodeEventHandler {

	private static final long serialVersionUID = 1452027625250484381L;

	private String contextPath;

	private Map sessionAttrs;

	public void execute(ExecutionContext context) throws Exception {
		super.execute(context);

		String status = "";
		String flow_status = "";
		String trName = context.getTransition().getName();

		String nodeName = context.getToken().getNode().getName();
		String LYDQF = (String) context.getVariable("LYDQF");
		String XQJHMXINFO = (String) context.getContextInstance().getVariable(
				"XQJHMXINFO"); // （形式;wzbm+"="+xqjhmxid）
		String loginName = (String) context.getContextInstance()
				.getTransientVariable(WFConstants.WF_VAR_LOGINNAME);
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		Statement stm = null;
		try {
			String docid = (String) context.getContextInstance().getVariable(
					"docid");
			DataSource dataSource = (DataSource) SpringContextUtil
					.getBean("dataSource");
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			st = conn.createStatement();
			 
			if ("填写申请".equals(nodeName)) {
				rs = st
						.executeQuery("select * from tb_wz_ylysqmx where lysqbh = '"
								+ docid + "'");
				while (rs.next()) {
					double sqsl = rs.getDouble("sqsl");
					String xqjhmx = rs.getString("xqjhmxbm");
					if (StringUtil.isNotEmpty(xqjhmx))
						st
								.addBatch("update tb_wz_xqjh_detail set slsl = slsl + "
										+ sqsl + " where id ='" + xqjhmx + "'");
				}
				st.executeBatch();
			}
			if ("提交审批".equals(trName)) {
				if ("2".equals(LYDQF)) {
					String yhdid = XQJHMXINFO;
					YhdglManager yhdglManager = (YhdglManager) SpringContextUtil
							.getBean("yhdglManager");
					Yhdgl yhdgl = yhdglManager.findUniqueBy("id", yhdid);
					// jlzt【0】表示没有选择过的验货单；【1】表示已经选择过的验货单
					yhdgl.setJlzt("1");
					yhdglManager.save(yhdgl);
					yhdglManager.flush();
				}
			}
			if ("审批完成".equals(trName)) {
//				rs = st
//				.executeQuery("select * from tb_wz_ylysqmx where lysqbh = '"
//						+ docid + "'");
//				while (rs.next()) {
//					double sqsl = rs.getDouble("sqsl");
//					String xqjhmx = rs.getString("xqjhmxbm");
//					if (StringUtil.isNotEmpty(xqjhmx))
//						st
//								.addBatch("update tb_wz_xqjh_detail set slsl = slsl + "
//										+ sqsl + " where id ='" + xqjhmx + "'");
//				}
//				st.executeBatch();
				XqjhDetailManager xqjhDetailManager = (XqjhDetailManager) SpringContextUtil
						.getBean("xqjhDetailManager");
				XqjhManager xqjhManager = (XqjhManager) SpringContextUtil
						.getBean("xqjhManager");
				XqjhsqManager xqjhsqManager = (XqjhsqManager) SpringContextUtil
						.getBean("xqjhsqManager");
				XqjhsqDetailManager xqjhsqDetailManager = (XqjhsqDetailManager) SpringContextUtil
						.getBean("xqjhsqDetailManager");
				flow_status = "已完成";
				status = "1";
				context.getContextInstance().setVariable("FLOW_STATUS", status);
				if (StringUtil.isNotEmpty(XQJHMXINFO)) {
					String xqjhinfoList[] = XQJHMXINFO.split(",");
					for (int i = 0; i < xqjhinfoList.length; i++) {
						String xqjhmxList[] = xqjhinfoList[i].split("=");
						String wzbm = xqjhmxList[0];
						String xqjhmxid = xqjhmxList[1];
						//如果不是自由领用
						if(!"undefined".equals(xqjhmxid)){
							XqjhDetail xqjhDetail = xqjhDetailManager.get(xqjhmxid);
							Xqjh xqjh = xqjhManager.get(xqjhDetail.getXqjh()
									.getId());
							Xqjhsq xqjhsq = xqjhsqManager.get(xqjh.getXqjhsqbh());
							String xqjhsqId = xqjhsq.getId();
							String hql = "from XqjhsqDetail as x where x.xqjhsq.id = ? and x.wzbm= ?";
							Object args[] = { xqjhsqId, wzbm };
							List<XqjhsqDetail> xqjhsqDetailList = new ArrayList<XqjhsqDetail>();
							xqjhsqDetailList = xqjhsqManager.find(hql, args);
							for (int j = 0; j < xqjhsqDetailList.size(); j++) {
								XqjhsqDetail xqjhsqDetail = (XqjhsqDetail) xqjhsqDetailList
										.get(0);
								xqjhsqDetail.setStatus("已申领");
								xqjhsqDetailManager.save(xqjhsqDetail);
							}
						}
					}
				}
			}

			if (status.equals("1")) {
				if ("2".equals(LYDQF)) {
					rs = st
							.executeQuery("select p.personname  from tb_sys_person p where p.login_name = '"
									+ loginName + "'");
					String userName = "";
					while (rs.next()) {
						userName = rs.getString(1);
					}
					if ("".equals(userName)) {
						throw new NullPointerException("领料人为空");
					}

					String bh = this.calculate();
					String uuid = UUIDGenerator.hibernateUUID();
					String date = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
					StringBuffer insetSQ = new StringBuffer();
					insetSQ
							.append(
									"insert into tb_wz_ylyd(id,BH,LYBM,LLR,GCXM,ZT,LYSQBH,CZR,GCLB,LYSJ,LYDQF,LCZT) ")
							.append("select '").append(uuid).append("','")
							.append(bh).append("',LYBM,CZYXM,GCXM,'0',ID,'")
							.append(userName).append("',GCLB,to_date('")
							.append(date).append(
									"','yyyy-MM-dd hh24:mi:ss') , '" + LYDQF
											+ "','批准中'").append(
									" from tb_wz_ylysq where id = '").append(
									docid).append("' ");
					st.addBatch(insetSQ.toString());
					stm = conn.createStatement();
					rs = stm
							.executeQuery("select * from tb_wz_ylysqmx where lysqbh='"
									+ docid + "'");
					int index = 0;
					while (rs.next()) {
						int slsl = rs.getInt("SQSL");
						int lysl = rs.getInt("LYSL");
						if (slsl > lysl) {
							index++;
							StringBuffer insetSQMX = new StringBuffer();
							String id = UUIDGenerator.hibernateUUID();
							insetSQMX
									.append(
											"insert into tb_wz_ylydmx(ID,LYDBH,XH,WZBM,JLDW,PZLYSL,SJLYSL,JHDJ,LYSQMX,GCXM,GCLB,ZT) values (")
									.append("'").append(id).append("','")
									.append(uuid).append("','").append(index)
									.append("','").append(rs.getString("WZBM"))
									.append("','").append(rs.getString("JLDW"))
									.append("','").append(slsl - lysl).append(
											"','").append(0).append("','")
									.append(rs.getInt("JG")).append("','")
									.append(rs.getString("ID")).append("',")
									//添加工程项目
									.append("(select gcxm from tb_wz_ylysq where id = '"+docid+"')").append(",")
									//添加工程类别
									.append("(select gclb from tb_wz_ylysq where id = '"+docid+"')").append(",")
									.append("'0')");
							stm.addBatch(insetSQMX.toString());
						}
					}
				} else {
					rs = st
							.executeQuery("select p.personname  from tb_sys_person p where p.login_name = '"
									+ loginName + "'");
					String userName = "";
					while (rs.next()) {
						userName = rs.getString(1);
					}
					if ("".equals(userName)) {
						throw new NullPointerException("领料人为空");
					}

					String bh = this.calculate();
					String uuid = UUIDGenerator.hibernateUUID();
					String date = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
					StringBuffer insetSQ = new StringBuffer();
					insetSQ
							.append(
									"insert into tb_wz_ylyd(id,BH,LYBM,LLR,GCXM,ZT,LYSQBH,CZR,GCLB,LYSJ,LYDQF,LCZT) ")
							.append("select '").append(uuid).append("','")
							.append(bh).append("',LYBM,CZYXM,GCXM,'0',ID,'")
							.append(userName).append("',GCLB,to_date('")
							.append(date).append(
									"','yyyy-MM-dd hh24:mi:ss') , '" + LYDQF
											+ "','已完结'").append(
									" from tb_wz_ylysq where id = '").append(
									docid).append("' ");
					st.addBatch(insetSQ.toString());
					stm = conn.createStatement();
					rs = stm
							.executeQuery("select * from tb_wz_ylysqmx where lysqbh='"
									+ docid + "'");
					int index = 0;
					while (rs.next()) {
						double slsl = rs.getDouble("SQSL");
						double lysl = rs.getDouble("LYSL");
						if (slsl > lysl) {
							index++;
							StringBuffer insetSQMX = new StringBuffer();
							String id = UUIDGenerator.hibernateUUID();
							insetSQMX
									.append(
											"insert into tb_wz_ylydmx(ID,LYDBH,XH,WZBM,JLDW,PZLYSL,SJLYSL,JHDJ,SJJE,LYSQMX,GCXM,GCLB,ZT) values (")
									.append("'").append(id).append("','")
									.append(uuid).append("','").append(index)
									.append("','").append(rs.getString("WZBM"))
									.append("','").append(rs.getString("JLDW"))
									.append("','").append(slsl - lysl).append(
											"','").append(0).append("','")
									.append(rs.getInt("JG")).append("','")
									.append(TjrwUtils.getSjdj(rs.getString("ID"))).append("','")
									.append(rs.getString("ID")).append("',")
									//添加工程项目
									.append("(select gcxm from tb_wz_ylysq where id = '"+docid+"')").append(",")
									//添加工程类别
									.append("(select gclb from tb_wz_ylysq where id = '"+docid+"')").append(",")
									.append("'0')");
							stm.addBatch(insetSQMX.toString());
						}
					}
				}
				// stm.close();
			}
			// ResultSet rs = st.executeQuery("");
			String sql = "update tb_wz_ylysq set flow_status='" + flow_status
					+ "', zt='" + status + "' where id='" + docid + "'";
			st.addBatch(sql);
			// st.executeUpdate(sql);
			st.executeBatch();
			if (stm != null)
				stm.executeBatch();
			conn.commit();
			st.close();
			// ("2"标识借料单)借料单申请流程结束后，需要起草一个补料计划申请；
			// 补料计划调整在自由入库生效的时候起草（所以借料申请流程结束后不需要再起草一个补料计划申请）
			/**
			 * if ("审批完成".equals(trName)) { if("2".equals(LYDQF)){
			 * draftNewWorkFlowInstance(context); } }
			 */
		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
			if (st != null) {
				st.close();
			}
			if (stm != null) {
				stm.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
	}

	private String calculate() {
		String retValue = "00000001";
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil
				.getBean("dataSource");
		try {
			conn = dataSource.getConnection();
			String sql = "select max(BH) from TB_WZ_YLYD";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				String ckbmMax = rs.getString(1);
				if (StringUtil.isNotEmpty(ckbmMax)) {
					NumberFormat nformat = NumberFormat.getNumberInstance();
					nformat.setMinimumIntegerDigits(8);
					int max = Integer.parseInt(ckbmMax) + 1;
					retValue = nformat.format(max).replaceAll(",", "");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return retValue;
	}

	/**
	 * 起草一个新的流程实例 通过Request输入参数 docid 业务对象编号 recordJson 业务对象数据 curNodeName
	 * 当前处理环节 curActors 当前处理人 flowConfigId 流程配置编号
	 * 
	 * @return 表单数据
	 *         {"nextActors":"0845,0490,0963,0223,0390~0845","token":45974,"creatdt":"2009-10-22
	 *         17:40:46","pid":45973,"ver":1,"tid":45975,"flowName":"项目审批","nextNodes":"部门经理审批~总经理审批","creator":"root"}
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public String draftNewWorkFlowInstance(ExecutionContext context)
			throws Exception {
		// 领用单id
		String lysqbh = (String) context.getContextInstance().getVariable(
				"docid");

		String lydid = findLydId(lysqbh);

		// 补料计划申请编号
		XqjhsqJDBCManager xqjhsqJDBCManager = (XqjhsqJDBCManager) SpringContextUtil
				.getBean("xqjhsqJDBCManager");
		String bljhsqBH = xqjhsqJDBCManager.getBljhsqBHMax(3);

		// 业务数据编号
		String docid = UUIDGenerator.hibernateUUID();
		insertXqjhsq(docid, lydid, "2", bljhsqBH);
		String taskName = "填写申请";
		// 借料申请起草人
		String userLoginName = (String) context.getVariable("CZY");

		// 借料申请起草人对象
		String personId = findPersonIdByLoginName(userLoginName);

		// 获取起草人所属班组人员
		String qcrGroups = findAdminGroupIdsOfThePerson(personId);

		String actorLoginName = userLoginName; // 补料需求计划起草人
		String actor = findPersonName(actorLoginName); // 操作人中文名(刘枫)

		String taskActor = qcrGroups;
		// 流程配置编号
		// String flowConfigId =
		// (String)context.getContextInstance().getVariable("flowConfigId");
		// String flowConfigNm = "8a65ade52d13f42b012d140f1f350192";
		// //补料计划申请内码(只有重新定义流程才需要更改该值)
		// String flowConfigId = findNewFlowConfig(flowConfigNm);
		FlowConfigManager flowConfigManager = (FlowConfigManager) SpringContextUtil
				.getBean("flowConfigManager");
		List<FlowConfig> flowConfigList = new ArrayList<FlowConfig>();
		flowConfigList = flowConfigManager.getAll();
		FlowConfig flowConfig = null;
		for (int i = 0; i < flowConfigList.size(); i++) {
			flowConfig = flowConfigList.get(i);
			if (("物资补料计划申请").equals(flowConfig.getName())) {
				if (flowConfig.isNewVer()) {
					break;
				}
			}
		}

//		FlowConfigManager flowConfigManager = (FlowConfigManager) SpringContextUtil
//				.getBean("flowConfigManager");
//		FlowConfig flowConfig = flowConfigManager.get(flowConfigId);
		ProcessInstance pi = draftNewProcess(flowConfig, docid, userLoginName);

		HashMap map = new HashMap();
		map.put("pid", pi.getId());

		// 表单类型
		String formType = flowConfig.getFormtype();
		HashMap recordmap = null;
		// 获得业务数据的MAP
		// EFORM
		if (FlowConfig.FORM_TYPE_EFORM.equals(formType)) {
			String formSn = "TB_WZ_BLJHSQ"; // 补料计划申请SN

			EFormManager eformManager = (EFormManager) SpringContextUtil
					.getBean("eformManager");
			EForm eform = eformManager.findUniqueBy("sn", formSn);
			DefTableInfo defTabInfo = eform.getDefTable();
			String tableName = defTabInfo.getTableCode();
			String schema = defTabInfo.getSchema();

			JdbcManager jdbcManager = (JdbcManager) SpringContextUtil
					.getBean("jdbcManager");
			recordmap = (HashMap) jdbcManager.getRecById(docid, schema,
					tableName);
			pi.getContextInstance().setVariables(recordmap);
			// CFORM
		} else if (FlowConfig.FORM_TYPE_CFORM.equals(formType)) {
			String recordJson = (String) context.getContextInstance()
					.getVariable("recordJson");
			recordmap = (HashMap) JSONUtil.parseObjectHasDate(recordJson);
			pi.getContextInstance().setVariables(recordmap);
		}

		if (pi != null) {
			pi.getContextInstance().setTransientVariable(
					WFConstants.WF_VAR_LOGINNAME, userLoginName);

			JbpmOperateManager jbpmOperateManager = (JbpmOperateManager) SpringContextUtil
					.getBean("jbpmOperateManager");
			TaskInstance ti = jbpmOperateManager.findCurrentTaskInstance(pi
					.getId(), userLoginName);
			if (ti != null) {
				// 下一结点处理人
				// String nextActors =
				// jbpmOperateManager.getNextTaskActorByNode(ti);
				String nextActors = qcrGroups;
				// 下一结点名称
				String nextNodes = "物资补料计划申请";
				map.put("nextNodes", nextNodes);
				map.put("nextActors", nextActors);

				map.put("tid", ti.getId());
				map.put("token", ti.getToken().getId());
			}

			// 流程起草人
			String creator = userLoginName;
			// 流程起草时间
			Date creatdt = (Date) pi.getContextInstance()
					.getVariable("creatdt");
			// 流程名称
			String flowName = "物资补料计划申请";
			map.put("creator", creator);
			map.put("creatdt", DateUtils.formatDate(creatdt));
			map.put("flowName", flowName);
			map.put("ver", pi.getProcessDefinition().getVersion());
		}

		// 计算待办主题并保存待办对象
		String cf = "物资补料计划申请:" + bljhsqBH;
		// cf = evalTodoTopic(cf, recordmap);

		TaskToDoManager taskToDoManager = (TaskToDoManager) SpringContextUtil
				.getBean("taskToDoManager");
		// 创建流程申请代办
		taskToDoManager.saveToDo(flowConfig, pi, actor, taskActor, taskName,
				cf, docid, pi.getRootToken().getId() + "", null, userLoginName);

		String nextActorIds = (String) map.get("nextActors"); // 下一个环节处理人
		String nextActorNames = changeToChineseName(nextActorIds); // 下一个环节处理人中文名称

		WorkFlowLogManager workFlowLogManager = (WorkFlowLogManager) SpringContextUtil
				.getBean("workFlowLogManager");
		// 记录流程日志
		workFlowLogManager.addFlowLog(pi.getId(), taskName, actor,
				actorLoginName, nextActorNames, (String) map.get("nextNodes"),
				"起草流程", "");

		String returnJson = JSONUtil.mapToJson(map);
		returnJson = "{success:true,data:" + returnJson + "}";
		// PrintWriter out = getOut();
		// out.print(returnJson);
		return null;
	}

	/**
	 * 根据流程配置编号，起草一个新的流程实例
	 * 
	 * @param flowConfigId
	 * @throws Exception
	 */
	public ProcessInstance draftNewProcess(FlowConfig flowConfig, String docid,
			String userLoginName) throws Exception {
		if (flowConfig.getPd_id() == null) {
			throw new BusinessException("流程还没发布，请先发布流程！");
		}

		String userName = userLoginName;

		JbpmOperateManager jbpmOperateManager = (JbpmOperateManager) SpringContextUtil
				.getBean("jbpmOperateManager");
		long pid = jbpmOperateManager.createProcessInstance(flowConfig.getNm(),
				userName);
		ProcessInstance pi = jbpmOperateManager.getJbpmTemplate()
				.findProcessInstance(pid);
		pi.getContextInstance().setVariable(WFConstants.WF_VAR_DOCID, docid);
		// if (StringUtil.isNotEmpty(flowConfig.getFormId())) {
		// request.setAttribute("formId", flowConfig.getFormId());
		// request.setAttribute("type", flowConfig.getFormtype());
		// }
		// request.setAttribute("processPageUrl", flowConfig.getProcess_url());

		return pi;
	}

	/**
	 * 待办主题计算
	 * 
	 * @author 肖平松
	 * @version Oct 28, 2009
	 * @param cf
	 * @param map
	 * @return
	 */
	private String evalTodoTopic(String cf, HashMap map) {
		if (StringUtil.isEmpty(cf))
			return "";
		String regex = "\\$\\{[^\\}\\{\"']*\\}"; // 匹配${}公式
		Pattern p = Pattern.compile(regex);
		Matcher mt = p.matcher(cf);
		while (mt.find()) {
			String group = mt.group();
			String topic = group.substring(group.indexOf("{") + 1,
					group.lastIndexOf("}")).toUpperCase();
			Object value = map.get(topic);
			if (value == null) {
				value = "";
			}
			if (value instanceof Date) {
				value = DateUtils.formatDate((Date) value,
						"yyyy-MM-dd HH:mm:ss");
			}
			cf = mt.replaceFirst(value + "");
			mt = p.matcher(cf);
		}
		return cf;
	}

	/**
	 * 将登陆名称转换为用户中文名
	 * 
	 * @param nextActors
	 *            要转换的处理人字符串(例如:0001,0002~0003,0004)
	 * @return
	 */
	private String changeToChineseName(String userLoginNames) {
		PersonManager personManager = (PersonManager) SpringContextUtil
				.getBean("personManager");
		String nextActor = userLoginNames;
		if (StringUtil.isNotEmpty(userLoginNames)) {
			String tempActor = "";
			String[] nextTasksActorTemp = userLoginNames.split("~");
			for (int j = 0; j < nextTasksActorTemp.length; j++) {
				String[] nextTasksActorArray = nextTasksActorTemp[j].split(",");
				for (int i = 0; i < nextTasksActorArray.length; i++) {
					if (nextTasksActorArray[i]
							.equals(Constants.ADMINISTRATOR_ACCOUNT)) {
						tempActor += Constants.ADMINISTRATOR_NAME + ",";
					} else {
						tempActor += personManager.findPersonByLoginName(
								nextTasksActorArray[i]).getUserName()
								+ ",";
					}
				}
			}
			nextActor = tempActor.substring(0, tempActor.length() - 1);
		}
		return nextActor;
	}

	/**
	 * 获取当前用户登录名
	 */
	private String getCurUserLoginName() {
		return sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME)
				.toString();
	}

	/**
	 * 通过起草人登录名获取起草人id
	 */
	public String findPersonIdByLoginName(String userLoginName) {
		Connection conn = null;
		String personId = "";
		DataSource dataSource = (DataSource) SpringContextUtil
				.getBean("dataSource");
		try {
			conn = dataSource.getConnection();
			String sql = "select p.id from tb_sys_person p where p.login_name = '"
					+ userLoginName + "'";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				personId = rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return personId;
	}

	/**
	 * 通过起草人id获取所属班组下所有用户名
	 */
	public String findAdminGroupIdsOfThePerson(String personId) {
		Connection conn = null;
		StringBuffer returnValue = new StringBuffer();
		DataSource dataSource = (DataSource) SpringContextUtil
				.getBean("dataSource");
		try {
			conn = dataSource.getConnection();
			String sql = "select p.login_name from tb_sys_person p where p.id in "
					+ "(select s.personid from tb_sys_person2group s where s.groupid in (select t.groupid from tb_sys_person2group t "
					+ "where personid = '" + personId + "'))";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				returnValue.append(rs.getString(1) + ",");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return returnValue.toString().substring(0,
				returnValue.toString().length() - 1);
	}

	/**
	 * 通过登录名获取中文名称
	 */
	public String findPersonName(String userLoingName) {
		Connection conn = null;
		String personName = "";
		DataSource dataSource = (DataSource) SpringContextUtil
				.getBean("dataSource");
		try {
			conn = dataSource.getConnection();
			String sql = "select p.personname from tb_sys_person p where p.login_name = '"
					+ userLoingName + "'";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				personName = rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return personName;
	}

	/**
	 * 匹配一条补料计划申请(与当前借料申请对应)
	 */
	public void insertXqjhsq(String id, String lydid, String xqjhqf,
			String bljhsqBH) {
		Connection conn = null;
		DataSource dataSource = (DataSource) SpringContextUtil
				.getBean("dataSource");
		try {
			conn = dataSource.getConnection();
			String sql = "insert into tb_wz_xqjhsq(id,lydid,xqjhqf,xqjhsqbh,lyzt) values('"
					+ id
					+ "','"
					+ lydid
					+ "','"
					+ xqjhqf
					+ "','"
					+ bljhsqBH
					+ "','2')";
			PreparedStatement psts = conn.prepareStatement(sql);
			psts.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 通过FlowConfig里面的内码和是否最新版本获取最新Flowconfig对象
	 */
	public String findNewFlowConfig(String flowConfigNm) {
		Connection conn = null;
		String flowConfigId = "";
		DataSource dataSource = (DataSource) SpringContextUtil
				.getBean("dataSource");
		try {
			conn = dataSource.getConnection();
			String sql = "select * from tb_wf_flowconfig t where t.FLOW_NM = '"
					+ flowConfigNm + "' and t.IS_NEW_VER = 1";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				flowConfigId = rs.getString("id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return flowConfigId;
	}

	/**
	 * 通过lysqbh获取id
	 */
	public String findLydId(String lydbh) {
		// LydglManager lydglManager =
		// (LydglManager)SpringContextUtil.getBean("lydglManager");
		// String lydId = "";
		// List list = new ArrayList();
		// String hql = "from Lydgl as l where l.lysqbh = ?";
		// Object args[] = {lydbh};
		// list = lydglManager.find(hql, args);
		// for (int i = 0; i < list.size(); i++) {
		// Lydgl lydgl = (Lydgl)list.get(0);
		// lydId = lydgl.getId();
		// }
		// return lydId;

		Connection conn = null;
		String lydId = "";
		DataSource dataSource = (DataSource) SpringContextUtil
				.getBean("dataSource");
		try {
			conn = dataSource.getConnection();
			String sql = "select l.id from tb_wz_ylyd l where l.lysqbh = '"
					+ lydbh + "'";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				lydId = rs.getString("id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return lydId;
	}

}
