package com.jteap.gcht.gcxmgl.actionHandler;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.node.TaskNode;

import com.jteap.core.web.SpringContextUtil;
import com.jteap.wfengine.workflow.actionHandler.InitNodeEventHandler;

/**
 * 工程验收　工作流初始化节点事件
 * @author caihuiwen
 *
 */
public class GcyjssNodeInitEventHandler extends InitNodeEventHandler {

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
			} else if ("编制人员审批".equals(taskNode.getName())) {
				status = "申请审批";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("审核人员审批".equals(taskNode.getName())) {
				status = "编制人员已审批";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("部门主管审批".equals(taskNode.getName())) {
				status = "审核人员已审批";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("财务审批".equals(taskNode.getName())) {
				status = "部门主管已审批";
				context.getContextInstance().setVariable("STATUS", status);
			} else if ("主管经理审批".equals(taskNode.getName())) {
				status = "财务已审批";
				context.getContextInstance().setVariable("STATUS", status);
			} 
			
			String sql = "update TB_HT_GCYJSS set status='"+status+"' where id='"+docid+"'";
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
