package com.jteap.gcht.htzx.actionHandler;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.node.TaskNode;

import com.jteap.core.web.SpringContextUtil;
import com.jteap.wfengine.workflow.actionHandler.InitNodeEventHandler;

/**
 * 工程合同付款审批　工作流初始化节点事件
 * @author caihuiwen
 *
 */
public class GcHeTongYWCFkNodeInitEventHandler extends InitNodeEventHandler {

	private static final long serialVersionUID = 1742159021856916726L;
	
	@Override
	public void execute(ExecutionContext context) throws Exception {
		super.execute(context);

		TaskNode taskNode = (TaskNode) context.getNode();

		Connection conn = null;
		try {
			DataSource dataSource = (DataSource)SpringContextUtil.getBean("dataSource");
			conn = dataSource.getConnection();
			
			String docid = (String)context.getContextInstance().getVariable("docid");
			
			String status = "";
			if ("起草合同".equals(taskNode.getName())) {
				status = "起草合同";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("使用部门专工审批".equals(taskNode.getName())) {
				status = "申请审批";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("使用部门主任审批".equals(taskNode.getName())) {
				status = "使用部门专工已审批";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("生技部专工审批".equals(taskNode.getName())) {
				status = "使用部门主任已审批";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("生技部主任审批".equals(taskNode.getName())) {
				status = "生技部专工已审批";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("结算人员审批".equals(taskNode.getName())) {
				status = "生技部主任已审批";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("安监部审批".equals(taskNode.getName())) {
				status = "结算人员已审批";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("电厂计划部审批".equals(taskNode.getName())) {
				status = "安监部已审批";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("财务部审批".equals(taskNode.getName())) {
				status = "电厂计划部已审批";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("主管副经理审批".equals(taskNode.getName())) {
				status = "财务部已审批";
				context.getContextInstance().setVariable("STATUS", status);
				
			} else if ("总经理审批".equals(taskNode.getName())) {
				status = "主管副经理已审批";
				context.getContextInstance().setVariable("STATUS", status);
			}
			
			String sql = "update TB_HT_GCHTFKX set status='"+status+"' where id='"+docid+"'";
			Statement st = conn.createStatement();
			st.executeUpdate(sql);
			st.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}
	
}
