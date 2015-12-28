package com.jteap.gcht.gcxmgl.actionHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.node.TaskNode;

import com.jteap.core.web.SpringContextUtil;
import com.jteap.wfengine.workflow.actionHandler.InitNodeEventHandler;

/**
 * 结算支付　工作流初始化节点事件
 * @author caihuiwen
 *
 */
public class JszfNodeInitEventHandler extends InitNodeEventHandler {

	private static final long serialVersionUID = 1742159021856916726L;
	
	@Override
	public void execute(ExecutionContext context) throws Exception {
		super.execute(context);

		TaskNode taskNode = (TaskNode) context.getNode();

		Connection conn = null;
		Statement st=null;
		ResultSet fqrs=null;
		try {
			DataSource dataSource = (DataSource)SpringContextUtil.getBean("dataSource");
			conn = dataSource.getConnection();
			
			String docid = (String)context.getContextInstance().getVariable("docid");
			
			String status = "";
			String taskNum="";
			if ("支付单申请人起草".equals(taskNode.getName())) {
				taskNum="1";
				status = "起草支付单";
				context.getContextInstance().setVariable("STATUS", status);
			}else if ("安监部主任签批".equals(taskNode.getName())) {
				taskNum="3";
				status = "安监部主任签批";
				context.getContextInstance().setVariable("STATUS", status);
			}else if ("生技部".equals(taskNode.getName()) || "燃运部".equals(taskNode.getName())) {
				taskNum="2";
				status = "生技燃运部主任会签";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("生技部1".equals(taskNode.getName()) || "检修部1".equals(taskNode.getName())) {
				taskNum="2";
				status = "生技检修部主任会签";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("检修部".equals(taskNode.getName()) || "燃运部1".equals(taskNode.getName())) {
				taskNum="2";
				status = "检修燃运部主任会签";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("生技部2".equals(taskNode.getName()) || "发电部".equals(taskNode.getName())) {
				taskNum="2";
				status = "生技发电部主任会签";
				context.getContextInstance().setVariable("STATUS", status);
			}  else if ("检修部2".equals(taskNode.getName()) || "发电部1".equals(taskNode.getName())) {
				taskNum="2";
				status = "检修发电部主任会签";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("燃运部2".equals(taskNode.getName()) || "发电部2".equals(taskNode.getName())) {
				taskNum="2";
				status = "燃运发电部主任会签";
				context.getContextInstance().setVariable("STATUS", status);
			}else if ("燃运部3".equals(taskNode.getName())) {
				taskNum="2";
				status = "实施部门(燃运部)签批";
				context.getContextInstance().setVariable("STATUS", status);
			}else if ("生技部3".equals(taskNode.getName())) {
				taskNum="2";
				status = "实施部门(生技部)签批";
				context.getContextInstance().setVariable("STATUS", status);
			}else if ("检修部3".equals(taskNode.getName())) {
				taskNum="2";
				status = "实施部门(检修部)签批";
				context.getContextInstance().setVariable("STATUS", status);
			}else if ("发电部3".equals(taskNode.getName())) {
				taskNum="2";
				status = "实施部门(发电部)签批";
				context.getContextInstance().setVariable("STATUS", status);
			}else if ("综合事务部".equals(taskNode.getName())) {
				taskNum="2";
				status = "实施部门(综合事务部)签批";
				context.getContextInstance().setVariable("STATUS", status);
			}else if ("计划部专工签批".equals(taskNode.getName())) {
				taskNum="4";
				status = "计划部专工签批";
				context.getContextInstance().setVariable("STATUS", status);
			}else if ("二期计划专工签批".equals(taskNode.getName())) {
				taskNum="4";
				status = "二期计划部专工签批";
				context.getContextInstance().setVariable("STATUS", status);
			}else if ("计划部主任签批".equals(taskNode.getName())) {
				taskNum="5";
				status = "计划部主任签批";
				context.getContextInstance().setVariable("STATUS", status);
			}else if ("二期计划部主任签批".equals(taskNode.getName())) {
				taskNum="5";
				status = "二期计划部主任签批";
				context.getContextInstance().setVariable("STATUS", status);
			}else if ("生技部主任签批".equals(taskNode.getName())) {
				taskNum="6";
				status = "生技部主任签批";
				context.getContextInstance().setVariable("STATUS", status);
			}else if ("财务部主任签批".equals(taskNode.getName())) {
				taskNum="7";
				status = "财务部主任签批";
				context.getContextInstance().setVariable("STATUS", status);
			}else if ("二期财务签批".equals(taskNode.getName())) {
				taskNum="7";
				status = "二期财务部主任签批";
				context.getContextInstance().setVariable("STATUS", status);
			}else if ("总会计师签批".equals(taskNode.getName())) {
				taskNum="8";
				status = "总会计师签批";
				context.getContextInstance().setVariable("STATUS", status);
			}else if ("分管副总经理签批".equals(taskNode.getName())) {
				taskNum="9";
				status = "分管副总签批";
				context.getContextInstance().setVariable("STATUS", status);
			}else if ("二期分管副总签批".equals(taskNode.getName())) {
				taskNum="9";
				status = "二期分管副总签批";
				context.getContextInstance().setVariable("STATUS", status);
			}else if ("总经理签批".equals(taskNode.getName())) {
				taskNum="10";
				status = "总经理签批";
				context.getContextInstance().setVariable("STATUS", status);
			}
			st = conn.createStatement();
			if (taskNum.equals("2")) {
				String fqzt = "1期";
				String fqzf = "select max(fqzt) as fqzt from tb_ht_gcxmjszfd where xmbh=(select XMBH from tb_ht_gcxmjszfd where id ='"+docid+"') ORDER BY CJSJ DESC";
				fqrs= st.executeQuery(fqzf);
				while(fqrs.next()){
					if(fqrs.getString("fqzt") != null){
						int fqs =Integer.parseInt(fqrs.getString("fqzt").substring(0,1));
						if(fqs >= 1){
							fqzt = fqs+1+"期";
						}
					}
				}
				String fqsql = "update TB_HT_GCXMJSZFD set fqzt='"+fqzt+"' where id='"+docid+"'";
				st.executeUpdate(fqsql);
			}
			String sql = "update TB_HT_GCXMJSZFD set status='"+status+"',zfzt='流程中' where id='"+docid+"'";
			String zfsql = "update TB_HT_YSD set iszf='流程中' where id=(select gcys_id from TB_HT_GCXMJSZFD where id='"+docid+"')";
			st.executeUpdate(sql);
			st.executeUpdate(zfsql);
			st.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
			if(fqrs != null){
				fqrs.close();
			}
		}
	}
	
}
